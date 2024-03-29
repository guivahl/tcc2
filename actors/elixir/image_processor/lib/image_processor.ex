defmodule ImageProcessor do
  alias Image

  def split_and_invert_image(input_path, split_width, split_height, initial_time) do
    parent_pid = self()

    counter_pid = spawn(ImageProcessor, :counter, [0, split_width * split_height, [], split_width, split_height, initial_time, parent_pid])

    input_image = Image.open!(input_path)

    input_width = Image.width(input_image)
    input_height = Image.height(input_image)

    chunk_width = div(input_width, split_width)
    chunk_height = div(input_height, split_height)

    for i <- 1..(split_height), j <- 1..(split_width) do
      x_start = (j - 1) * chunk_width
      y_start = (i - 1) * chunk_height

      order = ((j - 1)) + ((i - 1) * split_width)

      spawn(ImageProcessor, :split_save, [input_image, order, x_start, y_start, chunk_width, chunk_height, counter_pid])
    end

    receive do
      {:finish} -> :ok
    end
  end

  def split_save(input_image, order, x_start, y_start, chunk_width, chunk_height, counter_pid) do
    piece = Image.crop!(input_image, x_start, y_start, chunk_width, chunk_height)

    rotated_piece = Image.rotate!(piece, 180.0)

    send(counter_pid, {:count, {order, rotated_piece}})
  end

  def counter(count, max, images, split_width, split_height, initial_time, parent_pid) when count == max do
    spawn(ImageProcessor, :join, [images, split_width, split_height, initial_time, parent_pid])
  end

  def counter(count, max, images, split_width, split_height, initial_time, parent_pid) do
    receive do
      {:count, {order, rotated_piece}} ->
        counter(count + 1, max, images ++ [{order, rotated_piece}], split_width, split_height, initial_time, parent_pid)
    end
  end

  def join(images, split_width, split_height, initial_time, parent_pid) do
    sorted_images = images
      |> Enum.sort(fn {index_a, _}, {index_b, _} -> index_a > index_b end)
      |> Enum.map(fn {_, image} -> image end)

    final_image = Image.join!(sorted_images, [{:across, split_width}])

    output_path = "output_images/final_#{:os.system_time(:millisecond)}.png"

    with {:ok,_} <-  Image.write(final_image, output_path) do
      finish_time = Time.utc_now()
      time_execution = Time.diff(finish_time, initial_time, :millisecond)

      n_actors = split_width * split_height

      csv_string = "#{n_actors},#{split_width},#{split_height},#{time_execution},#{output_path}\n"

      filename = "results.csv"

      {:ok, file} = File.open(filename, [:append])

      IO.write(file, csv_string)
      File.close(file)

      send(parent_pid, {:finish})
    end
  end
end

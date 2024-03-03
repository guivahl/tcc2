defmodule ImageProcessor do
  alias Image

  def split_and_invert_image(input_path, split_width, split_height, initial_time) do
    parent_pid = self()

    counter_pid = spawn(ImageProcessor, :counter, [0, split_width * split_height, split_width, split_height, initial_time, parent_pid])

    input_image = Image.open!(input_path)

    input_width = Image.width(input_image)
    input_height = Image.height(input_image)

    chunk_width = div(input_width, split_width)
    chunk_height = div(input_height, split_height)

    for i <- 1..(split_height), j <- 1..(split_width) do
      x_start = (j - 1) * chunk_width
      y_start = (i - 1) * chunk_height

      order = (i * split_height) + j

      output_filename = "tmp/rotated_#{(i - 1)}_#{(j - 1)}.png"
      # IO.puts(output_filename)
      order = (i * split_height) + j
      spawn(ImageProcessor, :split_save, [input_image, x_start, y_start, chunk_width, chunk_height, output_filename, counter_pid])
    end

    receive do
      {:finish} -> :ok
    end
  end

  def split_save(input_image, x_start, y_start, chunk_width, chunk_height, output_filename, counter_pid) do
    # IO.puts("teste")
    piece = Image.crop!(input_image, x_start, y_start, chunk_width, chunk_height)

    rotated_piece = Image.rotate!(piece, 180.0)

    with {:ok,_} <- Image.write(rotated_piece, output_filename) do
      # IO.puts("count")
      send(counter_pid, {:count})
    end
  end

  def counter(count, max, split_width, split_height, initial_time, parent_pid) when count == max do
    # IO.puts("max count")
    spawn(ImageProcessor, :join, [split_width, split_height, initial_time, parent_pid])
  end

  def counter(count, max, split_width, split_height, initial_time, parent_pid) do
    receive do
      {:count} ->
        # IO.puts(count)
        counter(count + 1, max, split_width, split_height, initial_time, parent_pid)
    end
  end

  def join(split_width, split_height, initial_time, parent_pid) do
    images = for i <- (split_height)..1, j <- (split_width)..1 do
      Image.open!("tmp/rotated_#{i - 1}_#{j - 1}.png")
    end

    final_image = Image.join!(images, [{:across, split_width}])
    output_path = "output_images/final_#{:os.system_time(:millisecond)}.png"
    # IO.puts(output_path)
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

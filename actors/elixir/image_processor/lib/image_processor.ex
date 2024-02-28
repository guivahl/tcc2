defmodule ImageProcessor do
  alias Image

  def split_and_invert_image(input_path, split_width, split_height, parent_pid) do
    counter_pid = spawn(ImageProcessor, :counter, [0, split_width * split_height, split_width, split_height, parent_pid])

    input_image = Image.open!(input_path)

    input_width = Image.width(input_image)
    input_height = Image.height(input_image)

    chunk_width = div(input_width, split_width)
    chunk_height = div(input_height, split_height)

    for i <- 1..(split_height), j <- 1..(split_width) do
      x_start = (j - 1) * chunk_width
      y_start = (i - 1) * chunk_height

      output_filename = "tmp/rotated_#{(i - 1)}_#{(j - 1)}.png"

      spawn(fn -> ImageProcessor.split_save(input_image, x_start, y_start, chunk_width, chunk_height, output_filename, counter_pid) end)
    end
  end

  def split_save(input_image, x_start, y_start, chunk_width, chunk_height, output_filename, counter_pid) do
    piece = Image.crop!(input_image, x_start, y_start, chunk_width, chunk_height)

    rotated_piece = Image.rotate!(piece, 180.0)

    with {:ok,_} <- Image.write(rotated_piece, output_filename) do
      send(counter_pid, {:count})
    end


  end

  def counter(count, max, split_width, split_height, parent_pid) when count == max do
    spawn(fn -> ImageProcessor.join(split_width, split_height, parent_pid) end)
  end

  def counter(count, max, split_width, split_height, parent_pid) do
    receive do
      {:count} ->
        counter(count + 1, max, split_width, split_height, parent_pid)
    end
  end

  def join(split_width, split_height, parent_pid) do
    images = for i <- (split_height)..1, j <- (split_width)..1 do
      Image.open!("tmp/rotated_#{i - 1}_#{j - 1}.png")
    end

    final_image = Image.join!(images, [{:across, split_width}])
    output_path = "output_images/final_#{:os.system_time(:millisecond)}.png"

    with {:ok,_} <-  Image.write(final_image, output_path) do
      send(parent_pid, {:finish, output_path})
    end
  end
end

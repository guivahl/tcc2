defmodule ImageProcessor do
  alias Image

  def split_and_invert_image(input_path, parent_pid) do
    num_parts = 4

    counter_pid = spawn(ImageProcessor, :counter, [1, num_parts * num_parts, parent_pid])

    input_image = Image.open!(input_path)

    input_width = Image.width(input_image)
    input_height = Image.height(input_image)

    chunk_width = div(input_width, num_parts)
    chunk_height = div(input_height, num_parts)

    for x <- 0..(num_parts - 1), y <- 0..(num_parts - 1) do
      x_start = x * chunk_width
      y_start = y * chunk_height

      output_filename = "tmp/rotated_#{x}_#{y}.png"

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

  def counter(count, max, parent_pid) when count == max do
    spawn(fn -> ImageProcessor.join(parent_pid) end)
  end

  def counter(count, max, parent_pid) do
    receive do
      {:count} ->
        counter(count + 1, max, parent_pid)
    end
  end

  def join(parent_pid) do
    image1 = Image.open!("tmp/rotated_#{0}_#{0}.png")
    image2 = Image.open!("tmp/rotated_#{0}_#{1}.png")
    image3 = Image.open!("tmp/rotated_#{0}_#{2}.png")
    image4 = Image.open!("tmp/rotated_#{0}_#{3}.png")
    image5 = Image.open!("tmp/rotated_#{1}_#{0}.png")
    image6 = Image.open!("tmp/rotated_#{1}_#{1}.png")
    image7 = Image.open!("tmp/rotated_#{1}_#{2}.png")
    image8 = Image.open!("tmp/rotated_#{1}_#{3}.png")
    image9 = Image.open!("tmp/rotated_#{2}_#{0}.png")
    image10 = Image.open!("tmp/rotated_#{2}_#{1}.png")
    image11 = Image.open!("tmp/rotated_#{2}_#{2}.png")
    image12 = Image.open!("tmp/rotated_#{2}_#{3}.png")
    image13 = Image.open!("tmp/rotated_#{3}_#{0}.png")
    image14 = Image.open!("tmp/rotated_#{3}_#{1}.png")
    image15 = Image.open!("tmp/rotated_#{3}_#{2}.png")
    image16 = Image.open!("tmp/rotated_#{3}_#{3}.png")

    image_joined1 = Image.join!([image4, image3, image2, image1])
    image_joined2 = Image.join!([image8, image7, image6, image5])
    image_joined3 = Image.join!([image12, image11, image10, image9])
    image_joined4 = Image.join!([image16, image15, image14, image13])

    final_image = Image.join!([image_joined4, image_joined3, image_joined2, image_joined1], [{:across, 4}])
    output_path = "output_images/final_#{:os.system_time(:millisecond)}.png"

    with {:ok,_} <-  Image.write(final_image, output_path) do
      send(parent_pid, {:finish, output_path})
    end
  end
end

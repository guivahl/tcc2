# defmodule ImageProcessor do
#   # alias Image

#   # Public function to process the image
#   def process(file_path, num_parts, _output_file) do
#     # {:ok, image_binary} = File.read(file_path)

#     {:ok, image} = Image.open(file_path)
#     # width = Image.width(image)
#     # height = Image.height(image)

#     # chunk_width = div(width, num_parts)
#     # chunk_height = div(height, num_parts)

#     pixel_values =
#       Enum.map(0..(100 - 1), fn x ->
#         Enum.map(0..(100 - 1), fn y ->
#           {:ok, pixel_value} = Image.get_pixel(image, x, y)

#           r = Enum.at(pixel_value, 0)
#           g = Enum.at(pixel_value, 1)
#           b = Enum.at(pixel_value, 2)

#           [r, g, b]  # Create a list with color components
#         end)
#       end)

#       IO.inspect(pixel_values)

#     IO.puts("length list #{length(pixel_values)}")

#     # pixel_values =
#     #   Enum.map(0..(10 - 1), fn x ->
#     #     list = Enum.map(0..(10 - 1), fn y ->
#     #       # Get the color of the pixel
#     #       {:ok, pixel_value} = Image.get_pixel(image, x, y)

#     #       r = Enum.at(pixel_value,0)
#     #       g = Enum.at(pixel_value,1)
#     #       b = Enum.at(pixel_value,2)

#     #       IO.puts("width: #{x}, height: #{y}. #{r}|#{g}|#{b}")
#     #       # IO.puts(is_integer(r))
#     #       # Extract the individual color components and return them as a list
#     #       [r, g, b]
#     #     end)

#     #     # Enum.each(list, fn y ->
#     #     #   # Get the color of the pixel
#     #     #   Enum.each(y, fn x ->
#     #     #     # Get the color of the pixel
#     #     #     # IO.puts(is_list(x))
#     #     #     IO.puts("valor dentro x: #{x}")
#     #     #   end)

#     #     #   # IO.puts("valor de y: #{y}")
#     #     # end)

#     #     list
#     #   end)

#     # Enum.each(pixel_values, fn y ->
#     #     # Get the color of the pixel
#     #     # Enum.each(y, fn x ->
#     #     #   # Get the color of the pixel
#     #     #   # IO.puts(is_list(x))
#     #     #   IO.puts("is z list #{is_list(x)}, valor dentro z: #{x}")
#     #     # end)

#     #     IO.puts("valor de y: #{y}, is list: #{is_list(y)}")
#     #   end)
#     # IO.puts(length(pixel_values))

#     {:ok, img2} = Vix.Vips.Image.new_from_list(pixel_values)
#     # {:ok, mask} = Vix.Vips.Image.new_matrix_from_array(9, 9, pixel_values)

#     Image.write(img2,  "processed_image6.jpg")

#     # IO.puts("#{Enum.at(pixel_value,0)}|#{Enum.at(pixel_value,1)}|#{Enum.at(pixel_value,2)}")

#     # for x <- 0..(num_parts - 1), y <- 0..(num_parts - 1) do
#     #   x_start = x * chunk_width
#     #   y_start = y * chunk_height

#     #   # _subpart = Image.crop(image, x_start, y_start, chunk_width, chunk_height)
#     #   _subpart2 = process_subpart(image, x_start, y_start, chunk_width, chunk_height)
#     #   # subpart_resized = Image.resize(subpart, 1, 1)
#     #   IO.puts("start width: #{x_start} start height: #{y_start}, chunk width: #{chunk_width} chunk height: #{chunk_height}")
#     #   {x_start, y_start}
#     # end
#   end

#   # defp process_subpart(image, x_start, y_start, chunk_width, chunk_height) do
#   #   # Iterate over each pixel in the subpart and change its color
#   #   processed_pixels =
#   #     for x <- 0..(chunk_width - 1), y <- 0..(chunk_height - 1) do
#   #       # Calculate the coordinates of the current pixel in the original image
#   #       original_x = x_start + x
#   #       original_y = y_start + y

#   #       # Get the color of the pixel in the original image
#   #       {:ok, pixel_value} = Image.get_pixel(image, x, y)
#   #       IO.puts(pixel_value)
#   #       # Example: Change the color of the pixel (swap red and blue channels)
#   #       # processed_pixel = {elem(pixel_value, 2), elem(pixel_value, 1), elem(pixel_value, 0)}

#   #       # # Return the processed pixel
#   #       # {original_x, original_y, pixel_value}
#   #     end

#   #   # Create a new image with the processed pixels
#   #   processed_pixels
#   # end


#   #   # Create a new image with the processed pixels
#   #   Image.from_pixels(processed_pixels, width, height)
#   # end
#     # parts = Enum.chunk_every(image_binary, div(length(image_binary), num_parts))

#     # Divide the image into smaller parts
#     # subparts = split_image(image, num_parts)

#     # # Process each subpart (e.g., change color)
#     # processed_subparts = Enum.map(subparts, &process_subpart/1)

#         # # Join the processed subparts
#         # processed_image = join_image(processed_subparts)

#         # # Save the processed image
#         # save_image(processed_image, output_file)

#   end

#   # # Private function to split the image into smaller parts
#   # defp split_image(image, num_parts) do
#   #   Image.crop(image, num_parts, num_parts)
#   #   |> Enum.map(&Image.resize(&1, 1, 1))
#   # end

#   # # Private function to process a subpart (change color, etc.)
#   # defp process_subpart(subpart) do
#   #   # Example: Change the color of the subpart
#   #   Image.map_pixels(subpart, fn {r, g, b} -> {b, g, r} end)
#   # end

#   # # Private function to join the processed subparts
#   # defp join_image(processed_subparts) do
#   #   Image.join(processed_subparts)
#   # end

#   # # Private function to save the image to a file
#   # defp save_image(image, output_file) do
#   #   Image.write(image, output_file)
#   # end

# # Process the image file
# ImageProcessor.process("image.jpg", 4, "processed_image.jpg")

# Add `imagemagick` and `nimble_png` to your mix.exs dependencies:
# defp deps do
#   [
#     {:imagemagick, "~> 0.17"},
#     {:nimble_png, "~> 0.6"}
#   ]
# end

defmodule ImageProcessor do
  alias Image

  def split_and_invert_image(input_path) do
    num_parts = 4

    # list_images = []

    pid = spawn(ImageProcessor, :save2, [[]])

    for x <- 0..(num_parts - 1), y <- 0..(num_parts - 1) do
      input_image = Image.open!(input_path)

      input_width = Image.width(input_image)
      input_height = Image.height(input_image)

      chunk_width = div(input_width, num_parts)
      chunk_height = div(input_height, num_parts)

      x_start = x * chunk_width
      y_start = y * chunk_height

      # end_x = min(x_start + chunk_height, input_width)
      # end_y = min(y_start + chunk_width, input_height)

      # IO.puts("start width: #{x_start} start height: #{y_start}, end width: #{end_x} end height: #{end_y}")

      {:ok, piece} = Image.crop(input_image, x_start, y_start, chunk_width, chunk_height)

      {:ok, rotated_piece} = Image.rotate(piece, 90.0)

      IO.puts("sending rotated_piece...")
      output_filename = "output_images/rotated_#{x}_#{y}.jpg"
      # spawn(fn -> ImageProcessor.save(rotated_piece, output_filename) end)
      send(pid, {:add, rotated_piece, output_filename})
      # :timer.sleep(1000)
      # send(pid, {:add, output_filename, rotated_piece})
    end

    # IO.puts("sending finish...")
    # send(pid, {:finish})

    # output_filename = "#{output_path}/piece_#{x_start}_#{y_start}.jpg"

    # IO.puts(length(list_images))

    # Image.write(rotated_piece, output_filename)
  end

  def save(rotated_piece, output_filename) do # isso pelo menos funciona
    Image.write!(rotated_piece, output_filename)
  end

  def save2(list_images) do # isso pelo menos funciona
  receive do
    {:add, rotated_piece, output_filename} ->
      IO.puts("adding value... save2 length: #{length(list_images)}")
      # output_filename = "output_images/part_rotated_image_#{:os.system_time(:millisecond)}.jpg"

      # Image.write!(value, output_filename)
      # Image.write!(rotated_piece, output_filename)
      spawn(fn -> ImageProcessor.save(rotated_piece, output_filename) end)

      list_images = if length(list_images) == 0, do: [rotated_piece], else: list_images ++ [rotated_piece]

      save2(list_images)
    end

    save2(list_images)
  end

  def save_list(list) do
    receive do
      {:add, output_filename, rotated_piece} ->
        Image.write!(rotated_piece, output_filename)

      {:add, value} ->
        IO.puts("adding value...")
        # output_filename = "output_images/part_rotated_image_#{:os.system_time(:millisecond)}.jpg"

        # Image.write!(value, output_filename)

        list = if length(list) == 0, do: [value], else: list ++ [value]

        save_list(list)
      {:finish} ->
          # Enum.each(list, fn value -> Image.write(value, "output_images/final_rotated_image_#{:os.system_time(:millisecond)}.jpg") end)
          IO.puts("length actor: #{length(list)}, is list #{is_list(list)}")
          output_image = Image.join!(list)
          # IO.puts(output_image)
          # Enum.each(list, fn img -> Image.write!(img, "output_images/partial_#{:os.system_time(:millisecond)}.jpg") end)
          # output_image = Image.join!(list)
          output_filename = "output_images/final_rotated_image_#{:os.system_time(:millisecond)}.jpg"

          # Image.write!(Enum.at(list, 1), output_filename)
          # Image.write!(output_image, output_filename)
          save_list(list)

    end

    save_list(list)
  end
end

# Example usage:
# ImageProcessor.split_and_invert_image("image.jpg", "output_images")
ImageProcessor.split_and_invert_image("image.jpg")

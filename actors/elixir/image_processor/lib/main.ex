initial_time = Time.utc_now()

default_split_width = 4
default_split_height = 4

args = System.argv()
has_args = length(args) == 2

split_width = if has_args, do: String.to_integer(Enum.at(args, 0)), else: default_split_width
split_height = if has_args, do: String.to_integer(Enum.at(args, 1)), else: default_split_height

input_image_path = "image.png"

ImageProcessor.split_and_invert_image(input_image_path, split_width, split_height, initial_time)

File.mkdir_p!(Path.dirname("tmp"))

initial_time = Time.utc_now()

default_split_width = 4
default_split_height = 4

args = System.argv()
has_args = length(args) == 2

split_width = if has_args, do: String.to_integer(Enum.at(args, 0)), else: default_split_width
split_height = if has_args, do: String.to_integer(Enum.at(args, 1)), else: default_split_height

# parent_pid = self()

input_image_path = "image.png"

# spawn(fn -> ImageProcessor.split_and_invert_image(input_image_path, split_width, split_height, initial_time) end)

ImageProcessor.split_and_invert_image(input_image_path, split_width, split_height, initial_time)

# receive do
#   {:finish, output_path} ->
#     finish_time = Time.utc_now()
#     time_execution = Time.diff(finish_time, initial_time, :millisecond)

#     n_actors = split_width * split_height

#     csv_string = "#{n_actors},#{split_width},#{split_height},#{time_execution},#{output_path}\n"

#     filename = "results.csv"

#     {:ok, file} = File.open(filename, [:append])

#     IO.write(file, csv_string)
#     File.close(file)
# end

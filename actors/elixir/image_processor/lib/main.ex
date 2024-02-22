initial_time = Time.utc_now()
parent_pid = self()

ImageProcessor.split_and_invert_image("image.png", parent_pid)

receive do
  {:finish, output_path} ->
    finish_time = Time.utc_now()
    time_execution = Time.diff(finish_time, initial_time, :millisecond)

    csv_string = "#{time_execution},#{output_path}\n"

    filename = "results.csv"

    {:ok, file} = File.open(filename, [:append])

    IO.write(file, csv_string)
    File.close(file)
end

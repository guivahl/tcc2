default_total_points = 1_000
default_num_actors = 1_000

args = System.argv()
has_args = length(args) == 2

total_points = if has_args, do: String.to_integer(Enum.at(args, 0)), else: default_total_points
num_actors = if has_args, do: String.to_integer(Enum.at(args, 1)), else: default_num_actors

parent_pid = self()

initial_time = Time.utc_now()

PI.start(total_points, num_actors, parent_pid)

receive do
  {:finish, total_points, points_inside_circle, num_actors, estimated_pi} ->

    finish_time = Time.utc_now()
    time_execution = Time.diff(finish_time, initial_time, :millisecond)

    diff_pi = abs(:math.pi - estimated_pi)

    points_per_actor = trunc(total_points / num_actors)

    csv_string = "#{total_points},#{num_actors},#{points_per_actor},#{points_inside_circle},#{estimated_pi},#{:math.pi},#{diff_pi},#{time_execution}\n"

    filename = "results.csv"

    {:ok, file} = File.open(filename, [:append])
    IO.write(file, csv_string)
    File.close(file)
end

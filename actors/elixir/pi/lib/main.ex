default_total_points = 1_000
default_num_actors = 1_000

args = System.argv()
has_args = length(args) == 2

total_points = if has_args, do: String.to_integer(Enum.at(args, 0)), else: default_total_points
num_actors = if has_args, do: String.to_integer(Enum.at(args, 1)), else: default_num_actors

initial_time = Time.utc_now()

points_per_actor = trunc(total_points / num_actors)

PI.estimate(points_per_actor, num_actors, total_points, initial_time)

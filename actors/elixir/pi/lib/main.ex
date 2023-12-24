default_total_points = 1_000
default_num_actors = 1_000

args = System.argv()
has_args = length(args) == 2

total_points = if has_args, do: String.to_integer(Enum.at(args, 0)), else: default_total_points
num_actors = if has_args, do: String.to_integer(Enum.at(args, 1)), else: default_num_actors

parent_pid = self()

PI.start(total_points, num_actors, parent_pid)

receive do
  {:finish, estimated_pi} ->
    IO.puts("Valor estimado de PI para #{total_points} foi #{estimated_pi}")
end

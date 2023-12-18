total_points = 1_000
num_actors = 1_000

args = System.argv()

# todo: pass total points and num actors as args

parent_pid = self()

PI.start(total_points, num_actors, parent_pid)

receive do
  {:finish, estimated_pi} ->
    IO.puts("Valor estimado de PI para #{total_points} foi #{estimated_pi}")
end

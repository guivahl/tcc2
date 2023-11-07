# defmodule PI do
#   def estimate(num_points) when is_integer(num_points) and num_points > 0 do
#     {time_microseconds, result} = :timer.tc(fn ->
#       inside_circle = Enum.count(1..num_points, fn _ ->
#         x = :rand.uniform()
#         y = :rand.uniform()
#         x * x + y * y <= 1.0
#       end)
#       estimated_pi = 4.0 * inside_circle / num_points
#       estimated_pi
#     end)

#     time_seconds = time_microseconds / 1_000_000.0

#     IO.puts("Estimated π with #{num_points} points: #{result}")
#     IO.puts("Time taken: #{time_seconds} seconds")
#     result
#   end
# end

defmodule RandomXY do


end

defmodule PI do
  def estimate(num_points) when is_integer(num_points) and num_points > 0 do
    {time_microseconds, result} = :timer.tc(fn ->
      inside_circle = Enum.count(1..num_points, fn _ ->
        generate_xy()
      end)
      estimated_pi = 4.0 * inside_circle / num_points
      estimated_pi
    end)

    time_seconds = time_microseconds / 1_000_000.0

    IO.puts("Estimated π with #{num_points} points: #{result}")
    IO.puts("Time taken: #{time_seconds} seconds")

    result
  end

  def generate_xy() do
    x = :rand.uniform()
    y = :rand.uniform()

    x * x + y * y <= 1.0
  end
end

pid = spawn fn ->
  receive do
    {sender, :ping} ->
      IO.puts "Got ping"
      send sender, :pong
  end
end

send pid, {self(), :ping}

# Got ping

receive do
  message -> IO.puts "Got #{message} back"
end

PI.estimate(1_000)
# PI.estimate(10_000)
# PI.estimate(100_000)
# PI.estimate(1_000_000)
# PI.estimate(10_000_000)
# PI.estimate(100_000_000)
# PI.estimate(1_000_000_000)

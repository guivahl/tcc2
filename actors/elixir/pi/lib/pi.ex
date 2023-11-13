defmodule Randomize do
  def start do
    pid = spawn(__MODULE__, :loop, [])
    pid
  end

  def loop do
    receive do
      {sender} ->
        x = :rand.uniform()
        y = :rand.uniform()

        is_inside = x * x + y * y <= 1.0

        send(sender, {is_inside})
        loop()
    end
  end
end

defmodule PI do
  def start(total_points) do
    pid = spawn(__MODULE__, :estimate, [total_points, total_points, 0])
    pid
  end

  def estimate(total_points, 0, inside_circle) do
    estimated_pi = 4.0 * inside_circle / total_points
    IO.puts("Valor estimado de PI para #{total_points} foi #{estimated_pi}")
  end

  def estimate(total_points, num_points, inside_circle) do
    actor_pid = Randomize.start()

    send(actor_pid, {self()})
    receive do
      {true} ->
        estimate(total_points, num_points - 1, inside_circle + 1)
      {false} ->
        estimate(total_points, num_points - 1, inside_circle)
    end
  end
end

PI.start(1_000)
# not working for values over 5k

# pi = PI.start(1_000)
# Process.exit(pi, :normal)
# pi = PI.start(10_000)
# PI.start(100_000)
# PI.start(1_000_000)
# PI.start(10_000_000)
# PI.start(100_000_000)
# PI.start(1_000_000_000)

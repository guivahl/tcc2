defmodule RandomXY do
  def start() do
    spawn fn -> generate() end
  end

  def generate() do
    receive do
      {sender, :estimate} ->
        x = :rand.uniform()
        y = :rand.uniform()

        is_inside = x * x + y * y <= 1.0
        is_inside
    end
  end
end

defmodule PI do
  def start(num_points) do
    spawn fn -> estimate(num_points) end
  end

  def estimate(num_points) do
    random = RandomXY.start()

    Enum.count(1..num_points, fn _ ->
      task = Task.async(fn ->
        x = :rand.uniform()
        y = :rand.uniform()

        is_inside = x * x + y * y <= 1.0
        is_inside
      end)
      r = Task.await(task)
      Task.shutdown(task)
    end)

  end

end

PI.estimate(10)

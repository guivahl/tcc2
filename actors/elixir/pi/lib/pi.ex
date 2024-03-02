defmodule MonteCarlo do
  def calculate do
    receive do
      {sender, points} ->
        {inside_count} =
          Enum.reduce(1..points, {0}, fn _, {count} ->
            x = :rand.uniform()
            y = :rand.uniform()
            is_inside = x * x + y * y <= 1.0

            sum_value = if is_inside, do: 1, else: 0

            {count + sum_value}
          end)

        send(sender, {inside_count})
    end
  end
end

defmodule PI do
  def estimate(_, 0, _) do
    receive do
      {:finish} ->
        :ok
    end
  end

  def estimate(points_per_actor, num_actors, receiver_pid) do
    actor_pid = spawn(MonteCarlo, :calculate, [])

    send(actor_pid, {receiver_pid, points_per_actor})

    estimate(points_per_actor, num_actors - 1, receiver_pid)
  end

  def estimate(points_per_actor, num_actors, total_points, initial_time) do
    estimate_pid = self()
    receiver_pid = spawn(__MODULE__, :receiver, [0, 0, points_per_actor, num_actors, total_points, initial_time, estimate_pid])

    actor_pid = spawn(MonteCarlo, :calculate, [])

    send(actor_pid, {receiver_pid, points_per_actor})

    estimate(points_per_actor, num_actors - 1, receiver_pid)
  end

  def receiver(points_inside_circle, count, points_per_actor, num_actors, total_points, initial_time, estimate_pid) when (count) == num_actors do
    pi = 4.0 * points_inside_circle / total_points

    finish_time = Time.utc_now()
    time_execution = Time.diff(finish_time, initial_time, :millisecond)

    diff_pi = abs(:math.pi - pi)

    csv_string = "#{total_points},#{num_actors},#{points_per_actor},#{points_inside_circle},#{pi},#{:math.pi},#{diff_pi},#{time_execution}\n"

    filename = "results.csv"

    {:ok, file} = File.open(filename, [:append])
    IO.write(file, csv_string)
    File.close(file)

    send(estimate_pid, {:finish})
  end

  def receiver(points_inside_circle, count, points_per_actor, num_actors, total_points, initial_time, estimate_pid) do
    receive do
      {inside_count} ->
        receiver(points_inside_circle + inside_count, count + 1, points_per_actor, num_actors, total_points, initial_time, estimate_pid)
    end
  end
end

defmodule MonteCarlo do
  def start do
    pid = spawn(__MODULE__, :calculate, [])
    pid
  end

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

        # IO.puts("Quantidade dentro do circulo: #{inside_count} para #{iterations} iteracoes")
        send(sender, {inside_count})
    end
  end
end

defmodule PI do
  def start(total_points, num_actors, parent_pid) do
    if num_actors > total_points do
      raise("Numero de pontos deve ser maior que numero de atores")
    end

    points_per_actor = trunc(total_points / num_actors)

    receiver_pid = spawn(__MODULE__, :receiver, [0, 0, num_actors, total_points, parent_pid])

    # IO.puts("Total atores: #{num_actors}.")
    # IO.puts("Cada ator calculará #{points_per_actor} pontos")

    spawn(__MODULE__, :estimate, [points_per_actor, num_actors, receiver_pid])
  end

  def estimate(_, 0, _) do :ok
  end

  def estimate(points, num_actors, receiver_pid) do
    actor_pid = MonteCarlo.start()

    send(actor_pid, {receiver_pid, points})

    estimate(points, num_actors - 1, receiver_pid)
  end

  def receiver(points_inside_circle, count, num_actors, total_points, parent_pid) when (count) == num_actors do
    # IO.puts("Total dentro do circulo: #{points_inside_circle} para #{num_actors} atores")
    estimated_pi = 4.0 * points_inside_circle / total_points

    # IO.puts("Valor estimado de PI para #{total_points} foi #{estimated_pi}")

    send(parent_pid, {:finish, total_points, points_inside_circle, num_actors, estimated_pi})
  end

  def receiver(points_inside_circle, count, num_actors, total_points, parent_pid) do
    receive do
      {inside_count} ->
        receiver(points_inside_circle + inside_count, count + 1, num_actors, total_points, parent_pid)
    end
  end
end

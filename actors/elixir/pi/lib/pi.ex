defmodule Randomize do
  def start do
    pid = spawn(__MODULE__, :calculate, [])
    pid
  end

  def calculate do
    receive do
      {sender} ->
        # Process.sleep(500)
        x = :rand.uniform()
        y = :rand.uniform()

        is_inside = (x * x + y * y) <= 1.0
        # IO.puts("Esta dentro do circulo: #{is_inside}...")

        send(sender, {is_inside})
    end
  end
end

defmodule PI do
  def start(total_points, parent_pid) do
    receiver_pid = spawn(__MODULE__, :receiver, [0, 1, total_points, parent_pid])
    pid = spawn(__MODULE__, :estimate, [total_points, total_points, receiver_pid])
    pid
  end

  def estimate(total_points, 0, receiver_pid) do :ok
  end

  def estimate(total_points, num_points, receiver_pid) do
    actor_pid = Randomize.start()

    send(actor_pid, {receiver_pid})
    # IO.puts("Enviando mensagem: #{num_points}...")

    estimate(total_points, num_points - 1, receiver_pid)
  end

  def receiver(points_inside_circle, count, total_points, parent_pid) when count == total_points do
    estimated_pi = 4.0 * points_inside_circle / total_points

    send(parent_pid, {:finish, estimated_pi})
  end

  def receiver(points_inside_circle, count, total_points, parent_pid) do
    receive do
      {true} ->
        receiver(points_inside_circle + 1, count + 1, total_points, parent_pid)
      {false} ->
        receiver(points_inside_circle, count + 1, total_points, parent_pid)
      {sender, :finish} ->
        send(sender, points_inside_circle)
    end
  end
end

total_points = 1_000_000
parent_pid = self()

PI.start(total_points, parent_pid)

receive do
  {:finish, estimated_pi} ->
    IO.puts("Valor estimado de PI para #{total_points} foi #{estimated_pi}")
end

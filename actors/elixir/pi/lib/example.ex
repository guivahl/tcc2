defmodule MonteCarlo do
  use GenServer

  def start_link do
    GenServer.start_link(__MODULE__, :ok, name: __MODULE__)
  end

  def init(:ok) do
    {:ok, %{}}
  end

  def handle_info({:send_messages, target_pid, count}, state) do
    send_messages(target_pid, count)
    {:noreply, state}
  end

  defp send_messages(_target_pid, 0), do: :ok
  defp send_messages(target_pid, count) do
    x = :rand.uniform()
    y = :rand.uniform()
    is_inside = x * x + y * y <= 1.0

    send(target_pid, {:message, x, y, is_inside})
    send_messages(target_pid, count - 1)
  end
end

defmodule Counter do
  use GenServer

  def start_link do
    {_, counter_pid} = GenServer.start_link(__MODULE__, %{:count => 0})
    counter_pid
  end

  def init(state) do
    {:ok, state}
  end

  def handle_info({:message, _x, _y, true}, state) do
    updated_state = Map.update!(state, :count, &(&1 + 1))
    {:noreply, updated_state}
  end

  def handle_info({:message, _x, _y, false}, state) do
    {:noreply, state}
  end

  def get_count(pid) do
    GenServer.call(pid, :get_count)
  end

  def handle_call(:get_count, _from, state) do
    {:reply, state[:count], state}
  end
end

iterations = 1_000_000

MonteCarlo.start_link()
counter_pid = Counter.start_link()

MonteCarlo.handle_info({:send_messages, counter_pid, iterations}, nil)

qt_inside_circle = Counter.get_count(counter_pid)

pi_estimation = 4.0 * qt_inside_circle / iterations

IO.puts("Valor estimado de para #{iterations} foi #{pi_estimation}")

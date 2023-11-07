defmodule PiTest do
  use ExUnit.Case
  doctest Pi

  test "greets the world" do
    assert Pi.hello() == :world
  end
end

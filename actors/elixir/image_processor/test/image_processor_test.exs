defmodule ImageProcessorTest do
  use ExUnit.Case
  doctest ImageProcessor

  test "greets the world" do
    assert ImageProcessor.hello() == :world
  end
end

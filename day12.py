from math import prod

with open('inputs/day12.txt') as input:
    puzzle_input: list[str] = input.read().split('\n\n')
    instructions: list[list[str]] = [i.split() for i in puzzle_input[-1].splitlines()]

shapes_areas: list[int] = [i.count('#') for i in puzzle_input[:-1]]
grid_areas: list[int] = [prod(map(int, i[0][:-1].split('x'))) for i in instructions]
shapes_to_fit: list[list[list[int]]] = [[[int(number), index] for index, number in enumerate(i[1:])] for i in instructions]
required_shape_areas: list[int] = [sum([(number * shapes_areas[index]) for number, index in i]) for i in shapes_to_fit]


def part1() -> int:
    return sum(grid - shape > 0 for grid, shape in zip(grid_areas, required_shape_areas))

print(part1())
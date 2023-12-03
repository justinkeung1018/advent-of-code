from typing import List

def parse(file: str) -> List[str]:
    with open(file, "r") as f:
        lines = f.readlines()
    for i in range(len(lines)):
        lines[i] = lines[i].replace("\n", "")
    return lines

def part_one(lines: List[str]) -> int:
    rows, cols = len(lines), len(lines[0])

    def is_adjacent_to_symbol(row: int, col: int) -> bool:
        def is_symbol(row: int, col: int) -> bool:
            if row < 0 or row >= rows or col < 0 or col >= cols:
                return False
            return not (lines[row][col].isdigit() or lines[row][col] == ".")
        return any([is_symbol(row + dr, col + dc) for dr in range(-1, 2) for dc in range(-1, 2)])
    
    sum_part_numbers = 0
    for row in range(rows):
        l = 0
        is_part_number = False
        for col in range(cols):
            if lines[row][col].isdigit():
                is_part_number = is_part_number or is_adjacent_to_symbol(row, col)
            else:
                if is_part_number:
                    sum_part_numbers += int(lines[row][l:col])
                l = col + 1
                is_part_number = False
        if is_part_number:
            sum_part_numbers += int(lines[row][l:])
    
    return sum_part_numbers

def part_two(lines: List[str]) -> int:
    rows, cols = len(lines), len(lines[0])

    def is_digit(row: int, col: int) -> bool:
        if row < 0 or row >= rows or col < 0 or col >= cols:
            return False
        return lines[row][col].isdigit()
    
    def search_for_num(row: int, col: int, used: List[List[bool]]) -> int:
        if not is_digit(row, col) or used[row][col]:
            return 0
        for l in range(col, -1, -1):
            if not is_digit(row, l):
                l = l + 1
                break
        for r in range(col, cols):
            if not is_digit(row, r):
                break
        for i in range(l, r):
            used[row][i] = True
        return int(lines[row][l:r])
        
    sum_gear_ratios = 0
    for row in range(rows):
        for col in range(cols):
            if lines[row][col] != "*":
                continue
            used = [[False for _ in range(cols)] for _ in range(rows)]
            adjacent_part_numbers = []
            for adj_row in range(row - 1, row + 2):
                for adj_col in range(col - 1, col + 2):
                    if not is_digit(adj_row, adj_col):
                        continue
                    if used[adj_row][adj_col]:
                        continue
                    adjacent_part_numbers.append(search_for_num(adj_row, adj_col, used))
            if len(adjacent_part_numbers) == 2:
                sum_gear_ratios += adjacent_part_numbers[0] * adjacent_part_numbers[1]
    
    return sum_gear_ratios

lines = parse("input.txt")
print(part_one(lines))
print(part_two(lines))
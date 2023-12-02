def part_one() -> None:
    def get_id(line: str) -> int:
        return int(line.split(":")[0].split(" ")[1])
    
    limits = {"red": 12, "green": 13, "blue": 14}
    
    with open("input.txt", "r") as f:
        id_sum = 0
        for line in f.readlines():
            is_possible = True
            for game_set in line.split(":")[1].split(";"):
                game_set = list(map(lambda string: string.strip(), game_set.split(",")))
                for cubes in game_set:
                    info = cubes.split(" ")
                    num = int(info[0])
                    colour = info[1]
                    if num > limits[colour]:
                        is_possible = False
                        break
                if not is_possible:
                    break
            if is_possible:
                id_sum += get_id(line)
        print(id_sum)

def part_two() -> None:
    with open("input.txt", "r") as f:
        power_sum = 0
        for line in f.readlines():
            min_cubes = {"red": 0, "green": 0, "blue": 0}
            for game_set in line.split(":")[1].split(";"):
                game_set = list(map(lambda string: string.strip(), game_set.split(",")))
                for cubes in game_set:
                    info = cubes.split(" ")
                    num = int(info[0])
                    colour = info[1]
                    min_cubes[colour] = max(min_cubes[colour], num)
            power = min_cubes["red"] * min_cubes["green"] * min_cubes["blue"]
            power_sum += power
        print(power_sum)
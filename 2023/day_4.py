def count_matching(line: str) -> int:
    line = line.replace("\n", "")
    numbers = line.split(":")[1].split("|")
    winning_numbers = set(map(int, numbers[0].strip().split()))
    my_numbers = set(map(int, numbers[1].strip().split()))
    return len(winning_numbers.intersection(my_numbers))

def part_one() -> int:
    total_points = 0
    with open("input.txt", "r") as f:
        for line in f.readlines():
            num_matching = count_matching(line)
            if num_matching:
                total_points += 2 ** (num_matching - 1)
    return total_points

def part_two() -> int:
    with open("input.txt", "r") as f:
        num_distinct_cards = len(f.readlines())
        f.seek(0)
        cards = [1] * num_distinct_cards
        for i in range(num_distinct_cards):
            line = f.readline()
            num_matching = count_matching(line)
            for j in range(i + 1, min(num_distinct_cards, i + num_matching + 1)):
                cards[j] += cards[i]
    return sum(cards)

print(part_one())
print(part_two())

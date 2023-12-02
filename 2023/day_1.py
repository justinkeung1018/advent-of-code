digits = {"one": 1, "two": 2, "three": 3, "four": 4, "five": 5, "six": 6, "seven": 7, "eight": 8, "nine": 9}

with open("input.txt", "r") as f:
    sum = 0
    for line in f.readlines():
        first_digit = last_digit = ""
        for i in range(len(line)):
            c = line[i]
            if c.isdigit():
                if not first_digit:
                    first_digit = int(c)
                last_digit = int(c)
            for digit in digits:
                substr = line[i:i + len(digit)]
                if substr == digit:
                    if not first_digit:
                        first_digit = digits[substr]
                    last_digit = digits[substr]
        calibration_value = first_digit * 10 + last_digit
        sum += calibration_value
    print(sum)
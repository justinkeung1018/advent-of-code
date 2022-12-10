main = do 
        contents <- readFile "./input.txt"
        let lines = splitBy "\n" contents
            indices = [19, 59, 99, 139, 179, 219]
            part1 = sum [signalStrengths lines !! i | i <- indices]
            part2 = draw lines 
        print part1
        print part2

draw :: [String] -> String
draw []
  = ""
draw lls@(l : ls)
  = map pixel positions
  where
    positions = zip [0..] (1 : vals lls 1) 
    pixel (crt, sprite)
      | abs(crt `mod` 40 - sprite) <= 1 = '#'
      | otherwise                       = '.'

signalStrengths :: [String] -> [Int]
signalStrengths lls
  = zipWith (*) (vals lls 1) [1..]

vals :: [String] -> Int -> [Int]
vals [] _
  = []
vals (l : ls) prev
  | i == "noop" = prev : vals ls prev 
  | i == "addx" = prev : res : vals ls res
  where
    (i : v) = words l 
    res = (read (head v) :: Int) + prev

splitBy :: String -> String -> [String]
splitBy dl s
  = splitBy' s ""
  where
    splitBy' "" s'
      = [s']
    splitBy' s@(c : cs) s'
      | pre == dl = s' : splitBy' suf ""
      | otherwise = splitBy' cs (s' ++ [c])
      where
        (pre, suf) = splitAt (length dl) s

-- ###...##..#..#.####..##..#....#..#..##..
-- #..#.#..#.#..#.#....#..#.#....#..#.#..#.
-- #..#.#....####.###..#....#....#..#.#....
-- ###..#.##.#..#.#....#.##.#....#..#.#.##.
-- #....#..#.#..#.#....#..#.#....#..#.#..#.
-- #.....###.#..#.#.....###.####..##...###..
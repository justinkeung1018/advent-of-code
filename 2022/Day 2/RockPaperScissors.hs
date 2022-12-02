import System.IO
import Data.Maybe

main = do 
        contents <- readFile "./input.txt"
        let lines = splitBy "\n" contents
            rounds = map (splitBy " ") lines
        print (totalScore1 rounds)
        print (totalScore2 rounds)

readInt :: String -> Int
readInt = read

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

lose = [("A", "Z"), ("B", "X"), ("C", "Y")]
win = [("A", "Y"), ("B", "Z"), ("C", "X")]
scores = [("X", 1), ("Y", 2), ("Z", 3)]
shapes = [("A", "X"), ("B", "Y"), ("C", "Z")]

totalScore1 :: [[String]] -> Int
totalScore1 [] 
  = 0
totalScore1 ([op, pl] : rnds)
  | pl == losing  = score + rest
  | pl == winning = 6 + score + rest
  | otherwise     = 3 + score + rest
  where
    score   = lookUp pl scores
    rest    = totalScore1 rnds
    losing  = lookUp op lose
    winning = lookUp op win

totalScore2 :: [[String]] -> Int
totalScore2 []
  = 0
totalScore2 ([op, pl] : rnds) 
  | pl == "X" = calc lose + rest
  | pl == "Y" = 3 + calc shapes + rest
  | pl == "Z" = 6 + calc win + rest
  where
    calc = flip lookUp scores . lookUp op
    rest = totalScore2 rnds

lookUp :: Eq a => a -> [(a, b)] -> b
lookUp k kvs = fromJust (lookup k kvs)
    
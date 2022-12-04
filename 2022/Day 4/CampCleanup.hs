main = do 
        contents <- readFile "./input.txt"
        let lines = splitBy "\n" contents
            pairs = map (map (map readStr . splitBy "-") . splitBy ",") lines
        print (numContains pairs)
        print (numOverlaps pairs)

contains :: [Int] -> [Int] -> Bool
contains [s1, e1] [s2, e2]
  = s1 <= s2 && e2 <= e1

numContains :: [[[Int]]] -> Int
numContains [] 
  = 0
numContains ([sec1, sec2] : prs)
  | sec1 `contains` sec2 || sec2 `contains` sec1 = 1 + rest
  | otherwise                                    = rest
  where
    rest = numContains prs
    
numOverlaps :: [[[Int]]] -> Int
numOverlaps []
  = 0
numOverlaps ([sec1, sec2] : prs)
  | sec1 `overlaps` sec2 = 1 + rest
  | otherwise            = rest
  where
    rest = numOverlaps prs
    overlaps sec1@[s1, e1] sec2@[s2, e2]
      = s2 <= e1 && e1 <= e2 || 
        s1 <= e2 && e2 <= e1 || 
        sec1 `contains` sec2 || 
        sec2 `contains` sec1

readStr :: String -> Int
readStr = read

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
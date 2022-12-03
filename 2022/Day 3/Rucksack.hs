import Data.List
import Data.Char

main = do 
        contents <- readFile "./input.txt"
        let lines = splitBy "\n" contents
        print (sumCommonPriority lines)
        print (sumBadgePriority lines)

priority c
  | ord 'a' <= ord c && ord c <= ord 'z' = ord c - ord 'a' + 1
  | ord 'A' <= ord c && ord c <= ord 'Z' = ord c - ord 'A' + 27

sumCommonPriority :: [String] -> Int
sumCommonPriority []
  = 0
sumCommonPriority (r : rs)
  = sum (map priority dups) + sumCommonPriority rs
  where
    dups = nub (fc `intersect` sc)
    (fc, sc) = splitAt (length r `div` 2) r

sumBadgePriority :: [String] -> Int
sumBadgePriority []
  = 0
sumBadgePriority (e1 : e2 : e3 : es)
  = sum (map priority dups) + sumBadgePriority es
  where
    dups = nub (e1 `intersect` (e2 `intersect` e3))

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
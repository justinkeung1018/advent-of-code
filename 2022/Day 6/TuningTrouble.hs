import Data.List

main = do 
        contents <- readFile "./input.txt"
        print (firstPacket contents)
        print (firstMessage contents)

firstPacket :: String -> Int
firstPacket ""
  = -1
firstPacket s@(c : cs)
  | (length . nub . take 4) s == 4 = 4
  | otherwise                      = 1 + firstPacket cs

firstMessage :: String -> Int
firstMessage ""
  = -1
firstMessage s@(c : cs)
  | (length . nub . take 14) s == 14 = 14
  | otherwise                        = 1 + firstMessage cs

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
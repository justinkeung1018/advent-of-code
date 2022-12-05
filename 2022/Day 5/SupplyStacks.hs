import Data.List
import Data.Char
import Data.Array
import Text.Read

type Procedure = (Int, (Int, Int))
type Crates = Array Int String

main = do 
        contents <- readFile "./input.txt"
        let [cLines, pLines] = splitBy "\n\n" contents
            cs = parseCrates cLines
            ps = parseProcedure pLines
            done1 = execute1 (reverse ps) cs
            done2 = execute2 (reverse ps) cs
        print (map head (elems done1))
        print (map head (elems done2))

execute1 :: [Procedure] -> Crates -> Crates
execute1 [] cs
  = cs
execute1 ((c, (from, to)) : ps) cs 
  = rest // [(from, from'), (to, reverse to' ++ (rest ! to))]
  where
    (to', from') = splitAt c (rest ! from)
    rest = execute1 ps cs 

execute2 :: [Procedure] -> Crates -> Crates
execute2 [] cs
  = cs
execute2 ((c, (from, to)) : ps) cs 
  = rest // [(from, from'), (to, to' ++ (rest ! to))]
  where
    (to', from') = splitAt c (rest ! from)
    rest = execute2 ps cs 
  
parseProcedure :: String -> [Procedure]
parseProcedure pLines
  = map parseProcedure' (splitBy "\n" pLines)
  where
    parseProcedure' line
      = (crates, (from, to))
      where
        [move, fromTo] = splitBy "from" line
        crates = readInt (drop (length "move") move)
        [from, to] = map readInt (splitBy "to" fromTo)

parseCrates :: String -> Crates
parseCrates cLines
  = array (1, numCrates) [(i, (trim . init) line) 
                          | i <- [1 .. numCrates], 
                            line <- transposed, 
                            readIntMaybe (filter isDigit line) == Just i]
  where
    transposed = (transpose . splitBy "\n") cLines
    numCrates = length transposed `div` 4 + 1

trim :: String -> String
trim = dropWhile isSpace

readIntMaybe :: String -> Maybe Int
readIntMaybe = readMaybe

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
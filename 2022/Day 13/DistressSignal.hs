import Data.List

main = do
        contents <- readFile "./input.txt"
        let packets = map (map parse . splitBy "\n") (splitBy "\n\n" contents)
            indices = [i | ([p1, p2], i) <- zip packets [1..], p1 < p2]
            dividers = [List [List [Int 2]], List [List [Int 6]]]
            sorted = sort (concat packets ++ dividers)
            dividerIndices = [i | (p, i) <- zip sorted [1..], 
                                  p `elem` dividers]
        print (sum indices)
        print (product dividerIndices)

data X = Int Int | List [X] | Bracket Char
         deriving (Show, Eq)

instance Ord X where
  compare (Int n) (Int n')
    | n > n'  = GT
    | n == n' = EQ
    | n < n'  = LT
  compare (List []) (List [])
    = EQ
  compare (List []) (List (_ : _))
    = LT
  compare (List (_ : _)) (List [])
    = GT
  compare (List (x : xs)) (List (x' : xs'))
    | cmp == EQ = compare (List xs) (List xs')
    | otherwise = cmp
    where
      cmp = compare x x'
  compare num@(Int n) list@(List ls)
    = compare (List [num]) list
  compare list@(List ls) num@(Int n)
    = compare list (List [num])

parse :: String -> X
parse s
  = parse' s [] (List [])
  where
    parse' :: String -> [X] -> X -> X
    parse' "" [stack] _
      = stack
    parse' ccs@(c : cs) stack result@(List res)
      | c == ']' && pop == Bracket '[' = parse' cs (List res : rest) (List [])
      | c == '['                       = parse' cs (Bracket c : stack) result
      | c == ']'                       = parse' ccs rest result'
      | c == ','                       = parse' cs stack result
      | otherwise                      = parse' after (Int next : stack) result
      where
        result'       = List (pop : res)
        (next, after) = nextInt ccs
        (pop : rest)  = stack

nextInt :: String -> (Int, String)
nextInt s
  = (read n, rest)
  where
    (n, rest) = nextInt' s
    nextInt' ""
      = ("", "")
    nextInt' ccs@(c : cs)
      | c == ','  = ("", cs)
      | c == ']'  = ("", ccs)
      | otherwise = (c : n', cs')
      where
        (n', cs') = nextInt' cs

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
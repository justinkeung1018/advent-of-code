import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RopeBridge {
    private final List<Pair<Integer, Integer>> rope = new ArrayList<>();
    private final Map<String, Pair<Integer, Integer>> directions = initDirections();

    private Map<String, Pair<Integer, Integer>> initDirections() {
        Map<String, Pair<Integer, Integer>> directions = new HashMap<>();
        directions.put("U", new Pair<>(0, 1));
        directions.put("D", new Pair<>(0, -1));
        directions.put("R", new Pair<>(1, 0));
        directions.put("L", new Pair<>(-1, 0));
        return directions;
    }

    public RopeBridge(int numKnots) {
        for (int i = 0; i < numKnots; i++) {
            rope.add(new Pair<>(0, 0));
        }
    }

    public int numTailPositions(List<Pair<String, Integer>> motions) {
        int numKnots = rope.size();
        Set<Pair<Integer, Integer>> visited = new HashSet<>();
        visited.add(rope.get(numKnots - 1));
        for (Pair<String, Integer> motion : motions) {
            String direction = motion.getKey();
            Pair<Integer, Integer> move = directions.get(direction);
            int dx = move.getKey();
            int dy = move.getValue();
            int steps = motion.getValue();
            for (int step = 0; step < steps; step++) {
                Pair<Integer, Integer> head = rope.get(0);
                rope.set(0, new Pair<>(head.getKey() + dx, head.getValue() + dy));
                for (int i = 1; i < numKnots; i++) {
                    Pair<Integer, Integer> knot = rope.get(i);
                    Pair<Integer, Integer> front = rope.get(i - 1);
                    int xDiff = front.getKey() - knot.getKey();
                    int yDiff = front.getValue() - knot.getValue();
                    if (Math.abs(xDiff) == 2) {
                       rope.set(i, new Pair<>(knot.getKey() + xDiff / 2, knot.getValue() + Integer.signum(yDiff)));
                    }
                    if (Math.abs(yDiff) == 2) {
                        rope.set(i, new Pair<>(knot.getKey() + Integer.signum(xDiff), knot.getValue() + yDiff / 2));
                    }
                }
                visited.add(rope.get(numKnots - 1));
            }
        }
        return visited.size();
    }

    @Override
    public String toString() {
        int size = 14;
        char[][] bridge = new char[size][size];
        for (char[] row : bridge) {
            Arrays.fill(row, '.');
        }
        int delta = size / 2;
        bridge[delta][delta] = 's';
        for (int i = 0; i < rope.size(); i++) {
            Pair<Integer, Integer> knot = rope.get(i);
            char c = (char) ('0' + i);
            if (i == 0) {
                c = 'H';
            }
            int x = knot.getKey() + delta;
            int y = knot.getValue() + delta;
            char curr = bridge[y][x];
            if (curr == '.' || curr == 's') {
                bridge[y][x] = c;
            }
            if (bridge[delta][delta] == '.') {
                bridge[delta][delta] = 's';
            }
        }
        List<String> rowStrs = new ArrayList<>();
        for (char[] row : bridge) {
            String rowStr = String.valueOf(row);
            rowStrs.add(0, rowStr);
        }
        return String.join("\n", rowStrs) + "\n";
    }

    public static void main(String[] args) {
        try {
            File file = new File("./input.txt");
            Scanner scanner = new Scanner(file);
            List<Pair<String, Integer>> motions = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split(" ");
                String direction = split[0];
                int steps = Integer.parseInt(split[1]);
                motions.add(new Pair<>(direction, steps));
            }
            RopeBridge part1 = new RopeBridge(2);
            System.out.println("Part 1: " + part1.numTailPositions(motions));
            RopeBridge part2 = new RopeBridge(10);
            System.out.println("Part 2: " + part2.numTailPositions(motions));
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }
    }
}

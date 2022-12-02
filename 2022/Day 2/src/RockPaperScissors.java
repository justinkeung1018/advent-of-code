import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javafx.util.Pair;

public class RockPaperScissors {
    private static final Map<String, Integer> playerShapes = initialisePlayerShapes();
    private static final Map<String, Integer> opponentShapes = initialiseOpponentShapes();

    private static Map<String, Integer> initialisePlayerShapes() {
        Map<String, Integer> shapes = new HashMap<>();
        shapes.put("X", 1);
        shapes.put("Y", 2);
        shapes.put("Z", 3);
        return shapes;
    }

    private static Map<String, Integer> initialiseOpponentShapes() {
        Map<String, Integer> shapes = new HashMap<>();
        shapes.put("A", 1);
        shapes.put("B", 2);
        shapes.put("C", 3);
        return shapes;
    }

    public static int totalScorePartOne(List<Pair<String, String>> rounds) {
        int totalScore = 0;
        for (Pair<String, String> round : rounds) {
            totalScore += scorePartOne(round);
        }
        return totalScore;
    }

    private static int scorePartOne(Pair<String, String> round) {
        String opponent = round.getKey();
        String player = round.getValue();
        int opponentScore = opponentShapes.get(opponent);
        int playerScore = playerShapes.get(player);
        int compare = Math.floorMod(playerScore - opponentScore, 3);
        return playerScore + switch (compare) {
            case 0 -> 3;
            case 1 -> 6;
            default -> 0;
        };
    }

    public static int totalScorePartTwo(List<Pair<String, String>> rounds) {
        int totalScore = 0;
        for (Pair<String, String> round : rounds) {
            totalScore += scorePartTwo(round);
        }
        return totalScore;
    }

    private static int scorePartTwo(Pair<String, String> round) {
        String opponent = round.getKey();
        int opponentScore = opponentShapes.get(opponent);
        String result = round.getValue();
        int resultScore = 0;
        int shapeScore = 0;
        if (result.equals("X")) {
            shapeScore = Math.floorMod(opponentScore - 1 + 2, 3) + 1;
        } else if (result.equals("Y")) {
            resultScore = 3;
            shapeScore = opponentScore;
        } else if (result.equals("Z")) {
            resultScore = 6;
            shapeScore = Math.floorMod(opponentScore - 1 + 1, 3) + 1;
        }
        return resultScore + shapeScore;
    }

    public static void main(String[] args) {
        try {
            File file = new File("./input.txt");
            Scanner scanner = new Scanner(file);
            List<Pair<String, String>> rounds = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                String[] roundStr = nextLine.split("\s");
                String opponent = roundStr[0];
                String player = roundStr[1];
                Pair<String, String> round = new Pair<>(opponent, player);
                rounds.add(round);
            }
            System.out.println("totalScore (Part 1): " + totalScorePartOne(rounds));
            System.out.println("totalScore (Part 2): " + totalScorePartTwo(rounds));
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }
    }
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TreeHouse {
    public static int numVisible(List<List<Integer>> trees) {
        int rows = trees.size();
        int cols = trees.get(0).size();

        int[][] top = new int[rows][cols];
        int[][] bottom = new int[rows][cols];
        int[][] left = new int[rows][cols];
        int[][] right = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            left[i][0] = -1;
            right[i][cols - 1] = -1;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                left[i][j] = Math.max(trees.get(i).get(j - 1), left[i][j - 1]);
                right[i][cols - j - 1] = Math.max(trees.get(i).get(cols - j), right[i][cols - j]);
            }
        }

        for (int j = 0; j < cols; j++) {
            top[0][j] = -1;
            bottom[rows - 1][j] = -1;
        }

        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                top[i][j] = Math.max(trees.get(i - 1).get(j), top[i - 1][j]);
                bottom[rows - i - 1][j] = Math.max(trees.get(rows - i).get(j), bottom[rows - i][j]);
            }
        }

        int numVisible = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int tree = trees.get(i).get(j);
                if (tree > top[i][j] || tree > bottom[i][j] || tree > left[i][j] || tree > right[i][j]) {
                    numVisible++;
                }
            }
        }
        return numVisible;
    }

    public static int maxScenicScore(List<List<Integer>> trees) {
        int rows = trees.size();
        int cols = trees.get(0).size();

        int maxScenicScore = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int top = distance(trees, trees.get(i).get(j), i, j, 0);
                int bottom = distance(trees, trees.get(i).get(j), i, j, 1);
                int left = distance(trees, trees.get(i).get(j), i, j, 2);
                int right = distance(trees, trees.get(i).get(j), i, j, 3);
                maxScenicScore = Math.max(maxScenicScore, top * bottom * left * right);
            }
        }
        return maxScenicScore;
    }

    private static int distance(List<List<Integer>> trees, int tree, int row, int col, int direction) {
        int rows = trees.size();
        int cols = trees.get(0).size();

        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return 0;
        }

        return switch (direction) {
            case 0 -> // top
                    row == 0 ? 0 : 1 + (tree > trees.get(row - 1).get(col) ? distance(trees, tree, row - 1, col, direction) : 0);
            case 1 -> // bottom
                    row == rows - 1 ? 0 : 1 + (tree > trees.get(row + 1).get(col) ? distance(trees, tree, row + 1, col, direction) : 0);
            case 2 -> // left
                    col == 0 ? 0 : 1 + (tree > trees.get(row).get(col - 1) ? distance(trees, tree, row, col - 1, direction) : 0);
            case 3 -> // right
                    col == cols - 1 ? 0 : 1 + (tree > trees.get(row).get(col + 1) ? distance(trees, tree, row, col + 1, direction) : 0);
            default -> 0;
        };
    }

    public static void main(String[] args) {
        try {
            File file = new File("./input.txt");
            Scanner scanner = new Scanner(file);
            List<List<Integer>> trees = new ArrayList<>();
            while (scanner.hasNextLine()) {
                List<Integer> row = new ArrayList<>();
                for (char c : scanner.nextLine().toCharArray()) {
                    row.add(c - '0');
                }
                trees.add(row);
            }
            System.out.println("Part 1: " + numVisible(trees));
            System.out.println("Part 2: " + maxScenicScore(trees));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}

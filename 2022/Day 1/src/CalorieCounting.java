import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CalorieCounting {
    public static int maxCalories(List<List<Integer>> elves) {
        int maxCalories = 0;
        for (List<Integer> elf : elves) {
            maxCalories = Math.max(sum(elf), maxCalories);
        }
        return maxCalories;
    }

    public static int topThree(List<List<Integer>> elves) {
        elves.sort((elf1, elf2) -> Integer.compare(sum(elf2), sum(elf1)));
        return sum(elves.get(0)) + sum(elves.get(1)) + sum(elves.get(2));
    }

    private static int sum(List<Integer> list) {
        int sum = 0;
        for (int num : list) {
            sum += num;
        }
        return sum;
    }

    public static void main(String[] args) {
        try {
            File file = new File("./input.txt");
            Scanner scanner = new Scanner(file);
            List<List<Integer>> elves = new ArrayList<>();
            List<Integer> elf = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                if (nextLine.equals("")) {
                    elves.add(elf);
                    elf = new ArrayList<>();
                } else {
                    elf.add(Integer.parseInt(nextLine));
                }
            }
            elves.add(elf);
            System.out.println("maxCalories: " + maxCalories(elves));
            System.out.println("topThree: " + topThree(elves));
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }
    }
}



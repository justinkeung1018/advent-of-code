import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileTree {
    Node root;

    Set<Node> part1 = new HashSet<>();
    PriorityQueue<Node> part2 = new PriorityQueue<>(Comparator.comparingInt(n -> n.size));

    public FileTree(Node root) {
        this.root = root;
    }

    public void computeSize() {
        computeSize(root);
    }

    private int computeSize(Node curr) {
        int size = curr.size;
        if (size != 0) {
            return size;
        }
        for (Node child : curr.children.values()) {
            size += computeSize(child);
        }
        if (size <= 100000) {
            part1.add(curr);
        }
        curr.size = size;
        return size;
    }

    public void dfs(Node curr, int spaceToFreeUp) {
        if (curr.size >= spaceToFreeUp) {
            part2.offer(curr);
        }
        for (Node child : curr.children.values()) {
            dfs(child, spaceToFreeUp);
        }
    }

    public static void main (String[] args) {
        try {
            File file = new File("./input.txt");
            Scanner scanner = new Scanner(file);
            FileTree tree = new FileTree(new Node("", true, 0));
            tree.root.children.put("/",  new Node("/", true, 0));
            Node curr = tree.root;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("$")) {
                    if (line.substring(2, 4).equals("cd")) {
                        String name = line.substring(5);
                        if (name.equals("..")) {
                            curr = curr.parent;
                        } else {
                            curr = curr.children.get(name);
                        }
                    }
                } else {
                    String[] info = line.split(" ");
                    String name = info[1];
                    if (curr.children.containsKey(name)) {
                        continue;
                    }
                    boolean isDir = info[0].equals("dir");
                    int size = isDir ? 0 : Integer.parseInt(info[0]);
                    Node child = new Node(curr, name, isDir, size);
                    curr.children.put(name, child);
                }
            }
            tree.computeSize();

            int part1 = 0;
            for (Node node : tree.part1) {
                part1 += node.size;
            }
            System.out.println("Part 1: " + part1);

            int unusedSpace = 70000000 - tree.root.size;
            int spaceToFreeUp = 30000000 - unusedSpace;
            tree.dfs(tree.root, spaceToFreeUp);
            Node part2 = tree.part2.peek();
            if (part2 == null) {
                System.out.println("Priority queue should not be null");
            } else {
                System.out.println("Part 2: " + part2.size);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }
    }
}

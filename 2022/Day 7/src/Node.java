import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Node {
    Node parent;
    Map<String, Node> children = new HashMap<>();
    int size;
    String name;
    boolean isDir;

    public Node(String name, boolean isDir, int size) {
        this.name = name;
        this.isDir = isDir;
        this.size = size;
    }

    public Node(Node parent, String name, boolean isDir, int size) {
        this.parent = parent;
        this.name = name;
        this.isDir = isDir;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return size == node.size && isDir == node.isDir && Objects.equals(parent, node.parent) &&  name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, size, name, isDir);
    }
}

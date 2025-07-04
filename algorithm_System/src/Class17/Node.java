package Class17;

import java.util.ArrayList;

public class Node {
    public int value;
    public int in;
    public int out;
    ArrayList<Node> nexts;
    ArrayList<Edge> edges;

    public Node(int value) {
        this.value = value;
        in = 0;
        out = 0;
        nexts = new ArrayList<>();
        edges = new ArrayList<>();
    }
}

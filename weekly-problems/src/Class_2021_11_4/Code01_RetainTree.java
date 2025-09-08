package Class_2021_11_4;

import java.util.ArrayList;//实现动态数组
import java.util.List;

//给定一棵多叉树的头节点head，每个节点只有黑白两色
//所有黑节点都保留，所有从头节点到黑节点路径上的点都保留
//返回处理后树的头节点
public class Code01_RetainTree {

    public static class Node {
        public int value;
        public boolean retain;
        public List<Node> nexts;//存储当前节点的所有子节点

        public Node(int v, boolean r) {
            value = v;
            retain = r;
            nexts = new ArrayList<>();
        }
    }

    public static Node retain(Node x) {
        if (x.nexts.isEmpty()) {
            return x.retain ? x : null;
        }
        //存储处理后需要保留的节点
        List<Node> newNexts = new ArrayList<>();
        for (Node next : x.nexts) {
            Node newNext = retain(next);
            if (newNext != null) {
                newNexts.add(newNext);
            }
        }
        //判断当前节点是否需要保留
        if (!newNexts.isEmpty() || x.retain) {
            x.nexts = newNexts;
            return x;
        }
        return null;
    }

    public static void preOrderPrint(Node head) {
        System.out.println(head.value);
        for (Node next : head.nexts) {
            preOrderPrint(next);
        }
    }

    public static void main(String[] args) {
        Node n1 = new Node(1, false);
        Node n2 = new Node(2, true);
        Node n3 = new Node(3, false);
        Node n4 = new Node(4, false);
        Node n5 = new Node(5, false);
        Node n6 = new Node(6, true);
        Node n7 = new Node(7, true);
        Node n8 = new Node(8, false);
        Node n9 = new Node(9, false);
        Node n10 = new Node(10, false);
        Node n11 = new Node(11, false);
        Node n12 = new Node(12, false);
        Node n13 = new Node(13, true);
        n1.nexts.add(n2);
        n1.nexts.add(n3);
        n2.nexts.add(n4);
        n1.nexts.add(n5);
        n3.nexts.add(n6);
        n3.nexts.add(n7);
        n6.nexts.add(n8);
        n6.nexts.add(n9);
        n6.nexts.add(n10);
        n7.nexts.add(n11);
        n7.nexts.add(n12);
        n9.nexts.add(n13);

        Node head = retain(n1);
        preOrderPrint(head);
    }
}

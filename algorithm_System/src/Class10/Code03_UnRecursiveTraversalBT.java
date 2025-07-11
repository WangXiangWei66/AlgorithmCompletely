package Class10;

import java.util.Stack;

public class Code03_UnRecursiveTraversalBT {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int v) {
            value = v;
        }
    }

    public static void pre(Node head) {
        System.out.println("pre-order");
        if (head != null) {
            Stack<Node> stack = new Stack<Node>();
            stack.push(head);
            while (!stack.isEmpty()) {
                head = stack.pop();
                System.out.println(head.value + " ");
                if (head.right != null) {
                    stack.push(head.right);
                }
                if (head.left != null) {
                    stack.push(head.left);
                }
            }
        }
        System.out.println();
    }

    public static void in(Node cur) {
        System.out.println("in-order");
        if (cur != null) {
            Stack<Node> stack = new Stack<Node>();
            while (!stack.isEmpty() || cur != null) {
                if (cur != null) {
                    stack.push(cur);
                    //cur不为空，便往左移
                    cur = cur.left;
                } else {
                    cur = stack.pop();
                    System.out.println(cur.value + " ");
                    //否则往右移
                    cur = cur.right;
                }
            }
        }
        System.out.println();
    }

    public static void pos1(Node head) {
        System.out.println("pos-order");
        if (head != null) {
            Stack<Node> s1 = new Stack<Node>();
            Stack<Node> s2 = new Stack<Node>();
            s1.push(head);
            while (!s1.isEmpty()) {
                head = s1.pop();// 头右左
                s2.push(head);
                if (head.left != null) {
                    s1.push(head.left);
                }
                if (head.right != null) {
                    s2.push(head.right);
                }
            }
            //左右头
            while (!s2.isEmpty()) {
                System.out.println(s2.pop().value + " ");
            }
        }
        System.out.println();
    }

    public static void pos2(Node h) {
        System.out.println("pos-order:");//二叉树的后序遍历
        if (h != null) {
            Stack<Node> stack = new Stack<Node>();
            stack.push(h);
            //c用来指向当前栈顶的节点
            Node c = null;
            while (!stack.isEmpty()) {
                c = stack.peek();
                if (c.left != null && h != c.left && h != c.right) {
                    stack.push(c.left);
                } else if (c.right != null && h != c.right) {
                    stack.push(c.right);
                } else {
                    System.out.println(stack.pop().value + " ");
                    //使用h判断左右子树，是否已经访问完
                    h = c;
                }
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);
        pre(head);
        System.out.println("==========");
        in(head);
        System.out.println("==========");
        pos1(head);
        System.out.println("==========");
        pos2(head);
        System.out.println("==========");
    }
}

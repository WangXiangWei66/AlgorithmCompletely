package Class11;

import java.util.ArrayList;
import java.util.List;

public class Code03_EncodeNaryTreeToBinaryTree {

    public static class Node {
        public int val;
        public List<Node> children;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }

        class Codec {
            //Encode an n-ary tree to a binary tree
            public TreeNode encode(Node root) {
                if (root == null) {
                    return null;
                }
                TreeNode head = new TreeNode(root.val);
                head.left = en(root.children);
                return head;
            }

            //将每个节点的第一个子节点作为其左子节点，将下一个兄弟节点作为其右子节点
            public TreeNode en(List<Node> children) {
                //指向二叉树的根节点
                TreeNode head = null;
                //用来遍历并连接当前层的兄弟节点
                TreeNode cur = null;
                for (Node child : children) {
                    TreeNode tNode = new TreeNode(child.val);
                    if (head == null) {
                        head = tNode;
                    } else {
                        cur.right = tNode;
                    }
                    cur = tNode;
                    cur.left = en(child.children);
                }

                return head;
            }

            //Decode your binary tree to an n-ary tree
            //二叉树中每个节点的右子链转换为多叉树的子节点列表。
            public List<Node> de(TreeNode root) {
                List<Node> children = new ArrayList<>();
                while (root != null) {
                    Node cur = new Node(root.val, de(root.left));
                    children.add(cur);
                    root = root.right;
                }
                return children;
            }
        }
    }

}

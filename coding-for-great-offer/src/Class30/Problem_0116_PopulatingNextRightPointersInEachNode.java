package Class30;

//给定一个完美二叉树，其所有叶子节点都在同一层，每个父节点都有两个子节点。二叉树定义如下：
//struct Node {
//int val;
//Node *left;
//Node *right;
//Node *next;
//}
//填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 NULL。
//初始状态下，所有next 指针都被设置为 NULL。
//进阶：
//这棵树如果是普通二叉树，该怎么做。
//你只能使用常量级额外空间。
//Leetcode题目 : https://leetcode.com/problems/populating-next-right-pointers-in-each-node/
public class Problem_0116_PopulatingNextRightPointersInEachNode {
    //时间复杂度为O(N)
    //空间复杂度为O(N),完美二叉树最后一层节点数约为二分之N
    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;
    }

    //填充每个节点的next指针
    public static Node connect(Node root) {
        if (root == null) {
            return root;
        }
        //创建自定义队列用于层序遍历
        MyQueue queue = new MyQueue();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node pre = null;//设置next指针用的节点
            int size = queue.size;
            for (int i = 0; i < size; i++) {
                Node cur = queue.poll();
                if (cur.left != null) {
                    queue.offer(cur.left);
                }
                if (cur.right != null) {
                    queue.offer(cur.right);
                }
                if (pre != null) {
                    pre.next = cur;
                }
                pre = cur;//更新pre，为下一次的循环做准备
            }
        }
        return root;
    }

    public static class MyQueue {
        public Node head;//队列的头
        public Node tail;//队列的尾
        public int size;//队列中元素的数量

        //将队列初始化
        public MyQueue() {
            head = null;
            tail = null;
            size = 0;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        //从队列的尾部加节点
        public void offer(Node cur) {
            size++;
            if (head == null) {
                head = cur;
                tail = cur;
            } else {
                tail.next = cur;
                tail = cur;
            }
        }

        //从队列的头部取出节点
        public Node poll() {
            size--;
            Node ans = head;
            head = head.next;
            ans.next = null;
            return ans;
        }
    }
}

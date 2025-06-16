package Class09;

import java.util.HashMap;

public class Code04_CopyListWithRandom {

    public static class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    public static Node copyRandomList(Node head) {
        //key 为老节点
        //value 为新节点
        HashMap<Node, Node> map = new HashMap<Node, Node>();
        Node cur = head;
        while (cur != null) {
            map.put(cur, new Node(cur.val));
            cur = cur.next;
        }
        cur = head;
        while (cur != null) {
            //cur老
            //map.get(cur)新
            //新.next-> cur.next 克隆节点找到
            map.get(cur).next = map.get(cur.next);
            map.get(cur).random = map.get(cur.random);
            cur = cur.next;
        }
        return map.get(head);
    }

    //带有随机指针的链表的深拷贝，且只遍历链表三次，时间复杂度O(N),额外空间复杂度为O(1)
    public static Node copyRandomList2(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        Node next = null;
        //1->2->3->null
        //1->1'->2->2'->3->3'
        while (cur != null) {
            next = cur.next;
            cur.next = new Node(cur.val);
            cur.next.next = next;
            cur = next;
        }
        //设置复制节点的随机指针
        cur = head;
        Node copy = null;
        //1  1'  2  2'  3  3'
        //依次设置 1' 2' 3' random指针
        while (cur != null) {
            next = cur.next.next;
            copy = cur.next;
            copy.random = cur.random != null ? cur.random.next : null;
            cur = next;
        }
        //将原链表和复制链表分离
        Node res = head.next;
        cur = head;
        //老 新混在一起，next 方向上 random正确
        //next方向上，把老新链表分离
        while (cur != null) {
            next = cur.next.next;
            copy = cur.next;
            cur.next = next;
            copy.next = next != null ? next.next : null;
            cur = next;
        }
        return res;
    }
}

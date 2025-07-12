package Class16;

//约瑟夫环讲解：https://blog.csdn.net/u011500062/article/details/72855826
//约瑟夫环的递归公式：f(n, m) = (f(n-1, m) + m-1) % n + 1
//给定一个链表头节点head，和一个正数m，从头开始，每次数到m就杀死当前节点
// 然后被杀节点的下一个节点从1开始重新数
// 周而复始直到只剩一个节点，返回最后的节点
//LeetCode题目： https://leetcode-cn.com/problems/yuan-quan-zhong-zui-hou-sheng-xia-de-shu-zi-lcof/
public class Code05_JosephusProblem {

    // 提交直接通过
    // 给定的编号是0~n-1的情况下，数到m就杀
    // 返回谁会活？
    public int lastRemaining1(int n, int m) {
        return getLive(n, m) - 1; //将结果转化为从0开始的编号
    }

    // 课上题目的设定是，给定的编号是1~n的情况下，数到m就杀
    // 返回谁会活？
    public static int getLive(int n, int m) {
        if (n == 1) {
            return 1;
        }

        return (getLive(n - 1, m) + m - 1) % n + 1;
    }

    // 提交直接通过
    // 给定的编号是0~n-1的情况下，数到m就杀
    // 返回谁会活？
    // 这个版本是迭代版
    //状态转移：当我们从 r-1 人环过渡到 r 人环时
    // 存活者的位置会发生变化。变化规律是：在上一轮的位置基础上，向前移动 m-1 步，再对当前环的大小取模。
    public int lastRemaining2(int n, int m) {
        int ans = 1; //存活者的编号从1开始，当前规模为r时，最后存活者编号
        int r = 1;//当前处理的人数，从1开始逐步递增
        while (r <= n) {
            ans = (ans + m - 1) % (r++) + 1;
        }
        return ans - 1;
    }

    public static class Node {
        public int value;
        public Node next;

        public Node(int data) {
            this.value = data;
        }
    }

    public static Node josephusKill1(Node head, int m) {
        if (head == null || head.next == head || m < 1) {
            return head;
        }
        //找到链表的尾节点，使得形成一个循环链表
        Node last = head;
        while (last.next != head) {
            last = last.next;
        }
        //当head与last指向同一个节点时，说明链表只剩一个人，循环结束
        int count = 0;
        while (head != last) {
            if (++count == m) {
                last.next = head.next;//将当前节点移走
                count = 0;
            } else {
                last = last.next;
            }
            head = last.next;//保证每次都指向当前要处理的节点
        }
        return head;
    }

    //本代码结合了递推和链表操作，本代码时间复杂度为O(N)
    public static Node josephusKill2(Node head, int m) {
        if (head == null || head.next == head || m < 1) {
            return head;
        }
        Node cur = head.next;
        int size = 1;
        while (cur != head) {
            size++;
            cur = cur.next;
        }
        //调递推公式计算存活者位置
        int live = getLive(size, m);
        while (--live != 0) {
            head = head.next;//保证head指向了存活节点
        }
        head.next = head;
        return head;
    }

    public static void printCircularList(Node head) {
        if (head == null) {
            return;
        }
        System.out.print("Circular List:" + head.value + "->");
        Node cur = head.next;
        while (cur != head) {
            System.out.print(cur.value + " ");
            cur = cur.next;
        }
        System.out.println(" " + head.value);
    }

    public static void main(String[] args) {
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = head1;
        printCircularList(head1);
        head1 = josephusKill1(head1, 3);
        printCircularList(head1);

        Node head2 = new Node(1);
        head2.next = new Node(2);
        head2.next.next = new Node(3);
        head2.next.next.next = new Node(4);
        head2.next.next.next.next = head2;

        printCircularList(head2);
        head2 = josephusKill2(head2, 3);
        printCircularList(head2);
    }
}

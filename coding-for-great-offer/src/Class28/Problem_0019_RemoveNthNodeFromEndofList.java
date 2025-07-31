package Class28;
//双指针策略：
//cur指针先出发，向前移动 n+1 步后，pre指针才开始从头部移动
//保持两个指针之间的距离为 n+1，当cur到达链表尾部时，pre正好指向要删除节点的前一个节点

//给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
//进阶：你能尝试使用一趟扫描实现吗？
//Leetcode题目：https://leetcode.com/problems/remove-nth-node-from-end-of-list/
public class Problem_0019_RemoveNthNodeFromEndofList {
    //定义链表的节点类
    public static class ListNode {
        public int val;//节点存储的值
        public ListNode next;//指向下一个节点的引用
    }

    //删除链表倒数第n个节点并返回头节点
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode cur = head;//遍历指针，从链表头开始
        ListNode pre = null;//定位到要删除节点的前一个节点
        while (cur != null) {
            n--;//计算当前节点距离尾部的位置
            if (n == -1) {
                pre = head;//此时pre开始从头部跟随移动
            }
            if (n < -1) {
                pre = pre.next; //当n小于-1时，pre和cur保持n+1的距离继续同步移动
            }
            cur = cur.next;//此时cur指针继续往前移动
        }
        if (n > 0) {
            return head;//遍历都结束了，n还是大于0，说明没有要删除的节点，返回头节点即可
        }
        if (pre == null) {
            return head.next;//说明要删除的就是头节点
        }
        pre.next = pre.next.next;//否则删除的是pre的下一个节点
        return head;
    }
}

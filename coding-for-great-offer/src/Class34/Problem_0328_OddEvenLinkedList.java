package Class34;

//给定一个单链表，把所有的奇数节点和偶数节点分别排在一起。请注意，这里的奇数节点和偶数节点指的是节点编号的奇偶性，而不是节点的值的奇偶性。
//请尝试使用原地算法完成。你的算法的空间复杂度应为 O(1)，时间复杂度应为 O(nodes)，nodes 为节点总数。
//示例 1:
//输入: 1->2->3->4->5->NULL
//输出: 1->3->5->2->4->NULL
//示例 2:
//输入: 2->1->3->5->6->4->7->NULL
//输出: 2->3->6->7->1->5->4->NULL
//说明:
//应当保持奇数节点和偶数节点的相对顺序。
//链表的第一个节点视为奇数节点，第二个节点视为偶数节点，以此类推。
//Leetcode题目 : https://leetcode.com/problems/odd-even-linked-list/
public class Problem_0328_OddEvenLinkedList {

    public static class ListNode {
        int val;
        ListNode next;
    }

    public ListNode oddEvenList(ListNode head) {
        ListNode firstOdd = null;//记录第一个奇数节点
        ListNode firstEven = null;//记录第一个偶数节点
        ListNode odd = null;//当前奇数链表的尾节点
        ListNode even = null;//当前偶数链表的尾节点
        ListNode next = null;//临时存储下一个节点
        int count = 1;//记录当前节点的位置编号
        while (head != null) {
            next = head.next;
            head.next = null;
            if ((count & 1) == 1) {
                firstOdd = firstOdd == null ? head : firstOdd;
                if (odd != null) {
                    odd.next = head;
                }
                odd = head;//更新奇数链表的尾节点
            } else {
                firstEven = firstEven == null ? head : firstEven;
                if (even != null) {
                    even.next = head;
                }
                even = head;
            }
            count++;
            head = next;
        }
        if (odd != null) {
            odd.next = firstEven;//将奇数的尾节点连接偶数的头节点
        }
        return firstOdd != null ? firstOdd : firstEven;
    }
}

package Class31;

//给你链表的头结点head，请将其按升序排列并返回，排序后的链表头节点。
//进阶：你可以在O(nlogn) 时间复杂度和常数级空间复杂度下，对链表进行排序吗？
//Leetcode题目 : https://leetcode.com/problems/sort-list/
public class Problem_0148_SortList {

    public static class ListNode {
        int val;
        ListNode next;

        public ListNode(int v) {
            val = v;
        }
    }

    //时间复杂度O（N * logN），额外空间复杂度O（1）
    public static ListNode sortList1(ListNode head) {
        int N = 0;
        ListNode cur = head;
        //计算链表的长度
        while (cur != null) {
            N++;
            cur = cur.next;
        }
        ListNode h = head;//排序后链表的头节点
        ListNode teamFirst = head;//当前处理的子链表的起始节点
        ListNode pre = null;//上一组合并后的尾节点
        //子链表从1开始，每次翻倍
        for (int len = 1; len < N; len <<= 1) {
            while (teamFirst != null) {
                //将链表按照当前长度分割为两组，返回相关节点
                ListNode[] hthtn = hthtn(teamFirst, len);
                //将分割后的两组链表合并
                ListNode[] mhmt = merge(hthtn[0], hthtn[1], hthtn[2], hthtn[3]);
                if (h == teamFirst) {
                    h = mhmt[0];
                    pre = mhmt[1];//更新前驱节点为当前合并的尾节点
                } else {
                    pre.next = mhmt[0];
                    pre = mhmt[1];
                }
                teamFirst = hthtn[4];//处理下一组子链表
            }
            teamFirst = h;
            pre = null;
        }
        return h;
    }

    public static ListNode[] hthtn(ListNode teamFirst, int len) {
        ListNode ls = teamFirst;//第一组的头节点
        ListNode le = teamFirst;//第一组的尾节点
        ListNode rs = null;//第二组的头节点
        ListNode re = null;//第二组的尾节点
        ListNode next = null;//下一组的起始节点
        int pass = 0;//用于确定节点归属的计数器
        while (teamFirst != null) {
            pass++;
            if (pass <= len) {
                le = teamFirst;
            }
            //找到第二组的头节点
            if (pass == len + 1) {
                rs = teamFirst;
            }
            if (pass > len) {
                re = teamFirst;
            }
            if (pass == (len << 1)) {
                break;
            }
            teamFirst = teamFirst.next;
        }
        le.next = null;//断开第一组与后续节点的连接
        //处理第二组的尾节点和下一组的起始节点
        if (re != null) {
            next = re.next;
            re.next = null;
        }
        //返回分割结果：[第一组头, 第一组尾, 第二组头, 第二组尾, 下一组起始]
        return new ListNode[]{ls, le, rs, re, next};
    }

    public static ListNode[] merge(ListNode ls, ListNode le, ListNode rs, ListNode re) {
        //第二组为空，直接返回第一组
        if (rs == null) {
            return new ListNode[]{ls, le};
        }
        ListNode head = null;//合并后的头节点
        ListNode pre = null;//当前处理节点的前驱
        ListNode cur = null;//当前处理的节点
        ListNode tail = null;//合并后的尾节点
        //双指针合并法
        while (ls != le.next && rs != re.next) {
            if (ls.val <= rs.val) {
                cur = ls;
                ls = ls.next;
            } else {
                cur = rs;
                rs = rs.next;
            }
            if (pre == null) {
                head = cur;
                pre = cur;
            } else {
                pre.next = cur;
                pre = cur;
            }
        }
        if (ls != le.next) {
            while (ls != le.next) {
                pre.next = ls;
                pre = ls;
                tail = ls;
                ls = ls.next;
            }
        } else {
            while (rs != re.next) {
                pre.next = rs;
                pre = rs;
                tail = rs;
                rs = rs.next;
            }
        }
        return new ListNode[]{head, tail};
    }

    //链表的快速排序，时间复杂度为O(N * logN)
    //额外空间复杂度为O(logN)
    public static ListNode sortList2(ListNode head) {
        int n = 0;
        ListNode cur = head;
        while (cur != null) {
            cur = cur.next;
            n++;
        }
        //最终返回排序后的头节点
        return process(head, n).head;
    }

    public static class HeadAndTail {
        public ListNode head;//子链表的头节点
        public ListNode tail;//子链表的尾节点

        public HeadAndTail(ListNode h, ListNode t) {
            head = h;
            tail = t;
        }
    }

    public static HeadAndTail process(ListNode head, int n) {
        if (n == 0) {
            return new HeadAndTail(head, head);
        }
        //随机选择一个快速排序的基准点
        int index = (int) (Math.random() * n);
        ListNode cur = head;
        while (index-- != 0) {
            cur = cur.next;
        }
        //以pivot为中心开始分割链表
        Record r = partition(head, cur);
        //递归处理左右两部分
        HeadAndTail lht = process(r.lhead, r.lsize);
        HeadAndTail rht = process(r.rhead, r.rsize);
        if (lht.tail != null) {
            lht.tail.next = r.mhead;//左半部分的尾，连接中间部分的头
        }
        r.mtail.next = rht.head;
        //确定返回的头尾节点
        return new HeadAndTail(lht.head != null ? lht.head : r.mhead, rht.tail != null ? rht.tail : r.mtail);
    }


    public static class Record {
        public ListNode lhead;//左半部分的头
        public int lsize;//左部分的长度
        public ListNode rhead;
        public int rsize;
        public ListNode mhead;
        public ListNode mtail;

        public Record(ListNode lh, int ls, ListNode rh, int rs, ListNode mh, ListNode mt) {
            lhead = lh;
            lsize = ls;
            rhead = rh;
            rsize = rs;
            mhead = mh;
            mtail = mt;
        }
    }

    public static Record partition(ListNode head, ListNode mid) {
        ListNode lh = null;//左部分的头
        ListNode lt = null;
        int ls = 0;//左部分的大小
        ListNode mh = null;
        ListNode mt = null;
        ListNode rh = null;
        ListNode rt = null;
        int rs = 0;
        ListNode tmp = null;
        //遍历链表，将节点分割成三部分
        while (head != null) {
            tmp = head.next;//暂时保存下一个节点
            head.next = null;
            if (head.val < mid.val) {
                if (lh == null) {
                    lh = head;
                    lt = head;
                } else {
                    lt.next = head;
                    lt = head;
                }
                ls++;
            } else if (head.val > mid.val) {
                if (rh == null) {
                    rh = head;
                    rt = head;
                } else {
                    rt.next = head;
                    rt = head;
                }
                rs++;
            } else {
                if (mh == null) {
                    mh = head;
                    mt = head;
                } else {
                    mt.next = head;
                    mt = head;
                }
            }
            head = tmp;//处理下一个节点
        }
        return new Record(lh, ls, rh, rs, mh, mt);
    }
}

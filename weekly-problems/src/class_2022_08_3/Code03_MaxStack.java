package class_2022_08_3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

//设计一个最大栈数据结构，既支持栈操作，又支持查找栈中最大元素。
//实现MaxStack类：
//MaxStack()初始化栈对象
//void push(int x)将元素 x 压入栈中。
//int pop()移除栈顶元素并返回这个元素。
//int top()返回栈顶元素，无需移除。
//int peekMax()检索并返回栈中最大元素，无需移除。
//int popMax()检索并返回栈中最大元素，并将其移除。
//如果有多个最大元素，只要移除 最靠近栈顶 的那个。
//测试链接 : https://leetcode.cn/problems/max-stack/
public class Code03_MaxStack {

    class MaxStack {
        //记录元素插入顺序(区分相同值的元素)
        public int cnt;
        //大根堆来维护最大元素
        public HeapGreater<Node> heap;
        //双向链表的头指针
        public Node top;

        public MaxStack() {
            cnt = 0;
            heap = new HeapGreater<>(new NodeComparator());
            top = null;
        }

        public void push(int x) {
            Node cur = new Node(x, ++cnt);
            heap.push(cur);
            if (top == null) {
                top = cur;
            } else {
                //将新节点加入栈顶
                top.last = cur;
                cur.next = top;
                top = cur;
            }
        }

        public int pop() {
            Node ans = top;
            //只有一个元素,将栈清空
            if (top.next == null) {
                top = null;
            } else {
                top = top.next;
                top.last = null;
            }
            heap.remove(ans);
            return ans.val;
        }
        //直接返回栈顶元素的值
        public int top() {
            return top.val;
        }

        public int peekMax() {
            return heap.peek().val;
        }

        public int popMax() {
            Node ans = heap.pop();
            //最大元素是栈顶
            if (ans == top) {
                if (top.next == null) {
                    top = null;
                } else {
                    top = top.next;
                    top.last = null;
                }
            } else {
                if (ans.next != null) {
                    ans.next.last = ans.last;
                }
                if (ans.last != null) {
                    ans.last.next = ans.next;
                }
            }
            return ans.val;
        }


        class Node {
            public int val;//节点存储的值
            public int cnt;//插入顺序计数器
            public Node next;
            public Node last;

            public Node(int v, int c) {
                val = v;
                cnt = c;
            }
        }

        class NodeComparator implements Comparator<Node> {

            @Override
            public int compare(Node o1, Node o2) {
                return o1.val != o2.val ? (o2.val - o1.val) : (o2.cnt - o1.cnt);
            }
        }

        class HeapGreater<T> {
            private ArrayList<T> heap;//使用动态数组存储堆元素
            private HashMap<T, Integer> indexMap;//记录元素在堆中的索引
            private int heapSize;
            private Comparator<? super T> comp;

            public HeapGreater(Comparator<? super T> c) {
                heap = new ArrayList<>();
                indexMap = new HashMap<>();
                heapSize = 0;
                comp = c;
            }

            public T peek() {
                return heap.get(0);
            }

            public void push(T obj) {
                heap.add(obj);
                indexMap.put(obj, heapSize);
                //维护堆结构
                heapInsert(heapSize++);
            }

            public T pop() {
                T ans = heap.get(0);
                swap(0, heapSize - 1);
                indexMap.remove(ans);
                heap.remove(--heapSize);
                //维护堆结构
                heapify(0);
                return ans;
            }

            public void remove(T obj) {
                //用最后一个元素为替换者
                T replace = heap.get(heapSize - 1);
                int index = indexMap.get(obj);
                indexMap.remove(obj);
                //删除最后一个元素
                heap.remove(--heapSize);
                if (obj != replace) {
                    //替换元素到目标索引
                    heap.set(index, replace);
                    indexMap.put(replace, index);
                    resign(replace);
                }
            }

            private void resign(T obj) {
                heapInsert(indexMap.get(obj));
                heapify(indexMap.get(obj));
            }

            private void heapInsert(int index) {
                //当前节点比父节点更优
                while (comp.compare(heap.get(index), heap.get((index - 1) / 2)) < 0) {
                    swap(index, (index - 1) / 2);
                    index = (index - 1) / 2;
                }
            }

            private void heapify(int index) {
                int left = index * 2 + 1;
                while (left < heapSize) {
                    //找左右孩子更优的
                    int best = left + 1 < heapSize && comp.compare(heap.get(left + 1), heap.get(left)) < 0 ? (left + 1) : left;
                    best = comp.compare(heap.get(best), heap.get(index)) < 0 ? best : index;
                    if (best == index) {
                        break;
                    }
                    swap(best, index);
                    index = best;
                    left = index * 2 + 1;
                }
            }

            private void swap(int i, int j) {
                T o1 = heap.get(i);
                T o2 = heap.get(j);
                heap.set(i, o2);
                heap.set(j, o1);
                //更新索引映射
                indexMap.put(o2, i);
                indexMap.put(o1, j);
            }
        }
    }
}

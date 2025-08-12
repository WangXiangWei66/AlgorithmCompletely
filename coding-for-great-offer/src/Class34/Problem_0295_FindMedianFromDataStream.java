package Class34;

import java.util.PriorityQueue;

//中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。
//例如，
//[2,3,4]的中位数是 3
//[2,3] 的中位数是 (2 + 3) / 2 = 2.5
//设计一个支持以下两种操作的数据结构：
//void addNum(int num) - 从数据流中添加一个整数到数据结构中。
//double findMedian() - 返回目前所有元素的中位数。
//Leetcode题目 : https://leetcode.com/problems/find-median-from-data-stream/
public class Problem_0295_FindMedianFromDataStream {

    class MedianFinder {
        private PriorityQueue<Integer> maxH;//大根堆，存储较小的一半元素
        private PriorityQueue<Integer> minH;//小根堆，存储较大的一半元素

        public MedianFinder() {
            maxH = new PriorityQueue<>((a, b) -> (b - a));
            minH = new PriorityQueue<>((a, b) -> (a - b));
        }

        public void addNum(int num) {
            if (maxH.isEmpty() || maxH.peek() >= num) {
                maxH.add(num);
            } else {
                minH.add(num);
            }
            balance();//平衡两个堆的大小
        }

        public double findMedian() {
            if (maxH.size() == minH.size()) {
                return (double) (maxH.peek() + minH.peek()) / 2;
            } else {
                //返回元素较多的哪个堆顶的值
                return maxH.size() > minH.size() ? maxH.peek() : minH.peek();
            }
        }

        private void balance() {
            if (Math.abs(maxH.size() - minH.size()) == 2) {
                if (maxH.size() > minH.size()) {
                    minH.add(maxH.poll());
                } else {
                    minH.add(minH.poll());
                }
            }
        }
    }


}

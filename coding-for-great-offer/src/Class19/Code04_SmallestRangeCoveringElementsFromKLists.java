package Class19;
//该算法实现了寻找最小区间的算法，该区间包含k个有序列表中的至少一个元素
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

//你有k个非递减排列的整数列表。找到一个最小区间，使得k个列表中的每个列表至少有一个数包含在其中
//我们定义如果b-a < d-c或者在b-a == d-c时a < c，则区间 [a,b] 比 [c,d] 小。
//Leetcode题目：https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/
public class Code04_SmallestRangeCoveringElementsFromKLists {
    //包括值、所在列表的ID和索引位置
    public static class Node {
        public int value;
        public int arrid;
        public int index;

        public Node(int v, int ai, int i) {
            value = v;
            arrid = ai;
            index = i;
        }
    }
    //比较器：首先按照值比较，值相同按照列表ID排序
    public static class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return o1.value != o2.value ? o1.value - o2.value : o1.arrid - o2.arrid;
        }
    }

    public static int[] smallestRange(List<List<Integer>> nums) {
        int N = nums.size();//获取列表的数量K
        //使用TreeSet来维护当前候选区的元素，按值排序
        TreeSet<Node> orderSet = new TreeSet<>(new NodeComparator());
        //初始化：将每个列表的第一个元素加入TreeSet：先获取第i个列表，在获取第i个列表的第一个元素
        //然后创建一个新的节点，记录该元素的值，所在列表编号i、以及索引位置0
        for (int i = 0; i < N; i++) {
            orderSet.add(new Node(nums.get(i).get(0), i, 0));
        }
        boolean set = false;//标记有效区间是否已经找到
        //区间的左右端点
        int a = 0;
        int b = 0;
        //当TreeSet中包含了所有的元素时，继续循环
        while (orderSet.size() == N) {
            //找到当前最小与最大节点
            Node min = orderSet.first();
            Node max = orderSet.last();
            //如果是第一次找到该区间，或者是当前的区间更优
            if (!set || (max.value - min.value < b - a)) {
                set = true;
                a = min.value;
                b = max.value;
            }
            //将当前的最小节点移除
            min = orderSet.pollFirst();
            //将min的下一个元素尝试加入TreeSet
            int arrid = min.arrid;
            int index = min.index + 1;
            //如果还有下一个元素，则加入TreeSet
            if (index != nums.get(arrid).size()) {
                orderSet.add(new Node(nums.get(arrid).get(index), arrid, index));
            }
        }
        return new int[]{a, b};
    }
}

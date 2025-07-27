package Class26;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

//有三个有序数组，分别在三个数组中挑出3个数，x、y、z。返回 |x-y| + |y-z| + |z-x|最小是多少？
//Leetcode题目：https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/
public class Code01_MinRange {

    public static class Node {
        public int val;//元素的具体值
        public int arr;// 元素所属列表的索引（第几个列表）
        public int idx;// 元素在所属列表中的位置索引

        // 构造方法：初始化元素值、所属列表索引、在列表中的位置
        public Node(int value, int arrIndex, int index) {
            val = value;
            arr = arrIndex;
            idx = index;
        }
    }

    public static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node a, Node b) {
            return a.val != b.val ? (a.val - b.val) : (a.arr - b.arr);
        }
    }

    public static int[] smallestRange(List<List<Integer>> nums) {
        int N = nums.size();// 输入列表的数量（需要覆盖的列表总数）
        // 创建TreeSet，按NodeComparator规则排序，存储当前区间内的元素
        TreeSet<Node> set = new TreeSet<>(new NodeComparator());
        // 初始化：将每个列表的第一个元素加入set（确保初始区间覆盖所有列表）
        for (int i = 0; i < N; i++) {
            set.add(new Node(nums.get(i).get(0), i, 0));
        }
        int r = Integer.MAX_VALUE;//记录当前区间的最小长度
        //区间的左右端点
        int a = 0;
        int b = 0;
        // 循环：只要set中包含所有N个列表的元素（即覆盖所有列表），就继续寻找更小区间
        while (set.size() == N) {
            Node max = set.last();//set中的最大元素
            Node min = set.pollFirst();//set中的最小元素
            // 若当前区间长度（max.val - min.val）小于已知最小长度，更新结果
            if (max.val - min.val < r) {
                r = max.val - min.val;
                a = min.val;
                b = max.val;
            }
            // 若当前最小元素所在的列表还有下一个元素，将下一个元素加入set（扩展区间）
            if (min.idx < nums.get(min.arr).size() - 1) {
                set.add(new Node(nums.get(min.arr).get(min.idx + 1), min.arr, min.idx + 1));
            }
        }
        return new int[]{a, b};
    }

    //距离和等价于 2 * max(a,b,c) - 2 * min(a,b,c)，由三元组中的最大值和最小值决定，与中间值无关。
    public static int minRange1(int[][] m) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < m[0].length; i++) {
            for (int j = 0; j < m[1].length; j++) {
                for (int k = 0; k < m[2].length; k++) {
                    min = Math.min(min, Math.abs(m[0][i] - m[1][j]) + Math.abs(m[1][j] - m[2][k]) + Math.abs(m[2][k] - m[0][i]));
                }
            }
        }
        return min;
    }

    public static int minRange2(int[][] matrix) {
        int N = matrix.length;
        TreeSet<Node> set = new TreeSet<>(new NodeComparator());
        for (int i = 0; i < N; i++) {
            set.add(new Node(matrix[i][0], i, 0));
        }
        int min = Integer.MAX_VALUE;
        while (set.size() == N) {
            //当前集合的最大值是set的最后一个元素，最小值是第一个元素
            //计算当前差值（最大值-最小值），更新min
            min = Math.min(min, set.last().val - set.first().val);
            Node cur = set.pollFirst();//移除当前集合中的最小值元素（为了尝试缩小范围，需要扩展该元素所在数组的下一个元素）
            if (cur.idx < matrix[cur.arr].length - 1) {
                set.add(new Node(matrix[cur.arr][cur.idx + 1], cur.arr, cur.idx + 1));
            }
        }
        return min << 1;
    }

    public static int[][] generateRandomMatrix(int n, int v) {
        int[][] m = new int[3][];
        int s = 0;//存储每个子数组的随机长度
        for (int i = 0; i < 3; i++) {
            s = (int) (Math.random() * n) + 1;
            m[i] = new int[s];//初始化当前子数组的长度为s
            for (int j = 0; j < s; j++) {
                m[i][j] = (int) (Math.random() * v);
            }
            Arrays.sort(m[i]);//对当前子数组进行升序排序
        }
        return m;
    }

    public static void main(String[] args) {
        int n = 20;
        int v = 200;
        int testTime = 1000000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            int[][] m = generateRandomMatrix(n, v);
            int ans1 = minRange1(m);
            int ans2 = minRange2(m);
            if (ans1 != ans2) {
                System.out.println("Oops");
            }
        }
        System.out.println("test finish!");
    }
}

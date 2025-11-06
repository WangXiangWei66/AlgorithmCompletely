package class_2022_09_1;

import java.util.PriorityQueue;

//来自学员问题
//给你一个长度为n的数组，并询问q次
//每次询问区间[l,r]之间是否存在小于等于k个数的和大于等于x
//每条查询返回true或者false
//1 <= n, q <= 10^5
//k <= 10
//1 <= x <= 10^8
public class Code04_QueryTopKSum {

    public static class SegmentTree {
        private int n;//线段树叶子节点数
        private int k;
        // max[rt][i]：第rt个线段树节点对应的区间中，第i+1大的元素（i从0到k-1）
        // 例：max[rt][0]是该区间最大元素，max[rt][1]是第二大，...，max[rt][k-1]是第k大
        private int[][] max;
        // query[rt][i]：查询时临时存储第rt个节点的前k大元素（避免重复计算，复用合并逻辑）
        private int[][] query;

        public SegmentTree(int[] arr, int K) {
            n = arr.length;
            k = K;
            //每个节点都存k个元素
            max = new int[(n + 1) << 2][k];
            query = new int[(n + 1) << 2][k];
            //逐个插入数组元素来构建选段树
            for (int i = 0; i < n; i++) {
                update(i, arr[i]);
            }
        }

        //获取区间前k大元素和
        public int topKSum(int l, int r) {
            // 收集区间[l+1, r+1]（转换为线段树1-based索引）的前k大元素，存入query[1]（根节点）
            collect(l + 1, r + 1, 1, n, 1);
            // 计算query[1]中前k大元素的和（query[1]是整个区间的前k大元素）
            int sum = 0;
            for (int num : query[1]) {
                sum += num;
            }
            return sum;
        }

        //插入与修改数组的元素
        private void update(int i, int v) {
            update(i + 1, i + 1, v, 1, n, 1);
        }

        //l...r:当前节点rt对应的区间
        private void update(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) {
                //因为是叶子节点
                max[rt][0] = C;
                return;
            }
            int mid = (l + r) >> 1;
            if (L <= mid) {
                update(L, R, C, l, mid, rt << 1);
            }
            if (R > mid) {
                update(L, R, C, mid + 1, r, rt << 1 | 1);
            }
            //左右子节点更新后,取合并他们的前k大元素
            merge(max[rt], max[rt << 1], max[rt << 1 | 1]);
        }

        private void merge(int[] father, int[] left, int[] right) {
            // i：father的索引（0到k-1）
            // p1：left的指针（当前已取到第p1个元素）
            // p2：right的指针（当前已取到第p2个元素）
            for (int i = 0, p1 = 0, p2 = 0; i < k; i++) {
                //左区间已经取完了所有元素
                if (left == null || p1 == k) {
                    father[i] = right[p2++];
                    //右区间已取完所有元素
                } else if (right == null || p2 == k) {
                    father[i] = left[p1++];
                    //都有元素取较大的
                } else {
                    father[i] = left[p1] >= right[p2] ? left[p1++] : right[p2++];
                }
            }
        }

        //查询时收集区间覆盖的节点并合并
        // 收集逻辑：查询区间[L,R]（1-based）的前k大元素，存入query[rt]
        // l,r：当前节点rt对应的区间（1-based）
        // rt：当前线段树节点编号（1-based）
        private void collect(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) {
                for (int i = 0; i < k; i++) {
                    query[rt][i] = max[rt][i];
                }
            } else {
                int mid = (l + r) >> 1;
                boolean leftUpdate = false;
                boolean rightUpdate = false;
                if (L <= mid) {
                    leftUpdate = true;
                    collect(L, R, l, mid, rt << 1);
                }
                if (R > mid) {
                    rightUpdate = true;
                    collect(L, R, mid + 1, r, rt << 1 | 1);
                }
                merge(query[rt], leftUpdate ? query[rt << 1] : null, rightUpdate ? query[rt << 1 | 1] : null);
            }
        }
    }

    public static class Right {
        public int[] arr;
        public int k;

        public Right(int[] nums, int K) {
            k = K;
            arr = new int[nums.length];
            for (int i = 0; i < nums.length; i++) {
                arr[i] = nums[i];
            }
        }

        public int topKSum(int l, int r) {
            PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> b - a);
            for (int i = l; i <= r; i++) {
                heap.add(arr[i]);
            }
            int sum = 0;
            for (int i = 0; i < k && !heap.isEmpty(); i++) {
                sum += heap.poll();
            }
            return sum;
        }
    }

    // 为了验证
    public static int[] randomArray(int n, int v) {
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = (int) (Math.random() * v) + 1;
        }
        return ans;
    }

    // 为了验证
    public static void main(String[] args) {
        int N = 100;
        int K = 10;
        int V = 100;
        int testTimes = 5000;
        int queryTimes = 100;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            int n = (int) (Math.random() * N) + 1;
            int k = (int) (Math.random() * K) + 1;
            int[] arr = randomArray(n, V);
            SegmentTree st = new SegmentTree(arr, k);
            Right right = new Right(arr, k);
            for (int j = 0; j < queryTimes; j++) {
                int a = (int) (Math.random() * n);
                int b = (int) (Math.random() * n);
                int l = Math.min(a, b);
                int r = Math.max(a, b);
                int ans1 = st.topKSum(l, r);
                int ans2 = right.topKSum(l, r);
                if (ans1 != ans2) {
                    System.out.println("出错了!");
                    System.out.println(ans1);
                    System.out.println(ans2);
                }
            }
        }
        System.out.println("测试结束");

        System.out.println("性能测试开始");
        int n = 100000;
        int k = 10;
        int[] arr = randomArray(n, n);
        //进行n次查询,每次查询的区间是[l,r]
        int[][] queries = new int[n][2];
        for (int i = 0; i < n; i++) {
            int a = (int) (Math.random() * n);
            int b = (int) (Math.random() * n);
            queries[i][0] = Math.min(a, b);
            queries[i][1] = Math.max(a, b);
        }
        System.out.println("数据规模 : " + n);
        System.out.println("数值规模 : " + n);
        System.out.println("查询规模 : " + n);
        System.out.println("k规模 : " + k);
        long start, end1, end2;
        start = System.currentTimeMillis();
        SegmentTree st = new SegmentTree(arr, k);
        end1 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            st.topKSum(queries[i][0], queries[i][1]);
        }
        end2 = System.currentTimeMillis();
        System.out.println("初始化运行时间 : " + (end1 - start) + " 毫秒");
        System.out.println("执行查询运行时间 : " + (end2 - end1) + " 毫秒");
        System.out.println("总共运行时间 : " + (end2 - start) + " 毫秒");
        System.out.println("性能测试结束");
    }
}

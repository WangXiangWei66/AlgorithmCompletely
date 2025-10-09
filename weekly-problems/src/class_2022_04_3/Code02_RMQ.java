package class_2022_04_3;

//来自小红书
//3.13 笔试
//给定一个数组，想随时查询任何范围上的最大值
//如果只是根据初始数组建立、并且以后没有修改，
//那么RMQ方法比线段树方法好实现，时间复杂度O(N*logN)，额外空间复杂度O(N*logN)
public class Code02_RMQ {
    //适用于静态数组的范围内最大值查询
    public static class RMQ {
        public int[][] max;

        public RMQ(int[] arr) {
            int n = arr.length;
            int k = power2(n);//确定二维数组的列数
            //max[i][j]表示从索引i开始，长度为2^j的区间内的最大值。
            max = new int[n + 1][n + 1];
            for (int i = 1; i <= n; i++) {
                max[i][0] = arr[i - 1];//长度为1的区间最大值
            }
            //外层遍历区间长度
            for (int j = 1; (1 << j) <= n; j++) {
                //遍历区间的起始位置
                for (int i = 1; i + (1 << j) - 1 <= n; i++) {
                    max[i][j] = Math.max(max[i][j - 1], max[i + (1 << (j - 1))][j - 1]);
                }
            }
        }

        public int max(int l, int r) {
            //使用的最大子区间长度
            int k = power2(r - l + 1);
            return Math.max(max[l][k], max[r - (1 << k) + 1][k]);
        }

        private int power2(int m) {
            int ans = 0;
            while ((1 << ans) <= (m >> 1)) {
                ans++;
            }
            return ans;
        }
    }

    public static class Right {
        //max[l][r]表示区间[l, r]（1-based 索引）内的最大值。
        public int[][] max;

        public Right(int[] arr) {
            int n = arr.length;
            max = new int[n + 1][n + 1];
            for (int l = 1; l <= n; l++) {
                max[l][l] = arr[l - 1];
                for (int r = l + 1; r <= n; r++) {
                    max[l][r] = Math.max(max[l][r - 1], arr[r - 1]);
                }
            }
        }

        public int max(int l, int r) {
            return max[l][r];
        }
    }

    public static int[] randomArray(int n, int v) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * v);
        }
        return arr;
    }

    // 为了测试
    public static void main(String[] args) {
        int N = 150;
        int V = 200;
        int testTimeOut = 20000;
        int testTimeIn = 200;
        System.out.println("测试开始");
        for (int i = 0; i < testTimeOut; i++) {
            int n = (int) (Math.random() * N) + 1;
            int[] arr = randomArray(n, V);
            int m = arr.length;
            RMQ rmq = new RMQ(arr);
            Right right = new Right(arr);
            for (int j = 0; j < testTimeIn; j++) {
                int a = (int) (Math.random() * m) + 1;
                int b = (int) (Math.random() * m) + 1;
                int l = Math.min(a, b);
                int r = Math.max(a, b);
                int ans1 = rmq.max(l, r);
                int ans2 = right.max(l, r);
                if (ans1 != ans2) {
                    System.out.println("出错了!");
                }
            }
        }
        System.out.println("测试结束");
    }

}

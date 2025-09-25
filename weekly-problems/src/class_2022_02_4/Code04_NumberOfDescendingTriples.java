package class_2022_02_4;

import java.util.Arrays;

//来自学员贡献
//返回一个数组中，所有降序三元组的数量
//比如 : {5, 3, 4, 2, 1}
//所有降序三元组为 :
//{5, 3, 2}、{5, 3, 1}、{5, 4, 2}、{5, 4, 1}、{5, 2, 1}、{3, 2, 1}、{4, 2, 1}
//所以返回数量7
public class Code04_NumberOfDescendingTriples {

    public static int num1(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        return process(arr, 0, new int[3], 0);
    }

    //index：当前处理的索引
    //path：用于存储已选择的元素
    //size：当前已经选择的元素的数量
    public static int process(int[] arr, int index, int[] path, int size) {
        if (size == 3) {
            return path[0] > path[1] && path[1] > path[2] ? 1 : 0;
        }
        int ans = 0;
        if (index < arr.length) {
            ans = process(arr, index + 1, path, size);
            path[size] = arr[index];
            ans += process(arr, index + 1, path, size + 1);
        }
        return ans;
    }

    public static class IndexTree {
        private int[] tree;//存储索引树的核心数组
        private int N;//索引树的有效大小

        public IndexTree(int size) {
            N = size;
            tree = new int[N + 1];
        }

        public int sum(int index) {
            int ret = 0;
            while (index > 0) {
                ret += tree[index];
                index -= index & -index;//实现了从叶子节点跳上父节点
            }
            return ret;
        }

        public void add(int index, int d) {
            while (index <= N) {
                tree[index] += d;
                index += index & -index;
            }
        }
    }

    public static int num2(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int n = arr.length;
        int[] sorted = Arrays.copyOf(arr, n);
        Arrays.sort(sorted);
        int max = Integer.MIN_VALUE;
        //将原数组值转化为排名值
        for (int i = 0; i < n; i++) {
            arr[i] = rank(sorted, arr[i]);
            max = Math.max(max, arr[i]);//最大排名值，为了初始化索引树的大小
        }
        IndexTree it2 = new IndexTree(max);//记录元素出现的次数
        IndexTree it3 = new IndexTree(max);//当前元素为中间元素j时，左侧满足条件的i的数量
        int ans = 0;
        for (int i = n - 1; i >= 0; i--) {
            ans += arr[i] == 1 ? 0 : it3.sum(arr[i] - 1);
            it2.add(arr[i], 1);
            it3.add(arr[i], arr[i] == 1 ? 0 : it2.sum(arr[i] - 1));
        }
        return ans;
    }

    public static int rank(int[] sorted, int num) {
        int l = 0;
        int r = sorted.length - 1;
        int m = 0;
        int ans = 0;
        while (l <= r) {
            m = (l + r) / 2;
            if (sorted[m] >= num) {
                ans = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return ans + 1;
    }

    public static int[] randomArray(int len, int value) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * value) - (int) (Math.random() * value);
        }
        return arr;
    }

    public static void printArray(int[] arr) {
        int n = arr.length;
        for(int i = 0;i < n;i++) {
            System.out.print(arr[i]+" ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int len = 5;
        int value = 10;
        int testTimes = 10000;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            int[] arr = randomArray(len, value);
            int ans1 = num1(arr);
            int ans2 = num2(arr);
            if (ans1 != ans2) {
                printArray(arr);
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println("出错了!");
                break;
            }
        }
        System.out.println("测试结束");
    }
}

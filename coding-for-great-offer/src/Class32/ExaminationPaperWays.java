package Class32;

import java.util.Arrays;

//拼多多笔试题 : 给定一个数组arr，arr[i] = j，表示第i号试题的难度为j。给定一个非负数M
//想出一张卷子，对于任何相邻的两道题目，前一题的难度不能超过后一题的难度+M
//返回所有可能的卷子种数
public class ExaminationPaperWays {

    //暴力，时间复杂度为O(n! * n)   空间复杂度为O(n)
    public static int ways1(int[] arr, int m) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return process(arr, 0, m);
    }

    public static int process(int[] arr, int index, int m) {
        if (index == arr.length) {
            for (int i = 1; i < index; i++) {
                if (arr[i - 1] > arr[i] + m) {
                    return 0;
                }
            }
            return 1;
        }
        int ans = 0;//记录有效排列的数量
        for (int i = index; i < arr.length; i++) {
            swap(arr, index, i);
            ans += process(arr, index + 1, m);
            swap(arr, index, i);//回溯
        }
        return ans;
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    //时间复杂度为O(N * logN)
    //排序+二分
    public static int ways2(int[] arr, int m) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        Arrays.sort(arr);
        int all = 1;//总的种类数
        //乘法原理累积
        for (int i = 1; i < arr.length; i++) {
            all = all * (num(arr, i - 1, arr[i] - m) + 1);
        }
        return all;
    }

    //t：前一个题难度的允许上限
    public static int num(int[] arr, int r, int t) {
        int i = 0;//左指针
        int j = r;//右指针
        int m = 0;//用于二分查找的中间指针
        int a = r + 1;//记录符合条件的最小索引，初始化为超出范围的值
        while (i <= j) {
            m = (i + j) / 2;
            if (arr[m] >= t) {
                a = m;
                j = m + 1;
            } else {
                i = m + 1;
            }
        }
        return r - a + 1;
    }

    //时间复杂度为O(N * logV)
    //arr[i] - min + 1将值映射到[1, max-min+1]范围，适配索引树的 1-based 索引特性。
    public static int ways3(int[] arr, int m) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        //计算最大最小值用于 压缩坐标
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int num : arr) {
            max = Math.max(max, num);
            min = Math.min(min, num);
        }
        //初始化索引树，+2是为了避免边界问题
        IndexTree itree = new IndexTree(max - min + 2);
        Arrays.sort(arr);
        int a = 0;//当前元素压缩后的坐标
        int b = 0;//符合条件的元素数量
        int all = 1;//总方案数
        //先将第一个元素加入索引树
        itree.add(arr[0] - min + 1, 1);//压缩坐标：保证从1开始（索引树特性）
        for (int i = 1; i < arr.length; i++) {
            a = arr[i] - min + 1;
            //符合条件的数量 = 已加入的元素总数(i个) - 不符合条件的数量
            b = i - (a - m - 1 >= 1 ? itree.sum(a - m - 1) : 0);
            all = all * (b + 1);
            itree.add(a, 1);//将当前元素也加入索引树
        }
        return all;
    }

    public static class IndexTree {
        private int[] tree;//树状数组的本体
        private int N;//数组的大小

        public IndexTree(int size) {
            N = size;
            tree = new int[N + 1];
        }

        public int sum(int index) {
            int ret = 0;
            while (index > 0) {
                ret += tree[index];
                index -= index & -index;//减去最低为的1
            }
            return ret;
        }

        //给index位置的数+d
        public void add(int index, int d) {
            while (index <= N) {
                tree[index] += d;
                index += index & -index;
            }
        }
    }

    public static int[] randomArray(int len, int value) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * (value + 1));
        }
        return arr;
    }

    public static void main(String[] args) {
        int N = 10;
        int value = 20;
        int testTimes = 10;
        System.out.println("test begin!");
        for (int i = 0; i < testTimes; i++) {
            int len = (int) (Math.random() * (N + 1));
            int[] arr = randomArray(len, value);
            int m = (int) (Math.random() * (value + 1));
            int ans1 = ways1(arr, m);
            int ans2 = ways2(arr, m);
            int ans3 = ways3(arr, m);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Oops");
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println(ans3);
                break;
            }
        }
        System.out.println("test finish!");
    }
}

package Class24;

import java.util.Arrays;
import java.util.Comparator;

//长度为N的数组arr，一定可以组成N^2个数字对。例如arr = [3,1,2]，数字对有(3,3) (3,1) (3,2) (1,3) (1,1) (1,2) (2,3) (2,1) (2,2)
//也就是任意两个数都可以，而且自己和自己也算数字对。数字对怎么排序？第一维数据从小到大；第一维数据一样的，第二维数组也从小到大
//所以上面的数值对排序的结果为：(1,1)(1,2)(1,3)(2,1)(2,2)(2,3)(3,1)(3,2)(3,3)。给定一个数组arr，和整数k，返回第k小的数值对
public class Code02_KthMinPair {

    public static class Pair {
        public int x;
        public int y;

        Pair(int a, int b) {
            x = a;
            y = b;
        }
    }

    public static class PairComparator implements Comparator<Pair> {
        @Override
        public int compare(Pair arg0, Pair arg1) {
            return arg0.x != arg1.x ? arg0.x - arg1.x : arg0.y - arg1.y;
        }
    }

    //最后返回第K小的元素对, O(N^2 * log (N^2))的复杂度，
    public static int[] kthMinPair1(int[] arr, int k) {
        int N = arr.length;
        if (k > N * N) {
            return null;
        }
        Pair[] pairs = new Pair[N * N]; //数组长度为N×N，因为每个元素都可以和包括自身在内的所有元素组成对
        int index = 0;//定义索引变量，用于填充pairs数组
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                pairs[index++] = new Pair(arr[i], arr[j]);
            }
        }
        Arrays.sort(pairs, new PairComparator());
        //最终返回第K-1位置的元素对
        return new int[]{pairs[k - 1].x, pairs[k - 1].y};
    }

    //O(N*logN)的复杂度
    public static int[] kthMinPair2(int[] arr, int k) {
        int N = arr.length;
        if (k > N * N) {
            return null;
        }
        //O(N * logN)
        Arrays.sort(arr);
        //计算元素对中第一个元素的值
        //(k-1)/N得到的是排序后数组中第一个元素的索引
        //因为每N个元素对会对应同一个第一个元素
        int firstNum = arr[(k - 1) / N];
        int lessFirstNumSize = 0;   //统计小于firstNum的元素个数
        int firstNumSize = 0; //统计等于firstNum的元素个数
        //遍历数组，计算上述两个统计值
        for (int i = 0; i < N && arr[i] <= firstNum; i++) {
            if (arr[i] < firstNum) {
                lessFirstNumSize++;
            } else {
                firstNumSize++;
            }
        }
        //计算在第一个元素确定为firstNum后，还需要找第几个元素对
        //减去那些第一个元素小于firstNum的所有元素对（每个这样的元素可以和N个元素组成对）
        int rest = k - (lessFirstNumSize * N);
        //确定第二个元素：(rest-1)/firstNumSize得到第二个元素在排序数组中的索引
        //因为每个等于firstNum的元素可以和firstNumSize个元素组成对
        return new int[]{firstNum, arr[(rest - 1) / firstNumSize]};
    }

    // O(N)的复杂度
    public static int[] kthMinPair3(int[] arr, int k) {
        int N = arr.length;
        if (k > N * N) {
            return null;
        }
        // 第一步：确定元素对的第一个元素（x）
        // (k-1)/N 计算的是第一个元素在"排序后数组"中的索引
        // getMinKth方法直接找到数组中第[(k-1)/N]小的元素（无需完整排序）
        int firstNum = getMinKth(arr, (k - 1) / N);
        int lessFirstNumSize = 0;
        int firstNumSize = 0;
        for (int i = 0; i < N; i++) {
            if (arr[i] < firstNum) {
                lessFirstNumSize++;
            }
            if (arr[i] == firstNum) {
                firstNumSize++;
            }
        }
        //计算在第一个元素确定为firstNum后，还需要找第几个元素对
        //减去所有"第一个元素小于firstNum"的元素对总数（每个这样的元素可与N个元素组成对）
        int rest = k - (lessFirstNumSize * N);
        //第二步：确定元素对的第二个元素（y）
        //(rest-1)/firstNumSize 计算的是第二个元素在"排序后数组"中的索引
        //再次通过getMinKth找到对应位置的元素
        return new int[]{firstNum, getMinKth(arr, (rest - 1) / firstNumSize)};
    }

    //不需要对数组进行排序，时间复杂度为O(N)
    public static int getMinKth(int[] arr, int index) {
        int L = 0;
        int R = arr.length - 1;
        int pivot = 0;
        int[] range = null;// 存储等于基准值的元素范围 [左边界, 右边界]
        while (L < R) {
            pivot = arr[L + (int) (Math.random() * (R - L + 1))];
            range = partition(arr, L, R, pivot);
            if (index < range[0]) {
                R = range[0] - 1;
            } else if (index > range[1]) {
                L = range[1] + 1;
            } else {
                return pivot;
            }
        }
        return arr[L];
    }

    public static int[] partition(int[] arr, int L, int R, int pivot) {
        int less = L - 1;
        int more = R + 1;
        int cur = L;//当前遍历的位置
        while (cur < more) {
            if (arr[cur] < pivot) {
                swap(arr, ++less, cur++);
            } else if (arr[cur] > pivot) {
                swap(arr, cur, --more);
            } else {
                cur++;
            }
        }
        return new int[]{less + 1, more - 1};
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static int[] getRandomArray(int max, int len) {
        int[] arr = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
        }
        return arr;
    }

    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    public static void main(String[] args) {
        int max = 100;
        int len = 30;
        int testTime = 100000;
        System.out.println("test begin,test times: " + testTime);
        for (int i = 0; i < testTime; i++) {
            int[] arr = getRandomArray(max, len);
            int[] arr2 = copyArray(arr);
            int[] arr3 = copyArray(arr);
            int N = arr.length * arr.length;
            int k = (int) (Math.random() * N) + 1;
            int[] ans1 = kthMinPair1(arr, k);
            int[] ans2 = kthMinPair1(arr2, k);
            int[] ans3 = kthMinPair1(arr3, k);
            if (ans1[0] != ans2[0] || ans2[0] != ans3[0] || ans1[1] != ans2[1] || ans2[1] != ans3[1]) {
                System.out.println("Oops");
            }
        }
        System.out.println("test finish");
    }
}

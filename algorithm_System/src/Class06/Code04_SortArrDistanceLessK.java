package Class06;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Code04_SortArrDistanceLessK {
    public static void sortedArrDistanceLessK(int[] arr, int k) {
        if (k == 0) {
            return;
        }
        //默认使用小根堆，用于动态维护当前最小元素
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int index = 0;
        //0...K-1，将数组前k个数加入堆
        for (; index <= Math.min(arr.length - 1, k - 1); index++) {
            heap.add(arr[index]);
        }
        int i = 0;
        //index从k开始，遍历到数组的末尾
        for (; index < arr.length ; i++, index++) {
            heap.add(arr[index]);
            arr[i] = heap.poll();//弹出栈顶元素，即当前数中的最小值
        }
        while (!heap.isEmpty()) {
            arr[i++] = heap.poll();
        }
    }

    public static void comparator(int[] arr, int k) {
        Arrays.sort(arr);
    }

    public static int[] randomArrayMoveMoreK(int maxSize, int maxValue, int k) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
        }
        //先排个序
        Arrays.sort(arr);
        //然后开始随意交换，但是保证每个数距离不超过K
        //swap[i]=true,表示i位置已经参与过交换
        //swap[i]=false,表示i位置没有参与过交换
        boolean[] isSwap = new boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            //对于每个位置的数，生成一个随机的偏移量
            int j = Math.min(i + (int) (Math.random() * (k + 1)), arr.length - 1);
            if (!isSwap[i] && !isSwap[j]) {
                isSwap[i] = true;
                isSwap[j] = true;
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
        }
        return arr;
    }

    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int maxSize = 10;
        int maxValue = 10;
        int testTime = 1000000;
        System.out.println("测试开始!");
        for (int i = 0; i < testTime; i++) {
            int k = (int) (Math.random() * maxSize) + 1;
            int[] arr = randomArrayMoveMoreK(maxSize, maxValue, k);
            int[] arr1 = copyArray(arr);
            int[] arr2 = copyArray(arr);
            sortedArrDistanceLessK(arr1, k);
            comparator(arr2, k);
            if (!isEqual(arr1, arr2)) {
                System.out.println("出错了");
                printArray(arr);
                printArray(arr1);
                printArray(arr2);
                break;
            }
        }
        System.out.println("测试结束!");
    }
}

package Class01;

import java.util.Arrays;

public class Code06_InsertionSort {
    public static void insertionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {//0~i做到有序
            for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) {
                swap(arr, j, j + 1);
            }
        }
    }

    //i和j数做交换
    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[j];
        arr[j] = arr[i];
        arr[i] = tmp;
    }

    //for test
    public static void comparator(int[] arr) {
        Arrays.sort(arr);
    }

    //for test
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        //Math.random() ->[0,1)所有的小数，等概率返回一个
        //Math.random()*N ->[0,N)所有的小数，等概率返回一个
        //(int)(Math.random()*N)->[0,N-1]所有的小数，等概率返回一个
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];//数组的长度是随机的
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
        }
        return arr;
    }

    //for test
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

    //for test
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

    //for test
    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    //for test
    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 100;//随机数组的长度0~100
        int maxValue = 100;//随机数组的值-100~100
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            insertionSort(arr1);
            comparator(arr2);
            if (!isEqual(arr1, arr2)) {
                printArray(arr1);
                printArray(arr2);
                succeed = false;
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucted");
        int[] arr = generateRandomArray(maxSize, maxValue);
        printArray(arr);
        insertionSort(arr);
        printArray(arr);
    }

}

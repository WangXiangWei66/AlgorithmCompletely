package Class40;

// 给定两个数组A和B，长度都是N
// A[i]不可以在A中和其他数交换，只可以选择和B[i]交换(0<=i<n)
// 你的目的是让A有序，返回你能不能做到
public class Code04_LetASorted {

    public static boolean letASorted(int[] A, int[] B) {
        return process1(A, B, 0, Integer.MIN_VALUE);
    }

    //lastA：A的上一个元素
    public static boolean process1(int[] A, int[] B, int i, int lastA) {
        if (i == A.length) {
            return true;
        }
        if (A[i] >= lastA && process1(A, B, i + 1, A[i])) {
            return true;
        }
        if (B[i] > lastA && process1(A, B, i + 1, B[i])) {
            return true;
        }
        return false;
    }
    //时间复杂度为O(N)
    public static boolean letASorted2(int[] A, int[] B) {
        return process2(A, B, 0, true);
    }

    public static boolean process2(int[] A, int[] B, int i, boolean fromA) {
        if (i == A.length) {
            return true;
        }
        if (i == 0 || (A[i] >= (fromA ? A[i - 1] : B[i - 1])) && process2(A, B, i + 1, true)) {
            return true;
        }
        if (i == 0 || (B[i] >= (fromA ? A[i - 1] : B[i - 1])) && process2(A, B, i + 1, false)) {
            return true;
        }
        return false;
    }
}
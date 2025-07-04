package Class14;

public class Code06_MissingNumber {

    public static int firstMissingPositive(int[] arr) {
        int L = 0;
        int R = arr.length;
        while (L != R) {
            if (arr[L] == L + 1) {
                L++;
            } else if (arr[L] <= L || arr[L] > R || arr[arr[L] - 1] == arr[L]) {
                swap(arr, L, --R);
            } else {//当前元素有效，但是位置错误，见他放到合适的位置去
                swap(arr, L, arr[L] - 1);
            }
        }
        return L + 1;
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}

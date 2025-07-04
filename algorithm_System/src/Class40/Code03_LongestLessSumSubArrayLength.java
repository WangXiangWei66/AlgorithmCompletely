package Class40;

public class Code03_LongestLessSumSubArrayLength {

    public static int maxLengthAweSome(int[] arr, int K) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int[] minSums = new int[arr.length];
        int[] minSumEnds = new int[arr.length];
        minSums[arr.length - 1] = arr[arr.length - 1];
        minSumEnds[arr.length - 1] = arr.length - 1;
        for (int i = arr.length - 2; i >= 0; i--) {
            if (minSums[i + 1] < 0) {
                minSums[i] = arr[i] + minSums[i + 1];
                minSumEnds[i] = minSumEnds[i + 1];
            } else {
                minSums[i] = arr[i];
                minSumEnds[i] = i;
            }
        }

        int end = 0;
        int sum = 0;
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            while (end < arr.length && sum + minSums[end] <= K) {
                sum += minSums[end];
                end = minSumEnds[end] + 1;
            }
            ans = Math.max(ans, end - i);
            if (end > i) {
                sum -= arr[i];
            } else {
                end = i + 1;
            }
        }
        return ans;
    }

    public static int maxLength(int[] arr, int K) {
        int[] h = new int[arr.length + 1];
        int sum = 0;
        h[0] = sum;
        for (int i = 0; i != arr.length; i++) {
            sum += arr[i];
            h[i + 1] = Math.max(sum, h[i]);
        }
        sum = 0;
        int res = 0;
        int pre = 0;
        int len = 0;
        for (int i = 0; i != arr.length; i++) {
            sum += arr[i];
            pre = getLessIndex(h, sum - K);
            len = pre == -1 ? 0 : i - pre + 1;
            res = Math.max(res, len);
        }
        return res;
    }

    public static int getLessIndex(int[] arr, int num) {
        int low = 0;
        int high = arr.length - 1;
        int mid = 0;
        int res = -1;
        while (low <= high) {
            mid = (low + high) / 2;
            if (arr[mid] >= num) {
                res = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return res;
    }

    public static int[] generateRandomArray(int len, int maxValue) {
        int[] ans = new int[len];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * maxValue) - (maxValue / 3);
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        for (int i = 0; i < 100000; i++) {
            int[] arr = generateRandomArray(10, 20);
            int K = (int) (Math.random() * 20) - 5;
            if (maxLengthAweSome(arr, K) != maxLength(arr, K)) {
                System.out.println("Oops");
            }
        }
        System.out.println("test finish");
    }
}

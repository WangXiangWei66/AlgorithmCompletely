package Class46;

public class Code01_BurstBalloons {

    public static int maxCoins0(int[] arr) {
        int N = arr.length;
        int[] help = new int[N + 2];
        for (int i = 0; i < N; i++) {
            help[i + 1] = arr[i];
        }
        help[0] = 1;
        help[N + 1] = 1;
        return func(help, 1, N);
    }

    public static int func(int[] arr, int L, int R) {
        if (L == R) {
            return arr[L - 1] * arr[L] * arr[R + 1];
        }
        int max = func(arr, L + 1, R) + arr[L - 1] * arr[L] * arr[R + 1];
        max = Math.max(max, func(arr, L, R - 1) + arr[L - 1] * arr[R] * arr[R + 1]);
        for (int i = L + 1; i < R; i++) {
            int left = func(arr, L, i - 1);
            int right = func(arr, i + 1, R);
            int last = arr[L - 1] * arr[i] * arr[R + 1];
            int cur = left + right + last;
            max = Math.max(max, cur);
        }
        return max;
    }

    public static int maxCoins1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0];
        }
        int N = arr.length;
        int[] help = new int[N + 2];
        for (int i = 0; i < N; i++) {
            help[i + 1] = arr[i];
        }
        help[0] = 1;
        help[N + 1] = 1;
        return process(help, 1, N);
    }

    public static int process(int[] arr, int L, int R) {
        if (L == R) {
            return arr[L - 1] * arr[L] * arr[R + 1];
        }
        int max = Math.max(arr[L - 1] * arr[L] * arr[R + 1] + process(arr, L + 1, R), arr[L - 1] * arr[R] * arr[R + 1] + process(arr, L, R - 1));
        for (int i = L + 1; i < R; i++) {
            max = Math.max(max, arr[L - 1] * arr[i] * arr[R + 1] + process(arr, L, i - 1) + process(arr, i + 1, R));
        }
        return max;
    }

    public static int maxCoins2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0];
        }
        int N = arr.length;
        int[] help = new int[N + 2];
        for (int i = 0; i < N; i++) {
            help[i + 1] = arr[i];
        }
        help[0] = 1;
        help[N + 1] = 1;
        int[][] dp = new int[N + 2][N + 2];
        for (int i = 1; i <= N; i++) {
            dp[i][i] = help[i - 1] * help[i] * help[i + 1];
        }
        for (int L = N; L >= 1; L--) {
            for (int R = L + 1; R <= N; R++) {
                int ans = help[L - 1] * help[L] * help[R + 1] + dp[L + 1][R];
                ans = Math.max(ans, help[L - 1] * help[R] * help[R + 1] + dp[L][R - 1]);
                for (int i = L + 1; i < R; i++) {
                    ans = Math.max(ans, help[L - 1] * help[i] * help[R + 1] + dp[L][R - 1] + dp[i + 1][R]);
                }
                dp[L][R] = ans;
            }
        }
        return dp[1][N];
    }
}

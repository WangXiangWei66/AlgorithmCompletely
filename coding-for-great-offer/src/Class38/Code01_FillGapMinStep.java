package Class38;

//来自字节
//给定两个数a和b
//第1轮，把1选择给a或者b
//第2轮，把2选择给a或者b
//...
//第i轮，把i选择给a或者b
//想让a和b的值一样大，请问至少需要多少轮？
public class Code01_FillGapMinStep {

    public static int minStep0(int a, int b) {
        if (a == b) {
            return 0;
        }
        int limit = 15;//限制搜索次数为15轮
        return process(a, b, 1, limit);
    }

    public static int process(int a, int b, int i, int n) {
        if (i > n) {
            return Integer.MAX_VALUE;
        }
        if (a + i == b || a == b + i) {
            return i;
        }
        return Math.min(process(a + i, b, i + 1, n), process(a, b + i, i + 1, n));
    }

    //时间复杂度为O(Math.sqrt(s))
    public static int minStep1(int a, int b) {
        if (a == b) {
            return 0;
        }
        int s = Math.abs(a - b);
        int num = 1;//当前的轮次
        int sum = 0;//从1到当前轮次的累加和
        for (; !(sum >= s && (sum - s) % 2 == 0); num++) {
            sum += num;
        }
        return num - 1;
    }

    public static int minStep2(int a, int b) {
        if (a == b) {
            return 0;
        }
        int s = Math.abs(a - b);
        //使用 2s 是因为 1+2+...+n = n(n+1)/2，需要 n*(n+1) ≥ 2*s
        int begin = best(s << 1);
        for (; (begin * (begin + 1) / 2 - s) % 2 != 0; ) {
            begin++;
        }
        return begin;
    }

    public static int best(int s2) {
        int L = 0;
        int R = 1;
        //寻找右边界范围
        for (; R * (R + 1) < s2; ) {
            L = R;
            R *= 2;
        }
        int ans = 0;
        while (L <= R) {
            int M = (L + R) / 2;
            if (M * (M + 1) >= s2) {
                ans = M;
                R = M - 1;
            } else {
                L = M + 1;
            }
        }
        return ans;
    }


    public static void main(String[] args) {
        System.out.println("功能测试开始：");
        for (int a = 1; a < 100; a++) {
            for (int b = 1; b < 100; b++) {
                int ans1 = minStep0(a, b);
                int ans2 = minStep1(a, b);
                int ans3 = minStep2(a, b);
                if (ans2 != ans1 || ans3 != ans2) {
                    System.out.println("Oops");
                    System.out.println(a + " , " + b);
                    break;
                }
            }
        }
        System.out.println("功能测试结束");
        int a = 19019;
        int b = 8439284;
        int ans2 = minStep1(a, b);
        int ans3 = minStep2(a, b);
        System.out.println(ans2);
        System.out.println(ans3);
    }
}

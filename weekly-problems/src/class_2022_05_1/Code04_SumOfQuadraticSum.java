package class_2022_05_1;

//来自学员问题，蓝桥杯练习题
//f(i) : i的所有因子，每个因子都平方之后，累加起来
//比如f(10) = 1平方 + 2平方 + 5平方 + 10平方 = 1 + 4 + 25 + 100 = 130
//给定一个数n，求f(1) + f(2) + .. + f(n)
//n <= 10的9次方
// O(n)的方法都会超时！低于它的！
// O(根号N)的方法，就过了，一个思路
// O(log N)的方法，
public class Code04_SumOfQuadraticSum {

    public static long sum1(long n) {
        //每个数作为因子出现的次数
        int[] cnt = new int[(int) n + 1];//下标0抛弃不用
        for (int num = 1; num <= n; num++) {
            //内层循环寻找num的所有因子
            for (int j = 1; j <= num; j++) {
                if (num % j == 0) {
                    cnt[j]++;
                }
            }
        }
        long ans = 0;
        for (long i = 1; i <= n; i++) {
            ans += i * i * (long) (cnt[(int) i]);
        }
        return ans;
    }

    public static long sum2(long n) {
        //n的平方根，作为分段处理的临界点
        long sqrt = (long) Math.pow((double) n, 0.5);
        long ans = 0;
        for (long i = 1; i <= sqrt; i++) {
            ans += i * i * (n / i);
        }
        //k:二分出来的因子个数
        for (long k = n / (sqrt + 1); k >= 1; k--) {
            ans += sumOfLimitNumber(n, k);
        }
        return ans;
    }

    //v：确定因子的范围
    //n：因子出现的次数
    public static long sumOfLimitNumber(long v, long n) {
        long r = cover(v, n);
        long l = cover(v, n + 1);
        // 计算[l+1, r]范围内所有数的平方和，再乘以k值(n)
        // 平方和公式：1²+2²+...+m² = m*(m+1)*(2m+1)/6
        return ((r * (r + 1) * ((r << 1) + 1) - l * (l + 1) * ((l << 1) + 1)) * n) / 6;
    }

    public static long cover(long v, long n) {
        long l = 1;
        long r = v;
        long m = 0;
        long ans = 0;
        while (l <= r) {
            m = (l + r) / 2;
            if (m * n <= v) {
                ans = m;
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return ans;
    }

    public static void test(int n) {
        int[] cnt = new int[n + 1];
        for (int num = 1; num <= n; num++) {
            for (int j = 1; j <= num; j++) {
                if (num % j == 0) {
                    cnt[j]++;
                }
            }
        }
        for (int i = 1; i <= n; i++) {
            System.out.println("因子: " + i + "，个数:" + cnt[i]);
        }
    }

    public static void main(String[] args) {

//		test(100);

        System.out.println("测试开始");
        for (long i = 1; i < 1000; i++) {
            if (sum1(i) != sum2(i)) {
                System.out.println("出错了!");
            }
        }
        System.out.println("测试结束");

        long n = 50000000000L; // 5 * 10的10次方
        long start = System.currentTimeMillis();
        sum2(n);
        long end = System.currentTimeMillis();
        System.out.println("大样本测试，n = " + n);
        System.out.println("运行时间 : " + (end - start) + " ms");
    }
}

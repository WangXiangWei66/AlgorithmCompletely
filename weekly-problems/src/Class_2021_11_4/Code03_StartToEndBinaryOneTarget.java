package Class_2021_11_4;

import java.util.Arrays;

//来自真实面试，同学给我的问题
//限制：0 <= start <= end，0 <= target <= 64
//[start,end]范围上的数字，有多少数字二进制中1的个数等于target
public class Code03_StartToEndBinaryOneTarget {

    public static long num1(long start, long end, int target) {
        if (start < 0 || end < 0 || start > end || target < 0) {
            return -1;
        }
        long ans = 0;
        for (long i = start; i <= end; i++) {
            if (bitOne(i) == target) {
                ans++;
            }
        }
        return ans;
    }

    public static int bitOne(long num) {
        int bits = 0;
        for (int i = 63; i >= 0; i--) {
            if ((num & (1L << i)) != 0) {
                bits++;
            }
        }
        return bits;
    }

    //采用了类似前缀和的思想
    public static long nums2(long start, long end, int target) {
        if (start < 0 || end < 0 || start > end || target < 0) {
            return -1;
        }
        if (start == 0 && end == 0) {
            return target == 0 ? 1 : 0;
        }
        //找到end最高位的1所在的位置
        int ehigh = 62;
        while ((end & (1L << ehigh)) == 0) {
            ehigh--;
        }
        //从[0,end]计算
        if (start == 0) {
            return process2(ehigh, 0, target, end);
        } else {
            start--;
            //找start最高位1所在的位置
            int shigh = 62;
            while (shigh >= 0 && (start & (1L << shigh)) == 0) {
                shigh--;
            }
            return process2(ehigh, 0, target, end) - process2(shigh, 0, target, start);
        }
    }

    //index:当前处理的二进制位
    //less：状态标记
    //rest：还需要的1的个数
    //num：上限数字
    public static long process2(int index, int less, int rest, long num) {
        //需要的1的数量超过了剩余位数
        if (rest > index + 1) {
            return 0;
        }
        if (rest == 0) {
            return 1L;
        }
        //当前前缀已经小于了num的前缀
        //index+1：剩余可处理的二进制位
        if (less == 1) {
            if (rest == index + 1) {
                return 1;
            } else {
                return process2(index - 1, 1, rest - 1, num) + process2(index - 1, 1, rest, num);
            }
            //当前的前缀和num的前缀相同
        } else {
            if (rest == index + 1) {
                return (num & (1L << index)) == 0 ? 0 : process2(index - 1, 0, rest - 1, num);
            } else {
                if ((num & (1L << index)) == 0) {
                    return process2(index - 1, 0, rest, num);
                } else {
                    return process2(index - 1, 0, rest - 1, num) + process2(index - 1, 1, rest, num);
                }
            }
        }
    }

    public static long nums3(long start, long end, int target) {
        if (start < 0 || end < 0 || start > end || target < 0) {
            return -1;
        }
        if (start == 0 && end == 0) {
            return target == 0 ? 1 : 0;
        }
        //寻找end表示位中，最高位1的位置
        int ehigh = 62;
        while ((end & (1L << ehigh)) == 0) {
            ehigh--;
        }
        //动态规划表，计算0到end中符合条件的数字个数
        //index：当前处理的二进制位
        //less：标记前面的位，是否已经小于num对应位
        long[][][] dpe = new long[ehigh + 1][2][target + 1];
        for (int i = 0; i <= ehigh; i++) {
            Arrays.fill(dpe[i][0], -1);
            Arrays.fill(dpe[i][1], -1);
        }
        //0到end中，符合条件的1的个数
        long anse = process3(ehigh, 0, target, end, dpe);
        if (start == 0) {
            return anse;
        } else {
            start--;
            int sHigh = 62;
            while (sHigh >= 0 && (start & (1L << sHigh)) == 0) {
                sHigh--;
            }
            long[][][] dps = new long[sHigh + 1][2][target + 1];
            for (int i = 0; i <= sHigh; i++) {
                Arrays.fill(dps[i][0], -1);
                Arrays.fill(dps[i][1], -1);
            }
            long anss = process3(sHigh, 0, target, start, dps);
            return anse - anss;
        }
    }

    public static long process3(int index, int less, int rest, long num, long[][][] dp) {
        if (rest > index + 1) {
            return 0;
        }
        if (rest == 0) {
            return 1L;
        }
        if (dp[index][less][rest] != -1) {
            return dp[index][less][rest];
        }
        long ans = 0;
        //前面的位已经比num小，当前位可以自由选择0或1
        if (less == 1) {
            if (rest == index + 1) {
                ans = 1;
            } else {
                //需要的1的数量等于剩余位数
                ans = process3(index - 1, 1, rest - 1, num, dp) + process3(index - 1, 1, rest, num, dp);
            }
        } else {
            if (rest == index + 1) {
                ans = (num & (1L << index)) == 0 ? 0 : process3(index - 1, 0, rest - 1, num, dp);
            } else {
                if ((num & (1L << index)) == 0) {
                    ans = process3(index - 1, 0, rest, num, dp);
                } else {
                    ans = process3(index - 1, 0, rest - 1, num, dp) + process3(index - 1, 1, rest, num, dp);
                }
            }
        }
        dp[index][less][rest] = ans;
        return ans;
    }

    public static long nums4(long start, long end, int target) {
        if (start < 0 || end < 0 || start > end || target < 0) {
            return -1;
        }
        long anse = process4(63, target, end);
        if (start == 0) {
            return anse;
        } else {
            long anss = process4(63, target, start - 1);
            return anse - anss;
        }
    }

    public static long process4(int index, int rest, long num) {
        if (rest > index + 1) {
            return 0;
        }
        if (rest == 0) {
            return 1;
        }
        if ((num & (1L << index)) == 0) {
            //当前位是0
            return process4(index - 1, rest, num);
        } else {
            return c(index, rest) + process4(index - 1, rest - 1, num);
        }
    }

    //组合数来计算
    public static long c(long n, long a) {
        if (n < a) {
            return 0L;
        }
        long up = 1L;//分子
        long down = 1L;//分母
        // 计算组合数C(n,a) = n!/(a!*(n-a)!)
        for (long i = a + 1, j = 2; i <= n || j <= n - 1; ) {
            if (i <= n) {
                up *= i++;
            }
            if (j <= n - a) {
                down *= j++;
            }
            //每次除完后，进行个约分
            long gcd = gcd(up, down);
            up /= gcd;
            down /= gcd;
        }
        return up / down;
    }

    public static long gcd(long m, long n) {
        return n == 0 ? m : gcd(n, m % n);
    }

    public static void main(String[] args) {
        long range = 600L;
        System.out.println("功能测试开始！");
        for (long start = 0L; start < range; start++) {
            for (long end = start; end < range; end++) {
                int target = (int) (Math.random() * 10);
                long ans1 = num1(start, end, target);
                long ans2 = nums2(start, end, target);
                long ans3 = nums3(start, end, target);
                long ans4 = nums4(start, end, target);
                if (ans1 != ans2 || ans1 != ans3 || ans1 != ans4) {
                    System.out.println("Oops");
                    break;
                }
            }
        }
        System.out.println("功能测试结束");

        long start = 33281731L;
        long end = 204356810L;
        int target = 17;
        long startTime;
        long endTime;
        long ans1;
        long asn2;
        long ans3;
        long ans4;

        System.out.println("大范围性能测试，开始");
        startTime = System.currentTimeMillis();
        ans1 = num1(start, end, target);
        endTime = System.currentTimeMillis();
        System.out.println("方法一的答案：" + ans1 + ",运行时间（毫秒）：" + (endTime - startTime));
        asn2 = nums2(start, end, target);
        endTime = System.currentTimeMillis();
        System.out.println("方法二的答案：" + asn2 + ",运行时间（毫秒）：" + (endTime - startTime));
        ans3 = num1(start, end, target);
        endTime = System.currentTimeMillis();
        System.out.println("方法三的答案：" + ans3 + ",运行时间（毫秒）：" + (endTime - startTime));
        ans4 = num1(start, end, target);
        endTime = System.currentTimeMillis();
        System.out.println("方法四的答案：" + ans4 + ",运行时间（毫秒）：" + (endTime - startTime));
        System.out.println("大范围性能测试，结束");

        System.out.println("超大范围性能测试，开始");
        start = 88193819381L;
        end = 92371283712182371L;
        target = 30;
        ans3 = num1(start, end, target);
        endTime = System.currentTimeMillis();
        System.out.println("方法三的答案：" + ans3 + ",运行时间（毫秒）：" + (endTime - startTime));
        ans4 = num1(start, end, target);
        endTime = System.currentTimeMillis();
        System.out.println("方法四的答案：" + ans4 + ",运行时间（毫秒）：" + (endTime - startTime));
        System.out.println("超大范围性能测试，结束");
    }
}
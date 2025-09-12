package Class_2021_12_3;

//来自TME2022校园招聘后台开发/运营开发/业务运维/应用开发笔试
//给定两个参数n和k，返回m
//含义：k进制的情况下，把1~m每一位的状态列出来，其中1状态出现的数量要>=n
//返回最小的、能做到这一点的m值
//具体详情请参加如下的测试页面
//测试链接 : https://www.nowcoder.com/test/33701596/summary
//本题目为该试卷第3题
public class Code03_OneCountsInKSystem {

    public static long minM(int n, int k) {
        int len = bits(n, k);//n在k进制下的位数
        long l = 1;
        long r = power(k, len + 1);
        long ans = r;
        while (l <= r) {
            long m = l + ((r - l) >> 1);
            if (ones(m, k) >= n) {
                ans = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return ans;
    }
    //计算num在k进制下的位数
    public static int bits(long num, int k) {
        int len = 0;
        while (num != 0) {
            len++;
            num /= k;
        }
        return len;
    }
    //计算base的power次方
    public static long power(long base, int power) {
        long ans = 1;
        while (power != 0) {
            if ((power & 1) != 0) {
                ans *= base;
            }
            base *= base;
            power >>= 1;
        }
        return ans;
    }

    public static long ones(long num, int k) {
        int len = bits(num, k);//计算num的k进制位数
        if (len <= 1) {
            return len;
        }
        long offset = power(k, len - 1);//最高位的权重
        long first = num / offset;//num在k进制下的最高位数字
        //计算最高位的1
        long curOne = first == 1 ? (num % offset) + 1 : offset;
        //计算除最高位以外的1
        long restOne = first * (len - 1) * (offset / k);
        return curOne + restOne + ones(num % offset, k);
    }
}

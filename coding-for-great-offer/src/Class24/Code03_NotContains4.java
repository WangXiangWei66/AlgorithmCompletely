package Class24;

//正常的里程表会依次显示自然数表示里程
//吉祥的里程表会忽略含有4的数字而跳到下一个完全不含有4的数
//正常：1 2 3 4 5 6 7 8  9 10 11 12 13 14 15
//吉祥：1 2 3 5 6 7 8 9 10 11 12 13 15 16 17 ... 38 39 50 51 52 53 55
//给定一个吉祥里程表的数字num(当然这个数字中不含有4)
//返回这个数字代表的真实里程
public class Code03_NotContains4 {

    public static long notContains4Nums1(long num) {
        long count = 0; //统计符合条件得数字个数
        for (long i = 1; i <= num; i++) {
            if (isNot4(i)) {
                count++;
            }
        }
        return count;
    }


    public static boolean isNot4(long num) {
        while (num != 0) {
            if (num % 10 == 4) {
                return false;
            }
            num /= 10;
        }
        return true;
    }

    //本方法的时间复杂度为O(N)
    //预定义的数组，存储了不同位数下 "不包含数字 4 的数字" 的总个数
    //规律：arr[n] = 9^(n-1)，例如：
    //1 位数：arr[1] = 1（实际是 1-9 中排除 4，共 8 个，这里可能是特殊处理）
    //2 位数：arr[2] = 9（第一位 9 种选择 1-9 排除 4，第二位 9 种选择 0-9 排除 4）
    //3 位数：arr[3] = 81 = 9^2，以此类推
    public static long[] arr = {0L, 1L, 9L, 81L, 729L, 6561L, 59049L, 531441L, 4782969L, 43046721L, 387420489L,
            3486784401L, 31381059609L, 282429536481L, 2541865828329L, 22876792454961L, 205891132094649L,
            1853020188851841L, 16677181699666569L, 150094635296999121L, 1350851717672992089L};

    public static long notContains4Nums2(long num) {
        if (num <= 0) {
            return 0;
        }
        //获取数字的位数
        int len = decimalLength(num);
        long offset = offset(len);//计算最高位的权重
        long first = num / offset;//获取最高位的数字
        // 核心计算公式：
        // 1. 所有位数比当前数字少的符合条件的数字总数
        // 2. 加上当前位数下，最高位小于当前数字最高位的符合条件的数字数
        // 3. 加上递归处理剩余部分的结果
        return arr[len] - 1 + (first - (first < 4 ? 1 : 2)) * arr[len] + process(num % offset, offset / 10, len - 1);
    }

    public static long process(long num, long offset, int len) {
        if (len == 0) {
            return 1;
        }
        long first = num / offset;//获取当前位的数字
        //计算当前位可选择的数字数量：
        //如果当前位数字小于4，有first种选择（0到first-1）
        //如果当前位数字大于等于4，有first-1种选择（0到first-1但排除4）
        // 然后乘以剩余位数的可能组合数，再递归处理下一位
        return (first < 4 ? first : (first - 1)) * arr[len] + process(num % offset, offset / 10, len - 1);
    }

    public static int decimalLength(long num) {
        int len = 0;
        while (num != 0) {
            len++;
            num /= 10;
        }
        return len;
    }

    public static long offset(int len) {
        long offset = 1;
        for (int i = 1; i < len; i++) {
            offset *= 10L;
        }
        return offset;
    }

    public static long notContains4Nums3(long num) {
        if (num <= 0) {
            return 0;
        }
        long ans = 0;
        // 循环处理num的每一位数字：
        // base：当前位的权重基数（从1开始，每处理一位就乘以9）
        // cur：当前正在处理的数字位
        // 循环条件：num不等于0（还有位需要处理）
        // 每次循环后：num去掉最后一位（num /= 10），base乘以9（下一位的权重）
        for (long base = 1, cur = 0; num != 0; num /= 10, base *= 9) {
            cur = num % 10;
            // 核心计算逻辑：
            // 如果当前位数字小于4，那么这一位有cur种合法选择（0到cur-1）
            // 如果当前位数字大于等于4，那么这一位有cur-1种合法选择（0到cur-1但要排除4）
            // 再乘以base（当前位的权重，即后面位数的所有可能组合数）
            // 累加结果到ans中
            ans += (cur < 4 ? cur : cur - 1) * base;
        }
        return ans;
    }

    public static void main(String[] args) {
        long max = 88888888L;
        System.out.println("功能测试开始，验证0~" + max + "以内所有的结果");
        for (long i = 0; i <= max; i++) {
            if (isNot4(i) && notContains4Nums2(i) != notContains4Nums3(i)) {
                System.out.println("Oops");
            }
        }
        System.out.println("如果没有打印Oops说明验证通过");

        long num = 8173528638135L;
        long start;
        long end;
        System.out.println("性能测试开始，计算num = " + num + "的答案");
        start = System.currentTimeMillis();
        long ans2 = notContains4Nums2(num);
        end = System.currentTimeMillis();
        System.out.println("方法二的答案:" + ans2 + "，运行时间:" + (end - start) + "ms");

        start = System.currentTimeMillis();
        long ans3 = notContains4Nums2(num);
        end = System.currentTimeMillis();
        System.out.println("方法三的答案:" + ans3 + "，运行时间:" + (end - start) + "ms");
    }
}

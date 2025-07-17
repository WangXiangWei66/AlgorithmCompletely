package Class19;

//给定一个正数N，比如N = 13，在纸上把所有数都列出来如下：
//1 2 3 4 5 6 7 8 9 10 11 12 13
//可以数出1这个字符出现了6次，给定一个正数N，如果把1~N都列出来，返回1这个字符出现的多少次
public class Code03_OneNumber {

    public static int solution1(int num) {
        if (num < 1) {
            return 0;
        }
        int count = 0;
        for (int i = 1; i != num + 1; i++) {
            count += get1Nums(i);
        }
        return count;
    }

    public static int get1Nums(int num) {
        int res = 0;
        while (num != 0) {
            if (num % 10 == 1) {
                res++;
            }
            num /= 10;
        }
        return res;
    }
    //本方法采用了数学分析的方式，通过递归分解来高效计算结果
    public static int countDigitOne(int num) {
        if (num < 1) {
            return 0;
        }
        int len = getLenOfNum(num);
        if (len == 1) {
            return 1;
        }
        int tmp1 = powerBaseOf10(len - 1);//获取最高位的权重
        int first = num / tmp1;//获取最高位的数字
        //如果最高位是 1（例如 123），则最高位中 1 的次数为剩余部分加 1（如 100-123 共 24 次）。
        //如果最高位大于 1（例如 324），则最高位中 1 的次数为tmp1（如 100-199 共 100 次）。
        int firstOneNum = first == 1 ? num % tmp1 + 1 : tmp1;
        //otherOneNum计算公式为：最高位数字 × (位数-1) × (tmp1/10)。
        //例如，对于 324，其他位中 1 的次数为：3 × 2 × 10 = 60。
        int otherOneNum = first * (len - 1) * (tmp1 / 10);
        //最后还要递归计算剩余的部分
        return firstOneNum + otherOneNum + countDigitOne(num % tmp1);
    }

    public static int getLenOfNum(int num) {
        int len = 0;
        while (num != 0) {
            len++;
            num /= 10;
        }
        return len;
    }

    public static int powerBaseOf10(int base) {
        return (int) Math.pow(10, base);
    }

    public static void main(String[] args) {
        int num = 50000000;
        long start1 = System.currentTimeMillis();
        System.out.println(solution1(num));
        long end1 = System.currentTimeMillis();
        System.out.println("cost time:" + (end1 - start1) + "ms");

        long start2 = System.currentTimeMillis();
        System.out.println(countDigitOne(num));
        long end2 = System.currentTimeMillis();
        System.out.println("cost time" + (end2 - start2) + "ms");
    }
}

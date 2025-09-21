package class_2022_02_2;

//给定一个长度为4的整数数组cards。你有 4 张卡片，每张卡片上都包含一个范围在 [1,9] 的数字
//您应该使用运算符['+', '-', '*', '/']和括号'('和')'将这些卡片上的数字排列成数学表达式，以获得值24。
//你须遵守以下规则:
//除法运算符 '/' 表示实数除法，而不是整数除法。
//例如，4 /(1 - 2 / 3)= 4 /(1 / 3)= 12。
//每个运算都在两个数字之间。特别是，不能使用 “-” 作为一元运算符。
//例如，如果 cards =[1,1,1,1] ，则表达式 “-1 -1 -1 -1” 是 不允许 的。
//你不能把数字串在一起
//例如，如果 cards =[1,2,1,2] ，则表达式 “12 + 12” 无效。
//如果可以得到这样的表达式，其计算结果为 24 ，则返回 true ，否则返回 false。
//leetcode链接: https://leetcode.com/problems/24-game/
public class Code01_24Game {

    public static boolean judgePoint24(int[] cards) {
        if (cards == null || cards.length == 0) {
            return false;
        }
        int n = cards.length;
        Number[] arr = new Number[n];
        //先将每个数字都转化为分数形式，分母为数字本身，分子为1
        for (int i = 0; i < n; i++) {
            arr[i] = new Number(cards[i], 1);
        }
        return judge(arr, cards.length);
    }
    //size：当前的有效元素数量
    public static boolean judge(Number[] arr, int size) {
        if (size == 1) {
            return arr[0].numerator == 24 && arr[0].denominator == 1;
        }
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Number inum = arr[i];
                Number jnum = arr[j];
                arr[j] = arr[size - 1];//将数字最后一个元素覆盖掉arr[j]
                arr[i] = add(inum, jnum);
                //尝试两数相加
                if (judge(arr, size - 1)) {
                    return true;
                }
                //尝试两数相减
                arr[i] = minus(inum, jnum);
                if (judge(arr, size - 1)) {
                    return true;
                }
                arr[i] = minus(jnum, inum);
                if (judge(arr, size - 1)) {
                    return true;
                }
                //尝试两数相乘
                arr[i] = multiply(inum, jnum);
                if (judge(arr, size - 1)) {
                    return true;
                }
                //尝试两数相除，注意保证分母不能为0
                arr[i] = divide(inum, jnum);
                if (arr[i] != null && judge(arr, size - 1)) {
                    return true;
                }
                arr[i] = divide(jnum, inum);
                if (arr[i] != null && judge(arr, size - 1)) {
                    return true;
                }
                //回溯
                arr[i] = inum;
                arr[j] = jnum;
            }
        }
        return false;
    }

    public static class Number {
        public int numerator;
        public int denominator;

        public Number(int numerator, int denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }
    }
    //(a/b + c/d) = (ad + bc)/bd
    public static Number add(Number a, Number b) {
        return simple(a.numerator * b.denominator + b.numerator * a.denominator, a.denominator * b.denominator);
    }
    //将分数进行化简
    public static Number simple(int up, int down) {
        if (up == 0) {
            return new Number(0, 1);
        }
        //计算最大公约数，并进行约分
        int gcd = Math.abs(gcd(up, down));
        return new Number(up / gcd, down / gcd);
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    //(a/b - c/d) = (ad - bc)/bd
    public static Number minus(Number a, Number b) {
        return simple(a.numerator * b.denominator - b.numerator * a.denominator, a.denominator * b.denominator);
    }
    //(a/b * c/d) = (ac)/(bd)
    public static Number multiply(Number a, Number b) {
        return simple(a.numerator * b.numerator, a.denominator * b.denominator);
    }
    //(a/b * c/d) = (ac)/(bd)
    public static Number divide(Number a, Number b) {
        return b.numerator == 0 ? null : simple(a.numerator * b.denominator, a.denominator * b.numerator);
    }
}

package Class08;

/*
本代码实现了两种不使用比较运算符来获取两个整数较大值的方法，核心思路是通过位运算和符号位判断来替代直接比较
 */
public class Code01_GetMax {
    public static int flip(int n) {
        return n ^ 1; //反转整数n的最后一位
    }
    //判断整数n的符号
    public static int sign(int n) {
        return flip((n >> 31) & 1);//正数返回1  0或负数返回0
    }

    public static int getMax1(int a, int b) {
        int c = a - b;
        int scA = sign(c);
        int scB = flip(scA);
        return a * scA + b * scB;
    }

    public static int getMax2(int a, int b) {
        int c = a - b;
        int sa = sign(a);
        int sb = sign(b);
        int sc = sign(c);
        int difSab = sa ^ sb;  //判断A与B的符号
        int sameSab = flip(difSab);//判断A与B的符号是否相同
        int returnA = difSab * sa + difSab * sc;
        int returnB = flip(returnA);
        return a * returnA + b * returnB;
    }

    public static void main(String[] args) {
        int a = -16;
        int b = -19;
        System.out.println(getMax1(a, b));
        System.out.println(getMax2(a, b));
        a = 2147483647;
        b = -2147483647;
        System.out.println(getMax1(a, b));//wrong answer because of overflow
        System.out.println(getMax2(a, b));
    }


}

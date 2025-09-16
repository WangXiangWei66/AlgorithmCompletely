package class003;

public class BinarySystem {
    public static boolean returnTrue() {
        System.out.println("进入了return True函数");
        return true;
    }

    public static boolean returnFalse() {
        System.out.println("进入了return False函数");
        return false;
    }

    public static void printBinary(int num) {
        for (int i = 31; i >= 0; i--) {
            System.out.print((num & (1 << i)) == 0 ? "0" : "1");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int a = 78;
        System.out.println(a);
        printBinary(a);
        System.out.println("=====a=====");
        //负数:计算机中，统一用补码表示负数
        int b = -6;
        System.out.println(b);
        printBinary(b);
        printBinary(6);
        System.out.println("======b======");
        //直接写二进制的形式定义变量
        int c = 0b1001110;
        System.out.println(c);
        printBinary(c);
        System.out.println("======c=====");
        int d = 0x4e;
        System.out.println(d);
        printBinary(d);
        System.out.println("====d=====");
        //相反数
        System.out.println(a);
        printBinary(a);
        printBinary(~a);
        int e = ~a + 1;
        System.out.println(e);
        printBinary(e);
        System.out.println("====e=====");
        // int、long的最小值，取相反数、绝对值，都是自己
        int f = Integer.MIN_VALUE;
        System.out.println(f);
        printBinary(f);
        System.out.println(-f);
        printBinary(-f);
        System.out.println(~f + 1);
        printBinary(~f + 1);
        System.out.println("===f====");

        int g = 0b0001010;
        int h = 0b0001100;
        printBinary(g | h);
        printBinary(g & h);
        printBinary(g ^ h);//相同为0，不同为1
        System.out.println("===g、h====");
        //&& 是 逻辑或、逻辑与，只能连接boolean类型
        //不仅如此，|、& 连接的两侧一定都会计算,而 ||、&& 有穿透性的特点
        System.out.println("test1测试开始");
        boolean test1 = returnTrue() | returnFalse();
        System.out.println("test1结果，" + test1);
        System.out.println("test2测试开始");
        boolean test2 = returnTrue() || returnFalse();
        System.out.println("test2结果，" + test2);
        System.out.println("test3测试开始");
        boolean test3 = returnFalse() & returnTrue();
        System.out.println("test3结果，" + test3);
        System.out.println("test4测试开始");
        boolean test4 = returnFalse() && returnTrue();
        System.out.println("test4结果，" + test4);
        System.out.println("===|、&、||、&&===");

        int i = 0b0011010;
        printBinary(i);
        printBinary(i << 1);
        printBinary(i << 2);
        printBinary(i << 3);
        System.out.println("====i<<====");

        printBinary(i);
        printBinary(i >> 2);
        printBinary(i >>> 2);
        System.out.println("====i>>>====");

        int j = 0b11110000000000000000000000000000;
        printBinary(j);
        printBinary(j >> 2);//符号位置不动
        printBinary(j >>> 2);//符号位置跟着往后移动
        System.out.println("=====j>>>=====");
        //只有非负数才有这个逻辑
        int k = 10;
        System.out.println(k);
        System.out.println(k << 1);
        System.out.println(k << 2);
        System.out.println(k << 3);
        System.out.println(k >> 1);
        System.out.println(k >> 2);
        System.out.println(k >> 3);
        System.out.println("===k===");
    }
}

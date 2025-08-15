package Class36;

//来自网易
//规定：L[1]对应a，L[2]对应b，L[3]对应c，...，L[25]对应y
//S1 = a
//S(i) = S(i-1) + L[i] + reverse(invert(S(i-1)));
//解释invert操作：
//S1 = a
//S2 = aby
//假设invert(S(2)) = 甲乙丙
//a + 甲 = 26, 那么 甲 = 26 - 1 = 25 -> y
//b + 乙 = 26, 那么 乙 = 26 - 2 = 24 -> x
//y + 丙 = 26, 那么 丙 = 26 - 25 = 1 -> a
//如上就是每一位的计算方式，所以invert(S2) = yxa
//所以S3 = S2 + L[3] + reverse(invert(S2)) = aby + c + axy = abycaxy
//invert(abycaxy) = yxawyba, 再reverse = abywaxy
//所以S4 = abycaxy + d + abywaxy = abycaxydabywaxy
//直到S25结束
//给定两个参数n和k，返回Sn的第k位是什么字符，n从1开始，k从1开始
//比如n=4，k=2，表示S4的第2个字符是什么，返回b字符
public class Code01_ReverseInvertString {
    //存储Sn的长度
    //前半段长度 = half = lens[n-1]
    //中间字符位置 = half + 1
    //后半段长度 = half（与前半段等长）
    //总长度 = 2*half + 1
    public static int[] lens = null;

    //初始化lens数组，计算每个Sn的长度
    public static void fillLens() {
        lens = new int[26];
        lens[1] = 1;
        //从S2开始计算，公式：len(n) = 2*len(n-1) + 1
        for (int i = 2; i <= 25; i++) {
            lens[i] = (lens[i - 1] << 1) + 1;
        }
    }

    //查询Sn的第k个字符
    public static char kth(int n, int k) {
        //先对len进行初始化
        if (lens == null) {
            fillLens();
        }
        if (n == 1) {
            return 'a';
        }
        //S(n) = S(n-1) + L[n] + reverse(invert(S(n-1)));
        int half = lens[n - 1];//S(n-1)的长度为lens[n-1]
        if (k <= half) {
            return kth(n - 1, k);
        } else if (k == half + 1) {
            return (char) ('a' + n - 1);
        } else {
            //先计算k在反转前对应的位置
            return invert(kth(n - 1, ((half + 1) << 1) - k));
        }
    }

    public static char invert(char c) {
        return (char) (('a' << 1) + 24 - c);//(char)('z' - (c - 'a'))
    }

    //生成完整字符串：S(n) = S(n-1) + L[n] + reverse(invert(S(n-1)))
    public static String generateString(int n) {
        String s = "a";
        for (int i = 2; i <= n; i++) {
            s = s + (char) ('a' + i - 1) + reverseInvert(s);
        }
        return s;
    }

    //反转+反转字符
    public static String reverseInvert(String s) {
        char[] invert = invert(s).toCharArray();//对s的每个字符执行invert操作
        for (int l = 0, r = invert.length - 1; l < r; l++, r--) {
            char tmp = invert[l];
            invert[l] = invert[r];
            invert[r] = tmp;
        }
        return String.valueOf(invert);
    }

    //字符批量反转
    public static String invert(String s) {
        char[] str = s.toCharArray();
        for (int i = 0; i < str.length; i++) {
            str[i] = invert(str[i]);
        }
        return String.valueOf(str);
    }

    public static void main(String[] args) {
        int n = 20;
        String str = generateString(n);
        int len = str.length();
        System.out.println("test begin!");
        for (int i = 1; i <= len; i++) {
            if (str.charAt(i - 1) != kth(n, i)) {
                System.out.println(i);
                System.out.println(str.charAt(i - 1));
                System.out.println(kth(n, i));
                System.out.println("Oops");
                break;
            }
        }
        System.out.println("test end");
    }
}

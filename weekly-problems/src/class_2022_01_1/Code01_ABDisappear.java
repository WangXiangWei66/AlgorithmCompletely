package class_2022_01_1;

//来自阿里
//给定一个只由'a'和'b'组成的字符串str，
//str中"ab"和"ba"子串都可以消除，
//消除之后剩下字符会重新靠在一起，继续出现可以消除的子串...
//你的任务是决定一种消除的顺序，最后让str消除到尽可能的短
//返回尽可能的短的剩余字符串
public class Code01_ABDisappear {

    public static String disappear1(String str) {
        String ans = str;
        for (int i = 1; i < str.length(); i++) {
            boolean hasA = str.charAt(i - 1) == 'a' || str.charAt(i) == 'a';
            boolean hasB = str.charAt(i - 1) == 'b' || str.charAt(i) == 'b';
            if (hasA && hasB) {
                String curAns = disappear1(str.substring(0, i - 1) + str.substring(i + 1));
                if (curAns.length() < ans.length()) {
                    ans = curAns;
                }
            }
        }
        return ans;
    }

    public static String disappear2(String str) {
        String ans = str;
        for (int i = 1; i < str.length(); i++) {
            boolean hasA = str.charAt(i - 1) == 'a' || str.charAt(i) == 'a';
            boolean hasB = str.charAt(i - 1) == 'b' || str.charAt(i) == 'b';
            if (hasA && hasB) {
                return disappear2(str.substring(0, i - 1) + str.substring(i + 1));
            }
        }
        return ans;
    }

    public static String disappear3(String s) {
        char[] str = s.toCharArray();
        int n = str.length;
        int[] stack = new int[n];//创建字符在数组中的索引
        int size = 0;
        for (int i = 0; i < n; i++) {
            boolean hasA = size != 0 && str[stack[size - 1]] == 'a';
            boolean hasB = size != 0 && str[stack[size - 1]] == 'b';
            hasA |= str[i] == 'a';
            hasB |= str[i] == 'b';
            if (hasA && hasB) {
                size--;
            } else {
                stack[size++] = i;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(str[stack[i]]);
        }
        return builder.toString();
    }

    public static String disappear4(String s) {
        int count = 0;
        for (char cha : s.toCharArray()) {
            count += cha == 'a' ? 1 : -1;
        }
        StringBuilder builder = new StringBuilder();
        char rest = count > 0 ? 'a' : 'b';
        count = Math.abs(count);
        for (int i = 0; i < count; i++) {
            builder.append(rest);
        }
        return builder.toString();
    }

    public static String randomString(int len, int varible) {
        char[] str = new char[len];
        for (int i = 0; i < str.length; i++) {
            str[i] = (char) ((int) (Math.random() * varible) + 'a');
        }
        return String.valueOf(str);
    }

    public static void main(String[] args) {
        int n = 14;
        int v = 2;
        int testTimes = 10000;
        System.out.println("test begin!");
        for (int i = 0; i < testTimes; i++) {
            int len = (int) (Math.random() * n) + 1;
            String test = randomString(len, v);
            String ans1 = disappear1(test);
            String ans2 = disappear2(test);
            String ans3 = disappear3(test);
            String ans4 = disappear4(test);
            if (!ans1.equals(ans2) || !ans1.equals(ans3) || !ans1.equals(ans4)) {
                System.out.println(test);
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println(ans3);
                System.out.println(ans4);
                System.out.println("出错了!");
                break;
            }
        }
        System.out.println("test finish!");
    }
}

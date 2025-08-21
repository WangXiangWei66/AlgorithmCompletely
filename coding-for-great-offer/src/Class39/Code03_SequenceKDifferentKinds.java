package Class39;

//来自百度
//给定一个字符串str，和一个正数k
//str子序列的字符种数必须是k种，返回有多少子序列满足这个条件
//已知str中都是小写字母
public class Code03_SequenceKDifferentKinds {

    public static int f(int[] bu, int index, int rest) {
        if (index == bu.length) {
            return rest == 0 ? 1 : 0;
        }

        int p1 = f(bu, index + 1, rest);
        int p2 = 0;
        if (rest > 0) {
            p2 = (1 << bu[index] - 1) * f(bu, index + 1, rest - 1);
        }
        return p1 + p2;
    }

    public static int nums(String s, int k) {
        char[] str = s.toCharArray();
        int[] counts = new int[26];
        for (char c : str) {
            counts[c - 97]++;
        }
        return ways(counts, 0, k);
    }
    //c：当前字符在字符串中出现的次数
    public static int ways(int[] c, int i, int r) {
        if (r == 0) {
            return 1;
        }
        if (i == c.length) {
            return 0;
        }
        return math(c[i]) * ways(c, i + 1, r - 1) + ways(c, i + 1, r);
    }
    //n个相同字符能组成的非空子序列数量
    public static int math(int n) {
        return (1 << n) - 1;
    }

    public static void main(String[] args) {
        String str = "acbbca";
        int k = 3;
        System.out.println(nums(str, k));
    }
}

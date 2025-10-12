package class_2022_05_1;

//来自optiver
//给定一个字符串str，和一个正数k
//你可以随意的划分str成多个子串，
//目的是找到在某一种划分方案中，有尽可能多的回文子串，长度>=k，并且没有重合
//返回有几个回文子串
public class Code05_PalindromeStringNoLessKLenNoOverlapingMaxParts {

    public static int maxPalindromes1(String s, int k) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        return process1(str, 0, k);
    }

    public static int process1(char[] str, int index, int k) {
        //剩余长度小于k，无法形成符合要求的回文子串
        if (str.length - index < k) {
            return 0;
        }
        //不适用当前索引的字符
        int ans = process1(str, index + 1, k);
        //从当前索引开始，寻找所有长度>=k的子串
        for (int i = index + k - 1; i < str.length; i++) {
            if (isPalindrome(str, index, i)) {
                ans = Math.max(ans, 1 + process1(str, i + 1, k));
            }
        }
        return ans;
    }

    public static boolean isPalindrome(char[] str, int L, int R) {
        while (L < R) {
            if (str[L++] != str[R--]) {
                return false;
            }
        }
        return true;
    }

    public static int maxPalindromes2(String s, int k) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = manacherString(s);
        int[] p = new int[str.length];//存储每个位置的回文半径
        int ans = 0;
        int next = 0;//记录查找位置
        while ((next = manacherFind(str, p, next, k)) != -1) //找符合条件的回文子串的结束位置
        {
            next = str[next] == '#' ? next : (next + 1);
            ans++;
        }
        return ans;
    }
    //转化为Manacher格式，用于统一处理奇数长度和偶数长度的回文子串
    public static char[] manacherString(String s) {
        char[] str = s.toCharArray();
        char[] ans = new char[s.length() * 2 + 1];
        int index = 0;
        for (int i = 0; i != ans.length; i++) {
            //在偶数位置插入#
            ans[i] = (i & 1) == 0 ? '#' : str[index++];
        }
        return ans;
    }
    //查找符合条件的回文子串
    //l：起始查找位置
    //k；最小长度要求
    public static int manacherFind(char[] s, int[] p, int l, int k) {
        int c = l - 1;//当前回文中心
        int r = l - 1;//当前回文右边界
        int n = s.length;
        //利用回文信息计算p[i]初始值
        for (int i = l; i < s.length; i++) {
            //当i在r范围内时，可通过对称点2*c-i的回文半径快速计算
            p[i] = r > i ? Math.min(p[2 * c - i], r - i) : 1;
            //尝试拓展回文半径
            while (i + p[i] < n && i - p[i] > l - 1 && s[i + p[i]] == s[i - p[i]]) {
                if (++p[i] > k) {
                    return i + k;
                }
            }
            //拓展回文右边界和中心
            if (i + p[i] > r) {
                r = i + p[i];
                c = i;
            }
        }
        return -1;
    }

    public static String randomString(int n, int r) {
        char[] str = new char[(int) (Math.random() * n)];
        for (int i = 0; i < str.length; i++) {
            str[i] = (char) ((int) (Math.random() * r) + 'a');
        }
        return String.valueOf(str);
    }

    // 为了测试
    public static void main(String[] args) {
        int n = 20;
        int r = 3;
        int testTime = 50000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            String str = randomString(n, r);
            int k = (int) (Math.random() * str.length()) + 1;
            int ans1 = maxPalindromes1(str, k);
            int ans2 = maxPalindromes2(str, k);
            if (ans1 != ans2) {
                System.out.println(str);
                System.out.println(k);
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println("出错了!");
                break;
            }
        }
        System.out.println("测试结束");
    }

}

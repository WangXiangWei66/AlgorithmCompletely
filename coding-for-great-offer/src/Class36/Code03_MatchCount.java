package Class36;

//来自美团
//给定两个字符串s1和s2
//返回在s1中有多少个子串等于s2
public class Code03_MatchCount {
    //时间复杂度为O(N + M)
    public static int sa(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() < s2.length()) {
            return 0;
        }

        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        return count(str1, str2);
    }

    public static int count(char[] str1, char[] str2) {
        int x = 0;//str1的当前匹配位置
        int y = 0;//str2的当前匹配位置
        int count = 0;//匹配成功的字串数量
        int[] next = getNextArray(str2);
        while (x < str1.length) {
            if (str1[x] == str2[y]) {
                x++;
                y++;
                if (y == str2.length) {
                    count++;
                    y = next[y];
                }
                //匹配失败了，且y已经到了初始位置
            } else if (next[y] == -1) {
                x++;
            } else {
                y = next[y];
            }
        }
        return count;
    }
    //next[i]:str2[0...i-1]中最长前缀和后缀相等的长度
    public static int[] getNextArray(char[] str2) {
        if (str2.length == 1) {
            return new int[]{-1, 0};
        }
        int[] next = new int[str2.length + 1];
        next[0] = -1;
        next[1] = 0;
        int i = 2;//当前的计算位置
        int cn = 0;//当前的最长前缀长度
        while (i < next.length) {
            if (str2[i - 1] == str2[cn]) {
                next[i++] = ++cn;
            } else if (cn > 0) {
                cn = next[cn];
            } else {
                next[i++] = 0;
            }
        }
        return next;
    }
}

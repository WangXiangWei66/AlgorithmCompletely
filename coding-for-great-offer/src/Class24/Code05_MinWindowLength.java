package Class24;

//给定两个字符串str1和str2，在str1中寻找一个最短子串，能包含str2的所有字符
//字符顺序无所谓，str1的这个最短子串也可以包含多余的字符，返回这个最短包含子串
public class Code05_MinWindowLength {
    //使用了双指针算法
    public static int minLength(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() < s2.length()) {
            return Integer.MAX_VALUE;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        //创建一个长度为256的数组（覆盖所有ASCII字符），用于记录字符出现次数
        int[] map = new int[256];
        //统计s2种每个字符出现的次数
        for (int i = 0; i != str2.length; i++) {
            map[str2[i]]++;
        }
        int all = str2.length;//还需要匹配的字符种数
        //滑动窗口的左右指针
        int L = 0;
        int R = 0;
        int minLen = Integer.MAX_VALUE;//记录可能的最短字串长度
        while (R != str1.length) {//使用右指针来遍历str1
            map[str1[R]]--;//将当前右指针指向的字符在map中的计数减1
            //如果减1后计数仍>=0，说明这个字符是s2中需要的，减少需要匹配的总数
            if (map[str1[R]] >= 0) {
                all--;
            }
            //当前窗口已包含[L,R]的所有字符
            if (all == 0) {
                //尝试移动左指针，缩小窗口范围，寻找更短的有效子串
                //当左指针指向的字符计数<0时，说明该字符在窗口中出现次数超过s2的需求，可以移动
                while (map[str1[L]] < 0) {
                    map[str1[L++]]++;//先恢复再缩小窗口
                }
                minLen = Math.min(minLen, R - L + 1);
                all++;//增加匹配的总数
                map[str1[L++]]++;//恢复计数并移动左指针
            }
            R++;
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    public static String minWindow(String s, String t) {
        if (s.length() < t.length()) {
            return "";
        }
        char[] str = s.toCharArray();
        char[] target = t.toCharArray();
        int[] map = new int[256];
        for (char cha : target) {
            map[cha]++;
        }
        int all = target.length;
        int L = 0;
        int R = 0;
        int minLen = Integer.MAX_VALUE;
        int ansl = -1;
        int ansr = -1;
        while (R != str.length) {
            map[str[R]]--;
            if (map[str[R]] >= 0) {
                all--;
            }
            if (all == 0) {
                while (map[str[L]] < 0) {
                    map[str[L++]]++;
                }
                if (minLen > R - L + 1) {
                    minLen = R - L + 1;
                    ansl = L;
                    ansr = R;
                }
                all++;
                map[str[L++]]++;
            }
            R++;
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(ansl, ansr + 1);
    }
}

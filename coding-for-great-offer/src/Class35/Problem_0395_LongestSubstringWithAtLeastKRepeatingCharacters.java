package Class35;

//给你一个字符串 s 和一个整数 k ，请你找出 s 中的最长子串，要求该子串中的每一字符出现次数都不少于 k 。返回这一子串的长度。
//示例 1：
//输入：s = "aaabb", k = 3
//输出：3
//解释：最长子串为 "aaa" ，其中 'a' 重复了 3 次。
//示例 2：
//输入：s = "ababbc", k = 2
//输出：5
//解释：最长子串为 "ababb" ，其中 'a' 重复了 2 次， 'b' 重复了 3 次。
//提示：
//1 <= s.length <= 10^4
//s 仅由小写英文字母组成
//Leetcode题目 : https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/
public class Problem_0395_LongestSubstringWithAtLeastKRepeatingCharacters {
    //时间复杂度为O(N^2),额外空间复杂度为O(1)
    public static int longestSubString1(String s, int k) {
        char[] str = s.toCharArray();
        int N = str.length;
        int max = 0;//记录找到的最长符合条件子串的长度
        //遍历所有可能的子串的起始位置
        for (int i = 0; i < N; i++) {
            int[] count = new int[256];
            int collect = 0;//当前字串中包含的不同字符的数量
            int satify = 0;//当前字串中出现次数达到k的字符数量
            //遍历所有可能的结束位置
            for (int j = i; j < N; j++) {
                if (count[str[j]] == 0) {
                    collect++;
                }
                if (count[str[j]] == k - 1) {
                    satify++;
                }
                count[str[j]]++;
                //收集到的所有字符都符合要求
                if (collect == satify) {
                    max = Math.max(max, j - i + 1);
                }
            }
        }
        return max;
    }

    //时间复杂度为O（N）
    public static int longestSubString2(String s, int k) {
        char[] str = s.toCharArray();
        int N = str.length;
        int max = 0;
        //枚举字串中可能包含的不同字符数量
        for (int require = 1; require <= 26; require++) {
            int[] count = new int[26];
            int collect = 0;
            int satisfy = 0;
            int R = -1;//滑动窗口的右边界
            //滑动窗口的左边界
            for (int L = 0; L < N; L++) {
                //R+1未越界，且不是(已收集够require种字符且下一个字符是新的)
                while (R + 1 < N && !(collect == require && count[str[R + 1] - 'a'] == 0)) {
                    R++;
                    if (count[str[R] - 'a'] == 0) {
                        collect++;
                    }
                    if (count[str[R] - 'a'] == k - 1) {
                        satisfy++;
                    }
                    count[str[R] - 'a']++;
                }
                if (satisfy == require) {
                    max = Math.max(max, R - L + 1);
                }
                if (count[str[L] - 'a'] == 1) {
                    collect--;
                }
                if (count[str[L] - 'a'] == k) {
                    satisfy--;
                }
                count[str[L] - 'a']--;//减少该字符出现的种类数
            }
        }
        return max;
    }
    //时间复杂度为O(N)
    public static int longestSubString3(String s, int k) {
        return process(s.toCharArray(), 0, s.length() - 1, k);
    }

    public static int process(char[] str, int L, int R, int k) {
        if (L > R) {
            return 0;
        }
        int[] counts = new int[256];
        for (int i = L; i <= R; i++) {
            counts[str[i] - 'a']++;//将字符映射为0~25的索引
        }
        char few = 0;//出现次数最少的字符
        int min = Integer.MAX_VALUE;//最少出现次数
        for (int i = 0; i < 26; i++) {
            if (counts[i] != 0 && min > counts[i]) {
                few = (char) (i + 'a');
                min = counts[i];
            }
        }
        if (min >= k) {
            return R - L + 1;
        }
        //以出现字符最少的为分割点，递归处理左右两部分
        int pre = 0;//上一个分割点位置
        int max = Integer.MIN_VALUE;//最大符合条件的字串长度
        for (int i = L; i <= R; i++) {
            if (str[i] == few) {
                max = Math.max(max, process(str, pre, i - 1, k));
                pre = i + 1;
            }
        }
        if (pre != R + 1) {
            max = Math.max(max, process(str, pre, R, k));
        }
        return max;
    }
}

package Class34;

//给定一个字符串str，和一个正数k，返回字符种类不超过k种的最长子串长度。
//Leetcode题目 : https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/
public class Problem_0340_LongestSubstringWithAtMostKDistinctCharacters {

    public static int lengthOfLongestSubStringDistinct(String s, int k) {
        if (s == null || s.length() == 0 || k < 1) {
            return 0;
        }
        char[] str = s.toCharArray();
        int N = str.length;
        int[] count = new int[256];//记录每个字符出现的次数
        int diff = 0;//记录当前窗口中不同字符的数量
        int R = 0;//滑动窗口的右边界指针
        int ans = 0;
        //i为滑动窗口的左指针
        for (int i = 0; i < N; i++) {
            while (R < N && (diff < k) || (diff == k && count[str[R]] > 0)) {
                diff += count[str[R]] == 0 ? 1 : 0;
                count[str[R++]]++;
            }
            ans = Math.max(ans, R - i);//更新最长的字串的长度
            diff -= count[str[i]] == 1 ? 1 : 0;
            count[str[i]]--;
        }
        return ans;
    }
}

package class_2022_06_1;
//把字符串 s 看作 "abcdefghijklmnopqrstuvwxyz"的无限环绕字符串，
//所以s 看起来是这样的：
//...zabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcd....
//现在给定另一个字符串 p 。返回s 中 不同 的 p 的 非空子串的数量
//测试链接 : https://leetcode.com/problems/unique-substrings-in-wraparound-string/
public class Code02_UniqueSubstringsInWraparoundString {

    public int findSubstringInWrapRoundString(String p) {
        char[] str = p.toCharArray();//便于按照索引访问字符
        int n = str.length;
        int ans = 0;
        int len = 1;//记录最长连续有效子串长度
        int[] max = new int[256];
        max[str[0]]++;
        for (int i = 1; i < n; i++) {
            char cur = str[i];
            char pre = str[i - 1];
            if ((pre == 'z' & cur == 'a') || pre + 1 == cur) {
                len++;
            } else {
                len = 1;
            }
            max[cur] = Math.max(max[cur], len);
        }
        for (int i = 0; i < 256; i++) {
            ans += max[i];
        }
        return ans;
    }
}

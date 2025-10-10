package class_2022_04_3;

//来自微众
//4.11笔试
//给定n位长的数字字符串和正数k，求该子符串能被k整除的子串个数
//(n<=1000，k<=100)
public class Code05_ModKSubstringNumbers {

    public static int modWays1(String s, int k) {
        int n = s.length();
        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (Long.valueOf(s.substring(i, j + 1)) % k == 0) {
                    ans++;
                }
            }
        }
        return ans;
    }

    //任何子串都可以看作某个位置结尾的数字
    public static int modWays2(String s, int k) {
        int[] cur = new int[k];//当前状态下，各余数对应的子串数量
        int[] next = new int[k];//临时存储下一轮计算状态
        int mod = 0;//当前结尾的数字对k的余数
        int ans = 0;//累积符合条件的子串总数
        for (char cha : s.toCharArray()) {
            for (int i = 0; i < k; i++) {
                next[(i * 10) % k] += cur[i];//在上一轮的基础上，继续添加在末尾
                cur[i] = 0;
            }
            int[] tmp = cur;
            cur = next;
            next = tmp;
            mod = (mod * 10 + (cha - '0')) % k;
            ans += (mod == 0 ? 1 : 0) + cur[mod];
            cur[mod]++;
        }
        return ans;
    }
}

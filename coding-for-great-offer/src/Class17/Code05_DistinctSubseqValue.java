package Class17;

import java.util.HashMap;

//给定一个字符串str，返回str的所有子序列中有多少不同的字面值
public class Code05_DistinctSubseqValue {
    //本代码使用了动态规划和哈希表的思想
    public static int distinctSubseqII(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        // 定义模数，防止溢出
        long m = 1000000007;
        char[] str = s.toCharArray();
        // count数组记录每个字符最后一次出现时新增的子序列数量
        long[] count = new long[26];
        // all表示当前所有不同子序列的数量（包括空序列）
        long all = 1;
        for (char x : str) {
            // 计算新增的子序列数量：
            // 用当前所有子序列数量减去该字符上次出现时的子序列数量
            // 加上m再取模是为了处理负数情况
            long add = (all - count[x - 'a'] + m) % m;
            // 更新所有子序列数量
            all = (all + add) % m;
            // 更新该字符最后一次出现时的子序列数量
            count[x - 'a'] = (count[x - 'a'] + add) % m;
        }
        // 减去空序列的情况
        all = (all - 1 + m) % m;
        return (int) all;
    }

    public static int wang(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int m = 1000000007;
        char[] str = s.toCharArray();
        // 使用哈希表记录每个字符最后一次出现时的新增子序列数量
        HashMap<Character, Integer> map = new HashMap<>();
        int all = 1;
        for (char x : str) {
            int newAdd = all;
//            int curAll = all + newAdd - (map.containsKey(x) ? map.get(x) : 0);
            int curAll = all;
            curAll = (curAll + newAdd) % m;
            curAll = (curAll - (map.containsKey(x) ? map.get(x) : 0) + m) % m;
            all = curAll;
            map.put(x, newAdd);
        }
        return all;
    }

    public static void main(String[] args) {
        String s = "bccaccbaabbc";
        System.out.println(distinctSubseqII(s) + 1);
        System.out.println(wang(s));
    }
}

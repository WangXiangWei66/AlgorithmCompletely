package Class17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// 再次优化，不用哈希表，不用前缀树，用字符串哈希！
// 本题测试链接 : https://leetcode.com/problems/palindrome-pairs/
public class Code03_PalindromePairs2 {
    //hash(s)=s0×BASE^(n−1)+s1×BASE^(n−2)+⋯+sn−1×BASE^0
    //选一个质数做进制数,选择质数可以减少哈希冲突的概率
    public static int BASE = 499;

    //计算一个字符串的哈希值
    public static long hashValue(String str) {
        if (str.equals("")) {
            return 0;
        }
        //str.charAt:获取字符串中指定位置的字符
        int n = str.length();
        long ans = str.charAt(0) - 'a' + 1; //将字符映射为1~26
        for (int j = 1; j < n; j++) {
            ans = ans * BASE + str.charAt(j) - 'a' + 1;
        }
        return ans;
    }

    //字符串最大长度
    //以下内容看字符串哈希的内容
    public static int MAXN = 301;//最多处理长度为300的字符串
    public static long[] pow = new long[MAXN];//预计算基数BASE的幂次

    //静态初始化快，在类加载时执行
    static {
        pow[0] = 1;
        for (int j = 1; j < MAXN; j++) {
            pow[j] = pow[j - 1] * BASE;//递推公式
        }
    }

    public static long[] hash = new long[MAXN];//预计算字符串的前缀哈希值

    //计算字符串str的前缀哈希数组
    public static void buildHash(String str) {
        hash[0] = str.charAt(0) - 'a' + 1;
        for (int j = 1; j < str.length(); j++) {
            hash[j] = hash[j - 1] * BASE + str.charAt(j) - 'a' + 1;
        }
    }

    //用于计算索引l到r的字串的哈希值
    public static long hashValue(int l, int r) {
        if (l > r) {
            return 0;
        }
        long ans = hash[r];
        ans -= l == 0 ? 0 : (hash[l - 1] * pow[r - l + 1]);
        return ans;
    }

    public static List<List<Integer>> palindromePairs(String[] words) {
        HashMap<Long, Integer> hash = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            hash.put(hashValue(words[i]), i); //计算单词的哈希值，将每个单词的哈希值映射到其索引
        }
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            res.addAll(findAll(words[i], i, hash));
        }
        return res;
    }

    public static List<List<Integer>> findAll(String word, int index, HashMap<Long, Integer> hash) {
        List<List<Integer>> res = new ArrayList<>();
        String reverse = reverse(word);
        //0代表""字符串
        Integer rest = hash.get(0L);
        if (rest != null && rest != index && word.equals(reverse)) {
            res.add(Arrays.asList(rest, index));
            res.add(Arrays.asList(index, rest));
        }
        if (!word.equals("")) {
            buildHash(reverse);//word的反转字符串reverse的前缀哈希数组
            int[] rs = manacherss(word);//获得word的每个单词的回文半径rs
            int mid = rs.length >> 1;
            for (int i = mid - 1; i >= 1; i--) {
                if (i - rs[i] == -1) {//word从开头到i的位置是回文
                    rest = hash.get(hashValue(0, mid - i - 1));
                    if (rest != null && rest != index) {
                        res.add(Arrays.asList(rest, index));
                    }
                }
            }

            for (int i = mid + 1; i < rs.length; i++) {
                if (i + rs[i] == rs.length) {
                    rest = hash.get(hashValue((mid << 1) - i, reverse.length() - 1));
                    if (rest != null && rest != index) {
                        res.add(Arrays.asList(index, rest));
                    }
                    //Arrays.asList：将多个元素或数组转化为一个固定大小的列表
                }
            }
        }
        return res;
    }

    //在O(N)的时间内，计算出每个位置的最长回文字串的长度
    public static int[] manacherss(String word) {
        char[] mchs = manachercs(word);
        int[] rs = new int[mchs.length];
        int center = -1;// 当前最大回文右边界的中心位置
        int pr = -1;// 当前最大回文右边界（即center+rs[center]）
        for (int i = 0; i != mchs.length; i++) {
            rs[i] = pr > i ? Math.min(rs[(center << 1) - i], pr - i) : 1;
            while (i + rs[i] < mchs.length && i - rs[i] > -1) {
                //判断继续拓展位置的字符是否相等
                if (mchs[i + rs[i]] != mchs[i - rs[i]]) {
                    break;
                }
                rs[i]++;
            }
            if (i + rs[i] > pr) {
                pr = i + rs[i];
                center = i;
            }
        }
        return rs;
    }

    public static char[] manachercs(String word) {
        char[] chs = word.toCharArray();
        char[] mchs = new char[chs.length * 2 + 1];
        int index = 0;
        for (int i = 0; i != mchs.length; i++) {
            mchs[i] = (i & 1) == 0 ? '#' : chs[index++];
        }
        return mchs;
    }

    public static String reverse(String str) {
        char[] chs = str.toCharArray();
        int l = 0;
        int r = chs.length - 1;
        while (l < r) {
            char tmp = chs[l];
            chs[l++] = chs[r];
            chs[r--] = tmp;
        }
        return String.valueOf(chs);
    }
}

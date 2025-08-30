package Class46;

//一个字符串可以用缩写的形式来代替，比如单词"substitution"，可以有以下几种缩写：
//"s10n" ("s+省略10个字符+n")
//"sub4u4" ("sub+省略4个字符+u+省略4个字符")
//"12" ("省略12个字符")
//等等还有很多
//给定一个字符串target、给定一个字符串数组作为字典dictionary，要求把target缩写成长度最短的形式，但是又不会和dictionary中任何单词混淆
//返回缩写最短形式的一种结果就可以
//例子1：
//输入：target = "apple", dictionary = ["blade"]
//输出: "a4"
//解释：
//"apple"最短的缩写是"5"，但是字典中"blade"长度也是5，所以会混淆
//"apple"第二短的缩写，有一种是"4e"，但是"4e"也可以是字典中"blade"，所以会混淆
//"apple"第二短的缩写，有一种是"a4"，字典中"blade"不以a开头，所以不会混淆
//例子2：
//输入: target = "apple", dictionary = ["blade","plain","amber"]
//输出: "1p3"
//leetcode题目：https://leetcode.com/problems/minimum-unique-word-abbreviation/
public class Problem_0411_MinimumUniqueWordAbbreviation {

    public static int min = Integer.MAX_VALUE;//记录最短缩写长度

    public static int best = 0;//用位运算来记录最优的缩写方案

    //计算缩写长度
    //功能：根据二进制掩码fix计算缩写的长度，fix中为1的位表示保留对应位置的字符，为0的位表示忽略
    public static int abbrLen(int fix, int len) {
        int ans = 0;//缩写总长度
        int cnt = 0;//连续省略的字符数
        for (int i = 0; i < len; i++) {
            if ((fix & (1 << i)) != 0) {//该字符需要保留
                ans++;
                if (cnt != 0) {
                    ans += 1;//已经有了省略的字符，数字也要占一个位置
                }
                cnt = 0;
            } else {
                cnt++;
            }
        }
        //处理末尾的情况
        if (cnt != 0) {
            ans += 1;
        }
        return ans;
    }

    //检查缩写是否唯一
    //word 的二进制位为 1 的位置：目标字符串与该字典单词不同的位置。
    //fix 的二进制位为 1 的位置：缩写中保留的字符位置。
    public static boolean canFix(int[] words, int fix) {
        for (int word : words) {
            if ((fix & word) == 0) {
                return false;
            }
        }
        return true;
    }

    public static String minAbbreviation1(String target, String[] dictionary) {
        min = Integer.MAX_VALUE;//重置最短长度
        best = 0;//重置最有方案
        char[] t = target.toCharArray();
        int len = t.length;
        //将与目标字符串长度相同的字符串筛选出来
        int siz = 0;
        for (String word : dictionary) {
            if (word.length() == len) {
                siz++;
            }
        }
        //用位运算记录每个同长单词的差异
        //计算目标字符串与字典单词的差异掩码
        int[] words = new int[siz];
        int index = 0;
        for (String word : dictionary) {
            //仅处理与目标字符串长度相同的单词
            if (word.length() == len) {
                char[] w = word.toCharArray();
                int status = 0;//记录差异的位置
                for (int j = 0; j < len; j++) {
                    //不同的字符位置标记为1
                    if (t[j] != w[j]) {
                        status |= 1 << j;
                    }
                }
                words[index++] = status;
            }
        }
        //深度优先所有寻找最优缩写
        dfs1(words, len, 0, 0);
        StringBuilder builder = new StringBuilder();
        int count = 0;//记录连续省略的字符数
        for (int i = 0; i < len; i++) {
            if ((best & (1 << i)) != 0) {//要保留的字符
                if (count > 0) {
                    builder.append(count);
                }
                builder.append(t[i]);
                count = 0;
            } else {
                count++;
            }
        }
        if (count > 0) {
            builder.append(count);
        }
        return builder.toString();
    }

    public static void dfs1(int[] words, int len, int fix, int index) {
        //如果当前缩写还没有区分所有的单词（需要继续优化）
        if (!canFix(words, fix)) {
            if (index < len) {
                dfs1(words, len, fix, index + 1);
                dfs1(words, len, fix | (1 << index), index + 1);
            }
        } else {
            int ans = abbrLen(fix, len);
            if (ans < min) {
                min = ans;
                best = fix;
            }
        }
    }

    public static String minAbbreviation2(String target, String[] dictionary) {
        min = Integer.MAX_VALUE;//最短长度
        best = 0;//最优方案
        char[] t = target.toCharArray();
        int len = t.length;
        int siz = 0;
        for (String word : dictionary) {
            if (word.length() == len) {
                siz++;
            }
        }
        int[] words = new int[siz];
        int index = 0;
        int diff = 0;//记录所有字典单词与目标的总差异为位
        for (String word : dictionary) {
            if (word.length() == len) {
                char[] w = word.toCharArray();
                int status = 0;
                for (int j = 0; j < len; j++) {
                    if (t[j] != w[j]) {
                        status |= 1 << j;
                    }
                }
                words[index++] = status;
                diff |= status;//累积所有的差异位
            }
        }
        dfs2(words, len, diff, 0, 0);
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (int i = 0; i < len; i++) {
            if ((best & (1 << i)) != 0) {
                if (count > 0) {
                    builder.append(count);
                }
                builder.append(t[i]);
                count = 0;
            } else {
                count++;
            }
        }
        if (count > 0) {
            builder.append(count);
        }
        return builder.toString();
    }
    //fix：当前缩写方案的掩码
    public static void dfs2(int[] words, int len, int diff, int fix, int index) {
        if (!canFix(words, fix)) {
            if (index < len) {
                dfs2(words, len, diff, fix, index + 1);
                //仅当当前位置是有效差异位时，才尝试保留
                if ((diff & (1 << index)) != 0) {
                    dfs2(words, len, diff, fix | (1 << index), index + 1);
                }
            }
        } else {
            int ans = abbrLen(fix, len);
            if (ans < min) {
                min = ans;
                best = fix;
            }
        }
    }
}

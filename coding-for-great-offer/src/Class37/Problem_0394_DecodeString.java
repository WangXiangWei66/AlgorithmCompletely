package Class37;

//给定一个经过编码的字符串，返回它解码后的字符串。
//编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
//你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
//此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像3a或2[4]的输入。
//Leetcode题目 : https://leetcode.com/problems/decode-string/
public class Problem_0394_DecodeString {

    public static String decodeString(String s) {
        char[] str = s.toCharArray();
        return process(str, 0).ans;
    }

    public static class Info {
        public String ans;//解码后的字串
        public int stop;//处理结束后的索引位置

        public Info(String a, int e) {
            ans = a;
            stop = e;
        }
    }

    public static Info process(char[] s, int i) {
        StringBuilder ans = new StringBuilder();
        int count = 0;//记录重复次数
        while (i < s.length && s[i] != ']') {
            if ((s[i] >= 'a' && s[i] <= 'z') || (s[i] >= 'A' && s[i] <= 'Z')) {
                ans.append(s[i++]);
            } else if (s[i] >= '0' && s[i] <= '9') {
                count = count * 10 + s[i++] - '0';
            } else {
                Info next = process(s, i + 1);
                ans.append(timeString(count, next.ans));//将内部字串重复count次后拼接
                count = 0;
                i = next.stop + 1;
            }
        }
        return new Info(ans.toString(), i);
    }

    public static String timeString(int times, String str) {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < times; i++) {
            ans.append(str);
        }
        return ans.toString();
    }
}

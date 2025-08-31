package Class47;

import java.util.ArrayList;
import java.util.Arrays;

//给定一个字符串s和一个正数k，重新组织s使得每一种相同的字符距离至少有k，如果无法做到返回空字符串
//示例1：
//输入：s = "aabbcc", k = 3
//输出："abcabc"
//示例2：
//输入：s = "aaabc", k = 3
//输出：""
//示例3：
//输入：s = "aaadbbcc", k = 2
//输出： "abacabcd"
//leetcode题目：https://leetcode.com/problems/rearrange-string-k-distance-apart/
public class Problem_0358_RearrangeStringKDistanceApart {
    //合法性判断+分桶填充
    public String rearrangeString(String s, int k) {
        if (s == null || s.length() < k) {
            return s;
        }
        char[] str = s.toCharArray();
        //初始化所有字符的出现次数为0
        //cnts[字符ASCII码][0=字符本身,1=出现次数]
        int[][] cnts = new int[256][2];
        for (int i = 0; i < 256; i++) {
            cnts[i] = new int[]{i, 0};
        }
        int maxCount = 0;//出现字符最多的字符数
        for (char task : str) {
            cnts[task][1]++;
            maxCount = Math.max(maxCount, cnts[task][1]);
        }
        int maxKinds = 0;//出现次数等于maxCount的字符种类数
        for (int task = 0; task < 256; task++) {
            if (cnts[task][1] == maxCount) {
                maxKinds++;
            }
        }
        int N = str.length;
        if (!isValid(N, k, maxCount, maxKinds)) {
            return "";
        }
        ArrayList<StringBuilder> ans = new ArrayList<>();
        for (int i = 0; i < maxCount; i++) {
            ans.add(new StringBuilder());
        }
        //按照字符出现的次数降序排序
        Arrays.sort(cnts, (a, b) -> (b[1] - a[1]));
        int i = 0;//辩词cnts的索引
        for (; i < 256 && cnts[i][1] == maxCount; i++) {
            for (int j = 0; j < maxCount; j++) {
                ans.get(j).append((char) cnts[i][0]);
            }
        }
        int out = 0;//桶的索引，用于循环分配低频字符
        for (; i < 256; i++) {
            for (int j = 0; j < cnts[i][1]; j++) {
                ans.get(out).append((char) cnts[i][0]);
                out = out == ans.size() - 2 ? 0 : out + 1;
            }
        }
        //将所有的桶连接在一起
        StringBuilder builder = new StringBuilder();
        for (StringBuilder b : ans) {
            builder.append(b.toString());
        }
        return builder.toString();
    }

    public static boolean isValid(int N, int k, int maxCount, int maxKinds) {
        int restTasks = N - maxKinds;//其他的字符种数
        int spaces = k * (maxCount - 1);//高频字符需要的最小间隔位置数
        return spaces - restTasks <= 0;
    }
}


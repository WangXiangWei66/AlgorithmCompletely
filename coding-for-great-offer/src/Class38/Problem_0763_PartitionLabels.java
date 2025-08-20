package Class38;

import java.util.ArrayList;
import java.util.List;

//字符串 S 由小写字母组成。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。返回一个表示每个字符串片段的长度的列表。
//示例：
//输入：S = "ababcbacadefegdehijhklij"
//输出：[9,7,8]
//解释：
//划分结果为 "ababcbaca", "defegde", "hijhklij"。
//每个字母最多出现在一个片段中。
//像 "ababcbacadefegde", "hijhklij" 的划分是错误的，因为划分的片段数较少。
//提示：
//S的长度在[1, 500]之间。
//S只包含小写字母 'a' 到 'z' 。
//Leetcode题目 : https://leetcode.com/problems/partition-labels/
public class Problem_0763_PartitionLabels {

    public static List<Integer> partitionLabels(String s) {
        char[] str = s.toCharArray();
        int[] far = new int[26];//存储每个此电影最后出现的位置
        for (int i = 0; i < str.length; i++) {
            far[str[i] - 'a'] = i;
        }
        List<Integer> ans = new ArrayList<>();
        int left = 0;//当前片段的起始位置
        int right = far[str[0] - 'a'];
        for (int i = 1; i < str.length; i++) {
            if (i > right) {
                ans.add(right - left + 1);//记录当前片段的长度并加入结果列表
                left = i;//开始新片段
            }
            right = Math.max(right, far[str[i] - 'a']);
        }
        ans.add(right - left + 1);
        return ans;
    }
}

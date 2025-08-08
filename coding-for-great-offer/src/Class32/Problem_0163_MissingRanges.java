package Class32;

import java.util.ArrayList;
import java.util.List;

//给定一个有序无重复的数组nums， 和两个整数lower和upper， 返回[lower,upper]上所有缺失的数字段
//示例1:
//nums = [0,1,3,50,75], lower = 0, upper = 99
//输出:["2","4->49","51->74","76->99"]
//示例2:
//nums = [], lower = 1, upper = 1
//输出: ["1"]
//示例3:
//nums = [], lower = -3, upper = -1
//输出： ["-3->-1"]
//示例4:
//nums = [-1], lower = -1, upper = -1
//输出: []
//示例5:
//nums = [-1], lower = -2, upper = -1
//输出: ["-2"]
//Leetcode题目 : https://leetcode.com/problems/missing-ranges/
public class Problem_0163_MissingRanges {
    //时间复杂度为 O (n)，空间复杂度为 O (1)
    public static List<String> findMissingRanges(int[] nums, int lower, int upper) {
        List<String> ans = new ArrayList<>();
        for (int num : nums) {
            if (num > lower) {
                ans.add(miss(lower, num - 1));
            }
            if (num == upper) {
                return ans;
            }
            lower = num + 1;
        }
        if (lower <= upper) {
            ans.add(miss(lower, upper));
        }
        return ans;
    }

    public static String miss(int lower, int upper) {
        String left = String.valueOf(lower);//将lower转化为字符串作为左边界
        String right = "";
        if (upper > lower) {
            right = "->" + String.valueOf(upper);
        }
        return left + right;
    }
}

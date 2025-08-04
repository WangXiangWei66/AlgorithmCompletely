package Class30;

import java.util.ArrayList;
import java.util.List;

//给定一个非负整数 numRows，生成「杨辉三角」的前 numRows 行。
//在「杨辉三角」中，每个数是它左上方和右上方的数的和。
//Leetcode题目 : https://leetcode.com/problems/pascals-triangle/
public class Problem_0118_PascalTriangle {
    //时间复杂度为O(n^2),空间复杂度为O(n^2)
    public static List<List<Integer>> generate(int numRows) {
        List<List<Integer>> ans = new ArrayList<>();//结果列表用来存储前numRows的所有行
        //初始化每一行，将他们的值都设为1
        for (int i = 0; i < numRows; i++) {
            ans.add(new ArrayList<>());
            ans.get(i).add(1);
        }
        //填充每行中间元素，并设置每行最后一个元素为1
        for (int i = 1; i < numRows; i++) {
            for (int j = 1; j < i; j++) {
                ans.get(i).add(ans.get(i - 1).get(j - 1) + ans.get(i - 1).get(j));
            }
            ans.get(i).add(1);
        }
        return ans;
    }
}

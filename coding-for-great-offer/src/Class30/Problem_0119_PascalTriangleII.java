package Class30;

import java.util.ArrayList;
import java.util.List;

//给定一个非负索引 rowIndex，返回「杨辉三角」的第 rowIndex 行。
//在「杨辉三角」中，每个数是它左上方和右上方的数的和。
//你可以优化你的算法到 O(1) 空间复杂度吗？
//Leetcode题目 : https://leetcode.com/problems/pascals-triangle-ii/
public class Problem_0119_PascalTriangleII {
    //时间复杂度为O(rowIndex ^ 2)
    //空间复杂度为O(1)
    public List<Integer> getRow(int rowIndex) {
        //创建的结果列表用来存储目标行的元素
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i <= rowIndex; i++) {
            //从当前行的倒数第一个元素开始，向前更新
            for (int j = i - 1; j > 0; j--) {
                // 当前位置的值 = 上一行前一列的值 + 上一行当前列的值
                ans.set(j, ans.get(j - 1) + ans.get(j));
            }
            ans.add(1);
        }
        return ans;
    }
}

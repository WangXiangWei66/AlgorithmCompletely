package Class29;

import java.util.Arrays;

//以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi]
//请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间
//Leetcode题目：https://leetcode.com/problems/merge-intervals/
public class Problem_0056_MergeIntervals {
    //本算法的时间复杂度为O(n * logN) 空间复杂度为O(n)
    public static int[][] merge(int[][] intervals) {
        if (intervals.length == 0) {
            return new int[0][0];
        }
        //按照每个区间的起始值进行升序排序
        Arrays.sort(intervals, (a, b) -> (a[0] - b[0]));
        int s = intervals[0][0];//当前合并区间的起始值
        int e = intervals[0][1];//当前合并区间的结束值
        int size = 0;//合并后区间的数量
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] > e) {//不重叠
                //将上一个合并好的区间存入结果数组size+1
                intervals[size][0] = s;
                intervals[size++][1] = e;
                s = intervals[i][0];
                e = intervals[i][1];
            } else {
                e = Math.max(e, intervals[i][1]);//如果重叠，更新为区间中的较大值
            }
        }
        //将最后一个合并的区间存入结果数组
        intervals[size][0] = s;
        intervals[size++][1] = e;
        //复制数组的前size个元素作为结果返回（去除未使用的空间）
        return Arrays.copyOf(intervals, size);
    }
}

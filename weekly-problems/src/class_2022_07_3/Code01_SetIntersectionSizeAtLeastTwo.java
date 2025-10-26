package class_2022_07_3;

import java.util.Arrays;

//一个整数区间[a, b](a < b) 代表着从a到b的所有连续整数，包括a和b。
//给你一组整数区间intervals，请找到一个最小的集合 S，
//使得 S 里的元素与区间intervals中的每一个整数区间都至少有2个元素相交。
//输出这个最小集合S的大小。
//测试链接 : https://leetcode.cn/problems/set-intersection-size-at-least-two/
public class Code01_SetIntersectionSizeAtLeastTwo {

    public static int intersectionSizeTwo(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[1] != b[1] ? (a[1] - b[1]) : (b[0] - a[0]));
        int n = intervals.length;
        int pos = intervals[0][1];
        int pre = pos - 1;
        int ans = 2;//集合的初始大小
        for (int i = 1; i < n; i++) {
            //左端点在pre右侧
            if (intervals[i][0] > pre) {
                //左端点在pos右侧
                if (intervals[i][0] > pos) {
                    pre = intervals[i][1] - 1;
                    ans += 2;
                } else {
                    pre = pos;
                    ans += 1;
                }
                pos = intervals[i][1];
            }
        }
        return ans;
    }
}

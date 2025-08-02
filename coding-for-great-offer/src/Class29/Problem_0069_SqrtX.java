package Class29;

//实现int sqrt(int x)函数。
//计算并返回x的平方根，其中x是非负整数。
//由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
//示例 1:
//输入: 4
//输出: 2
//示例 2:
//输入: 8
//输出: 2
//说明: 8 的平方根是 2.82842...，由于返回类型是整数，小数部分将被舍去。
//Leetcode题目：https://leetcode.com/problems/sqrtx/
public class Problem_0069_SqrtX {
    //本题的时间复杂度为O(logx),空间复杂度为O(1)
    public static int mySqrt(int x) {
        if (x == 0) {
            return 0;
        }
        if (x < 3) {
            return 1;
        }
        //使用long类型来避免整数溢出
        long ans = 1;
        //两亿二分查找的左中右指针
        long L = 1;
        long R = x;
        long M = 0;
        while (L <= R) {
            M = (L + R) / 2;
            //判断中间值的平方是否小于等于 x：如果是，说明中间值可能是平方根的整数部分，或者可以尝试更大的值
            if (M * M <= x) {
                ans = M;
                L = M + 1;
            } else {
                R = M - 1;
            }
        }
        return (int) ans;
    }
}

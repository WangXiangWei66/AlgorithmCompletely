package class_2022_08_3;

//这里有n个航班，它们分别从 1 到 n 进行编号。
//有一份航班预订表bookings ，
//表中第i条预订记录bookings[i] = [firsti, lasti, seatsi]
//意味着在从 firsti到 lasti
//（包含 firsti 和 lasti ）的 每个航班 上预订了 seatsi个座位。
//请你返回一个长度为 n 的数组answer，里面的元素是每个航班预定的座位总数。
//测试链接 : https://leetcode.cn/problems/corporate-flight-bookings/
public class Code04_CorporateFlightBookings {
    //差分+前缀和:
    public static int[] corpFlightBookings(int[][] bookings, int n) {
        //差分辅助数组
        int[] cnt = new int[n + 2];
        for (int[] book : bookings) {
            //区间起始位置要累加的座位数
            cnt[book[0]] += book[2];
            //结束位置的下一个位置要减少的座位数
            cnt[book[1] + 1] -= book[2];
        }
        //对差分数组计算前缀和
        for (int i = 1; i < cnt.length; i++) {
            cnt[i] += cnt[i - 1];
        }
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = cnt[i + 1];
        }
        return ans;
    }
}

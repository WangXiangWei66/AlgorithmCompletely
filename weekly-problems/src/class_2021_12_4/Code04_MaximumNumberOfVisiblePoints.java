package class_2021_12_4;

import java.util.Arrays;
import java.util.List;

//整个二维平面算是一张地图，给定[x,y]，表示你站在x行y列
//你可以选择面朝的任何方向
//给定一个正数值angle，表示你视野的角度为
//这个角度内你可以看无穷远，这个角度外你看不到任何东西
//给定一批点的二维坐标，
//返回你在朝向最好的情况下，最多能看到几个点
public class Code04_MaximumNumberOfVisiblePoints {
    //List<List<Integer>> points：所有待检测点的坐标集合
    //angle：视野角度
    //location：观测者的位置
    public static int visiblePoints(List<List<Integer>> points, int angle, List<Integer> location) {
        int n = points.size();//点的总数
        int a = location.get(0);//观测者x坐标
        int b = location.get(1);//观测者y坐标
        int zero = 0;//记录与观测点重合的点
        double[] arr = new double[n << 1];//处理360度循环问题
        int m = 0;//实际使用角度数组长度
        for (int i = 0; i < n; i++) {
            int x = points.get(i).get(0) - a;
            int y = points.get(i).get(1) - b;
            if (x == 0 && y == 0) {
                zero++;
            } else {
                arr[m] = Math.toDegrees(Math.atan2(y, x));// atan2(y,x)返回从x轴正方向到点(x,y)的角度（弧度）
                arr[m + 1] = arr[m] + 360;
                m += 2;//每次都存了两个角度的值
            }
        }
        Arrays.sort(arr, 0, m);
        int max = 0;//视野范围内的最大点数
        for (int L = 0, R = 0; L < n; L++) {
            while (R < m && arr[R] - arr[L] <= angle) {
                R++;
            }
            max = Math.max(max, R - L);
        }
        return max + zero;
    }
}

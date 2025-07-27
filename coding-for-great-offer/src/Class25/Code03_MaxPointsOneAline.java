package Class25;

import java.util.HashMap;
import java.util.Map;

//给你一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点。求最多有多少个点在同一条直线上。
//Leetcode题目：https://leetcode.com/problems/max-points-on-a-line/
public class Code03_MaxPointsOneAline {

    public static int maxPoints(int[][] points) {
        if (points == null) {
            return 0;
        }
        if (points.length <= 2) {
            return points.length;
        }
        //map为嵌套的哈希表，用来统计【斜率】出现的次数
        //外层 key：斜率的「x 分量」（简化后的 Δx）。
        //内层 key：斜率的「y 分量」（简化后的 Δy）。
        //内层 value：该斜率对应的点对数量（即有多少点与当前点i构成该斜率，共线）。
        Map<Integer, Map<Integer, Integer>> map = new HashMap<Integer, Map<Integer, Integer>>();
        int result = 0;//记录全局最多共线的点的个数
        for (int i = 0; i < points.length; i++) {//当前的基准点索引
            map.clear(); // 每次换基准点，清空之前的斜率统计
            int samePosition = 1;// 与基准点重合的点数（初始为1，即基准点自身）
            int sameX = 0;//与基准点x坐标相同的点数（垂直线，斜率不存在）
            int sameY = 0;//与基准点y坐标相同的点数（水平线，斜率为0）
            int line = 0; //记录当前基准点下，最多共线的点数（不包含重合点）
            //下面计算与基准点的关系
            for (int j = i + 1; j < points.length; j++) {
                int x = points[j][0] - points[i][0];//两点的x坐标差
                int y = points[j][1] - points[i][1];//两点y的坐标差
                if (x == 0 && y == 0) {//两点重合
                    samePosition++;
                } else if (x == 0) {//两点连线与x轴垂直
                    sameX++;
                } else if (y == 0) {//两点连线与y轴垂直
                    sameY++;
                } else {
                    int gcd = gcd(x, y);//计算最大公约数
                    //将x、y分量简化
                    x /= gcd;
                    y /= gcd;
                    //开始统计当前斜率出现的次数
                    if (!map.containsKey(x)) {
                        map.put(x, new HashMap<Integer, Integer>());
                    }
                    if (!map.get(x).containsKey(y)) {
                        map.get(x).put(y, 0);
                    }
                    map.get(x).put(y, map.get(x).get(y) + 1);
                    //更新当前基准点下的最大共线点数
                    line = Math.max(line, map.get(x).get(y));
                }
            }
            //计算当前基准点下的最大共线点数：max(水平线点数, 垂直线点数, 其他斜率点数) + 重合点数
            result = Math.max(result, Math.max(Math.max(sameX, sameY), line) + samePosition);
        }
        return result;
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}

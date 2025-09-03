package Class49;

import java.util.HashSet;

//如下是机器人的类
//interface Robot {
// 如果机器人面朝的方向没有障碍，那么机器人将移动一步，并且返回true
// 如果机器人面朝的方向有障碍，那么机器人将停在原地，并且返回false
//boolean move();
//机器人面朝的方向向左转动90度
//void turnLeft();
//机器人面朝的方向向右转动90度
//void turnRight();
// 机器人执行原地打扫的动作
//void clean();
//}
//机器人将空降在房屋的某个地点，这一片局域会有障碍物，房屋的整片区域也一定有边界，边界等同于障碍
//房屋的整片区域假设是由1*1的格子拼成的，并且机器人自己占地也是1*1的格子
//但是你只能通过机器人的move方法，来让机器人移动并且探知障碍
//当然也可以让机器人通过turnLeft或者turnRight方法，来改变面朝的方向
//你如何只通过调用上面提到的方法，就控制机器人打扫房间的每个格子
//请实现如下的这个方法：
//public void cleanRoom(Robot robot)
//leetcode题目：https://leetcode.com/problems/robot-room-cleaner/
public class Problem_0489_RobotRoomCleaner {

    interface Robot {
        public boolean move();

        public void turnLeft();

        public void turnRight();

        public void clean();
    }
    //初始方向为向上
    public static void cleanRoom(Robot robot) {
        clean(robot, 0, 0, 0, new HashSet<>());
    }

    private static final int[][] ds = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public static void clean(Robot robot, int x, int y, int d, HashSet<String> visited) {
        robot.clean();//清扫当前格子
        visited.add(x + "_" + y);
        for (int i = 0; i < 4; i++) {
            int nd = (i + d) % 4;//计算新方向
            int nx = ds[nd][0] + x;
            int ny = ds[nd][1] + y;
            if (!visited.contains(nx + "_" + ny) && robot.move()) {
                clean(robot, nx, ny, nd, visited);
            }
            robot.turnRight();
        }

        robot.turnRight();
        robot.turnRight();
        robot.move();
        robot.turnRight();
        robot.turnRight();
    }
}

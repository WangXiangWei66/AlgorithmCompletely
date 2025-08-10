package Class33;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

//你这个学期必须选修 numCourses 门课程，记为0到numCourses - 1 。
//在选修某些课程之前需要一些先修课程。 先修课程按数组prerequisites 给出，其中prerequisites[i] = [ai, bi] ，表示如果要学习课程ai 则 必须 先学习课程 bi 。
//例如，先修课程对[0, 1] 表示：想要学习课程 0 ，你需要先完成课程 1 。
//请你判断是否可能完成所有课程的学习？如果可以，返回 true ；否则，返回 false 。
//示例 1：
//输入：numCourses = 2, prerequisites = [[1,0]]
//输出：true
//解释：总共有 2 门课程。学习课程 1 之前，你需要完成课程 0 。这是可能的。
//示例 2：
//输入：numCourses = 2, prerequisites = [[1,0],[0,1]]
//输出：false
//解释：总共有 2 门课程。学习课程 1 之前，你需要先完成课程 0 ；并且学习课程 0 之前，你还应先完成课程 1 。这是不可能的。
//Leetcode题目 : https://leetcode.com/problems/course-schedule/
public class Problem_0207_CourseSchedule {
    //本题的本质是判断一个有向图是否存在环
    public static class Course {
        public int name;//课程编号
        public int in;//入度，依赖的先修课程数量
        public ArrayList<Course> nexts;//课程的列表

        public Course(int n) {
            name = n;
            in = 0;
            nexts = new ArrayList<>();
        }
    }
    //时间复杂度：O (N+E)，其中 N 是课程数，E 是先修课程对数
    //空间复杂度：O (N+E)，用于存储图结构和队列
    public static boolean canFinish1(int numCourses, int[][] prerequisities) {
        if (prerequisities == null || prerequisities.length == 0) {
            return true;
        }
        //存储所有课程节点，键是课程编号，值是课程对象
        HashMap<Integer, Course> nodes = new HashMap<>();
        for (int[] arr : prerequisities) {
            int to = arr[0];
            int from = arr[1];
            //将对应的课程节点添加上
            if (!nodes.containsKey(to)) {
                nodes.put(to, new Course(to));
            }
            if (!nodes.containsKey(from)) {
                nodes.put(from, new Course(from));
            }
            Course t = nodes.get(to);
            Course f = nodes.get(from);
            f.nexts.add(t);
            t.in++;//t的入度加1
        }
        int needPrerequisiteNums = nodes.size();//记录先修课程的总课程数
        //存储可以直接学习的课程
        Queue<Course> zeroInQueue = new LinkedList<>();
        for (Course node : nodes.values()) {
            if (node.in == 0) {
                zeroInQueue.add(node);
            }
        }
        int count = 0;
        while (!zeroInQueue.isEmpty()) {
            Course cur = zeroInQueue.poll();
            count++;
            for (Course next : cur.nexts) {
                if (--next.in == 0) {
                    zeroInQueue.add(next);
                }
            }
        }
        return count == needPrerequisiteNums;
    }
    //时间复杂度与第一个相同
    public static boolean canFinish2(int courses, int[][] relation) {
        if (relation == null || relation.length == 0) {
            return true;
        }
        //二维列表用于存储每个课程的后续课程
        ArrayList<ArrayList<Integer>> nexts = new ArrayList<>();
        //先为每门课程创建一个空的后续课程列表
        for (int i = 0; i < courses; i++) {
            nexts.add(new ArrayList<>());
        }
        //in数组用于记录每门课程的入度
        int[] in = new int[courses];
        for (int[] arr : relation) {
            nexts.get(arr[1]).add(arr[0]);
            in[arr[0]]++;
        }
        //zero数组存储入度为0的课程
        int[] zero = new int[courses];
        int l = 0;
        int r = 0;
        //先将所有入读为0的课程加入队列
        for (int i = 0; i < courses; i++) {
            if (in[i] == 0) {
                zero[r++] = i;
            }
        }
        int count = 0;
        while (l != r) {
            count++;
            for (int next : nexts.get(zero[l++])) {
                if (--in[next] == 0) {
                    zero[r++] = next;
                }
            }
        }
        return count == nexts.size();
    }
}

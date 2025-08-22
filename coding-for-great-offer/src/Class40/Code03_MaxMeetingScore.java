package Class40;

import java.util.Arrays;
import java.util.PriorityQueue;

//给定int[][] meetings，比如
//{
//{66, 70}   0号会议截止时间66，获得收益70
//{25, 90}   1号会议截止时间25，获得收益90
//{50, 30}   2号会议截止时间50，获得收益30
//}
//一开始的时间是0，任何会议都持续10的时间，但是一个会议一定要在该会议截止时间之前开始
//只有一个会议室，任何会议不能共用会议室，一旦一个会议被正确安排，将获得这个会议的收益
//请返回最大的收益
public class Code03_MaxMeetingScore {

    public static int maxScore1(int[][] meetings) {
        //将会议按照截止时间升序排序
        Arrays.sort(meetings, (a, b) -> a[0] - b[0]);
        //记录选择路径的数组
        int[][] path = new int[meetings.length][];
        int size = 0;
        return process1(meetings, 0, path, size);
    }

    public static int process1(int[][] meetings, int index, int[][] path, int size) {
        if (index == meetings.length) {
            int time = 0;//当前时间
            int ans = 0;//总收益
            for (int i = 0; i < size; i++) {
                if (time <= path[i][0]) {
                    ans += path[i][1];
                    time += 10;
                } else {
                    return 0;
                }
            }
            return ans;
        }
        int p1 = process1(meetings, index + 1, path, size);
        path[size] = meetings[index];
        int p2 = process1(meetings, index + 1, path, size + 1);
        return Math.max(p1, p2);
    }

    //时间复杂度为O(n * logN)
    //空间复杂度为O(n)
    public static int maxScore2(int[][] meetings) {
        Arrays.sort(meetings, (a, b) -> a[0] - b[0]);
        //存储已选择会议的收益
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int time = 0;
        for (int i = 0; i < meetings.length; i++) {
            if (time <= meetings[i][0]) {
                heap.add(meetings[i][1]);
                time += 10;
            } else {
                if (!heap.isEmpty() && heap.peek() < meetings[i][1]) {
                    heap.poll();
                    heap.add(meetings[i][1]);
                }
            }
        }
        int ans = 0;
        while (!heap.isEmpty()) {
            ans += heap.poll();
        }
        return ans;
    }

    public static int[][] randomMeetings(int n, int t, int s) {
        int[][] ans = new int[n][2];
        for (int i = 0; i < n; i++) {
            ans[i][0] = (int) (Math.random() * t) + 1;
            ans[i][1] = (int) (Math.random() * s) + 1;
        }
        return ans;
    }

    public static int[][] copyMeetings(int[][] meetings) {
        int n = meetings.length;
        int[][] ans = new int[n][2];
        for (int i = 0; i < n; i++) {
            ans[i][0] = meetings[i][0];
            ans[i][1] = meetings[i][1];
        }
        return ans;
    }

    public static void main(String[] args) {
        int n = 12;
        int t = 100;
        int s = 500;
        int testTime = 10000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            int size = (int) (Math.random() * n) + 1;
            int[][] meetings1 = randomMeetings(size, t, s);
            int[][] meetings2 = copyMeetings(meetings1);
            int ans1 = maxScore1(meetings1);
            int ans2 = maxScore2(meetings2);
            if (ans1 != ans2) {
                System.out.println("Oops");
                System.out.println(ans1);
                System.out.println(ans1);
                break;
            }
        }
        System.out.println("test end!");
    }
}

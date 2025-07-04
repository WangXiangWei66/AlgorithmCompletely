package Class14;

import java.util.Arrays;
import java.util.Comparator;

public class Code03_BestArrange {

    public static class Program {
        public int start;
        public int end;

        public Program(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    //暴力，所有情况都尝试
    public static int bestArrange1(Program[] programs) {
        if (programs == null || programs.length == 0) {
            return 0;
        }
        return process(programs, 0, 0);
    }
    //还剩下的会议都放在programs里
    //done之前已经安排了多少会议的数量
    //timeLine目前来到的时间点是什么
    //目前来到timeLine的时间点，已经安排了done多的会议，剩下的会议programs里可以自由安排
    //返回能安排的最小会议数量

    public static int process(Program[] programs, int done, int timeLine) {
        if (programs.length == 0) {
            return done;
        }
        //还剩下会议
        int max = done;
        //当前安排的会议是什么会，每一次都枚举
        for (int i = 0; i < programs.length; i++) {
            if (programs[i].start >= timeLine) {
                Program[] next = copyButExcept(programs, i);
                max = Math.max(max, process(next, done + 1, programs[i].end));
            }
        }
        return max;
    }

    public static Program[] copyButExcept(Program[] programs, int i) {
        Program[] ans = new Program[programs.length - 1];
        int index = 0;
        for (int k = 0; k < programs.length; k++) {
            if (k != i) {
                ans[index++] = programs[k];
            }
        }
        return ans;
    }

    //会议的开始时间和结束时间，都是数值，不会小于0
    public static int bestArrange2(Program[] programs) {
        Arrays.sort(programs, new ProgramComparator());
        int timeLine = 0;//当前时间点
        int result = 0;//安排会议数
        //依次遍历每一个会议，结束时间早的会议先遍历
        for (int i = 0; i < programs.length; i++) {
            if (timeLine <= programs[i].start) {
                result++;
                timeLine = programs[i].end;
            }
        }
        return result;
    }

    public static class ProgramComparator implements Comparator<Program> {

        @Override
        public int compare(Program o1, Program o2) {
            return o1.end - o2.end;
        }
    }
    //生成随机的节目时间安排数组
    public static Program[] generatePrograms(int programSize, int timeMax) {
        Program[] ans = new Program[(int) ((Math.random() * (programSize + 1)))];
        for (int i = 0; i < ans.length; i++) {
            int r1 = (int) (Math.random() * (timeMax + 1));
            int r2 = (int) (Math.random() * (timeMax + 1));
            if (r1 == r2) {
                ans[i] = new Program(r1, r1 + 1);
            } else {
                ans[i] = new Program(Math.min(r1, r2), Math.max(r1, r2));
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int programSize = 2;
        int timeMax = 20;
        int testTime = 1000000;
        for (int i = 0; i < testTime; i++) {
            Program[] programs = generatePrograms(programSize, timeMax);
            if (bestArrange1(programs) != bestArrange2(programs)) {
                System.out.println("Oops");
            }
        }
        System.out.println("finish");
    }
}

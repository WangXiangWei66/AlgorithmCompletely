package Class14;

import java.util.HashSet;

public class Code01_light {

    public static int minLight1(String road) {
        if (road == null || road.length() == 0) {
            return 0;
        }
        return process(road.toCharArray(), 0, new HashSet<>());
    }

    //str[index...]位置，自由选择放灯还是不放灯
    //str[0...index-1]位置呢?已经做完决定了，那些放了灯的位置，存在lights里
    //要求选出能照亮所有的方案，并且在这些有效的方案中，返回最少需要几个灯
    //index为索引的位置
    public static int process(char[] str, int index, HashSet<Integer> lights) {
        if (index == str.length) {
            //结束的时候，下面是检查所有的位置，是否都已经被照亮
            for (int i = 0; i < str.length; i++) {
                if (str[i] != 'X') {
                    //当前位置是点的话
                    if (!lights.contains(i - 1) && !lights.contains(i) && !lights.contains(i + 1)) {
                        return Integer.MAX_VALUE;
                    }
                }
            }
            return lights.size();
        } else {//str还没结束
            //i  X
            int no = process(str, index + 1, lights);
            int yes = Integer.MAX_VALUE;
            if (str[index] == '.') {
                lights.add(index);
                yes = process(str, index + 1, lights);
                lights.remove(index);//这一步为回溯操作
            }
            return Math.min(no, yes);
        }
    }

    public static int minLight2(String road) {
        char[] str = road.toCharArray();
        int i = 0;
        int light = 0;
        while (i < str.length) {
            if (str[i] == 'X') {
                i++;
            } else {
                light++;
                if (i + 1 == str.length) {
                    break;
                } else {//右I位置 i+1为X
                    if (str[i + 1] == 'X') {
                        i = i + 2;
                    } else {
                        i = i + 3;
                    }
                }
            }
        }
        return light;
    }

    //更简洁的解法
    //两个X之间，数一下.的数量，然后除以3，向上取整
    //把灯数累加
    public static int minLight3(String road) {
        char[] str = road.toCharArray();
        int cur = 0;//当前连续点的数量
        int light = 0;//当前灯的数量
        for (char c : str) {
            if (c == 'X') {
                //如果遇到了障碍物，计算当前连续灯的数量
                light += (cur + 2) / 3;
                cur = 0;//重置
            } else {
                cur++;
            }
        }
        light += (cur + 2) / 3;
        return light;
    }

    //for test
    public static String randomString(int len) {
        char[] res = new char[(int) (Math.random() * len) + 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = Math.random() < 0.5 ? 'X' : '.';
        }
        return String.valueOf(res);
    }

    public static void main(String[] args) {
        int len = 20;
        int testTime = 100000;
        for (int i = 0; i < testTime; i++) {
            String test = randomString(len);
            int ans1 = minLight1(test);
            int ans2 = minLight2(test);
            int ans3 = minLight3(test);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Oops");
            }
        }
        System.out.println("finish");
    }
}

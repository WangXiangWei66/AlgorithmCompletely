package Class19;

import java.util.LinkedList;

/*
 * 一张扑克有3个属性，每种属性有3种值（A、B、C）
 * 比如"AAA"，第一个属性值A，第二个属性值A，第三个属性值A
 * 比如"BCA"，第一个属性值B，第二个属性值C，第三个属性值A
 * 给定一个字符串类型的数组cards[]，每一个字符串代表一张扑克
 * 从中挑选三张扑克，一个属性达标的条件是：这个属性在三张扑克中全一样，或全不一样
 * 挑选的三张扑克达标的要求是：每种属性都满足上面的条件
 * 比如："ABC"、"CBC"、"BBC"
 * 第一张第一个属性为"A"、第二张第一个属性为"C"、第三张第一个属性为"B"，全不一样
 * 第一张第二个属性为"B"、第二张第二个属性为"B"、第三张第二个属性为"B"，全一样
 * 第一张第三个属性为"C"、第二张第三个属性为"C"、第三张第三个属性为"C"，全一样
 * 每种属性都满足在三张扑克中全一样，或全不一样，所以这三张扑克达标
 * 返回在cards[]中任意挑选三张扑克，达标的方法数
 *
 * */
public class Code05_CardsProblem {
    //使用了递归回溯的方法生成所有可能的三元组组合，并通过条件判断筛选出符合要求的组合
    public static int ways1(String[] cards) {
        LinkedList<String> picks = new LinkedList<>();//存储当前选择的字符串
        return process1(cards, 0, picks);//下面去递归生成所有可能的组合
    }

    public static int process1(String[] cards, int index, LinkedList<String> picks) {
        //如果已经选择了三个，先去检查一下这三个是否符合条件
        if (picks.size() == 3) {
            return getWays1(picks);
        }
        //如果遍历完所有的字符串也没有先出来三个，则返回0
        if (index == cards.length) {
            return 0;
        }
        //不选择当前字符串，直接处理下一个
        int ways = process1(cards, index + 1, picks);
        //选择当前字符串
        picks.addLast(cards[index]);
        //继续处理下一个，累加可能的组合数
        ways += process1(cards, index + 1, picks);
        //回溯，移除最后添加的字符串，尝试其他可能性
        picks.pollLast();
        return ways;
    }

    public static int getWays1(LinkedList<String> picks) {
        char[] s1 = picks.get(0).toCharArray();
        char[] s2 = picks.get(1).toCharArray();
        char[] s3 = picks.get(2).toCharArray();
        //for循环是都不相同与都相同的情况
        for (int i = 0; i < 3; i++) {
            if ((s1[i] != s2[i] && s1[i] != s3[i] && s2[i] != s3[i] || (s1[i] == s2[i] && s1[i] == s3[i]))) {
                continue;
            }
            return 0;
        }
        return 1;
    }

    public static int ways2(String[] cards) {
        // 初始化计数数组，每个位置对应一个可能的字符串状态(0-26)
        int[] counts = new int[27];//27是因为三个字母，最多27种不同的组合，i为第i种状态出现的次数
        //统计每个字符串中出现的次数
        for (String s : cards) {
            char[] str = s.toCharArray();
            counts[(str[0] - 'A') * 9 + (str[1] - 'A') * 3 + (str[2] - 'A') * 1]++;//本行使用了三进制编码
        }
        int ways = 0;//累计所有有效组合的数量
        //处理三个相同的字符串的情况
        for (int status = 0; status < 27; status++) {
            int n = counts[status];
            if (n > 2) {
                ways += n == 3 ? 1 : (n * (n - 1) * (n - 2) / 6);//重复的字符串，使用了组合数C(n,3)
            }
        }
        //处理三个不同字符串的情况
        LinkedList<Integer> path = new LinkedList<>();
        for (int i = 0; i < 27; i++) {
            if (counts[i] != 0) {
                path.addLast(i);
                ways += process2(counts, i, path);
                path.pollLast();
            }
        }
        return ways;
    }

    //使用了严格递增：大大减少了枚举的次数
    //counts:存储每个状态出现次数的数组（长度为27）
    //pre：当前递归层级的前一个状态值（确保后续状态严格大于pre）
    //path：记录当前已经选择好的状态路径（长度最大为3）
    public static int process2(int[] counts, int pre, LinkedList<Integer> path) {
        // 当收集到3个字符串时，检查有效性并计算组合数
        if (path.size() == 3) {
            return getWays2(counts, path);
        }
        int ways = 0;
        // 只考虑比pre大的状态，确保生成的三元组严格递增
        for (int next = pre + 1; next < 27; next++) {
            if (counts[next] != 0) {//将不存在的字符串进行了跳过
                path.addLast(next);
                ways += process2(counts, next, path);
                path.pollLast();
            }
        }
        return ways;
    }

    public static int getWays2(int[] counts, LinkedList<Integer> path) {
        int v1 = path.get(0);
        int v2 = path.get(1);
        int v3 = path.get(2);
        //逐位检查三个状态的对应位是否满足条件
        for (int i = 9; i > 0; i /= 3) {
            int cur1 = v1 / i;//获取当前位的值（0-2）。
            int cur2 = v2 / i;
            int cur3 = v3 / i;
            v1 %= i;//去掉已处理的高位，为下一轮循环准备。
            v2 %= i;
            v3 %= i;
            if ((cur1 != cur2 && cur1 != cur3 && cur2 != cur3) || (cur1 == cur2 && cur1 == cur3)) {
                continue;
            }
            return 0;
        }
        //前面进行了%运算，已经修改了值，因此需要重新赋值
        v1 = path.get(0);
        v2 = path.get(1);
        v3 = path.get(2);
        return counts[v1] * counts[v2] * counts[v3];//如果满足条件，则返回这三个状态的组合数
    }

    public static String[] generateCards(int size) {
        int n = (int) (Math.random() * size) + 3;
        String[] ans = new String[n];
        for (int i = 0; i < n; i++) {
            char cha0 = (char) ((int) (Math.random() * 3) + 'A');
            char cha1 = (char) ((int) (Math.random() * 3) + 'A');
            char cha2 = (char) ((int) (Math.random() * 3) + 'A');
            ans[i] = String.valueOf(cha0) + String.valueOf(cha1) + String.valueOf(cha2);
        }
        return ans;
    }

    public static void main(String[] args) {
        int size = 20;
        int testTime = 100000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            String[] arr = generateCards(size);
            int ans1 = ways1(arr);
            int ans2 = ways2(arr);
            if (ans1 != ans2) {
                for (String str : arr) {
                    System.out.println(str);
                }
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test finish");

        long start = 0;
        long end = 0;
        String[] arr = generateCards(1000000);
        System.out.println("arr size:" + arr.length + "runtime test begin");
        start = System.currentTimeMillis();
        ways2(arr);
        end = System.currentTimeMillis();
        System.out.println("run time:" + (end - start) + "ms");
        System.out.println("runtime test end");
    }
}

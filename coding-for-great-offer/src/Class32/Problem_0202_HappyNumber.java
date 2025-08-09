package Class32;

import java.util.HashSet;
import java.util.TreeSet;

//编写一个算法来判断一个数 n 是不是快乐数。
//「快乐数」定义为：
//对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和。
//然后重复这个过程直到这个数变为 1，也可能是 无限循环 但始终变不到 1。
//如果 可以变为 1，那么这个数就是快乐数。
//如果 n 是快乐数就返回 true ；不是，则返回 false 。
//示例 1：
//输入：19
//输出：true
//解释：
//1^2 + 9^2 = 82
//8^2 + 2^2 = 68
//6^2 + 8^2 = 100
//1^2 + 0^2 + 0^2 = 1
//示例 2：
//输入：n = 2
//输出：false
//提示：
//1 <= n <= 2^31 - 1
//Leetcode题目 : https://leetcode.com/problems/happy-number
public class Problem_0202_HappyNumber {

    public static boolean isHappy(int n) {
        HashSet<Integer> set = new HashSet<>();//检验是否进入了无限循环
        while (n != 1) {
            int sum = 0;//当前数字每个位置上的平方和
            while (n != 0) {
                int r = n % 10;//提取最后一位
                sum += r * r;
                n /= 10;//移除最后一位
            }
            n = sum;
            if (set.contains(n)) {
                break;
            }
            set.add(n);
        }
        return n == 1;
    }

    public static TreeSet<Integer> sum(int n) {
        TreeSet<Integer> set = new TreeSet<>();
        while (!set.contains(n)) {
            set.add(n);
            int sum = 0;
            while (n != 0) {
                sum += (n % 10) * (n % 10);
                n /= 10;
            }
            n = sum;
        }
        return set;
    }
    //非快乐数最终一定会进入以4为起点的循环
    //所有非快乐数最终都会进入一个固定循环：4 → 16 → 37 → 58 → 89 → 145 → 42 → 20 → 4
    public static boolean isHappy2(int n) {
        while (n != 1 && n != 4) {
            int sum = 0;
            while (n != 0) {
                sum += (n % 10) * (n % 10);
                n /= 10;
            }
            n = sum;
        }
        return n == 1;
    }

    public static void main(String[] args) {
        for (int i = 1; i < 1000; i++) {
            System.out.println(sum(i));
        }
    }
}

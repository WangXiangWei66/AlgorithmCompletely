package Class47;

import java.util.Arrays;

//冬季已经来临。你的任务是设计一个有固定加热半径的供暖器向所有房屋供暖。
//在加热器的加热半径范围内的每个房屋都可以获得供暖。
//现在，给出位于一条水平线上的房屋houses 和供暖器heaters 的位置，请你找出并返回可以覆盖所有房屋的最小加热半径。
//说明：所有供暖器都遵循你的半径标准，加热的半径也一样。
//示例 1:
//输入: houses = [1,2,3], heaters = [2]
//输出: 1
//解释: 仅在位置2上有一个供暖器。如果我们将加热半径设为1，那么所有房屋就都能得到供暖。
//示例 2:
//输入: houses = [1,2,3,4], heaters = [1,4]
//输出: 1
//解释: 在位置1, 4上有两个供暖器。我们需要将加热半径设为1，这样所有房屋就都能得到供暖。
//示例 3：
//输入：houses = [1,5], heaters = [2]
//输出：3
//leetcode题目：https://leetcode.com/problems/heaters/
public class Problem_0475_Heaters {

    public static int findRadius(int[] houses, int[] heaters) {
        Arrays.sort(houses);
        Arrays.sort(heaters);
        int ans = 0;
        //i用来遍历房屋，j用来遍历供暖器
        for (int i = 0, j = 0; i < houses.length; i++) {
            while (!best(houses, heaters, i, j)) {
                j++;//寻找当前房屋最近的供暖器
            }
            ans = Math.max(ans, Math.abs(heaters[j] - houses[i]));//更新最大半径
        }
        return ans;
    }

    public static boolean best(int[] houses, int[] heaters, int i, int j) {
        return j == heaters.length - 1 || Math.abs(heaters[j] - houses[i]) < Math.abs(heaters[j + 1] - houses[i]);
    }

    public static int findRadius2(int[] houses, int[] heaters) {
        Arrays.sort(houses);
        Arrays.sort(heaters);
        int ans = 0;
        for (int i = 0, j = 0; i < houses.length; i++) {
            while (!best2(houses, heaters, i, j)) {
                j++;
            }
            ans = Math.max(ans, Math.abs(heaters[j] - houses[i]));
        }
        return ans;
    }

    public static boolean best2(int[] houses, int[] heaters, int i, int j) {
        //不能等号，因为可能导致j的停滞，错过最优选择
        return j == heaters.length - 1 || Math.abs(heaters[j] - heaters[i]) <= Math.abs(heaters[j + 1] - houses[i]);
    }

    public static int[] randomArray(int len, int v) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * v) + 1;
        }
        return arr;
    }

    public static void main(String[] args) {
        int len = 5;
        int v = 10;
        int testTime = 10000;
        for (int i = 0; i < testTime; i++) {
            int[] a = randomArray(len, v);
            int[] b = randomArray(len, v);
            int ans1 = findRadius(a, b);
            int ans2 = findRadius2(a, b);
            if (ans1 != ans2) {
                System.out.print("A :");
                for (int num : a) {
                    System.out.print(num + " ");
                }
                System.out.println();
                System.out.print("B :");
                for (int num : b) {
                    System.out.print(num + " ");
                }
                System.out.println();
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
    }
}

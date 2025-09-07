package Class52;

//给定一个数组bulbs，bulds[i] = j，表示第i天亮起的灯是j。任何一天只会亮一盏灯，如果天数有1~i天，那么灯也一定只有1~i号。
//给定一个正数k，如果两盏亮起的灯之间有正好k个不亮的灯，那么说这个是达标情况。
//返回最早在哪一天，会出现达标情况，如果不存在达标情况，返回-1
//leetcode题目：https://leetcode.com/problems/k-empty-slots/
public class Problem_0683_KEmptySlots {

    public static int kEmptySlots1(int[] bulbs, int k) {
        int n = bulbs.length;
        int[] days = new int[n];//记录每个灯在哪天亮起来了
        //将第i天亮哪盏灯转化为每盏灯哪天亮
        for (int i = 0; i < n; i++) {
            days[bulbs[i] - 1] = i + 1;
        }
        int ans = Integer.MAX_VALUE;
        if (k == 0) {
            for (int i = 1; i < n; i++) {
                //寻找两盏灯都亮起的最早天数
                ans = Math.min(ans, Math.max(days[i - 1], days[i]));
            }
        } else {
            int[] minQ = new int[n];//记录A、B之间k个位置的亮灯天数最小值
            int l = 0;
            int r = -1;
            for (int i = 1; i < n && i < k; i++) {
                while (l <= r && days[minQ[r]] >= days[i]) {
                    r--;
                }
                minQ[++r] = i;
            }
            //窗口i和j之间有k个位置
            for (int i = 1, j = k; j < n - 1; i++, j++) {
                while (l <= r && days[minQ[r]] >= days[j]) {
                    r--;
                }
                minQ[++r] = j;
                int cur = Math.max(days[i - 1], days[j + 1]);
                if (days[minQ[l]] > cur) {
                    ans = Math.min(ans, cur);
                }
                if (i == minQ[l]) {
                    l++;
                }
            }
        }
        return (ans == Integer.MAX_VALUE) ? -1 : ans;
    }

    public static int kEmptySlots2(int[] bulbs, int k) {
        int n = bulbs.length;
        int[] days = new int[n];
        for (int i = 0; i < n; i++) {
            days[bulbs[i] - 1] = i + 1;
        }
        int ans = Integer.MAX_VALUE;
        //用mid来遍历left和right之间的位置
        for (int left = 0, mid = 1, right = k + 1; right < n; mid++) {
            if (days[mid] <= Math.max(days[left], days[right])) {
                if (mid == right) {
                    int cur = Math.max(days[left], days[right]);
                    ans = Math.min(ans, cur);
                    left = mid;
                    right = mid + k + 1;
                } else {
                    left = mid;
                    right = mid + k + 1;
                }
            }
        }
        return (ans == Integer.MAX_VALUE) ? -1 : ans;
    }
}
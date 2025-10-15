package class_2022_05_3;

import java.util.TreeMap;
import java.util.TreeSet;

//来自学员问题
//给定一个数组arr，表示从早到晚，依次会出现的导弹的高度
//大炮打导弹的时候，如果一旦大炮定了某个高度去打，那么这个大炮每次打的高度都必须下降一点
//如果所有的导弹都必须拦截，返回最少的大炮数量
public class Code03_NumberOfCannon {

    public static int numOfCannon(int[] arr) {
        TreeMap<Integer, Integer> ends = new TreeMap<>();
        for (int num : arr) {
            if (ends.ceilingKey(num + 1) == null) {
                //初始可拦截任意高度
                ends.put(Integer.MAX_VALUE, 1);
            }
            //获取当前可拦截的大炮中，最小的可拦截高度
            int ceilKey = ends.ceilingKey(num + 1);
            //有多个大炮可以拦截，就减少一个
            if (ends.get(ceilKey) > 1) {
                ends.put(ceilKey, ends.get(ceilKey) - 1);
            } else {
                ends.remove(ceilKey);
            }
            //将使用的大炮的可拦截高度更新为当前导弹高度
            ends.put(num, ends.getOrDefault(num, 0) + 1);
        }
        //统计大炮的数量
        int ans = 0;
        for (int value : ends.values()) {
            ans += value;
        }
        return ans;
    }

    public static void main(String[] args) {
        // 有序表来说
        // add
        // remove
        // ceiling
        // <= floor
        // O(logN)!
        TreeSet<Integer> set = new TreeSet<>();
        set.add(17);
        set.add(20);
        set.add(25);
        // >= 23
        System.out.println(set.ceiling(26));
        // 有序表是去重的，key去重
        // A ：99
        // B : 99
        // C : 99
        TreeMap<Integer, Integer> map = new TreeMap<>();
        map.put(99, 3);
        // 76
        if (map.ceilingKey(76) == null) {
            //  没有大炮可以打76
            // 新开一门大炮，打76
            // 这个新跑，只能打75~
            map.put(75, map.getOrDefault(75, 0) + 1);
        } else { // 之前有大炮可以打76，不需要新开一门炮！
            int key = map.ceilingKey(76);
            // 99  -1    75 +1
            if (map.get(key) > 1) {
                map.put(key, map.get(key) - 1);
            } else {
                map.remove(key);
            }
            map.put(75, map.getOrDefault(75, 0) + 1);
        }

        int[] arr = {15, 7, 14, 6, 5, 13, 5, 10, 9};
        System.out.println(numOfCannon(arr));
    }
}

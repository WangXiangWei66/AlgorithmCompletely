package class_2022_06_3;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

//Range模块是跟踪数字范围的模块。
//设计一个数据结构来跟踪表示为 半开区间 的范围并查询它们。
//半开区间[left, right)表示所有left <= x < right的实数 x 。
//实现 RangeModule 类:
//RangeModule()初始化数据结构的对象
//void addRange(int left, int right) :
//添加 半开区间[left, right)，跟踪该区间中的每个实数。
//添加与当前跟踪的数字部分重叠的区间时，
//应当添加在区间[left, right)中尚未跟踪的任何数字到该区间中。
//boolean queryRange(int left, int right) :
//只有在当前正在跟踪区间[left, right)中的每一个实数时，才返回 true
//否则返回 false 。
//void removeRange(int left, int right) :
//停止跟踪 半开区间[left, right)中当前正在跟踪的每个实数。
//测试连接 : https://leetcode.com/problems/range-module/
public class Code03_RangeModule1 {

    class RangeModule {
        TreeMap<Integer, Integer> map;

        public RangeModule() {
            map = new TreeMap<>();
        }

        public void addRange(int left, int right) {
            if (right <= left) {
                return;
            }
            //寻找小于等于left和right的最大起始点
            Integer start = map.floorKey(left);
            Integer end = map.floorKey(right);
            if (start == null && end == null) {
                map.put(left, right);
            } else if (start != null && map.get(start) >= left) {
                map.put(start, Math.max(map.get(end), right));
            } else {
                map.put(left, Math.max(map.get(end), right));
            }
            //移除被合并的中间区间
            // subMap(left, false, right, true)：获取键在(left, right]之间的区间
            Map<Integer, Integer> subMap = map.subMap(left, false, right, true);
            Set<Integer> set = new HashSet<>(subMap.keySet());
            map.keySet().removeAll(set);
        }

        public boolean queryRange(int left, int right) {
            Integer start = map.floorKey(left);
            if (start == null) {
                return false;
            }
            return map.get(start) >= right;
        }

        public void removeRange(int left, int right) {
            if (right <= left) {
                return;
            }
            Integer start = map.floorKey(left);
            Integer end = map.floorKey(right);
            if (end != null && map.get(end) > right) {
                map.put(right, map.get(end));
            }
            if (start != null && map.get(start) > left) {
                map.put(start, left);
            }
            Map<Integer, Integer> subMap = map.subMap(left, true, right, false);
            Set<Integer> set = new HashSet<>(subMap.keySet());
            map.keySet().removeAll(set);
        }
    }

    public static void main(String[] args) {
        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(6, "我是6");
        map.put(3, "我是3");
        map.put(9, "我是9");
        map.put(5, "我是9");
        map.put(4, "我是9");
        Map<Integer, String> subMap = map.subMap(4, true, 6, false);

        for (int key : subMap.keySet()) {
            System.out.println(key);
        }

    }

}

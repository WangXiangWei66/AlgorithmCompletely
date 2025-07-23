package Class23;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

//超级水王问题
//给定一个数组arr，长度为N，如果某个数出现次数大于N/2，称该数为水王数，如果arr中有水王数，打印这个数；如果没有水王数，打印没有水王数
//要求时间复杂度O(N)，额外空间复杂度O(1)
//扩展1：摩尔投票
//扩展2：给定一个正数K，返回所有出现次数>N/K的数
public class Code04_FindKMajority {

    public static void printHalfMajor(int[] arr) {
        int cand = 0;//候选元素/可能是过半元素
        int HP = 0; //候选元素的「票数」（用于投票算法）
        //对候选元素进行筛选
        for (int i = 0; i < arr.length; i++) {
            if (HP == 0) {
                //设置为新候选
                cand = arr[i];
                HP = 1;
            } else if (arr[i] == cand) {//当前元素与候选元素相同，则票数+1
                HP++;
            } else {//不同则票数减1
                HP--;
            }
        }
        //最后如果票数变成了0，则一定没有过半
        if (HP == 0) {
            System.out.println("no such number.");
            return;
        }
        //验证候选元素是否过半
        HP = 0;
        //再一次遍历数字，统计候选元素真正的出现次数
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == cand) {
                HP++;
            }
        }
        if (HP > arr.length / 2) {
            System.out.println(cand);
        } else {
            System.out.println("no such number.");
        }
    }

    public static void printKMajor(int[] arr, int K) {
        if (K < 2) {
            System.out.println("the value of K is invalid");
            return;
        }
        //创哈希表存储候选区元素及其“票数”
        HashMap<Integer, Integer> cands = new HashMap<Integer, Integer>();
        for (int i = 0; i != arr.length; i++) {
            if (cands.containsKey(arr[i])) {
                cands.put(arr[i], cands.get(arr[i]) + 1);
            } else {
                if (cands.size() == K - 1) {
                    allCandsMinusOne(cands);
                } else {
                    cands.put(arr[i], 1);//筛选位置还没有到K-1个，则加入候选区
                }
            }
        }
        //验证候选区的实际出现次数
        HashMap<Integer, Integer> reals = getReals(arr, cands);
        //检查并打印所有满足条件的元素
        boolean hasPrint = false;
        for (Entry<Integer, Integer> set : cands.entrySet()) {
            Integer key = set.getKey();
            if (reals.get(key) > arr.length / K) {
                hasPrint = true;
                System.out.print(key + " ");
            }
        }
        System.out.println(hasPrint ? " " : "no such number");
    }
    //所有候选元素的票数减1，移除票数为0的候选
    public static void allCandsMinusOne(HashMap<Integer, Integer> map) {
        List<Integer> removeList = new LinkedList<>();// 1. 记录需要移除的候选（票数即将变为0的）
        for (Entry<Integer, Integer> set : map.entrySet()) {
            Integer key = set.getKey();
            Integer value = set.getValue();
            if (value == 1) {
                removeList.add(key);
            }
            map.put(key, value - 1);
        }
        //将待删除的元素删除
        for (Integer removeKey : removeList) {
            map.remove(removeKey);
        }
    }
    //统计候选元素在原数组中的实际出现次数
    public static HashMap<Integer, Integer> getReals(int[] arr, HashMap<Integer, Integer> cands) {
        HashMap<Integer, Integer> reals = new HashMap<Integer, Integer>();
        for (int i = 0; i != arr.length; i++) {
            int curNum = arr[i];
            if (cands.containsKey(curNum)) {
                if (reals.containsKey(curNum)) {
                    reals.put(curNum, reals.get(curNum) + 1);//已经有这个元素了，则次数加1
                } else {
                    reals.put(curNum, 1);//否则将他的次数初始化为1
                }
            }
        }
        return reals;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 1, 1, 2, 1};
        printHalfMajor(arr);
        int K = 4;
        printKMajor(arr, K);
    }

}

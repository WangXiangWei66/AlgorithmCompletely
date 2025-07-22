package Class22;

import java.util.HashMap;

//你正在安装一个广告牌，并希望它高度最大。这块广告牌将有两个钢制支架，两边各一个。每个钢支架的高度必须相等。
//你有一堆可以焊接在一起的钢筋 rods。举个例子，如果钢筋的长度为 1、2 和 3，则可以将它们焊接在一起形成长度为 6 的支架。
//返回广告牌的最大可能安装高度。如果没法安装广告牌，请返回 0。
//Leetcode题目：https://leetcode.com/problems/tallest-billboard/
public class Code05_TallestBillboard {

    public int tallestBillboard(int[] rods) {
        //key:两组钢管的长度差
        //value:较短一组的长度
        //dp存储"长度差"到"较短长度"的映射
        //cur是仅声明，未初始化的哈希表，它用来临时保存当前迭代开始的dp状态
        HashMap<Integer, Integer> dp = new HashMap<>(), cur;
        // 初始状态：差值为0，较短长度为0（两组都为空）
        dp.put(0, 0);
        //遍历每个钢管
        for (int num : rods) {
            if (num != 0) {//将长度为0的钢管跳过
                // 复制当前状态作为基础（不选当前钢管的情况）
                cur = new HashMap<>(dp);//保存处理的原始状态
                //遍历所有可能的差值状态
                for (int d : cur.keySet()) {
                    int diffMore = cur.get(d); // 当前差值对应的较短长度
                    // 情况1：将当前钢管加入较长的一组
                    // 新差值 = 原差值 + 钢管长度
                    dp.put(d + num, Math.max(diffMore, dp.getOrDefault(num + d, 0)));
                    //将当前差值加入较短的一组
                    int diffXD = dp.getOrDefault(Math.abs(num - d), 0);
                    if (d >= num) {
                        // 原较长组仍较长，新差值 = 原差值 - 钢管长度
                        // 新较短长度 = 原较短长度 + 钢管长度
                        dp.put(d - num, Math.max(diffMore + num, diffXD));
                    } else {
                        // 原较短组变成长组，新差值 = 钢管长度 - 原差值
                        // 新较短长度 = 原较短长度 + 原差值（原长组长度）
                        dp.put(num - d, Math.max(diffMore + d, diffXD));
                    }
                }
            }
        }
        return dp.get(0);
    }
}

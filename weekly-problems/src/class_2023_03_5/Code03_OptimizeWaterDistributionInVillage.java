package class_2023_03_5;
//来自小红书、字节跳动
//村里面一共有 n 栋房子
//我们希望通过建造水井和铺设管道来为所有房子供水。
//对于每个房子 i，我们有两种可选的供水方案：
//一种是直接在房子内建造水井
//成本为 wells[i - 1] （注意 -1 ，因为 索引从0开始 ）
//另一种是从另一口井铺设管道引水
//数组 pipes 给出了在房子间铺设管道的成本
//其中每个 pipes[j] = [house1j, house2j, costj]
//代表用管道将 house1j 和 house2j连接在一起的成本。连接是双向的。
//请返回 为所有房子都供水的最低总成本 。
//这道题很高频，引起注意
//本身也不难，转化一下变成最小生成树的问题即可
//测试链接 : https://leetcode.cn/problems/optimize-water-distribution-in-a-village/
public class Code03_OptimizeWaterDistributionInVillage {
}

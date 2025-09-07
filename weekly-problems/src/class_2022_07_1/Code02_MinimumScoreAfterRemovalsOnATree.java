package class_2022_07_1;
//给定一个棵树
//树上每个节点都有自己的值，记录在数组nums里
//比如nums[4] = 10，表示4号点的值是10
//给定树上的每一条边，记录在二维数组edges里
//比如edges[8] = {4, 9}表示4和9之间有一条无向边
//可以保证输入一定是一棵树，只不过边是无向边
//那么我们知道，断掉任意两条边，都可以把整棵树分成3个部分。
//假设是三个部分为a、b、c
//a部分的值是：a部分所有点的值异或起来
//b部分的值是：b部分所有点的值异或起来
//c部分的值是：c部分所有点的值异或起来
//请问怎么分割，能让最终的：三个部分中最大的异或值 - 三个部分中最小的异或值，最小
//返回这个最小的差值
//测试链接 : https://leetcode.cn/problems/minimum-score-after-removals-on-a-tree/
public class Code02_MinimumScoreAfterRemovalsOnATree {
}

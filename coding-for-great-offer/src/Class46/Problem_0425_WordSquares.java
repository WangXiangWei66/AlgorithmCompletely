package Class46;
//给定n个字符串，并且每个字符串长度一定是n，请组成单词方阵，比如：
//给定4个字符串，长度都是4，["ball","area","lead","lady"]
//可以组成如下的方阵：
//b a l l
//a r e a
//l e a d
//l a d y
//什么叫单词方阵？如上的方阵可以看到，第1行和第1列都是"ball"，第2行和第2列都是"area"，第3行和第3列都是"lead"，第4行和第4列都是"lady"
//所以如果有N个单词，单词方阵是指，一个N*N的二维矩阵，并且i行和i列都是某个单词，不要求全部N个单词都在这个方阵里。
//请返回所有可能的单词方阵。
//示例：
//输入: words = ["abat","baba","atan","atal"]
//输出: [["baba","abat","baba","atal"],["baba","abat","baba","atan"]]
//解释：
//可以看到输出里，有两个链表，代表两个单词方阵
//第一个如下：
//b a b a
//a b a t
//b a b a
//a t a l
//这个方阵里没有atan，因为不要求全部单词都在方阵里
//第二个如下：
//b a b a
//a b a t
//b a b a
//a t a n
//这个方阵里没有atal，因为不要求全部单词都在方阵里
//leetcode题目：https://leetcode.com/problems/word-squares/
public class Problem_0425_WordSquares {
}

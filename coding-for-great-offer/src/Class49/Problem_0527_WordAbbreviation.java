package Class49;
//规定单词一定要遵循如下的缩写规则：
//1）一个单词保留若干长度的前缀，以及保留最后一个字符，中间用数字代表长度的方式来缩写
//2）如果缩写后的长度没有原始长度小，则该缩写依然保持原始串的样子
//比如:
//apple可以缩写成"a3e"，表示保留长度为1的前缀"a"，以及保留最后一个字符"e"，中间用3来代表"ppl"的长度。
//apple也可以缩写成"ap2e"，表示保留长度为2的前缀"ap"，以及保留最后一个字符"e"，中间用2来代表"pl"的长度。
//apple也可以缩写成"app1e"，但是因为"app1e"没有原始长度小，所以该缩写不能写成"app1e"，应该写成"apple"
//也就是说，你可以去选择保留前缀的长度，一旦前缀确定，那么缩写剩下的内容也就都决定了。因为一定要保留最后一个字符，以及中间部分用数字代表长度。
//理解了缩写规定之后，请理解对几个字符串都做缩写，但是不能混淆的规定。
//比如："abkkf"和"abcde"
//"abkkf"缩写为"a3f"，"abcde"缩写为"a3e"，两个字符串的缩写都能变得最短，且不会混淆。因为最后的字符不一样，所以可以区分开
//比如："abkkkkc"和"abkskkc"
//"abkkkkc"缩写为"a5c"，"abkskkc"缩写为"a5c"，此时发现会混淆，因为缩写之后完全一样
//"abkkkkc"缩写为"abkk2c"，"abkskkc"缩写为"abks2c"，这是两个字符串缩写都尽可能短，且不会混淆的缩写。
//给你一个字符串数组arr，请完成对每个字符串的缩写，要求任意两个字符串都不会混淆，且每个字符串的缩写都尽可能的短。
//例子一
//输入：["like","god","internal","me","internet","interval","intension","face","intrusion"]
//输出：["l2e","god","internal","me","i6t","interval","inte4n","f2e","intr4n"]
//例子二
//输入: words = ["aa","aaa"]
//输出: ["aa","aaa"]
//leetcode题目：https://leetcode.com/problems/word-abbreviation/
public class Problem_0527_WordAbbreviation {
}

package Class46;
//一个字符串可以用缩写的形式来代替，比如单词"substitution"，可以有以下几种缩写：
//"s10n" ("s+省略10个字符+n")
//"sub4u4" ("sub+省略4个字符+u+省略4个字符")
//"12" ("省略12个字符")
//等等还有很多
//给定一个字符串target、给定一个字符串数组作为字典dictionary，要求把target缩写成长度最短的形式，但是又不会和dictionary中任何单词混淆
//返回缩写最短形式的一种结果就可以
//例子1：
//输入：target = "apple", dictionary = ["blade"]
//输出: "a4"
//解释：
//"apple"最短的缩写是"5"，但是字典中"blade"长度也是5，所以会混淆
//"apple"第二短的缩写，有一种是"4e"，但是"4e"也可以是字典中"blade"，所以会混淆
//"apple"第二短的缩写，有一种是"a4"，字典中"blade"不以a开头，所以不会混淆
//例子2：
//输入: target = "apple", dictionary = ["blade","plain","amber"]
//输出: "1p3"
//leetcode题目：https://leetcode.com/problems/minimum-unique-word-abbreviation/
public class Problem_0411_MinimumUniqueWordAbbreviation {
}

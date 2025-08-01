package Class28;

//本代码时间复杂度与空间复杂度都是O(2 ^ n)
//给定一个正整数 n ，输出的第 n 项。
//前五项如下：
//1
//11
//21
//1211
//111221
//第一项是数字 1
//描述前一项，这个数是 1 即 “ 一 个 1 ”，记作 "11"
//描述前一项，这个数是 11 即 “ 二 个 1 ” ，记作 "21"
//描述前一项，这个数是 21 即 “ 一 个 2 + 一 个 1 ” ，记作 "1211"
//描述前一项，这个数是 1211 即 “ 一 个 1 + 一 个 2 + 二 个 1 ” ，记作 "111221"
//返回第N项的字符串
//Leetcode题目：https://leetcode.com/problems/count-and-say/
public class Problem_0038_CountAndSay {
    //返回第n项的报数序列字符串
    public static String countAndSay(int n) {
        if (n < 1) {
            return "";
        }
        if (n == 1) {
            return "1";
        }
        char[] last = countAndSay(n - 1).toCharArray();//获取第n-1项的报数序列，并转化为字符数组
        StringBuilder ans = new StringBuilder();//由于构建当前项的报数序列
        int times = 1;//记录连续相同数字的出现次数
        for (int i = 1; i < last.length; i++) {
            if (last[i - 1] == last[i]) {
                times++;
            } else {
                //将前一个数字出现的次数和该数字本身追加到结果中
                ans.append(String.valueOf(times));
                ans.append(String.valueOf(last[i - 1]));
                times = 1;//重置计数器，开始统计新的数字
            }
        }
        //处理最后一组数字
        ans.append(String.valueOf(times));
        ans.append(String.valueOf(last[last.length - 1]));
        return ans.toString();
    }

    public static void main(String[] args) {
        System.out.println(countAndSay(20));
    }
}

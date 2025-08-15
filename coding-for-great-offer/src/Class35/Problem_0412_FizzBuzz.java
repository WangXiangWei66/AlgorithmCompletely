package Class35;

import java.util.ArrayList;
import java.util.List;

//写一个程序，输出从 1 到 n 数字的字符串表示。
//
//1. 如果n是3的倍数，输出“Fizz”；
//2. 如果n是5的倍数，输出“Buzz”；
//   3.如果n同时是3和5的倍数，输出 “FizzBuzz”。
//   示例：
//   n = 15,
//   返回:
//   [
//   "1",
//   "2",
//   "Fizz",
//   "4",
//   "Buzz",
//   "Fizz",
//   "7",
//   "8",
//   "Fizz",
//   "Buzz",
//   "11",
//   "Fizz",
//   "13",
//   "14",
//   "FizzBuzz"
//   ]
//   Leetcode题目 : https://leetcode.com/problems/fizz-buzz/
public class Problem_0412_FizzBuzz {
    //时间复杂度和空间复杂度都是O(N)
    public static List<String> fizzBuzz(int n) {
        ArrayList<String> ans = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (i % 15 == 0) {
                ans.add("FizzBuzz");
            } else if (i % 5 == 0) {
                ans.add("Buzz");
            } else if (i % 3 == 0) {
                ans.add("Fizz");
            } else {
                ans.add(String.valueOf(i));
            }
        }
        return ans;
    }
}

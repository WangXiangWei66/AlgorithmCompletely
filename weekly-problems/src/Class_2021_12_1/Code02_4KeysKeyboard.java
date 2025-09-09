package Class_2021_12_1;

//一开始屏幕上什么也没有，粘贴板里什么也没有，
//你只能在键盘上做如下4种操作中的1种：
//输入：在屏幕上已经显示内容的后面加一个A
//全选：把屏幕上已经显示的全部内容选中
//复制：被选中的内容复制进粘贴板
//粘贴：在屏幕上已经显示内容的后面添加粘贴板里的内容
//给定一个正数n，表示你能操作的步数，
//返回n步内你能让最多多少个A显示在屏幕上
//Leetcode链接 : https://leetcode.com/problems/4-keys-keyboard/
public class Code02_4KeysKeyboard {

    public static int maxA(int n) {
        //dp[i]:i+1步能得到的最大A数量
        int[] dp = new int[n];
        //步数少于5步的时候，最优策略是一直输入
        for (int i = 0; i < 6 && i < n; i++) {
            dp[i] = i + 1;
        }
        for (int i = 6; i < n; i++) {
            dp[i] = Math.max(
                    Math.max(dp[i - 3] * 2, dp[i - 4] * 3),
                    Math.max(dp[i - 5] * 4, dp[i - 6] * 5));
        }
        return dp[n - 1];
    }
}

package Class27;

import java.util.Arrays;

//企鹅厂每年都会发文化衫，文化衫有很多种，厂庆的时候，企鹅们都需要穿文化衫来拍照
//一次采访中，记者随机遇到的企鹅，企鹅会告诉记者还有多少企鹅跟他穿一种文化衫
//我们将这些回答放在answers数组里，返回鹅厂中企鹅的最少数量
//输入: answers = [1]    输出：2
//输入: answers = [1, 1, 2]    输出：5
//Leetcode题目：https://leetcode.com/problems/rabbits-in-forest/
public class Code02_MinPeople {

    public static int numRabbits(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        Arrays.sort(arr);//目的是将回答相同的兔子聚集在一起，便于批量计算
        int x = arr[0];
        int c = 1;//回答数字为x的兔子的数量
        int ans = 0;//累计的兔子总数
        for (int i = 1; i < arr.length; i++) {
            if (x != arr[i]) {
                // 遇到新的回答数字，先计算上一组的兔子数量
                ans += ((c + x) / (x + 1)) * (x + 1);
                x = arr[i];//更新当前处理的数字和计数
                c = 1;
            } else {
                c++;
            }
        }
        return ans + ((c + x) / (x + 1)) * (x + 1);
    }
}

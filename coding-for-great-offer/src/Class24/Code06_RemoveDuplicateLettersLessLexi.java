package Class24;

//给你一个字符串 s ，请你去除字符串中重复的字母，使得每个字母只出现一次。需保证返回结果的字典序最小（要求不能打乱其他字符的相对位置）
//Leetcode题目：https://leetcode.com/problems/remove-duplicate-letters/
public class Code06_RemoveDuplicateLettersLessLexi {
    //接收字符串s并返回处理后的结果
    //时间复杂度为O(N)
    public static String removeDuplicateLetters(String s) {
        int[] cnts = new int[26]; // 用于记录每个字母剩余出现的次数，26个元素对应a-z
        boolean[] enter = new boolean[26];// 标记字母是否已加入结果栈中
        for (int i = 0; i < s.length(); i++) {
            cnts[s.charAt(i) - 'a']++;//统计每个字母出现的总次数
        }

        //单调栈
        //从左往右只保留依次变大的字符
        char[] stack = new char[26];// 用数组模拟栈，最多存储26个不同字母
        int size = 0;//栈的大小，即当前栈种元素的数量
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (!enter[cur - 'a']) {
                enter[cur - 'a'] = true;
                // 栈不为空，且栈顶元素大于当前字符，且栈顶元素在后面还会出现
                while (size > 0 && stack[size - 1] > cur && cnts[stack[size - 1] - 'a'] > 0) {
                    enter[stack[size - 1] - 'a'] = false;//取消标记栈顶元素
                    size--;//将栈顶元素弹出
                }
                stack[size++] = cur;//将当前字符压入栈中
            }
            cnts[cur - 'a']--;//减少当前字符的剩余出现次数
        }
        return String.valueOf(stack, 0, size);//将栈中元素转化为字符串并返回
    }
}

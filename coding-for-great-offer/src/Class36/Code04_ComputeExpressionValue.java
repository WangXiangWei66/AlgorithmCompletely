package Class36;

//来自美团
//() 分值为2
//(()) 分值为3
//((())) 分值为4
//也就是说，每包裹一层，分数就是里面的分值+1
//()() 分值为2 * 2
//(())() 分值为3 * 2
//也就是说，每连接一段，分数就是各部分相乘，以下是一个结合起来的例子
//(()())()(()) -> (2 * 2 + 1) * 2 * 3 -> 30
//给定一个括号字符串str，已知str一定是正确的括号结合，不会有违规嵌套
//返回分数
public class Code04_ComputeExpressionValue {

    public static int sores(String s) {
        return compute(s.toCharArray(), 0)[0];
    }
    //索引0：当前括号片段的分数
    //索引1：当前片段处理的字符位置
    public static int[] compute(char[] s, int i) {
        if (s[i] == ')') {
            return new int[]{1, i};
        }
        int ans = 1;//初始位置的分数
        while (i < s.length && s[i] != ')') {
            int[] info = compute(s, i + 1);
            ans *= info[0] + 1;
            i = info[1] + 1;
        }
        return new int[]{ans, i};
    }

    public static void main(String[] args) {
        String str1 = "(()())()(())";
        System.out.println(sores(str1));

        String str2 = "(()())(((())))((())())";
        System.out.println(sores(str2));

        String str3 = "(()()())(()(()))";
        System.out.println(sores(str3));
    }
}

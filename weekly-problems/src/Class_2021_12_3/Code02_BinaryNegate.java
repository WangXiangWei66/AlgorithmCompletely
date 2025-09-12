package Class_2021_12_3;

//来自TME2022校园招聘后台开发/运营开发/业务运维/应用开发笔试
//给定一个只由'0'和'1'组成的字符串，代表一个无符号整数
//你只能在连续的一个子串上，翻转子串，也就是子串上，0变1，1变0。返回最大的结果
//具体详情请参加如下的测试页面
//测试链接 : https://www.nowcoder.com/test/33701596/summary
//本题目为该试卷第2题
public class Code02_BinaryNegate {

    public static String maxLexicographical(String num) {
        char[] arr = num.toCharArray();
        int i = 0;
        //寻找字符串中第一个0位置
        while (i < arr.length) {
            if (arr[i] == '0') {
                break;
            }
            i++;
        }
        //从第一个0开始，将后面所有的0反转成1
        while (i < arr.length) {
            if (arr[i] == '1') {
                break;
            }
            arr[i++] = '1';
        }
        return String.valueOf(arr);
    }
}

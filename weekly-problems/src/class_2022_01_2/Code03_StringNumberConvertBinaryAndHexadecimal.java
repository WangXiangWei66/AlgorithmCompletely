package class_2022_01_2;

//来自兴业数金
//给定一个字符串形式的数，比如"3421"或者"-8731"
//如果这个数不在-32768~32767范围上，那么返回"NODATA"
//如果这个数在-32768~32767范围上，那么这个数就没有超过16个二进制位所能表达的范围
//返回这个数的2进制形式的字符串和16进制形式的字符串，用逗号分割
public class Code03_StringNumberConvertBinaryAndHexadecimal {

    public static String convert(String num) {
        if (num == null || num.length() == 0 || num.length() > 6) {
            return "NODATA";
        }
        int n = Integer.valueOf(num);//将字符串转化为整数
        if (n < -32768 || n > 32767) {
            return "NODATA";
        }
        //二进制数表示：符号位的确定和只保留后十六位
        int info = (n < 0 ? (1 << 15) : 0) | (n & 65535);
        StringBuilder builder = new StringBuilder();
        for (int i = 15; i >= 0; i--) {
            builder.append((info & (1 << i)) != 0 ? '1' : '0');
        }
        builder.append(",0x");
        //生成4位十六进制字符串
        for (int i = 12; i >= 0; i -= 4) {
            int cur = (info & (15 << i)) >> i;
            if (cur < 10) {
                builder.append(cur);
            } else {
                switch (cur) {
                    case 10:
                        builder.append('a');
                        break;
                    case 11:
                        builder.append('b');
                        break;
                    case 12:
                        builder.append('c');
                        break;
                    case 13:
                        builder.append('d');
                        break;
                    case 14:
                        builder.append('e');
                        break;
                    default:
                        builder.append('f');
                }
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        String num1 = "0";
        System.out.println(convert(num1));

        String zuo = "457";
        System.out.println(convert(zuo));

        String num2 = "-32768";
        System.out.println(convert(num2));

        String num3 = "32768";
        System.out.println(convert(num3));

        String num4 = "32767";
        System.out.println(convert(num4));

        String num5 = "-1";
        System.out.println(convert(num5));
    }
}

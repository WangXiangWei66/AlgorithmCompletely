package Class44;

//给定两个字符串low和high，它们只由数字字符组成，代表两个数字并且low<=high，返回在[low, high]范围上旋转有效串的数量
//旋转有效串：一个字符串以中心点为支点，旋转180度之后依然是原来字符串，叫做旋转有效串，比如：
//181旋转之后还是181，是旋转有效串
//8008旋转之后还是8008，是旋转有效串
//689旋转之后还是689，是旋转有效串
//而6816就不是旋转有效串，因为旋转之后是9189
//leetcode题目 : https://leetcode.com/problems/strobogrammatic-number-iii/
public class Problem_0248_StrobogrammaticNumberIII {

    public static int strobogrammaticInRange(String l, String h) {
        char[] low = l.toCharArray();
        char[] high = h.toCharArray();
        //检查low是否小于等于high，若不是则返回0
        if (!equalMore(low, high)) {
            return 0;
        }
        int lowLen = low.length;
        int highLen = high.length;
        if (lowLen == highLen) {
            int up1 = up(low, 0, false, 1);
            int up2 = up(high, 0, false, 1);
            return up1 - up2 + (valid(high) ? 1 : 0);
        }
        //low和high长度不同
        int ans = 0;
        //累加长度在(lowLen,highLen)之间的所有旋转有效数字
        for (int i = lowLen + 1; i < highLen; i++) {
            ans += all(i);
        }
        //加上大于等于low的旋转有效数字
        ans += up(low, 0, false, 1);
        ans += down(high, 0, false, 1);
        return ans;
    }

    public static boolean equalMore(char[] low, char[] cur) {
        if (low.length != cur.length) {
            return low.length < cur.length;
        }
        for (int i = 0; i < low.length; i++) {
            if (low[i] != cur[i]) {
                return low[i] < cur[i];
            }
        }
        return true;
    }
    //检查数字是否为旋转数字
    public static boolean valid(char[] str) {
        int L = 0;
        int R = str.length - 1;
        while (L <= R) {
            boolean t = L != R;//判断是否为同一个位置
            if (convert(str[L++], t) != str[R--]) {
                return false;
            }
        }
        return true;
    }

    //diff:判断当前字符是否处于对称位置
    public static int convert(char cha, boolean diff) {
        switch (cha) {
            case '0':
                return '0';
            case '1':
                return '1';
            case '6':
                return diff ? '9' : -1;
            case '8':
                return '8';
            case '9':
                return diff ? '6' : -1;
            default:
                return -1;
        }
    }
    //统计大于等于low的旋转有效数字
    //leftMore:标记“左侧以构建的位是否大于low的对应位"
    //rightLessEqualMore:右侧对应位与low对应位的关系
    public static int up(char[] low, int left, boolean leftMore, int rightLessEqualMore) {
        int N = low.length;
        int right = N - 1 - left;//与left对称的右指针位置
        if (left > right) {
            return leftMore || (!leftMore && rightLessEqualMore != 0) ? 1 : 0;
        }
        if (leftMore) {
            return num(N - (left << 1));//这里计算了，剩余的未处理位
        } else {
            int ways = 0;
            //尝试比low[left]大的字符
            for (char cha = (char) (low[left] + 1); cha <= '9'; cha++) {
                if (convert(cha, left != right) != -1) {
                    ways += up(low, left + 1, true, rightLessEqualMore);
                }
            }
            //尝试与low[left]相等的字符
            int convert = convert(low[left], left != right);
            if (convert != -1) {
                if (convert < low[right]) {
                    ways += up(low, left + 1, false, 0);
                } else if (convert == low[right]) {
                    ways += up(low, left + 1, false, rightLessEqualMore);
                } else {
                    ways += up(low, left + 1, false, 2);
                }
            }
            return ways;
        }
    }

    //“小于等于high”的旋转有效数字
    //l1:标记左侧已构建的数字是否已经小于high对应位置的数字
    //rs:标记右侧与high对应位的关系
    public static int down(char[] high, int left, boolean ll, int rs) {
        int N = high.length;
        int right = N - 1 - left;
        if (left > right) {
            return ll || (!ll && rs != 2) ? 1 : 0;
        }
        //左侧已对应小于high的对应位
        if (ll) {
            return num(N - (left << 1));
        } else {
            int ways = 0;
            for (char cha = (N != 1 && left == 0) ? '1' : '0'; cha < high[left]; cha++) {
                if (convert(cha, left != right) != -1) {
                    ways += down(high, left + 1, true, rs);
                }
            }
            int convert = convert(high[left], left != right);
            if (convert != -1) {
                if (convert < high[right]) {
                    ways += down(high, left + 1, false, 0);
                } else if (convert == high[right]) {
                    ways += down(high, left + 1, false, rs);
                } else {
                    ways += down(high, left + 1, false, 2);
                }
            }
            return ways;
        }
    }
    //计算特定剩余位数的旋转有效数字数量
    public static int num(int bits) {
        if (bits == 1) {
            return 3;
        }
        if (bits == 2) {
            return 5;
        }
        int p2 = 3;//bits为1的结果
        int p1 = 5;//bits为2的结果
        int ans = 0;
        for (int i = 3; i <= bits; i++) {
            ans = 5 * p2;
            p2 = p1;
            p1 = ans;
        }
        return ans;
    }
    //计算特定长度完整数字的旋转有效数字总数
    public static int all(int len) {
        int ans = (len & 1) == 0 ? 1 : 3;
        for (int i = (len & 1) == 0 ? 2 : 3; i < len; i += 2) {
            ans *= 5;
        }
        //处理前置0
        return ans << 2;
    }
    //通过init参数区分是否为初始调用
    public static int all(int len, boolean init) {
        if (len == 0) {
            return 1;
        }
        if (len == 1) {
            return 3;
        }
        if (init) {
            //首位+末尾为1组对称位，首位有四种选择
            return all(len - 2, false) << 2;
        } else {
            return all(len - 2, false) * 5;
        }
    }
}

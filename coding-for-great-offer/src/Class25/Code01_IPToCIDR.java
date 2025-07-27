package Class25;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// IP地址是一个格式化的32位无符号整数，其中每组8位被打印为十进制数和点字符'。的问题将两组分开。
//例如，二进制数00001111 10001000 11111111 01101011（为清晰起见添加空格）格式化为IP地址将是“15.136.255.107”。
// CIDR块是一种用来表示一组特定IP地址的格式。它是一个字符串，由一个基本IP地址，后跟一个斜杠，后跟一个前缀长度k组成。
//它覆盖的地址是所有前k位与基本IP地址相同的IP地址。
//例如： 123.45.67.89/20是一个前缀长度为20的CIDR块。任何二进制表示匹配的IP地址
//01111011 00101101 0100xxxx xxxxxxxx属于CIDR块覆盖的集合，其中x为0或1。
//你得到一个起始IP地址IP和我们需要覆盖的IP地址数目n。
//你的目标是使用尽可能少的CIDR块来覆盖[IP， IP + n - 1]范围内的所有IP地址
//范围之外的IP地址不能被覆盖。
//返回IP地址范围内最短的CIDR块列表。如果有多个答案，返回其中任何一个。
//Leetcode题目：https://leetcode.com/problems/ip-to-cidr/
public class Code01_IPToCIDR {

    public static List<String> ipToCIDR(String ip, int n) {
        int cur = status(ip);//将IP地址转化为32位整数表示
        int maxPower = 0;//当前IP地址最右侧连续0的位数，决定了从该IP开始能形成的最大CIDR块大小
        int solved = 0;//当前选择的CIDR块能覆盖的IP地址数量
        int power = 0;//计算CIDR块的前缀长度，指数运算
        List<String> ans = new ArrayList<>();//存储结果CIDR块
        while (n > 0) {//如果还有IP地址需要覆盖
            maxPower = mostRightPower(cur);//获取当前 IP 最右侧连续 0 的位数
            //下面开始确定当前最右的CIDR块大小
            solved = 1;//初始覆盖1个IP
            power = 0;//初始指数为0
            //尝试扩大CIDR块的覆盖范围
            while ((solved << 1) <= n && (power + 1) <= maxPower) {
                solved <<= 1;
                power++;
            }
            ans.add(content(cur, power));//将当前计算出的 CIDR 块添加到结果列表
            n -= solved;//更新剩余需要覆盖的IP数量
            cur += solved;//更新当前IP为下一个未覆盖的IP
        }
        return ans;
    }
    //将IP地址转化为32位整数来表示
    public static int status(String ip) {
        int ans = 0;
        int move = 24;// 第一个8位需要左移24位（IP地址的第一个数是最高8位）
        for (String str : ip.split("\\.")) {//按照点来分割IP值
            ans |= Integer.valueOf(str) << move; // 将每个8位段转换为整数并左移相应位数，然后合并到结果中
            move -= 8;//每次移动减少8位
        }
        return ans;//返回32位整数表示的IP
    }

    public static HashMap<Integer, Integer> map = new HashMap<>();// 缓存用于计算最右侧0位数量
    // 计算数字二进制表示中最右侧连续0的数量（即最大的2^k能整除该数）
    public static int mostRightPower(int num) {
        if (map.isEmpty()) {//初始化缓存
            map.put(0, 32);//0的所有位都是0，所以是32
            for (int i = 0; i < 32; i++) {
                map.put(1 << i, i);// 2^i对应的i值
            }
        }
        return map.get(num & (-num)); // num & (-num) 得到最右侧的1所在的位置，通过缓存获取对应的0的数量
    }
    // 将32位整数表示的IP和power转换为CIDR格式字符串
    public static String content(int status, int power) {
        StringBuilder builder = new StringBuilder();
        //依次处理4个八位段
        for (int move = 24; move >= 0; move -= 8) {
            // 提取当前8位段的值并添加到字符串
            builder.append(((status & (255 << move)) >>> move) + ".");
        }
        //将最后一个点替换为斜杠
        builder.setCharAt(builder.length() - 1, '/');
        builder.append(32 - power);//添加前缀长度
        return builder.toString();//返回CIDR字符串
    }
}

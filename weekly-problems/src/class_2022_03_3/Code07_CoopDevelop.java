package class_2022_03_3;

import java.util.HashMap;

//来自银联编程比赛
//为了不断提高用户使用的体验，开发团队正在对产品进行全方位的开发和优化。
//已知开发团队共有若干名成员，skills[i] 表示第 i 名开发人员掌握技能列表。
//如果两名成员各自拥有至少一门对方未拥有的技能，则这两名成员可以「合作开发」。
//请返回当前有多少对开发成员满足「合作开发」的条件。
//由于答案可能很大，请你返回答案对 10^9 + 7 取余的结果。
//测试链接 : https://leetcode-cn.com/contest/cnunionpay-2022spring/problems/lCh58I/
public class Code07_CoopDevelop {

    public static long mod = 1000000007L;

    public static int coopDevelop(int[][] skills) {
        int n = skills.length;
        //计算所有非完整技能集合的出现次数，key为子集   value为个数
        HashMap<Long, Long> noFullSetsNums = new HashMap<>();
        for (int[] people : skills) {
            fillNoFullMap(people, 0, 0, true, noFullSetsNums);
        }
        //存储完整技能集合的出现次数
        HashMap<Long, Long> cntsNums = new HashMap<>();
        long miuns = 0L;//不满足合作条件的总对数
        for (int[] people : skills) {
            //将当前技能编码为一个长整数
            long status = 0L;
            for (int skill : people) {
                status = (status << 10) | skill;
            }
            //累加无效对数
            miuns += noFullSetsNums.getOrDefault(status, 0L);
            miuns += cntsNums.getOrDefault(status, 0L);
            //记录当前技能集合的出现次数
            cntsNums.put(status, cntsNums.getOrDefault(status, 0L) + 1);
        }
        long ans = (long) n * (long) (n - 1) / 2L;
        return (int) ((ans - miuns) % mod);
    }
    //people:当前人的技能数组
    //i:当前处理的技能索引
    //status:用位运算编码的技能集合——已选技能的编码
    //fill:标记是否为完整技能集
    public static void fillNoFullMap(int[] people, int i, long status, boolean full, HashMap<Long, Long> noFullSetsNums) {
        if (i == people.length) {
            if (!full) {
                noFullSetsNums.put(status, noFullSetsNums.getOrDefault(status, 0L) + 1);
            }
        } else {
            fillNoFullMap(people, i + 1, status, false, noFullSetsNums);//不选择当前节能，一定不是完整集
            fillNoFullMap(people, i + 1, (status << 10) | people[i], full, noFullSetsNums);
        }
    }
}

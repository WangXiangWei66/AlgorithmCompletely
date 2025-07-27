package Class25;

//良好加油站问题最优解
//Leetcode题目：https://leetcode.com/problems/gas-station/
public class Code04_GasStation {

    //得到结果数组的过程，时间复杂度为O(N),额外空间复杂度为O(1)
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        if (gas == null || gas.length == 0) {
            return -1;
        }
        if (gas.length == 1) {
            return gas[0] < cost[0] ? -1 : 0;//汽油 >= 消耗则返回0，否则-1
        }
        boolean[] good = stations(cost, gas);//生成每个加油站是否为（好起点）的标记数组
        //遍历标记数组，返回第一个好起点
        for (int i = 0; i < gas.length; i++) {
            if (good[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean[] stations(int[] cost, int[] gas) {
        if (cost == null || gas == null || cost.length < 2 || cost.length != gas.length) {
            return null;
        }
        //转换数组并获取初始起点（第一个可能的好起点）// 转换数组并获取初始起点（第一个可能的好起点）
        int init = changeDisArrayGetInit(cost, gas);
        // 若初始起点不存在，返回全false数组；否则通过扩大区域寻找所有好起点
        return init == -1 ? new boolean[cost.length] : enlargeArea(cost, init);
    }
    //转换数组并找初始起点
    public static int changeDisArrayGetInit(int[] dis, int[] oil) {
        int init = -1;//默认没有初始起点
        for (int i = 0; i < dis.length; i++) {
            dis[i] = oil[i] - dis[i];//将dis[i]转换为「当前站加油量 - 到下一站的消耗量」（即净收益）
            if (dis[i] >= 0) {
                init = i;
            }
        }
        return init;
    }
    //扩大区域寻找好起点
    public static boolean[] enlargeArea(int[] dis, int init) {
        boolean[] res = new boolean[dis.length];//标记好起点的结果数组
        int start = init;//从初始起点开始
        int end = nextIndex(init, dis.length);//end指向start的下一站
        int need = 0;//当前需要补充的油量（若为正，说明之前的油不够）
        int rest = 0;//当前剩余的油量
        // 循环：以start为起点尝试扩大覆盖范围，直到回到初始起点
        do {
            // 若start不是初始起点，且start是end的前一站（形成闭环），则退出
            if (start != init && start == lastIndex(end, dis.length)) {
                break;
            }
            if (dis[start] < need) {
                need -= dis[start];
            } else {
                rest += dis[start] - need;
                need = 0;//将需求清0
                while (rest >= 0 && end != start) {
                    rest += dis[end];//加上end站的剩余油量
                    end = nextIndex(end, dis.length);//end往前移动
                }
                if (rest >= 0) {
                    res[start] = true;
                    // 检查start的前序站是否也能作为好起点（通过connectGood）
                    connectGood(dis, lastIndex(start, dis.length), init, res);
                    break;
                }
            }
            start = lastIndex(start, dis.length);//去尝试前一个站
        } while (start != init);
        return res;
    }
    //标记前序站是否为好起点
    public static void connectGood(int[] dis, int start, int init, boolean[] res) {
        int need = 0;
        while (start != init) {
            if (dis[start] < need) {
                need -= dis[start];
            } else {
                res[start] = true;
                need = 0;
            }
            start = lastIndex(start, dis.length);//继续移动到前一站
        }
    }
    //下面开始处理环形索引
    public static int lastIndex(int index, int size) {
        return index == 0 ? (size - 1) : index - 1;
    }

    public static int nextIndex(int index, int size) {
        return index == size - 1 ? 0 : (index + 1);
    }
}

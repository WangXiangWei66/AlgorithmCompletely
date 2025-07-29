package Class27;

import java.util.Arrays;
import java.util.HashMap;

//每一个项目都有三个数，[a,b,c]表示这个项目a和b乐队参演，花费为c
//每一个乐队可能在多个项目里都出现了，但是只能被挑一次
//nums是可以挑选的项目数量，所以一定会有nums*2只乐队被挑选出来
//返回一共挑nums轮(也就意味着一定请到所有的乐队)，最少花费是多少？
//如果怎么都无法在nums轮请到nums*2只乐队且每只乐队只能被挑一次，返回-1
//nums<9，programs长度小于500，每组测试乐队的全部数量一定是nums*2，且标号一定是0 ~ nums*2-1
public class Code01_PickBands {
    //programs是项目列表（每个项目为[a,b,c]
    //nums是需要挑选的项目数量
    public static int minCost1(int[][] programs, int nums) {
        if (nums == 0 || programs == null || programs.length == 0) {//无需选择或者是五项目的情况
            return 0;
        }
        int size = clean(programs);//清洗列表，返回有效项目的数量
        int[] map1 = init(1 << (nums << 1));// 计算状态总数：2^(2*nums)
        int[] map2 = null;//分治时存储第二部分的状态成本（仅在nums为奇数时使用）
        if ((nums & 1) == 0) {//根据nums的奇偶性来拆分任务
            f(programs, size, 0, 0, 0, nums >> 1, map1);//偶数只需处理一半数量
            map2 = map1;
        } else {// 奇数：拆分为两部分（nums//2 和 nums - nums//2）
            f(programs, size, 0, 0, 0, nums >> 1, map1);
            map2 = init(1 << (nums << 1));
            f(programs, size, 0, 0, 0, nums - (nums >> 1), map2);
        }
        int mask = (1 << (nums << 1)) - 1;//计算 “互补状态”—— 若map1中某状态为i，则map2中与之匹配的状态为mask & (~i)（即覆盖剩余未选中的乐队）。
        int ans = Integer.MAX_VALUE;
        //寻找两部分组合的最小成本（状态互补）
        for (int i = 0; i < map1.length; i++) {
            // 检查map1的状态i和map2的互补状态（mask & ~i）是否都有效
            if (map1[i] != Integer.MAX_VALUE && map2[mask & (~i)] != Integer.MAX_VALUE) {
                ans = Math.min(ans, map1[i] + map2[mask & (~i)]);
            }
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    //对原始项目列表进行标准化、去重和压缩
    public static int clean(int[][] programs) {
        //暂存项目的最小值与最大值
        int x = 0;
        int y = 0;
        // 标准化项目：确保a <= b（避免[a,b]和[b,a]被视为不同项目）
        for (int[] p : programs) {
            x = Math.min(p[0], p[1]);
            y = Math.max(p[0], p[1]);
            p[0] = x;
            p[1] = y;
        }
        // 排序项目：按a升序 → b升序 → 成本升序（便于去重）
        Arrays.sort(programs, (a, b) -> a[0] != b[0] ? (a[0] - b[0]) : (a[1] != b[1] ? (a[1] - b[1]) : (a[2] - b[2])));
        // 去重：标记重复项目（a和b相同的项目保留成本最低的）
        x = programs[0][0];//初始化基准值
        y = programs[0][1];
        int n = programs.length;//获取项目的总数
        for (int i = 1; i < n; i++) {
            if (programs[i][0] == x && programs[i][1] == y) {
                programs[i] = null;//重复项标记为null
            } else {
                x = programs[i][0];
                y = programs[i][1];
            }
        }
        //压缩数组，将有效项目移到数组前端
        int size = 1;//至少保留第一个项目
        for (int i = 1; i < n; i++) {
            if (programs[i] != null) {
                programs[size++] = programs[i];//移动到前端，并把有效数目加1
            }
        }
        return size;
    }

    public static int[] init(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = Integer.MAX_VALUE;
        }
        return arr;
    }

    //递归回溯来计算状态成本
    //programs:清洗后的项目列表（仅前size个有效）
    //size:有效项目的总数量
    //index：当前遍历到的项目索引
    //status：当前已经选中的乐队状态
    //cost：当前已选中项目的总成本
    //rest：还需要选择的项目数量
    //map：用于记录每个状态的最小成本数组（索引为状态、值为成本）
    public static void f(int[][] programs, int size, int index, int status, int cost, int rest, int[] map) {
        if (rest == 0) {// 终止条件：已选够rest个项目，更新当前状态的最小成本
            map[status] = Math.min(map[status], cost);
        } else {
            if (index != size) {//未遍历完所有有效项目时继续递归
                //不选当前项目，直接进入下一个
                f(programs, size, index + 1, status, cost, rest, map);
                //尝试选择当前项目（检查是否冲突）
                int pick = 0 | (1 << programs[index][0]) | (1 << programs[index][1]);// 计算当前项目占用的乐队位（例如乐队0和2 → 0b101）
                if ((pick & status) == 0) {//如果两个乐队都还没有被选中
                    f(programs, size, index + 1, status | pick, cost + programs[index][2], rest - 1, map);
                }
            }
        }
    }

    public static int minCost = Integer.MAX_VALUE;
    public static int[] map = new int[1 << 16];//map用来记录每种乐队状态的最小成本

    public static void process(int[][] programs, int size, int index, int rest, int pick, int cost) {
        if (rest == 0) {
            map[pick] = Math.min(map[pick], cost);
        } else {
            if (index != size) {
                process(programs, size, index + 1, rest, pick, cost);
                int x = programs[index][0];
                int y = programs[index][1];
                int cur = (1 << x) | (1 << y);
                if ((pick & cur) == 0) {
                    process(programs, size, index + 1, rest - 1, pick | cur, cost + programs[index][2]);
                }
            }
        }
    }

    public static void wang(int[] arr, int index, int rest) {
        if (rest == 0) {
            return;
        }
        if (index != arr.length) {
            wang(arr, index + 1, rest);
            wang(arr, index + 1, rest - 1);
        }
    }

    //用于存储已经计算过的状态，避免重复计算
    public static HashMap<Integer, Integer> cache = new HashMap<>();

    public static int minCost2(int[][] programs, int nums) {
        //标准化项目，每个项目的第一个编号始终小于第二个
        for (int i = 0; i < programs.length; i++) {
            if (programs[i][0] > programs[i][1]) {
                int t = programs[i][0];
                programs[i][0] = programs[i][1];
                programs[i][1] = t;
            }
        }

        Arrays.sort(programs, (((o1, o2) -> o1[0] - o2[0] != 0 ? o1[0] - o2[0] : o1[1] - o2[1] != 0 ? o1[1] - o2[1] : o1[2] - o2[2])));
        cache.clear();//清空缓存，避免之前计算的结果干扰
        return minCost2(programs, 0, nums, 0);
    }

    //nums：需要选择的项目的个数
    //status：表示乐队的选中状态
    public static int minCost2(int[][] programs, int index, int nums, int status) {
        if (nums == 0) {
            return 0;
        } else if (index == programs.length) {
            return -1;
        }
        Integer key = status * programs.length + index;//生成缓存键：用status和index组合唯一标识当前状态
        Integer ans = cache.get(key);
        if (ans != null) {
            return ans;
        } else {
            ans = Integer.MAX_VALUE;
        }
        int next = index + 1;
        while (next < programs.length && programs[index][0] == programs[next][0]) {
            next++;
        }
        //检查当前项目的一个乐队是否已经被选中
        if ((status & (1 << programs[index][0])) != 0) {
            return minCost2(programs, next, nums, status);
        }
        status |= 1 << programs[index][0];//标记第一个乐队为已选中状态
        for (int i = index; i < next; i++) {
            if (i - 1 >= index && programs[i][1] == programs[i - 1][1]) {
                continue;
            }
            //判断当前项目的一个乐队是否已经被选中
            if ((status & (1 << programs[i][1])) == 0) {
                int t = minCost2(programs, next, nums - 1, status | 1 << programs[i][1]);
                if (t != -1) {
                    ans = Math.min(ans, t + programs[i][2]);
                }
            }
        }
        ans = ans == Integer.MAX_VALUE ? -1 : ans;
        cache.put(key, ans);//将当前的状态加入缓存中
        return ans;
    }

    public static int right(int[][] programs, int nums) {
        min = Integer.MAX_VALUE;
        r(programs, 0, nums, 0, 0);
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    public static int min = Integer.MAX_VALUE;

    public static void r(int[][] programs, int index, int rest, int pick, int cost) {
        if (rest == 0) {
            min = Math.min(min, cost);
        } else {
            if (index < programs.length) {
                r(programs, index + 1, rest, pick, cost);
                int cur = (1 << programs[index][0]) | (1 << programs[index][1]);
                if ((pick & cur) == 0) {
                    r(programs, index + 1, rest - 1, pick | cur, cost + programs[index][2]);
                }
            }
        }
    }

    //N ：决定了乐队的总数量
    //v ：决定了每个项目的成本范围
    public static int[][] randomPrograms(int N, int V) {
        int nums = N << 1;
        int n = nums * (nums - 1);//计算项目的总数量
        int[][] programs = new int[n][3];
        for (int i = 0; i < n; i++) {
            int a = (int) (Math.random() * nums);
            int b = 0;
            do {
                b = (int) (Math.random() * nums);
            } while (b == a);
            programs[i][0] = a;
            programs[i][1] = b;
            programs[i][2] = (int) (Math.random() * V) + 1;
        }
        return programs;
    }

    public static int[][] copyPrograms(int[][] programs) {
        int n = programs.length;
        int[][] ans = new int[n][3];
        for (int i = 0; i < n; i++) {
            ans[i][0] = programs[i][0];
            ans[i][1] = programs[i][1];
            ans[i][2] = programs[i][2];
        }
        return ans;
    }

    public static void main(String[] args) {
        int N = 4;
        int V = 100;
        int T = 1000;
        System.out.println("test begin!");
        for (int i = 0; i < T; i++) {
            int nums = (int) (Math.random() * N) + 1;
            int[][] programs1 = randomPrograms(nums, V);
            int[][] programs2 = copyPrograms(programs1);
            int[][] programs3 = copyPrograms(programs1);
            int ans1 = right(programs1, nums);
            int ans2 = minCost1(programs2, nums);
            int ans3 = minCost2(programs3, nums);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Oops");
                break;
            }
        }
        System.out.println("test finish!");

        long start;
        long end;
        int[][] programs;

        programs = randomPrograms(7, V);
        start = System.currentTimeMillis();
        right(programs, 7);
        end = System.currentTimeMillis();
        System.out.println("right方法，在nums=7时候的运行时间（毫秒): " + (end - start));

        programs = randomPrograms(10, V);
        start = System.currentTimeMillis();
        minCost1(programs, 10);
        end = System.currentTimeMillis();
        System.out.println("minCost1方法，在nums=7时候的运行时间（毫秒): " + (end - start));

        programs = randomPrograms(10, V);
        start = System.currentTimeMillis();
        minCost2(programs, 10);
        end = System.currentTimeMillis();
        System.out.println("minCost2方法，在nums=7时候的运行时间（毫秒): " + (end - start));
    }
}

package Class_2021_12_3;

import java.util.Arrays;
import java.util.List;

//你被请来给一个要举办高尔夫比赛的树林砍树。树林由一个m x n 的矩阵表示， 在这个矩阵中：
//0 表示障碍，无法触碰
//1表示地面，可以行走
//比1大的数表示有树的单元格，也可以行走，数值表示树的高度
//每一步，你都可以向上、下、左、右四个方向之一移动一个单位，如果你站的地方有一棵树，那么你可以决定是否要砍倒它。
//你需要按照树的高度从低向高砍掉所有的树，每砍过一颗树，该单元格的值变为 1（即变为地面）。
//你将从 (0, 0) 点开始工作，返回你砍完所有树需要走的最小步数。 如果你无法砍完所有的树，返回 -1 。
//可以保证的是，没有两棵树的高度是相同的，并且你至少需要砍倒一棵树
//Leetcode链接 : https://leetcode.com/problems/cut-off-trees-for-golf-event/
public class Code04_CutOffTreesForGolfEvent {

    public static int MAXN = 51;//最大行数
    public static int MAXM = 51;//最大列数
    public static int LIMIT = MAXM * MAXN;//队列最大容量
    public static int[][] f = new int[MAXN][MAXM];//标记访问状态矩阵
    public static int[][] arr = new int[LIMIT][3];//存储树的信息（高度、x坐标、y坐标）
    public static int[] move = new int[]{1, 0, -1, 0, 1};//方向数组
    public static int n, m;//实际的行列数
    public static int[][] deque = new int[LIMIT][3];//双端队列
    public static int l, r, size;//双端队列的队列指针和队列的大小
    //队列初始化
    public static void buildDeque() {
        l = -1;
        r = -1;
        size = 0;
    }
    //从队列头取元素
    public static int[] pollFirst() {
        int[] ans = deque[l]; //获取队列头部元素
        if (l < LIMIT - 1) {
            l++;
        } else {
            l = 0;
        }
        size--;
        //队空了，l指向r的前一个元素
        if (size == 0) {
            l = r - 1;
        }
        return ans;
    }

    public static void offerFirst(int x, int y, int d) {
        if (l == -1) {
            deque[0][0] = x;
            deque[0][1] = y;
            deque[0][2] = d;
            l = r = 0;
        } else {
            //计算插入的位置，插在对头的前一个位置
            int fill = l == 0 ? (LIMIT - 1) : (l - 1);
            deque[fill][0] = x;
            deque[fill][1] = y;
            deque[fill][2] = d;
            l = fill;
        }
        size++;
    }

    public static void offerLast(int x, int y, int d) {
        if (l == -1) {
            deque[0][0] = x;
            deque[0][1] = y;
            deque[0][2] = d;
            l = r = 0;
        } else {
            int fill = (r == LIMIT - 1) ? 0 : (r + 1);
            deque[fill][0] = x;
            deque[fill][1] = y;
            deque[fill][2] = d;
            r = fill;
        }
        size++;
    }

    public static int cutOffTree(List<List<Integer>> forest) {
        n = forest.size();
        m = forest.get(0).size();
        int cnt = 0;//树的数量计数器
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int value = forest.get(i).get(j);
                f[i][j] = value > 0 ? 1 : 0;//标记是可通行还是碰见了障碍物
                if (value > 1) {
                    arr[cnt][0] = value;
                    arr[cnt][1] = i;
                    arr[cnt++][2] = j;
                }
            }
        }
        //按照树的高度进行排序
        Arrays.sort(arr, 0, cnt, (a, b) -> a[0] - b[0]);
        int ans = 0;
        //按树高顺序处理每棵树，串联路径计算
        for (int i = 0, x = 0, y = 0, block = 2; i < cnt; i++, block++) {
            int toX = arr[i][1];
            int toY = arr[i][2];
            //从当前位置到目标树的最短路径
            int step = walk(x, y, toX, toY, block);
            if (step == -1) {
                return -1;
            }
            ans += step;
            x = toX;
            y = toY;
        }
        return ans;
    }

    public static int walk(int a, int b, int c, int d, int block) {
        buildDeque();//初始化队列
        offerFirst(a, b, 0);//从起点入队
        while (size > 0) {
            int[] cur = pollFirst();//取出队列头的元素
            int x = cur[0];
            int y = cur[1];
            int distance = cur[2];
            if (f[x][y] != block) {
                f[x][y] = block;
                if (x == c && y == d) {
                    return distance;
                }
                //尝试4个方向移动
                for (int i = 1; i < 5; i++) {
                    int nextX = x + move[i];
                    int nextY = y + move[i - 1];
                    if (nextX >= 0 && nextX < n && nextY >= 0 && nextY < m && f[nextX][nextY] != 0
                            && f[nextX][nextY] != block) {
                        if ((i == 1 && y < d) || (i == 2 && x > c) || (i == 3 && y > d) || (i == 4 && x < c)) {
                            offerFirst(nextX, nextY, distance + 1);//优先级高，插入对头
                        } else {
                            offerLast(nextX, nextY, distance + 1);//优先级低，插入队尾
                        }
                    }
                }
            }
        }
        return -1;
    }
}

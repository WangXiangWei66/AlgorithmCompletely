package Class11;

public class Code07_paperFolding {

    public static void printAllFolds(int N) {
        process(1, N, true);
        System.out.println();
    }

    //当前你来了一个节点，脑海中想象的
    //这个节点在第I层，一共右N层，N固定不变
    //这个节点如果是凹的话，down=true
    //这个节点如果是凸的话，down=False
    //函数的功能：中序打印以你想象的节点为头的整棵树
    public static void process(int i, int N, boolean down) {
        if (i > N) {
            return;
        }
        process(i + 1, N, true);
        System.out.println(down ? "凹" : "凸");
        process(i + 1, N, false);
    }

    public static void main(String[] args) {
        int N = 4;
        printAllFolds(N);
    }
}

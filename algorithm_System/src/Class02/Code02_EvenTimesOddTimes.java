package Class02;

public class Code02_EvenTimesOddTimes {
    //在arr中，只有一种数，出现了奇数次

    public static void printOddTimesNum1(int[] arr) {
        //申请一个eor=0的变量，让它依次去异或数组中的每个数，最终得到答案
        //因为偶数个相同的数异或完是0，奇数个相同的数异或完是该数本身
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        System.out.println(eor);
    }

    //在arr中，有两种数，出现了奇数次

    public static void printOddTimesNum2(int[] arr) {
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        //a和b是两种数
        //最终eor=a^b 一定是非零的
        //把eor最右侧的1给他提取出来
        //eor=              00110010110111000
        //rightOne=     00000000000001000
        int rightOne = eor & (~eor + 1);//把eor最右侧的1，提取出来
        int onlyone = 0;//定义开始的时候eor'=0
        for (int i = 0; i < arr.length; i++) {
            //数组中的所有数分为两组：一组该位为 1，另一组该位为 0。
            if (((arr[i] & rightOne) != 0)) {
                onlyone ^= arr[i];
            }
        }
        //一个数通过onlyone达到
        //另一个数通过eor ^ onlyone得到
        System.out.println(onlyone + " " + (eor ^ onlyone));
    }


    //计算一个二进制数中，1的数量
    public static int bit1counts(int N) {
        int count = 0;
        while (N != 0) {
            int rightOne = N & ((~N) + 1);
            count++;
            N -= rightOne;
        }

        return count;
    }

    public static void main(String[] args) {
        int a = 5;
        int b = 7;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println(a);
        System.out.println(b);

        int[] arr1 = {3, 3, 2, 3, 1, 1, 1, 3, 1, 1, 1};
        printOddTimesNum1(arr1);
        int[] arr2 = {4, 3, 4, 2, 2, 2, 4, 1, 1, 1, 3, 3, 1, 1, 1, 4, 2, 2};
        printOddTimesNum2(arr2);
    }
}


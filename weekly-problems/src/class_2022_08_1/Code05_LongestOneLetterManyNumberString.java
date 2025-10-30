package class_2022_08_1;
//给定一个只由小写字母和数字字符组成的字符串str
//要求子串必须只含有一个小写字母，数字字符数量随意
//求这样的子串最大长度是多少
public class Code05_LongestOneLetterManyNumberString {

    public static int right(String s){
        char[]str=s.toCharArray();
        int ans=0;
        for(int i=0;i<str.length;i++){
            for(int j=i;j<str.length;j++){
                if (check(str,i,j)){
                    ans=Math.max(ans,j-i+1);
                }
            }
        }
        return ans;
    }

    public static boolean check(char[]str,int l,int r){
        int letterNumber=0;
        for(int i=l;i<=r;i++){
            if (str[i]>='a'&&str[i]<='z'){
                letterNumber++;
            }
        }
        return letterNumber==1;
    }

    public static int zuo(String s){
        char[]str=s.toCharArray();
        int n=str.length;
        int letters=0;
        int right=0;
        int ans=0;
        for(int left=0;left<n;left++){
            while(right<n){
                if (letters==1&&str[right]>='a'&&str[right]<='z'){
                    break;
                }
                if (str[right]>='a'&&str[right]<='z'){
                    letters++;
                }
                right++;
            }
            if (letters==1){
                ans=Math.max(ans,right-left);
            }
            if (str[left]>='a'&&str[left]<='z'){
                letters--;
            }
        }
        return ans;
    }

    public static char[] chars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z' };

    // 为了测试
    public static String randomString(int n) {
        char[] str = new char[n];
        for (int i = 0; i < n; i++) {
            str[i] = chars[(int) (Math.random() * chars.length)];
        }
        return String.valueOf(str);
    }

    // 为了测试
    public static void main(String[] args) {
        int N = 100;
        int testTimes = 10000;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            int n = (int) (Math.random() * N) + 1;
            String str = randomString(n);
            int ans1 = right(str);
            int ans2 = zuo(str);
            if (ans1 != ans2) {
                System.out.println(str);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("测试结束");
    }
}

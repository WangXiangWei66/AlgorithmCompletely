package class_2022_02_2;

//设计位图
//leetcode链接: https://leetcode-cn.com/problems/design-bitset/
public class Code02_DesignBitset {

    class Bitset {
        private int[] bits; //int数组存储位信息
        private final int size;//位图的总位数
        private int zeros;//记录0的数量
        private int ones;//记录1的数量
        private boolean reverse;//是否翻转
        //reverse为true的时候，数组实际的存储状态和逻辑状态相反
        public Bitset(int n) {
            bits = new int[(n + 31) / 32];
            size = n;
            zeros = n;
            ones = 0;
            reverse = false;
        }
        //将指定索引的位设置为1
        public void fix(int idx) {
            int index = idx / 32;
            int bit = idx % 32;
            if (!reverse) {
                if ((bits[index] & (1 << bit)) == 0) {
                    zeros--;
                    ones++;
                    bits[index] |= (1 << bit);
                }
            } else {
                if ((bits[index] & (1 << bit)) != 0) {
                    zeros--;
                    ones++;
                    bits[index] ^= (1 << bit);
                }
            }
        }

        public void unfix(int idx) {
            int index = idx / 32;//计算索引
            int bit = idx % 32;//计算位位置
            if (!reverse) {
                if ((bits[index] & (1 << bit)) != 0) {
                    ones--;
                    zeros++;
                    bits[index] ^= (1 << bit);
                }
            } else {
                if ((bits[index] & (1 << bit)) == 0) {
                    ones--;
                    zeros++;
                    bits[index] |= (1 << bit);
                }
            }
        }

        public void flip() {
            reverse = !reverse;
            int tmp = zeros;
            zeros = ones;
            ones = tmp;
        }

        public boolean all() {
            return ones == size;
        }

        public boolean one() {
            return ones > 0;
        }

        public int count() {
            return ones;
        }
        //返回位图的字符串表示
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                int status = bits[i / 32] & (1 << (i % 32));
                builder.append(reverse ? (status == 0 ? '1' : '0') : (status == 0 ? '0' : '1'));
            }
            return builder.toString();
        }
    }
}

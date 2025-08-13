package Class34;

import java.util.HashMap;

//设计一个支持在平均时间复杂度 O(1)下，执行以下操作的数据结构。
//insert(val)：当元素 val 不存在时，向集合中插入该项。
//remove(val)：元素 val 存在时，从集合中移除该项。
//getRandom：随机返回现有集合中的一项。每个元素应该有相同的概率被返回。
//Leetcode题目 : https://leetcode.com/problems/insert-delete-getrandom-o1/
public class Problem_0380_InsertDeleteGetRandom {

    public class RandomizedSet {

        private HashMap<Integer, Integer> keyIndexMap;//值到索引的映射
        private HashMap<Integer, Integer> indexKeyMap;//索引到值的映射
        private int size;//当前集合中元素数量

        public RandomizedSet() {
            keyIndexMap = new HashMap<Integer, Integer>();
            indexKeyMap = new HashMap<Integer, Integer>();
            size = 0;
        }

        public boolean insert(int val) {
            if (!keyIndexMap.containsKey(val)) {
                keyIndexMap.put(val, size);
                indexKeyMap.put(size++, val);
                return true;
            }
            return false;
        }

        public boolean remove(int val) {
            if (keyIndexMap.containsKey(val)) {
                int deleteIndex = keyIndexMap.get(val);
                int lastIndex = --size;//获取最后一个元素的索引
                int lastKey = indexKeyMap.get(lastIndex);
                keyIndexMap.put(lastKey, deleteIndex);//将最后一个元素移动到要删除元素的位置
                indexKeyMap.put(deleteIndex, lastKey);
                keyIndexMap.remove(val);
                indexKeyMap.remove(lastIndex);
                return true;
            }
            return false;
        }

        public int getRandom() {
            if (size == 0) {
                return -1;
            }
            int randomIndex = (int) (Math.random() * size);
            return indexKeyMap.get(randomIndex);
        }
    }
}

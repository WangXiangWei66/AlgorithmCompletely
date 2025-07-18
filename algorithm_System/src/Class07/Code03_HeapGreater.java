package Class07;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Code03_HeapGreater {


    public static class HeapGreater<T> {
        private ArrayList<T> heap;//使用 ArrayList 实现的堆数组，存储所有元素
        private HashMap<T, Integer> indexMap;// 哈希表，用于快速查找元素在堆中的位置
        private int heapSize;//堆的当前大小
        //泛型通配符 < ? super T >
        private Comparator<? super T> comp;//比较器，决定了堆的排序规则

        public HeapGreater(Comparator<? super T> c) {
            heap = new ArrayList<>();
            indexMap = new HashMap<>();
            heapSize = 0;
            comp = c;
        }

        public boolean isEmpty() {
            return heapSize == 0;
        }

        public int size() {
            return heapSize;
        }

        public boolean contains(T obj) {
            return indexMap.containsKey(obj);
        }

        public T peek() {
            return heap.get(0);
        }

        public void push(T obj) {
            heap.add(obj);
            indexMap.put(obj, heapSize);
            heapInsert(heapSize++);
        }

        public T pop() {
            T ans = heap.get(0);
            swap(0, heapSize - 1);
            indexMap.remove(ans);
            heap.remove(--heapSize);
            heapify(0);
            return ans;
        }

        //获取堆中最后一个元素为替换元素
        public void remove(T obj) {
            T replace = heap.get(heapSize - 1);
            int index = indexMap.get(obj);
            indexMap.remove(obj);
            heap.remove(--heapSize);
            if (obj != replace) {
                heap.set(index, replace);
                indexMap.put(replace, index);
                resign(replace);
            }
        }

        public void resign(T obj) {
            heapInsert(indexMap.get(obj));
            heapify(indexMap.get(obj));
        }

        //请返回堆上的所有元素
        public List<T> getAllElements() {
            List<T> ans = new ArrayList<>();
            for (T c : heap) {
                ans.add(c);
            }
            return ans;
        }

        private void heapInsert(int index) {
            while (comp.compare(heap.get(index), heap.get((index - 1) / 2)) < 0) {
                swap(index, (index - 1) / 2);
                index = (index - 1) / 2;
            }
        }

        private void heapify(int index) {
            int left = index * 2 + 1;
            while (left < heapSize) {
                int best = left + 1 < heapSize && comp.compare(heap.get(left + 1), heap.get(left)) < 0 ? (left + 1) : left;
                best = comp.compare(heap.get(best), heap.get(index)) < 0 ? best : index;
                if (best == index) {
                    break;
                }
                swap(best, index);
                index = best;
                left = index * 2 + 1;
            }
        }

        private void swap(int i, int j) {
            T o1 = heap.get(i);
            T o2 = heap.get(j);
            heap.set(i, o2);
            heap.set(j, o1);
            indexMap.put(o2, i);
            indexMap.put(o1, j);
        }
    }
}

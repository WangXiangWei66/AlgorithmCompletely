package Class14;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;
//SizeMap中有几条记录，便有几个集合
public class Code05_UnionFind {

    //用map实现的并查集
    public static class UnionFind<V> {
        public HashMap<V, V> father;
        public HashMap<V, Integer> size;

        public UnionFind(List<V> values) {
            father = new HashMap<>();
            size = new HashMap<>();
            for (V cur : values) {
                father.put(cur, cur);
                size.put(cur, 1);
            }
        }

        //给你一个节点，请你往上到不能再往上，把代表节点返回
        //实现了路径压缩
        public V findFather(V cur) {
            Stack<V> path = new Stack<>();
            while (cur != father.get(cur)) {
                path.push(cur);
                cur = father.get(cur);
            }
            //将路径上的所有节点都直接指向根节点
            while (!path.isEmpty()) {
                father.put(path.pop(), cur);
            }
            return cur;
        }
        //查询两个样本是否在同一个集合
        public boolean isSameSet(V a, V b) {
            return findFather(a) == findFather(b);//用==关心的是同一个节点的内存地址，不用管里面的值
        }
        //将A所在的集合全体，和b所在的集合全体合成一个集合
        public void union(V a, V b) {
            V aFather = findFather(a);
            V bFather = findFather(b);
            if (aFather != bFather) {
                int aSize = size.get(aFather);
                int bSize = size.get(bFather);
                if (aSize >= bSize) {
                    father.put(bFather, aFather);
                    size.put(aFather, aSize + bSize);
                    size.remove(bFather);
                } else {
                    father.put(aFather, bFather);
                    size.put(bFather, aSize + bSize);
                    size.remove(aFather);
                }
            }
        }

        public int sets() {
            return size.size();
        }
    }
}

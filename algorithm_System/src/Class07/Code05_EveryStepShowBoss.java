package Class07;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Code05_EveryStepShowBoss {

    public static class Customer {
        public int id;
        public int buy;
        public int enterTime;

        public Customer(int v, int b, int o) {
            id = v;
            buy = b;
            enterTime = 0;
        }
    }

    public static class CandidateComparator implements Comparator<Customer> {

        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.buy != o2.buy ? (o2.buy - o1.buy) : (o1.enterTime - o2.enterTime);
        }
    }

    public static class DaddyComparator implements Comparator<Customer> {

        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.buy != o2.buy ? (o1.buy - o2.buy) : (o1.enterTime - o2.enterTime);
        }
    }

    public static class WhosYourDaddy {
        //快速查找客户ID和对应的客户对象
        private HashMap<Integer, Customer> customers;
        //存储候选区的用户
        private Code03_HeapGreater.HeapGreater<Customer> candHeap;
        //存储得奖区的用户
        private Code03_HeapGreater.HeapGreater<Customer> daddyHeap;
        //得奖区中元素数的限制
        private final int daddyLimit;

        public WhosYourDaddy(int limit) {
            customers = new HashMap<Integer, Customer>();
            candHeap = new Code03_HeapGreater.HeapGreater<>(new CandidateComparator());
            daddyHeap = new Code03_HeapGreater.HeapGreater<>(new DaddyComparator());
            daddyLimit = limit;
        }

        //当前处理i号事件，arr[i]->id,buyOrRefund
        public void operate(int time, int id, boolean buyOrRefund) {
            if (!buyOrRefund && !customers.containsKey(id)) {
                return;
            }
            if (!customers.containsKey(id)) {
                customers.put(id, new Customer(id, 0, 0));
            }
            Customer c = customers.get(id);
            if (buyOrRefund) {
                c.buy++;
            } else {
                c.buy--;
            }
            if (c.buy == 0) {
                customers.remove(id);
            }
            if (!candHeap.contains(c) && !daddyHeap.contains(c)) {
                if (daddyHeap.size() < daddyLimit) {
                    c.enterTime = time;
                    daddyHeap.push(c);
                } else {
                    c.enterTime = time;
                    candHeap.push(c);
                }
            } else if (candHeap.contains(c)) {
                if (c.buy == 0) {
                    candHeap.remove(c);
                } else {
                    candHeap.resign(c);
                }
            } else {
                if (c.buy == 0) {
                    daddyHeap.remove(c);
                } else {
                    daddyHeap.resign(c);
                }
            }
            daddyMove(time);
        }

        public List<Integer> getDaddies() {
            List<Customer> customers = daddyHeap.getAllElements();
            List<Integer> ans = new ArrayList<>();
            for (Customer c : customers) {
                ans.add(c.id);
            }
            return ans;
        }

        private void daddyMove(int time) {
            if (candHeap.isEmpty()) {
                return;
            }
            if (daddyHeap.size() < daddyLimit) {
                Customer p = candHeap.pop();
                p.enterTime = time;
                daddyHeap.push(p);
            } else {
                if (candHeap.peek().buy > daddyHeap.peek().buy) {
                    Customer oldDaddy = daddyHeap.pop();
                    Customer newDaddy = candHeap.pop();
                    oldDaddy.enterTime = time;
                    newDaddy.enterTime = time;
                    daddyHeap.push(newDaddy);
                    candHeap.push(oldDaddy);
                }
            }
        }
    }

    //外层表示整个操作的时间线
    //内层表示一次操作的TopK结果
    public static List<List<Integer>> topK(int[] arr, boolean[] op, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        WhosYourDaddy whoDaddies = new WhosYourDaddy(k);
        for (int i = 0; i < arr.length; i++) {
            whoDaddies.operate(i, arr[i], op[i]);
            ans.add(whoDaddies.getDaddies());
        }
        return ans;
    }


    //干完所有的事，模拟，不优化
    public static List<List<Integer>> compare(int[] arr, boolean[] op, int k) {
        HashMap<Integer, Customer> map = new HashMap<>();
        ArrayList<Customer> cands = new ArrayList<>();
        ArrayList<Customer> daddy = new ArrayList<>();
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            int id = arr[i];
            boolean buyOrRefund = op[i];
            if (!buyOrRefund && !map.containsKey(id)) {
                ans.add(getCurAns(daddy));
                continue;
            }
            //没有发生，用户购买数为0并且又退货了
            //用户之前购买数为零，此时买货事件
            //用户之前购买数大于零，此时买货
            //用户之前购买数大于零，此时退货
            if (!map.containsKey(id)) {
                map.put(id, new Customer(id, 0, 0));
            }
            //买，卖
            Customer c = map.get(id);
            if (buyOrRefund) {
                c.buy++;
            } else {
                c.buy--;
            }
            if (c.buy == 0) {
                map.remove(id);
            }
            //c
            //下面做
            if (!cands.contains(c) && !daddy.contains(c)) {
                if (daddy.size() < k) {
                    c.enterTime = i;
                    daddy.add(c);
                } else {
                    c.enterTime = i;
                    cands.add(c);
                }
            }
            cleanZeroBuy(cands);
            cleanZeroBuy(daddy);
            cands.sort(new CandidateComparator());
            daddy.sort(new DaddyComparator());
            move(cands, daddy, k, i);
            ans.add(getCurAns(daddy));
        }
        return ans;
    }

    public static void move(ArrayList<Customer> cands, ArrayList<Customer> daddy, int k, int time) {
        if (cands.isEmpty()) {
            return;
        }
        //候选区不为空
        if (daddy.size() < k) {
            Customer c = cands.get(0);
            c.enterTime = time;
            daddy.add(c);
            cands.remove(0);
        } else {//得奖区满了，候选区有东西
            if (cands.get(0).buy > daddy.get(0).buy) {
                Customer oldDaddy = daddy.get(0);
                daddy.remove(0);
                Customer newDaddy = cands.get(0);
                cands.remove(0);
                newDaddy.enterTime = time;
                oldDaddy.enterTime = time;
                daddy.add(newDaddy);
                cands.add(oldDaddy);
            }
        }
    }

    public static void cleanZeroBuy(ArrayList<Customer> arr) {
        List<Customer> noZero = new ArrayList<Customer>();
        for (Customer c : arr) {
            if (c.buy != 0) {
                noZero.add(c);
            }
        }
        arr.clear();//用来清空集合中的所有元素
        for (Customer c : noZero) {
            arr.add(c);
        }
    }

    public static List<Integer> getCurAns(ArrayList<Customer> daddy) {
        List<Integer> ans = new ArrayList<>();
        for (Customer c : daddy) {
            ans.add(c.id);
        }
        return ans;
    }

    //为了测试
    public static class Data {
        public int[] arr;
        public boolean[] op;

        public Data(int[] a, boolean[] o) {
            arr = a;
            op = o;
        }
    }

    public static Data randomData(int maxValue, int maxLen) {
        int len = (int) (Math.random() * maxLen) + 1;
        int[] arr = new int[len];
        boolean[] op = new boolean[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * maxValue);
            op[i] = Math.random() < 0.5 ? true : false;
        }
        return new Data(arr, op);
    }

    public static boolean sameAnswer(List<List<Integer>> ans1, List<List<Integer>> ans2) {
        if (ans1.size() != ans2.size()) {
            return false;
        }
        for (int i = 0; i < ans1.size(); i++) {
            List<Integer> cur1 = ans1.get(i);
            List<Integer> cur2 = ans2.get(i);
            if (cur1.size() != cur2.size()) {
                return false;
            }
            cur1.sort((a, b) -> (a - b));
            cur2.sort((a, b) -> (a - b));
            for (int j = 0; j < cur1.size(); j++) {
                if (!cur1.get(j).equals(cur2.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

        public static void main (String[]args){
            int maxValue = 10;
            int maxLen = 10;
            int maxK = 3;
            int testTime = 1000000;
            System.out.println("测试开始!");
            for (int i = 0; i < testTime; i++) {
                Data testData = randomData(maxValue, maxLen);
                int K = (int) (Math.random() * maxK) + 1;
                int[] arr = testData.arr;
                boolean[] op = testData.op;
                List<List<Integer>> ans1 = topK(arr, op, K);
                List<List<Integer>> ans2 = compare(arr, op, K);
                if (!sameAnswer(ans1, ans2)) {
                    for (int j = 0; j < arr.length; j++) {
                        System.out.println(arr[j] + "," + op[j]);
                    }
                    System.out.println(K);
                    System.out.println(ans1);
                    System.out.println(ans2);
                    System.out.println("出错了!");
                    break;
                }
            }
            System.out.println("测试结束!");
        }
    }

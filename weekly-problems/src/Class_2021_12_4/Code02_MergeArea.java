package Class_2021_12_4;

import java.util.HashMap;
import java.util.List;
// 来自北京北明数科信息技术有限公司
// area表示的是地区全路径,最多可能有6级,用分隔符连接,分隔符是 spliter,
// 分隔符是逗号
// 例如：
// area = 中国,四川,成都  或者  中国,浙江,杭州  或者  中国,浙江,义乌
// spliter = ,
// count表示门店数
// class AreaResource {
//     String area;
//     String spliter;
//     long count;
// }

// area = "中国,四川,成都"
// spliter = ","
// count = 10
// 现在需要把  List<AreaResource> 进行字符串转换，供下游处理，需要做到同级的地域能合并
// 比如
// area为 中国,四川,成都  有10个门店
//        中国,浙江,杭州 有25个门店
//        中国,浙江,义乌 有22个门店
//        中国,四川,成都 有25个门店
// spliter为逗号 "," 最终转化成JSON的形式，并且同级的地域需要被合并，最终生成的JSON字符串如下所示
//
// 返回: {
//    "中国":
//           {"四川":{"成都":35]},
//            "浙江":{"义乌":22,"杭州":25}}}
// 请实现下面的方法 public String mergeCount(List<AreaResource> areas)
public class Code02_MergeArea {
    //存储单个地区的信息
    public static class AreaResource {
        public String area;//地区的全路径
        public String spliter;//路径的分隔符
        public long count;//地区的门店数量

        public AreaResource(String area, long count, String spliter) {
            this.area = area;
            this.count = count;
            this.spliter = spliter;
        }
    }

    public static String mergeCount(List<AreaResource> areas) {
        Area all = new Area("", 0);//所有地区的顶级容器
        for (AreaResource r : areas) {
            String[] path = r.area.split(r.spliter);//将地区按照分隔符拆分
            long count = r.count;
            f(path, 0, all, count);
        }
        return all.toString();
    }

    public static void f(String[] path, int index, Area pre, long count) {
        if (index == path.length) {
            pre.count += count;
        } else {
            String cur = path[index];
            if (!pre.next.containsKey(cur)) {
                pre.next.put(cur, new Area(cur, 0));
            }
            f(path, index + 1, pre.next.get(cur), count);
        }
    }
    //构建地域层级的辅助类
    public static class Area {
        public String name;//当前地区名称
        public HashMap<String, Area> next;//存储子地区的Map，（键为地区名，值为对应的Area对象）
        public long count;//该地区的门店总数

        public Area(String name, long count) {
            this.name = name;
            this.count = count;
            next = new HashMap<>();
        }

        public String toString() {
            StringBuilder ans = new StringBuilder();
            //非根节点需要添加名称
            if (!name.equals("")) {
                ans.append("\"" + name + "\"" + ":");
            }
            if (next.isEmpty()) {
                ans.append(count);
            } else {
                ans.append("{");
                for (Area child : next.values()) {
                    ans.append(child.toString() + ",");
                }
                //将最后一个逗号替换成右括号
                ans.setCharAt(ans.length() - 1, '}');
            }
            return ans.toString();
        }
    }
}
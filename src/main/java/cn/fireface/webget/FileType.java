package cn.fireface.webget;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Create by maoyi on 2018/11/23
 * don't worry be happy!
 * @author maoyi
 */
public class FileType {

    private static Set<String> keySet = new HashSet<String>();
    static {
        keySet.add("java");
        keySet.add("spring");
        keySet.add("redis");
        keySet.add("数据");
        keySet.add("分布式");
        keySet.add("Hadoop");
        keySet.add("Zookeeper");
        keySet.add("Elasticsearch");
        keySet.add("MySQL");
        keySet.add("hbase");
    }

    public static String createFileName(String articleName){
        AtomicReference<String> fileName = new AtomicReference<>("all.txt");
        keySet.forEach((key)->{
            if (articleName.toLowerCase().contains(key.toLowerCase())) {
                fileName.set(key + ".txt");
            }
        });
        return fileName.get();
    }
}

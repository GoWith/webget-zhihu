package cn.fireface.webget.aliyun;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

public class AliYunPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    private Set<String> used = new HashSet<String>(1000);

    @Override
    public void process(Page page) {
        if (used.contains(page.getUrl().get().split("#")[0])) {
            return;
        }else{
            used.add(page.getUrl().get().split("#")[0]);
        }
        page.addTargetRequests(page.getHtml().links().regex("(\\S*aliyun\\.com/\\S*)").all());
        try {
            String articleName = page.getHtml().$(".blog-title").xpath("/h1/text()").get();
            if (articleName == null || articleName.trim().length() == 0) {
                return;
            }
            String dir_name = "aliyun";
            if (!(new File(dir_name).isDirectory())) {
                new File(dir_name).mkdir();
            }
            File file = new File(dir_name + File.separator + "all.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            fileOutputStream.write((articleName + "\t" + page.getUrl().get()+"\n").getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Spider.create(new AliYunPageProcessor()).addUrl("https://yq.aliyun.com/ziliao/342037").thread(5).run();

    }
}

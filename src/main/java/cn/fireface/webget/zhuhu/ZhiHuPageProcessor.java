package cn.fireface.webget.zhuhu;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZhiHuPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    private Set<String> used = new HashSet<String>(1000);

    @Override
    public void process(Page page) {
        if (used.contains(page.getUrl().get())) {
            return;
        }
        page.addTargetRequests(page.getHtml().links().regex("(\\S*zhihu\\.com/\\S*)").all());
        try {
//            System.out.println(page.getHtml().toString());
            String questionName = page.getHtml().$(".QuestionHeader-title").xpath("/h1/text()").get();
            String meta = page.getHtml().$(".QuestionAnswer-content").xpath("/div/div/[2]").get();
            String context = meta.substring(4, meta.length() - 1);
            if (context.contains("upvoteCount")) {
                String count = context.substring(context.lastIndexOf("=") + 2, context.length() - 1);
                Integer integer = Integer.valueOf(count);
                if(integer > 500){
                    String dir_name = "question";  // 这里定义了截图存放目录名
                    if (!(new File(dir_name).isDirectory())) {  // 判断是否存在该目录
                        new File(dir_name).mkdir();  // 如果不存在则新建一个目录
                    }
                    File file = new File(dir_name + File.separator + "hot.txt");
                    FileOutputStream fileOutputStream = new FileOutputStream(file,true);
                    fileOutputStream.write((questionName + "\t" + page.getUrl().get()+"\n").getBytes());
                    fileOutputStream.close();
                    String s = page.getHtml().$(".QuestionAnswer-content").xpath("/div/div/div[2]/div[1]/").get();
                    SendMailService.sendByHtmlMail(questionName,s);
                    used.add(page.getUrl().get());
                }
            }
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
        Spider.create(new ZhiHuPageProcessor()).addUrl("https://www.zhihu.com/question/26350691/answer/42692522?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io").thread(5).run();

    }
}

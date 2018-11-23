package cn.fireface.webget.zhuhu;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * Created by maoyi on 2016/10/21.
 */
public class SendMailService {
    private static HtmlEmail email = new HtmlEmail();
    static {
        email.setCharset("UTF-8");
        email.setHostName("smtp.qq.com");
        email.setSmtpPort(587);
        email.setAuthentication("352399286@qq.com", "rfphunquyksibgde");
        email.setSSL(true);
        try {
            email.setFrom("352399286@qq.com");
            email.addTo("352399286@qq.com");
        } catch (EmailException e) {
            e.printStackTrace();
        }

    }

    public static synchronized void sendByHtmlMail(String subject,String content) throws Exception {
        email.setSubject(subject);
        email.setHtmlMsg(content);
        email.send();
    }
}

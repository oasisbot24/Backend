package oasisbot24.oasisapi.service.impl;

import oasisbot24.oasisapi.service.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Autowired
    JavaMailSender emailSender;

    public static final String authKey = UUID.randomUUID().toString();

    @Value("${properties.AdminMail.id}")
    private String fromEmail;

    private MimeMessage createMessage(String to)throws Exception{
        System.out.println("보내는 대상 : " + to);
        System.out.println("인증 번호 : " + authKey);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("Oasis 회원가입 인증 메일");//제목

        String msg="";
        msg += "<div style='margin:40px;'>";
        msg += "<h1 align='center'>Oasis 이메일 인증</h1>";
        msg += "<br>";
        msg += "<p align='center'>아래의 가입 완료 버튼을 클릭하여 가입을 완료해주세요!</p>";
        msg += "<p align='center'>감사합니다.</p>";
        msg += "<br>";
        msg += "<div style='font-size:130%' align='center'>";
        msg += "<a href=\"http://oasisbot24.com:8080/api/v1/signup?email=" + to + "&auth=" + authKey + "\" ";
        msg += "style='background-color: #405cf5;border-radius: 6px;color: #fff;";
        msg += "margin: 12px 0 0;padding: 10px 25px;position: relative;";
        msg += "text-align: center;width: auto;text-decoration: none;'>가입 완료</a>";
        msg += "</div>";
        message.setText(msg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress(fromEmail,"Oasis"));//보내는 사람

        return message;
    }

    @Override
    public String sendSimpleMessage(String to) throws Exception {
        // TODO Auto-generated method stub
        MimeMessage message = createMessage(to);
        try { //예외처리
            emailSender.send(message);
        } catch(MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return authKey;
    }
}

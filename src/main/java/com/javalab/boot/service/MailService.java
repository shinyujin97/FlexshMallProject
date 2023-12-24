package com.javalab.boot.service;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;  // JavaMailSender 라이브러리 가 가지고 있는 것?
    private static final String senderEmail= "ssy66822@gmail.com";
    private static int number;


    // 램던 숫자 생성
    public static void createNumber(){
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }


    // 클라이언트에게 전송된 문서의 종류를 알린다. 파일 변환을 위한 포맷 MimeMessage 메일 타입 이라고 생각하면 될듯
    // 첨부된 파일을 텍스트 문자 형태로 변환해서 이메일과 함께 전송하기 위한 포맷이다.
    public MimeMessage CreateMail(String mail){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            // 발신자 이메일 주소 설정
            message.setFrom(senderEmail);


            message.setRecipients(MimeMessage.RecipientType.TO, mail);

            // 이메일 제목 and 본문 내용
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";

            // 이메일 본문을 HTML로 설정
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // 생성된 MimeMessage 리턴
        return message;
    }

    public int sendMail(String mail){

        MimeMessage message = CreateMail(mail);

        // 스프링에서 제공하는 웹 어플리케이션에서 메일을 보다 손쉽게 보낼 수 있도록 해주는 API
        javaMailSender.send(message);

        return number;
    }
}

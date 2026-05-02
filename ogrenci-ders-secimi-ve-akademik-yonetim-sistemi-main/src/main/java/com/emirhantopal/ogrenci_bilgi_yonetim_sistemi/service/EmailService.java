package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        String subject = "KTÜ BYS - Şifre Sıfırlama Talebi";
        String resetUrl = "file:///C:/Users/Emirhan/Desktop/ileri%20web%20proje/obs-frontend-html/reset-password.html?token=" + token;
        
        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: 0 auto; border: 1px solid #e2e8f0; border-radius: 10px;'>" +
                             "<h2 style='color: #2563eb; text-align: center;'>KTÜ BYS Şifre Sıfırlama</h2>" +
                             "<p style='color: #475569; font-size: 16px;'>Merhaba,</p>" +
                             "<p style='color: #475569; font-size: 16px;'>Hesabınız için şifre sıfırlama talebinde bulundunuz. Yeni şifrenizi belirlemek için aşağıdaki butona tıklayın:</p>" +
                             "<div style='text-align: center; margin: 30px 0;'>" +
                             "<a href='" + resetUrl + "' style='background-color: #2563eb; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold; font-size: 16px; display: inline-block;'>Şifremi Sıfırla</a>" +
                             "</div>" +
                             "<p style='color: #64748b; font-size: 14px;'>Eğer butona tıklayamıyorsanız, aşağıdaki bağlantıyı kopyalayıp tarayıcınızın adres çubuğuna yapıştırın:</p>" +
                             "<p style='word-break: break-all; background-color: #f1f5f9; padding: 10px; border-radius: 5px; font-size: 12px; color: #475569;'>" + resetUrl + "</p>" +
                             "<p style='color: #94a3b8; font-size: 12px; margin-top: 30px; text-align: center;'>Bu talebi siz yapmadıysanız bu e-postayı dikkate almayın.</p>" +
                             "</div>";
                             
        sendHtmlEmail(to, subject, htmlContent);
    }

    public void sendGradeNotificationEmail(String to, String studentName, String courseName, String examType, Double grade) {
        String subject = "KTÜ BYS - Yeni Not Girişi: " + courseName;
        String examTypeName = examType.equals("MIDTERM") ? "Vize" : "Final";
        
        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: 0 auto; border: 1px solid #e2e8f0; border-radius: 10px; border-top: 5px solid #059669;'>" +
                             "<h2 style='color: #059669; text-align: center;'>Yeni Not Bilgisi</h2>" +
                             "<p style='color: #475569; font-size: 16px;'>Merhaba " + studentName + ",</p>" +
                             "<p style='color: #475569; font-size: 16px;'><strong>" + courseName + "</strong> dersi için yeni bir not sisteme girildi.</p>" +
                             "<div style='background-color: #f0fdf4; padding: 15px; border-radius: 8px; margin: 20px 0; text-align: center;'>" +
                             "<p style='margin: 0; color: #065f46; font-size: 18px;'><strong>" + examTypeName + "</strong> Notunuz</p>" +
                             "<h1 style='margin: 10px 0 0 0; color: #059669; font-size: 36px;'>" + grade + "</h1>" +
                             "</div>" +
                             "<p style='color: #64748b; font-size: 14px; text-align: center;'>Tüm detayları görmek için Öğrenci Paneline giriş yapabilirsiniz.</p>" +
                             "</div>";
                             
        sendHtmlEmail(to, subject, htmlContent);
    }

    public void sendAssignmentNotificationEmail(String to, String studentName, String courseName, String title, String description, LocalDateTime dueDate) {
        String subject = "KTÜ BYS - Yeni Ödev: " + courseName;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedDate = dueDate.format(formatter);
        
        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: 0 auto; border: 1px solid #e2e8f0; border-radius: 10px; border-top: 5px solid #0284c7;'>" +
                             "<h2 style='color: #0284c7; text-align: center;'>Yeni Ödev Eklendi</h2>" +
                             "<p style='color: #475569; font-size: 16px;'>Merhaba " + studentName + ",</p>" +
                             "<p style='color: #475569; font-size: 16px;'><strong>" + courseName + "</strong> dersi için yeni bir ödev tanımlandı.</p>" +
                             "<div style='background-color: #f0f9ff; padding: 15px; border-radius: 8px; margin: 20px 0; border: 1px solid #bae6fd;'>" +
                             "<h3 style='margin-top: 0; color: #0369a1;'>" + title + "</h3>" +
                             "<p style='color: #334155; font-size: 14px; white-space: pre-wrap;'>" + description + "</p>" +
                             "<div style='margin-top: 15px; padding-top: 10px; border-top: 1px solid #bae6fd;'>" +
                             "<span style='color: #e11d48; font-weight: bold; font-size: 14px;'>⏱ Son Teslim: " + formattedDate + "</span>" +
                             "</div>" +
                             "</div>" +
                             "</div>";
                             
        sendHtmlEmail(to, subject, htmlContent);
    }

    public void sendExamNotificationEmail(String to, String studentName, String courseName, String examName, String classroom, LocalDateTime examDate) {
        String subject = "KTÜ BYS - Sınav Tarihi Açıklandı: " + courseName;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedDate = examDate.format(formatter);
        
        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: 0 auto; border: 1px solid #e2e8f0; border-radius: 10px; border-top: 5px solid #d97706;'>" +
                             "<h2 style='color: #d97706; text-align: center;'>Sınav Tarihi Açıklandı</h2>" +
                             "<p style='color: #475569; font-size: 16px;'>Merhaba " + studentName + ",</p>" +
                             "<p style='color: #475569; font-size: 16px;'><strong>" + courseName + "</strong> dersinin <strong>" + examName + "</strong> sınavı için tarih ve yer bilgisi belirlendi.</p>" +
                             "<div style='background-color: #fffbeb; padding: 15px; border-radius: 8px; margin: 20px 0; border: 1px solid #fde68a; display: flex; justify-content: space-between;'>" +
                             "<div><p style='margin: 0; color: #92400e; font-size: 12px; font-weight: bold; text-transform: uppercase;'>Tarih & Saat</p><p style='margin: 5px 0 0 0; color: #b45309; font-size: 16px; font-weight: bold;'>" + formattedDate + "</p></div>" +
                             "<div style='text-align: right;'><p style='margin: 0; color: #92400e; font-size: 12px; font-weight: bold; text-transform: uppercase;'>Derslik</p><p style='margin: 5px 0 0 0; color: #b45309; font-size: 16px; font-weight: bold;'>" + classroom + "</p></div>" +
                             "</div>" +
                             "<p style='color: #64748b; font-size: 14px; text-align: center;'>Sınavınızda başarılar dileriz.</p>" +
                             "</div>";
                             
        sendHtmlEmail(to, subject, htmlContent);
    }

    public void sendAnnouncementEmail(String to, String studentName, String title, String content, String author) {
        String subject = "KTÜ BYS - Yeni Duyuru: " + title;
        
        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: 0 auto; border: 1px solid #e2e8f0; border-radius: 10px; border-top: 5px solid #6366f1;'>" +
                             "<h2 style='color: #6366f1; text-align: center;'>Yeni Bir Duyuru Var</h2>" +
                             "<p style='color: #475569; font-size: 16px;'>Merhaba " + studentName + ",</p>" +
                             "<p style='color: #475569; font-size: 16px;'>Sistemde sizinle ilgili yeni bir duyuru yayınlandı:</p>" +
                             "<div style='background-color: #eef2ff; padding: 20px; border-radius: 8px; margin: 20px 0; border: 1px solid #c7d2fe;'>" +
                             "<h3 style='margin-top: 0; color: #4338ca;'>" + title + "</h3>" +
                             "<p style='color: #334155; font-size: 15px; line-height: 1.5; white-space: pre-wrap;'>" + content + "</p>" +
                             "</div>" +
                             "<p style='color: #64748b; font-size: 12px; text-align: right; font-style: italic;'>Gönderen: " + author + "</p>" +
                             "</div>";
                             
        sendHtmlEmail(to, subject, htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("E-posta gönderimi başarısız (" + to + "): " + e.getMessage());
        }
    }
}
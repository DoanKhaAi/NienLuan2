package com.khaai.NuoiEm.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.khaai.NuoiEm.Entities.MyUser;
import com.khaai.NuoiEm.Repository.MyUserRepository;

import java.util.List;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    
    @Autowired
    private MyUserRepository userRepository;

    public String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("khaai142003@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Mã OTP của bạn");
        message.setText("Mã OTP của bạn là: " + otp);
        javaMailSender.send(message);
    }
    
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("khaai142003@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
    
    @Scheduled(cron = "0 32 11 20 * ?")
    public void sendMonthlyReminder() {
        List<MyUser> usersWithChildren = userRepository.findAllWithChildren();
        for (MyUser user : usersWithChildren) {
           sendEmail(user.getEmail(), 
                                   "NHẮC NHỞ TỪ DỰ ÁN NUÔI EM", 
                                   "Thân gửi bạn,\r\n"
                                   + "\r\n"
                                   + "Chúng tôi xin gửi đến bạn lời nhắc yêu thương rằng hôm nay đã đến ngày 10 — thời điểm đặc biệt mỗi tháng để chúng ta cùng chăm lo cho những thiên thần nhỏ mà bạn đã nhận nuôi. "
                                   + "Sự hỗ trợ quý báu của bạn không chỉ là nguồn động viên lớn lao cho các em mà còn là niềm hy vọng giúp các em có một tương lai tươi sáng hơn.\r\n"
                                   + "\r\n"
                                   + "Chúng tôi mong bạn sẽ tiếp tục gửi đi sự yêu thương của mình qua việc chuyển khoản định kỳ. Mỗi đồng tiền bạn trao gửi sẽ mang lại nụ cười, niềm vui và một cuộc sống tốt đẹp hơn cho các em.\r\n"
                                   + "\r\n"
                                   + "Nếu bạn đã hoàn tất việc chuyển tiền, xin vui lòng bỏ qua thông báo này. Chúng tôi rất biết ơn sự hỗ trợ không ngừng nghỉ của bạn trong hành trình đầy ý nghĩa này.\r\n"
                                   + "\r\n"
                                   + "Chúc bạn một ngày thật an lành và hạnh phúc!\r\n"
                                   + "\r\n"
                                   + "Trân trọng,\r\n"
                                   + "\r\n"
                                   + "Dự án Nuôi Em");
        }
    }
}

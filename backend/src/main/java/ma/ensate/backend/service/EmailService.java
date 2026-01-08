package ma.ensate.backend.service;

import ma.ensate.backend.domain.CandidatureRecrutement;
import ma.ensate.backend.domain.Recrutement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender, @Value("${app.mail.from:}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendAcceptanceEmail(CandidatureRecrutement c, Recrutement r) {
        if (from == null || from.isBlank()) {
            throw new IllegalStateException("Email sender not configured (app.mail.from)");
        }
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(c.getEmail());
        msg.setSubject("Candidature retenue - " + r.getPoste());
        msg.setText("Bonjour " + c.getPrenom() + " " + c.getNom() + ",\n\n" +
                "Votre candidature au poste \"" + r.getPoste() + "\" a été retenue.\n" +
                "Nous vous contacterons pour la suite.\n\n" +
                "Cordialement.");
        mailSender.send(msg);
    }
}

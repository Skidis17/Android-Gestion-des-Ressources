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


    private void ensureFromConfigured() {
        if (from == null || from.isBlank()) {
            throw new IllegalStateException("Email sender not configured (app.mail.from)");
        }
    }

    // ✅ NEW: email d’invitation
    public void sendUserInvitationEmail(String toEmail, String username, String email, String rawPassword) {
        ensureFromConfigured();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(toEmail);
        msg.setSubject("Invitation - Création de votre compte");
        msg.setText(
                "Bonjour " + (username != null ? username : "") + ",\n\n" +
                        "Votre compte a été créé.\n\n" +
                        "Identifiants:\n" +
                        "Email: " + email + "\n" +
                        "Mot de passe: " + rawPassword + "\n\n" +
                        "Merci de changer votre mot de passe après votre première connexion.\n\n" +
                        "Cordialement."
        );

        mailSender.send(msg);
    }

    public void sendInterviewScheduledEmail(CandidatureRecrutement c, Recrutement r, ma.ensate.backend.domain.Entretien e) {
        ensureFromConfigured();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(c.getEmail());
        msg.setSubject("Entretien planifié - " + r.getPoste());
        String when = e.getScheduledAt() != null ? e.getScheduledAt().toString() : "à confirmer";
        String mode = e.getMode() != null ? e.getMode() : "Présentiel";
        String location = e.getLocation() != null ? e.getLocation() : "à confirmer";
        msg.setText("Bonjour " + c.getPrenom() + " " + c.getNom() + ",\n\n" +
                "Votre entretien pour le poste \"" + r.getPoste() + "\" est planifié.\n" +
                "Date/Heure: " + when + "\n" +
                "Mode: " + mode + "\n" +
                "Lieu/Lien: " + location + "\n\n" +
                "Cordialement.");
        mailSender.send(msg);
    }

    public void sendDemandeStatusChangeEmail(ma.ensate.backend.domain.Demande demande, String personnelName, String personnelEmail) {
        ensureFromConfigured();
        if (personnelEmail == null || personnelEmail.isBlank()) {
            return; // No email to send to
        }
        
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(personnelEmail);
        
        String statutText = "";
        String statutSubject = "";
        switch (demande.getStatut()) {
            case ACCEPTEE:
                statutText = "acceptée";
                statutSubject = "Demande acceptée";
                break;
            case REFUSEE:
                statutText = "refusée";
                statutSubject = "Demande refusée";
                break;
            case EN_ATTENTE:
                statutText = "remise en attente";
                statutSubject = "Demande en attente";
                break;
        }
        
        msg.setSubject(statutSubject + " - " + demande.getType().name());
        
        String greeting = personnelName != null && !personnelName.isBlank() 
            ? "Bonjour " + personnelName 
            : "Bonjour";
        
        msg.setText(greeting + ",\n\n" +
                "Votre demande de " + demande.getType().name().toLowerCase() + 
                " du " + demande.getDateDebut() + " au " + demande.getDateFin() + 
                " a été " + statutText + ".\n\n" +
                "Motif: " + (demande.getMotif() != null ? demande.getMotif() : "Non spécifié") + "\n\n" +
                "Cordialement,\n" +
                "Service des Ressources Humaines");
        
        mailSender.send(msg);
    }

}

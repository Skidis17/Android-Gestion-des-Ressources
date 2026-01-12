package ma.ensate.myapplication.model;

public class StatusChangeRequest {
    private String statut;
    private String reason;
    private Boolean sendEmail;
    private String changedBy;

    public StatusChangeRequest(String statut, String reason, Boolean sendEmail, String changedBy) {
        this.statut = statut;
        this.reason = reason;
        this.sendEmail = sendEmail;
        this.changedBy = changedBy;
    }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Boolean getSendEmail() { return sendEmail; }
    public void setSendEmail(Boolean sendEmail) { this.sendEmail = sendEmail; }
    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
}

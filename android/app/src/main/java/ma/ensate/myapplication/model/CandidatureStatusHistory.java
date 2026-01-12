package ma.ensate.myapplication.model;

public class CandidatureStatusHistory {
    private Long id;
    private Long candidatureId;
    private String fromStatus;
    private String toStatus;
    private String reason;
    private String changedBy;
    private String changedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCandidatureId() { return candidatureId; }
    public void setCandidatureId(Long candidatureId) { this.candidatureId = candidatureId; }
    public String getFromStatus() { return fromStatus; }
    public void setFromStatus(String fromStatus) { this.fromStatus = fromStatus; }
    public String getToStatus() { return toStatus; }
    public void setToStatus(String toStatus) { this.toStatus = toStatus; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
    public String getChangedAt() { return changedAt; }
    public void setChangedAt(String changedAt) { this.changedAt = changedAt; }
}

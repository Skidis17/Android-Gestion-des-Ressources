package ma.ensate.myapplication.model;

import java.math.BigDecimal;

public class Entretien {
    private Long id;
    private Long candidatureId;
    private String type;
    private String scheduledAt;
    private String mode;
    private String location;
    private String status;
    private String notes;
    private BigDecimal scoreTotal;
    private String createdBy;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCandidatureId() { return candidatureId; }
    public void setCandidatureId(Long candidatureId) { this.candidatureId = candidatureId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(String scheduledAt) { this.scheduledAt = scheduledAt; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public BigDecimal getScoreTotal() { return scoreTotal; }
    public void setScoreTotal(BigDecimal scoreTotal) { this.scoreTotal = scoreTotal; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

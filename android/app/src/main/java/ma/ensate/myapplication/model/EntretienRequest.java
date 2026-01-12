package ma.ensate.myapplication.model;

public class EntretienRequest {
    private String type;
    private String scheduledAt;
    private String mode;
    private String location;
    private String status;
    private String notes;
    private String createdBy;

    public EntretienRequest(String type, String scheduledAt, String mode, String location, String status, String notes, String createdBy) {
        this.type = type;
        this.scheduledAt = scheduledAt;
        this.mode = mode;
        this.location = location;
        this.status = status;
        this.notes = notes;
        this.createdBy = createdBy;
    }

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
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}

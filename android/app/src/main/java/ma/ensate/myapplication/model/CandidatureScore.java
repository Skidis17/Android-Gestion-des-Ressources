package ma.ensate.myapplication.model;

import java.math.BigDecimal;

public class CandidatureScore {
    private Long id;
    private Long candidatureId;
    private String stage;
    private String criterion;
    private BigDecimal score;
    private BigDecimal weight;
    private String reviewer;
    private String notes;
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCandidatureId() { return candidatureId; }
    public void setCandidatureId(Long candidatureId) { this.candidatureId = candidatureId; }
    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }
    public String getCriterion() { return criterion; }
    public void setCriterion(String criterion) { this.criterion = criterion; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public String getReviewer() { return reviewer; }
    public void setReviewer(String reviewer) { this.reviewer = reviewer; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

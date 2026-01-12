package ma.ensate.myapplication.model;

import java.math.BigDecimal;

public class EntretienScoreRequest {
    private String criterion;
    private BigDecimal score;
    private BigDecimal weight;
    private String reviewer;
    private String notes;

    public EntretienScoreRequest(String criterion, BigDecimal score, BigDecimal weight, String reviewer, String notes) {
        this.criterion = criterion;
        this.score = score;
        this.weight = weight;
        this.reviewer = reviewer;
        this.notes = notes;
    }

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
}

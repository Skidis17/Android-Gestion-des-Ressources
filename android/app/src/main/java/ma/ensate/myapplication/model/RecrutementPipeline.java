package ma.ensate.myapplication.model;

public class RecrutementPipeline {
    private Long recrutementId;
    private int total;
    private int enAttente;
    private int preselection;
    private int test;
    private int entretien;
    private int retenu;
    private int refuse;

    public Long getRecrutementId() { return recrutementId; }
    public void setRecrutementId(Long recrutementId) { this.recrutementId = recrutementId; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public int getEnAttente() { return enAttente; }
    public void setEnAttente(int enAttente) { this.enAttente = enAttente; }
    public int getPreselection() { return preselection; }
    public void setPreselection(int preselection) { this.preselection = preselection; }
    public int getTest() { return test; }
    public void setTest(int test) { this.test = test; }
    public int getEntretien() { return entretien; }
    public void setEntretien(int entretien) { this.entretien = entretien; }
    public int getRetenu() { return retenu; }
    public void setRetenu(int retenu) { this.retenu = retenu; }
    public int getRefuse() { return refuse; }
    public void setRefuse(int refuse) { this.refuse = refuse; }
}

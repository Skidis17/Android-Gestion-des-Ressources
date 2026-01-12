package ma.ensate.myapplication.model;

public class Notification {
    public Long id;
    public Long utilisateurId;
    public String titre;
    public String message;
    public String type; // INFO, SUCCESS, WARNING, ERROR
    public Boolean estLu;
    public String createdAt;
}

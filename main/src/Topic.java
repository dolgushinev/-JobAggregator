import java.time.LocalDateTime;

public class Topic {

    private LocalDateTime modifiedDate;
    private String url;

    public Topic(LocalDateTime modifiedDate, String url) {
        this.modifiedDate = modifiedDate;
        this.url = url;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public String getUrl() {
        return url;
    }

}

import java.time.LocalDateTime;

public class Vacancy {
    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getcDate() {
        return cDate;
    }

    public String getUrl() {
        return url;
    }

    private LocalDateTime cDate;
    private String url; //подумать над типом данных URL

    public Vacancy(String title, String description, LocalDateTime cDate, String url) {
        this.title = title;
        this.description = description;
        this.cDate = cDate;
        this.url = url;
    }
}

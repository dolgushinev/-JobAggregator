import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vacancy {
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private String url;

    public Vacancy(String title, String description, LocalDateTime createdDate, String url) {
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.url = url;
    }

    public boolean containsKeywords(String[] keywords)
    {
        if(keywords.length == 0) return true;

        for (String keyword:keywords) {
            Pattern pattern = Pattern.compile("\\b" + keyword + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(getDescription());
            if(matcher.find()) return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return  "Дата публикации=" + createdDate +
                ", Заголовок='" + title + '\'' +
                ", URL='" + url + '\'' +
                ", Описание='" + description + '\'';
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getUrl() {
        return url;
    }
}

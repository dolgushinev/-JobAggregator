import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.ParserUtil;
import java.io.IOException;
import java.time.LocalDateTime;

public class Topic {

    private LocalDateTime lastDate;
    private String url; //подумать над типом данных URL

    public Topic(LocalDateTime lastDate, String url) {
        this.lastDate = lastDate;
        this.url = url;
    }

    public LocalDateTime getLastDate() {
        return lastDate;
    }

    public String getUrl() {
        return url;
    }

}

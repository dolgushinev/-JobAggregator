import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.ParserUtil;

import javax.print.Doc;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Topic {

    private LocalDateTime lastDate;
    private LocalDateTime date;
    private String url; //подумать над типом данных URL

    public Topic(LocalDateTime lastDate, String url) {
        this.lastDate = lastDate;
        this.url = url;
        date = null;
    }

    public LocalDateTime getLastDate() {
        return lastDate;
    }

    public String getUrl() {
        return url;
    }

    public boolean isVacancy(int months) {

        Document doc = null;

        try {
            doc = Jsoup.connect(this.url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String datetime = doc.getElementsByClass("msgFooter").get(0).text().split("\\[")[0];

        System.out.println(ParserUtil.getDate(datetime));

        return ParserUtil.getDate(datetime).isAfter(LocalDateTime.now().minusMonths(months));

    }

}

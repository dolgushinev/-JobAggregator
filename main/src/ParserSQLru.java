import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.Message;
import util.ParserUtil;

public class ParserSQLru implements Parser {

    private List<Vacancy> vacancies;
    private List<Vacancy> filteredVacancies;
    private static Logger logger;

    final String baseURL = "https://www.sql.ru/forum/job-offers/";

    public ParserSQLru() {
        vacancies = new ArrayList<>();
        filteredVacancies = new ArrayList<>();

        try {
            LogManager.getLogManager().readConfiguration(ParserSQLru.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println(Message.LOGGER_CONFIG_LOADING_ERROR.getText() + "\n" + e.toString());
        }

        logger = Logger.getLogger(ParserSQLru.class.getName());

        FileHandler handler = null;
        try {
            handler = new FileHandler("./LogFile.log");
        } catch (IOException e) {
            System.out.println(Message.CHECK_FILESYSTEM_ACCESS.getText());
        }
        logger.addHandler(handler);

        SimpleFormatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);

    }

    @Override
    public boolean load(int months) {
        System.out.println(Message.DATA_LOADING_STARTED.getText());
        logger.log(Level.FINE, Message.DATA_LOADING_STARTED.getText());

        Document doc = null;

        try {

            int i = 1;

            String pageURL = baseURL + String.valueOf(i);

            doc = Jsoup.connect(pageURL).get();
            System.out.println(Message.PAGE_DATA_LOADED.getText() + pageURL);
            logger.log(Level.FINE, Message.PAGE_DATA_LOADED.getText() + pageURL);

            while (!isLastPage(doc, months)) {

                Elements rows = doc.getElementsByTag("table").get(2).
                        getElementsByTag("tr");

                int index = getFirstTopicIndex(rows);

                for (; index < rows.size();
                     index++) {

                    Element currentRow = rows.get(index);

                    String modifiedDate = currentRow.
                            getElementsByTag("td").
                            last().
                            text();

                    Topic topic = new Topic(ParserUtil.getDate(modifiedDate), getURL(currentRow));

                    boolean isClosed = isClosed(currentRow);

                    Document vacancyDoc = null;

                    try {
                        vacancyDoc = Jsoup.connect(topic.getUrl()).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(Message.TOPIC_DATA_LOADING_ERROR.getText());
                        logger.log(Level.SEVERE, Message.TOPIC_DATA_LOADING_ERROR.getText());

                    }

                    String createdDate = vacancyDoc.getElementsByClass("msgFooter").get(0).text().split("\\[")[0];


                    if (ParserUtil.getDate(createdDate).isAfter(LocalDateTime.now().minusMonths(months)) && !isClosed) {
                        vacancies.add(new Vacancy(getTitle(vacancyDoc), getDescription(vacancyDoc), ParserUtil.getDate(createdDate), topic.getUrl()));
                    }
                }

                i++;

                pageURL = baseURL + String.valueOf(i);
                doc = Jsoup.connect(pageURL).get();
                System.out.println(Message.PAGE_DATA_LOADED.getText() + pageURL);
                logger.log(Level.FINE, Message.PAGE_DATA_LOADED.getText() + pageURL);

            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(Message.PAGE_DATA_LOADING_ERROR.getText());
            logger.log(Level.SEVERE, Message.PAGE_DATA_LOADING_ERROR.getText());
            return false;
        }

        System.out.println(Message.DATA_LOADING_COMPLETED.getText());
        logger.log(Level.FINE, Message.DATA_LOADING_COMPLETED.getText());

        return true;
    }

    private boolean isClosed(Element currentRow) {
        return !currentRow.getElementsByTag("td").get(1).
                getElementsByClass("closedTopic").isEmpty();
    }

    private String getURL(Element currentRow) {
        return currentRow.getElementsByTag("td").get(1).getElementsByTag("a").first().attributes().get("href");
    }

    private String getTitle(Document vacancyDoc) {
        return vacancyDoc.getElementsByClass("messageHeader").first().text();
    }

    private String getDescription(Document vacancyDoc) {

        return vacancyDoc.getElementsByClass("msgTable").first().getElementsByClass("msgBody").get(1).text();
    }

    private boolean isLastPage(Document doc, int months) {

        Elements rows = doc.getElementsByTag("table").
                get(2).
                getElementsByTag("tr");

        int index = getFirstTopicIndex(rows);

        String date = rows.
                get(index).
                getElementsByTag("td").
                last().
                text();

        LocalDateTime modifiedDate = ParserUtil.getDate(date);

        return modifiedDate.isBefore(LocalDateTime.now().minusMonths(months));
    }

    private int getFirstTopicIndex(Elements rows) {
        int index = 0;

        for (index = 1; index < rows.size(); index++) {
            if (!rows.get(index).
                    getElementsByTag("td").
                    get(1).
                    text().contains("Важно:")) break;
        }

        return index;
    }

    @Override
    public boolean process(String[] keywords) {
        System.out.println(Message.DATA_PROCESSING_STARTED.getText());
        logger.log(Level.FINE, Message.DATA_PROCESSING_STARTED.getText());

        filteredVacancies = vacancies.stream().filter(v -> v.containsKeywords(keywords)).collect(Collectors.toList());
        System.out.println(Message.DATA_PROCESSING_COMPLETED.getText());
        logger.log(Level.FINE, Message.DATA_PROCESSING_COMPLETED.getText());
        return true;
    }

    @Override
    public boolean save() {
        System.out.println(Message.SAVE_DATA_TO_FILE_STARTED.getText());
        logger.log(Level.FINE, Message.SAVE_DATA_TO_FILE_STARTED.getText());

        List<String> lines = filteredVacancies.stream().map(v -> v.toString()).collect(Collectors.toList());
        Path result = Paths.get("result.txt");
        try {
            Files.write(result, lines, Charset.forName("UTF-8"));
            System.out.println(Message.SAVE_DATA_TO_FILE_COMPLETED.getText());
            logger.log(Level.FINE, Message.SAVE_DATA_TO_FILE_COMPLETED.getText());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(Message.SAVE_DATA_TO_FILE_ERROR.getText());
            logger.log(Level.SEVERE, Message.SAVE_DATA_TO_FILE_ERROR.getText());
            return false;
        }
    }
}

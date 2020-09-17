import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
            System.err.println("Не смог загрузить файл конфигурации для логера: " + e.toString());
        }

        logger = Logger.getLogger(ParserSQLru.class.getName());

        FileHandler handler = null;
        try {
            handler = new FileHandler("./LogFile.log");
        } catch (IOException e) {
            System.out.println("Проверьте, что есть доступ к файловой системе");
        }
        logger.addHandler(handler);

        SimpleFormatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);

    }

    @Override
    public boolean load(int months) {

        System.out.println("Загрузка данных...");
        logger.log(Level.FINE, "Загрузка данных...");

        Document doc = null;

        try {

            int i = 1;

            String pageURL = baseURL + String.valueOf(i);
            System.out.println("Загружаем: " + pageURL);
            doc = Jsoup.connect(baseURL + String.valueOf(i)).get();

            logger.log(Level.FINE, "Загружены данные по странице: " + baseURL + String.valueOf(i));

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
                        logger.log(Level.SEVERE, "При загрузке данных топика возникла ошибка");
                    }

                    String createdDate = vacancyDoc.getElementsByClass("msgFooter").get(0).text().split("\\[")[0];


                    if (ParserUtil.getDate(createdDate).isAfter(LocalDateTime.now().minusMonths(months)) && !isClosed) {
                        vacancies.add(new Vacancy(getTitle(vacancyDoc), getDescription(vacancyDoc), ParserUtil.getDate(createdDate), topic.getUrl()));
                    }
                }

                i++;

                pageURL = baseURL + String.valueOf(i);
                System.out.println("Загружаем: " + pageURL);
                doc = Jsoup.connect(pageURL).get();
                logger.log(Level.FINE, "Загружены данные по странице: " + baseURL + String.valueOf(i));

            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "При загрузке данных возникла ошибка");
            return false;
        }

        logger.log(Level.FINE, "Загрузка данных успешно завершена");
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

        boolean isLastPage = modifiedDate.isBefore(LocalDateTime.now().minusMonths(months));

        if(isLastPage) logger.log(Level.FINE, "Все необходимые страницы загружены");

        return isLastPage;
    }

    private int getFirstTopicIndex(Elements rows) {
        int index = 0;

        for (index = 1; index < rows.size(); index++)
        {
            if (!rows.get(index).
                    getElementsByTag("td").
                    get(1).
                    text().contains("Важно:")) break;
        }

        return index;
    }

    @Override
    public boolean process(String[] keywords) {
        System.out.println("Обработка данных...");
        logger.log(Level.FINE, "Обработка данных, согласно заданным ключевым словам");

        filteredVacancies = vacancies.stream().filter(v -> v.containsKeywords(keywords)).collect(Collectors.toList());

        for (Vacancy vacancy : filteredVacancies) {
                System.out.println(vacancy.toString());
            }
        logger.log(Level.FINE, "Данные успешно обработаны");
        return true;
    }

    @Override
    public boolean save() {
        System.out.println("Сохранение результатов...");
        logger.log(Level.FINE, "Сохранение результатов работы программы в файл");

        List<String> lines = filteredVacancies.stream().map(v -> v.toString()).collect(Collectors.toList());
        Path result = Paths.get("result.txt");
        try {
            Files.write(result, lines, Charset.forName("UTF-8"));
            logger.log(Level.FINE, "Результаты успешно сохранены");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "При сохранении результатов возникла ошибка");
            return false;
        }
    }
}

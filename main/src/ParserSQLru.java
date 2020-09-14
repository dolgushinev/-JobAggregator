import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.ParserUtil;

public class ParserSQLru implements Parser {

    private List<Vacancy> vacancies = new ArrayList<>();
    private List<Vacancy> filteredVacancies = new ArrayList<>();

    final String URL = "https://www.sql.ru/forum/job-offers/";

    @Override
    public boolean load(int months) {

        System.out.println("Загрузка данных...");
        Document doc = null;

        try {

            int i = 1;

            System.out.println("Загружаем: " + URL + String.valueOf(i));
            doc = Jsoup.connect(URL + String.valueOf(i)).get();

            while (!isLastPage(doc, months)) {

                Elements rows = doc.getElementsByTag("table").get(2).
                        getElementsByTag("tr");

                for (int j = 4; j < rows.size();
                     j++) {

                    Element currentRow = rows.get(j);

                    String datetime = currentRow.
                            getElementsByTag("td").
                            last().
                            text();

                    Topic topic = new Topic(ParserUtil.getDate(datetime), getURL(currentRow));

                    boolean isClosed = isClosed(currentRow);

                    Document vacancyDoc = null;

                    try {
                        vacancyDoc = Jsoup.connect(topic.getUrl()).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String cDateTime = vacancyDoc.getElementsByClass("msgFooter").get(0).text().split("\\[")[0];


                    if (ParserUtil.getDate(cDateTime).isAfter(LocalDateTime.now().minusMonths(months)) && !isClosed) {
                        vacancies.add(new Vacancy(getTitle(vacancyDoc), getDescription(vacancyDoc), ParserUtil.getDate(cDateTime), topic.getUrl()));
                    }
                }

                i++;
                System.out.println("Загружаем: " + URL + String.valueOf(i));
                doc = Jsoup.connect(URL + String.valueOf(i)).get();
            }

/*            for (Vacancy vacancy : vacancies) {
                System.out.println(vacancy.getTitle() + " " + vacancy.getUrl() + " " + vacancy.getcDate() + " " + vacancy.getDescription());
            }*/

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

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

        //Подумать над тем как пропустить "важные объявления", сейчас хардкод, берем 4-ю строку
        String date = doc.getElementsByTag("table").
                get(2).
                getElementsByTag("tr").
                get(4).
                getElementsByTag("td").
                last().
                text();

        //подумать над тем, чтобы использовать метод getDate;

        String[] s_date = date.split(",")[0].split(" ");

        //System.out.println(date);

        if (s_date[0].equals("сегодня") || s_date[0].equals("вчера")) {
            return false;
        }

        int day = Integer.parseInt(s_date[0]);
        int month = ParserUtil.getMonth(s_date[1]);
        int year = Integer.parseInt(s_date[2]) + 2000; //подумать над годом

        //System.out.println("Самое свежее последнее изменение: " + LocalDate.of(year, month, day));
        //System.out.println("Дата для сравнения: " + LocalDate.now().minusMonths(months));

        return LocalDate.of(year, month, day).isBefore(LocalDate.now().minusMonths(months));
    }

    @Override
    public boolean process(String[] keywords) {
        System.out.println("processing...");

        filteredVacancies = vacancies.stream().filter(v -> v.containsKeyword(keywords)).collect(Collectors.toList());

        for (Vacancy vacancy : filteredVacancies) {
                System.out.println(vacancy.toString());
            }

        return true;
    }

    @Override
    public boolean save() {
        System.out.println("save results...");

        List<String> lines = filteredVacancies.stream().map(v -> v.toString()).collect(Collectors.toList());
        Path result = Paths.get("result.txt");
        try {
            Files.write(result, lines, Charset.forName("UTF-8"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

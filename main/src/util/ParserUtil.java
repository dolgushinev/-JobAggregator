package util;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * ParserUtil - вспомогательный класс, содержащий методы для обработки дат и месяцев
 */
public class ParserUtil {
    /**
     * Метод отвечающий за преобразование даты в формате сайта sql.ru в LocalDateTime
     *
     * @param datetime строка, содержащая дату и время в формате, используемом на сайте sql.ru
     * @return true, дата и время в виде LocalDateTime
     */
    public static LocalDateTime getDate(String datetime) {
        int day = 0;
        int month = 0;
        int year = 0;
        int minute = 0;
        int second = 0;
        String[] date = datetime.split(",")[0].split(" ");
        String[] time = datetime.split(",")[1].split(":");
        if (date[0].equals("сегодня")) {
            day = LocalDate.now().getDayOfMonth();
            month = LocalDate.now().getMonthValue();
            year = LocalDate.now().getYear();
        } else if (date[0].equals("вчера")) {
            day = LocalDate.now().getDayOfMonth() - 1;
            month = LocalDate.now().getMonthValue();
            year = LocalDate.now().getYear();
        } else {
            day = Integer.parseInt(date[0]);
            month = getMonth(date[1]);
            year = Integer.parseInt(date[2]) + LocalDateTime.now().getYear() / 100 * 100;
        }
        minute = Integer.parseInt(time[0].trim());
        second = Integer.parseInt(time[1].trim());
        return LocalDateTime.of(year, month, day, minute, second);
    }
    /**
     * Метод, отвечающий за преобразование имени месяца в его номер
     *
     * @param month имя месяца
     * @return номер месяца
     */
    public static int getMonth(String month) {
        int m_number = 0;
        switch (month) {
            case "янв":
                m_number = 1;
                break;
            case "фев":
                m_number = 2;
                break;
            case "мар":
                m_number = 3;
                break;
            case "апр":
                m_number = 4;
                break;
            case "май":
                m_number = 5;
                break;
            case "июн":
                m_number = 6;
                break;
            case "июл":
                m_number = 7;
                break;
            case "авг":
                m_number = 8;
                break;
            case "сен":
                m_number = 9;
                break;
            case "окт":
                m_number = 10;
                break;
            case "ноя":
                m_number = 11;
                break;
            case "дек":
                m_number = 12;
                break;
            default:
                m_number = 0;
        }
        return m_number;
    }
}

package util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ParserUtil {

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
        }
        else if (date[0].equals("вчера")) {
            day = LocalDate.now().getDayOfMonth() - 1;
            month = LocalDate.now().getMonthValue();
            year = LocalDate.now().getYear();
        }
        else {
            day = Integer.parseInt(date[0]);
            month = getMonth(date[1]);
            year = Integer.parseInt(date[2]) + 2000; //подумать над годом
        }

        minute = Integer.parseInt(time[0].trim());
        second = Integer.parseInt(time[1].trim());

        return LocalDateTime.of(year, month, day, minute, second);
    }

    public static int getMonth(String month) {
        int m_number = 0;

        switch (month) {
            case "янв":
                m_number = Month.JANUARY.getNumber();
                break;
            case "фев":
                m_number = Month.FEBRUARY.getNumber();
                break;
            case "мар":
                m_number = Month.MARCH.getNumber();
                break;
            case "апр":
                m_number = Month.APRIL.getNumber();
                break;
            case "май":
                m_number = Month.MAY.getNumber();
                break;
            case "июн":
                m_number = Month.JUNE.getNumber();
                break;
            case "июл":
                m_number = Month.JULY.getNumber();
                break;
            case "авг":
                m_number = Month.AUGUST.getNumber();
                break;
            case "сен":
                m_number = Month.SEPTEMBER.getNumber();
                break;
            case "окт":
                m_number = Month.OCTOBER.getNumber();
                break;
            case "ноя":
                m_number = Month.NOVEMBER.getNumber();
                break;
            case "дек":
                m_number = Month.DECEMBER.getNumber();
                break;
            default:
                m_number = 0;

        }

        return m_number;

    }

}

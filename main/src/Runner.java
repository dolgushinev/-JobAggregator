import util.Message;

import java.util.Arrays;

/**
 * Класс, отвечающий за запуск приложения и валидацию входных параметров
 */
public class Runner {

    public static void main(String[] args) {

        int input_months = 1;
        String[] keywords = {};

        boolean isValid = validateInputParam(args);

        if (isValid) {
            keywords = getKeywords(args);
            Parser parser = new ParserSQLru();
            if (args.length > 0)
                input_months = Integer.parseInt(args[1].trim());
            if (parser.load(input_months)) {
                parser.process(keywords);
                parser.save();
            }
        } else {
            System.out.println(Message.INPUT_PARAM_IS_NOT_VALID.getText());
        }
    }

    /**
     * Метод отвечающий за получение и валидацию списка ключевых слов
     *
     * @param args массив входных параметров
     * @return массив ключевых слов
     */
    private static String[] getKeywords(String[] args) {

        if (args.length <= 2) return new String[]{};
        else {
            String[] keywords = new String[args.length - 2];
            System.arraycopy(args, 2, keywords, 0, args.length - 2);

            return Arrays.stream(keywords).map(s -> s.trim()).toArray(String[]::new);
        }
    }

    /**
     * Метод отвечающий за валидацию входных параметров - сайта и количества месяцев
     *
     * @param args массив входных параметров
     * @return true, если валидация прошла успешно
     */
    private static boolean validateInputParam(String[] args) {

        if (args.length == 0)
            return true;
        else if (args.length == 1) return false;
        else {

            boolean isNotSQLRu = !args[0].toLowerCase().equals("sql.ru");
            boolean monthsIsNotDigit = !isDigit(args[1]);
            boolean monthsIsNotInRange = false;

            if (isDigit(args[1])) {
                monthsIsNotInRange = !(Integer.parseInt(args[1]) >= 1 && Integer.parseInt(args[1]) <= 12);
            }

            if (isNotSQLRu || monthsIsNotDigit || monthsIsNotInRange) {
                return false;
            }
        }

        return true;
    }

    /**
     * Метод отвечающий за проверку - является ли значение строковой переменной числом
     *
     * @param s входная строка
     * @return true, если значение строковой переменной является числом
     */
    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

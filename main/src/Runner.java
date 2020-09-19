import java.util.Arrays;

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
            if(parser.load(input_months)) {
                parser.process(keywords);
                parser.save();
            }
        } else {
            System.out.println("Входные параметры не валидны");
        }
    }

    private static String[] getKeywords(String[] args) {

        if (args.length <= 2) return new String[]{};
        else {
            String[] keywords = new String[args.length - 2];
            System.arraycopy(args, 2, keywords, 0, args.length - 2);

            return Arrays.stream(keywords).map(s -> s.trim()).toArray(String[]::new);
        }
    }

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

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

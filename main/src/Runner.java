public class Runner {

    public static void main(String[] args) {
        System.out.println("Runner запущен\n");

        String input_site = "sql.ru";
        int input_months = 2;
        String[] keywords = {/*"css", "javascript"*/};

        if (input_site.equals("sql.ru")) {
            Parser parser = new ParserSQLru();
            parser.load(input_months);
            parser.process(keywords);
            parser.save();
        }
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

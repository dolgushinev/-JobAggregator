public class Runner {

    public static void main(String[] args) {
        System.out.println("Runner is started\n");

        String input_site = "sql.ru";
        int input_months = 1;
        String[] keywords = {"css", "javascript"};

        Parser parser = new ParserSQLru();

        if (input_site.equals("sql.ru")) {
            parser.load(input_months);
            parser.process(keywords);
            parser.save();
        }
    }
}

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Runner {

    public static void main(String[] args) {
        System.out.println("Runner is started\n");

        String input_site = "sql.ru";

        int input_months = 1;

        List<String> keywords = new ArrayList<>();
        keywords.add("java");

        if(input_site.equals("sql.ru")){
            Parser parser = new ParserSQLru();
            parser.load(1);
            parser.process(keywords);
            parser.save();
        }

    }
}

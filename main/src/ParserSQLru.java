import java.io.File;
import java.util.List;

public class ParserSQLru implements Parser{

    private List<Vacancy> vacancies;

    @Override
    public boolean load(int months) {
        System.out.println("loading...");
        return false;
    }

    @Override
    public boolean process(List<String> keywords) {
        System.out.println("processing...");
        return false;
    }

    @Override
    public boolean save() {
        System.out.println("save results...");
        return false;
    }
}

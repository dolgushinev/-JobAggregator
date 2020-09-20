public interface Parser {
    boolean load(int months);

    boolean process(String[] keywords);

    boolean save();
}
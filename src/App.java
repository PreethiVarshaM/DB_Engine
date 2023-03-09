import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        String code = "create table mytab 2\nint a\nstring b\ncreate table mytab2 3\nint a\nstring b\nstring c\ncreate table mytab3 4\nint a1\nstring b1\nstring c1\nint d1\ninsert into mytab (12,23.4)";

        List<String> intermediateCode = cs20b030_parser.parse(code);
        System.out.println(intermediateCode.size());
        for (String line : intermediateCode) {
            System.out.println(line);
        }
    }
}


import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

public class cs20b030_parser {

    public static List<String> parse(String code) throws IOException {
        List<String> intermediateCode = new ArrayList<>();
        String[] lines = code.split("\\r?\\n");
        String currentTable = "";
        int lineNum = 0;
        for (String line : lines) {

            line = line.trim();
            if (line.startsWith("create table")) {
                String tableName = line.split(" ")[2];
                intermediateCode.add("create_table " + tableName);
                currentTable = tableName;
                int n = Integer.parseInt(line.split(" ")[3]);

                // creating the new table
                Table table = new Table(tableName, n);
                cs20b030_dbengine.tables.add(table);

                for (int i = 1; i <= n; i++) {
                    line = lines[lineNum + i].trim();
                    if (line.startsWith("int")) {
                        String[] parts = line.split(" ");
                        String attrType = parts[0];
                        String attrName = parts[1];
                        intermediateCode.add("add_attribute " + currentTable + " " + attrType + " " + attrName);
                    } else if (line.startsWith("string")) {
                        String[] parts = line.split(" ");
                        String attrType = parts[0];
                        String attrName = parts[1];
                        intermediateCode.add("add_attribute " + currentTable + " " + attrType + " " + attrName);
                    } else if (line.startsWith("date")) {
                        String[] parts = line.split(" ");
                        String attrType = parts[0];
                        String attrName = parts[1];
                        intermediateCode.add("add_attribute " + currentTable + " " + attrType + " " + attrName);
                    } else {
                        System.out.println("Invalid attribute type");
                    }
                }
            } else if (line.startsWith("insert into")) {
                String[] parts = line.split(" ");
                String tableName = parts[2];
                String dataRow = parts[3].substring(1, parts[3].length() - 1);
                intermediateCode.add("insert_into " + tableName + " (" + dataRow + ")");
            }
            lineNum++;
        }
        FileWriter writer = new FileWriter("cs20b030.code");
        BufferedWriter buffer = new BufferedWriter(writer);
        for (String line : intermediateCode) {
            buffer.write(line);
            buffer.newLine();
        }
        buffer.close();

        return intermediateCode;
    }

}

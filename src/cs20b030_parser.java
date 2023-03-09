
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Table {
    String name;
    int numAttributes;
    List<String> types;
    List<List<String>> data;

    public Table(String name, int numAttributes) {
        this.name = name;
        this.numAttributes = numAttributes;
        this.data = new ArrayList<>();
        this.types = new ArrayList<String>();
    }

    public void addAttribute(List<String> attr) {
        for (int i = 0; i < numAttributes; i++) {
            this.types.add(attr.get(i));
        }
    }

    public void insertInto(String dataRow) {
        String[] row = dataRow.split(",");
        for (int i = 0; i < row.length; i++) {
            row[i] = row[i].trim();

        }
    }

    public void print() {
        System.out.println("Table: " + this.name);
        System.out.println("Number of attributes: " + this.numAttributes);
        for (Attribute attribute : this.attributes) {
            attribute.print();
        }
    }
}

public class cs20b030_parser {

    public static List<String> parse(String code) {
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
                    } else if (line.startsWith("float")) {
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

        return intermediateCode;
    }

}

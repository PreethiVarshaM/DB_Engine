import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

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
        List<String> rowList = new ArrayList<String>(Arrays.asList(row));
        this.data.add(rowList);
    }

    public void print() {
        System.out.println("Table: " + this.name);
        System.out.println("Number of attributes: " + this.numAttributes);
        for (int i = 0; i < this.numAttributes; i++) {
            System.out.print(this.types.get(i) + "\t");
        }
        System.out.println();
        for (List<String> attr : this.data) {
            for (String val : attr) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
    }
}

public class cs20b030_dbengine {

}

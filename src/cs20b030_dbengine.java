import java.util.*;
import java.io.*;

// class to represent a table
class Table implements Serializable {
    String name;
    int numAttributes;
    List<String> types;
    List<String> names;
    List<List<String>> data;

    // constructor
    public Table(String name, int numAttributes) {
        this.name = name;
        this.numAttributes = numAttributes;
        this.data = new ArrayList<>();
        this.names = new ArrayList<String>();
        this.types = new ArrayList<String>();
    }

    // method to add an attribute to the table
    public void addAttribute(String type, String name) {

        this.types.add(type);
        this.names.add(name);
    }

    // method to insert a row into the table
    public void insertInto(String dataRow) {
        String[] row = dataRow.split(",");
        for (int i = 0; i < row.length; i++) {
            row[i] = row[i].trim();
        }
        List<String> rowList = new ArrayList<String>(Arrays.asList(row));
        this.data.add(rowList);
    }

    // method to print the table
    public void print() throws IOException {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("\nTable: " + this.name);
        System.out.println("Number of attributes: " + this.numAttributes + "\n");
        for (int i = 0; i < this.numAttributes; i++) {
            System.out.print(this.types.get(i) + "\t");
            if (this.types.get(i).equals("string") || this.types.get(i).equals("date")) {
                System.out.print("\t");
            }
        }
        System.out.println("\n");

        FileWriter writer = new FileWriter(this.name + ".table");
        BufferedWriter buffer = new BufferedWriter(writer);

        for (List<String> attr : this.data) {
            for (String val : attr) {
                System.out.print(val + "\t");
                buffer.write(val + "\t");
            }
            System.out.println();
            buffer.newLine();
        }
        buffer.close();
        System.out.println("--------------------------------------------------------------------------");
    }

    // method to get the index of an attribute
    int getNumAttributes() {
        return this.numAttributes;
    }

}

// class to represent a database
public class cs20b030_dbengine {

    // list of tables in the database
    public static List<Table> tables = new ArrayList<>();

    // method to get the index of a table in the database
    static int getTableIndex(String tableName) {
        for (int i = 0; i < tables.size(); i++) {
            if (tables.get(i).name.equals(tableName)) {
                return i;
            }
        }
        return -1;
    }

    // method to store a table in the database as csv file
    static void save_table(String tableName) throws IOException {
        try (FileWriter writer = new FileWriter(tableName + ".csv");
                BufferedWriter buffer = new BufferedWriter(writer)) {
            Table table = tables.get(getTableIndex(tableName));
            for (List<String> row : table.data) {
                for (int i = 0; i < row.size(); i++) {
                    writer.append(row.get(i));
                    if (i != row.size() - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method to load a table from the database
    static Table load_table(String tableName) throws IOException, ClassNotFoundException {
        List<List<String>> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(tableName + ".csv"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            List<String> row = new ArrayList<>();
            for (String value : values) {
                row.add(value);
            }
            data.add(row);
        }
        br.close();
        tables.get(getTableIndex(tableName)).data = data;
        return tables.get(getTableIndex(tableName));
    }

    // method to create a table
    static void create_table(String tableName) throws IOException {

        for (Table table : tables) {
            if (table.name.equals(tableName)) {
                save_table(tableName);
                return;
            }
        }

        Table table = new Table(tableName, 0);
        tables.add(table);
    }

    // method to add an attribute to a table
    static void add_attribute(String tableName, String attrType, String attrName)
            throws IOException {

        tables.get(getTableIndex(tableName)).addAttribute(attrType, attrName);

    }

    // method to insert a row into a table
    static void insert_into(int index, String values) {
        tables.get(index).insertInto(values);
    }

    // method to select all attributes from a table
    static void select_star(String tableName) throws IOException, ClassNotFoundException {
        Table table = load_table(tableName);
        table.print();
    }

    // method to parse and execute intermediate-code through the respective
    // function-calls
    static void execute_intermediate_code() throws IOException, ClassNotFoundException {

        BufferedReader br = new BufferedReader(new FileReader("cs20b030.code"));

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(" ");
            if (parts[0].equals("create_table")) {

                String tableName = parts[1];
                create_table(tableName);

            } else if (parts[0].equals("add_attribute")) {
                String tableName = parts[1];
                String attrType = parts[2];
                String attrName = parts[3];
                add_attribute(tableName, attrType, attrName);
                save_table(tableName);

            } else if (parts[0].equals("insert_into")) {
                int index = getTableIndex(parts[1]);

                insert_into(index, parts[2].substring(1, parts[2].length() - 1));
                save_table(parts[1]);
            } else if (parts[0].equals("select_star")) {
                select_star(parts[1]);
            }

        }
        br.close();

    }

    // main method
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // To read query from file
        BufferedReader br = new BufferedReader(new FileReader("cs20b030.query"));
        String code = "";
        String line;
        while ((line = br.readLine()) != null) {
            code += line + "\n";

        }
        br.close();

        // To convert query to intermediate code
        cs20b030_parser.parse(code);

        // To execute intermediate code
        execute_intermediate_code();

    }
}

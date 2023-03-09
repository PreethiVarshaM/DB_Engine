import java.util.*;
import java.io.*;

class Table implements Serializable {
    String name;
    int numAttributes;
    List<String> types;
    List<String> names;
    List<List<String>> data;

    public Table(String name, int numAttributes) {
        this.name = name;
        this.numAttributes = numAttributes;
        this.data = new ArrayList<>();
        this.names = new ArrayList<String>();
        this.types = new ArrayList<String>();
    }

    public void addAttribute(String type, String name) {

        this.types.add(type);
        this.names.add(name);
    }

    public void insertInto(String dataRow) {
        String[] row = dataRow.split(",");
        for (int i = 0; i < row.length; i++) {
            row[i] = row[i].trim();
        }
        List<String> rowList = new ArrayList<String>(Arrays.asList(row));
        this.data.add(rowList);
    }

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

    int getNumAttributes() {
        return this.numAttributes;
    }
}

public class cs20b030_dbengine {

    public static List<Table> tables = new ArrayList<>();

    static int getTableIndex(String tableName) {
        for (int i = 0; i < tables.size(); i++) {
            if (tables.get(i).name.equals(tableName)) {
                return i;
            }
        }
        return -1;
    }

    static void store_table(String tableName) throws IOException {
        int index = getTableIndex(tableName);
        Table table = tables.get(index);
        FileOutputStream file = new FileOutputStream(tableName + ".ser");
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(table);
        out.close();
        file.close();
    }

    static Table load_table(String tableName) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(tableName + ".ser");
        ObjectInputStream in = new ObjectInputStream(file);
        Table table = (Table) in.readObject();
        in.close();
        file.close();
        return table;
    }

    static void create_table(String tableName) throws IOException {

        for (Table table : tables) {
            if (table.name.equals(tableName)) {
                store_table(tableName);
                return;
            }
        }

        Table table = new Table(tableName, 0);
        tables.add(table);
    }

    static void add_attribute(String tableName, String attrType, String attrName)
            throws IOException {

        tables.get(getTableIndex(tableName)).addAttribute(attrType, attrName);

    }

    static void insert_into(int index, String values) {
        tables.get(index).insertInto(values);
    }

    static void select_star(String tableName) throws IOException, ClassNotFoundException {
        Table table = load_table(tableName);
        table.print();
    }

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
                store_table(tableName);

            } else if (parts[0].equals("insert_into")) {
                int index = getTableIndex(parts[1]);

                insert_into(index, parts[2].substring(1, parts[2].length() - 1));
                store_table(parts[1]);
            } else if (parts[0].equals("select_star")) {
                select_star(parts[1]);
            }

        }
        br.close();

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
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

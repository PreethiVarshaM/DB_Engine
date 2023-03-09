import java.util.Random;
import java.time.LocalDate;

public class generate_random_query {

    public static void main(String[] args) {
        Random rand = new Random();
        int length = 10; // The length of the random string
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // The characters to use
                                                                                              // for generating the
                                                                                              // random string
        Random rand1 = new Random();
        Random rand2 = new Random();

        String code = "";
        int n = 200;
        for (int i = 0; i < n; i++) {
            int year = rand2.nextInt(123) + 1900;

            // Generate a random month between 1 and 12
            int month = rand2.nextInt(12) + 1;

            // Generate a random day between 1 and the maximum number of days in the given
            // month and year
            int maxDay = LocalDate.of(year, month, 1).lengthOfMonth();
            int day = rand2.nextInt(maxDay) + 1;

            LocalDate randomDate = LocalDate.of(year, month, day);

            int year1 = rand2.nextInt(123) + 1900;

            // Generate a random month between 1 and 12
            int month1 = rand2.nextInt(12) + 1;

            // Generate a random day between 1 and the maximum number of days in the given
            // month and year
            int maxDay1 = LocalDate.of(year1, month1, 1).lengthOfMonth();
            int day1 = rand2.nextInt(maxDay1) + 1;

            LocalDate randomDate1 = LocalDate.of(year1, month1, day1);

            StringBuilder sb = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            for (int j = 0; j < length; j++) {
                int randomIndex = rand1.nextInt(characters.length());
                char randomChar = characters.charAt(randomIndex);
                sb.append(randomChar);
            }
            for (int j = 0; j < length; j++) {
                int randomIndex = rand1.nextInt(characters.length());
                char randomChar = characters.charAt(randomIndex);
                sb1.append(randomChar);
            }

            code += "insert into cs20b030_table2 (" + rand.nextInt(10000) + "," + rand.nextInt(10000) + ","
                    + sb.toString() + "," + sb1.toString() + "," + randomDate
                    + "," + randomDate1 + ")\n";
        }
        System.out.println(code);

    }
}

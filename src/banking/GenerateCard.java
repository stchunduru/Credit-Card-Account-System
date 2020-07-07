package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateCard {

    // generates new card that follows Luhn algorithm
    public String cardGenerate() {
        while (true) {
            Random random = new Random();
            String a = "400000";
            String nine = "";

            for (int x = 0; x < 9; x++) {
                nine += String.valueOf(random.nextInt(10));
            }
            String f = String.valueOf(random.nextInt(10));

            if (checkCard(a + nine + f)) {
                return a + nine + f;
            }
        }
    }

    // Checks to make sure card number is Luhn compliant
    public boolean checkCard(String a) {
        List<Integer> list = new ArrayList<>();

        for (int x = 0; x < a.length() - 1; x++) {
            int num = Integer.parseInt(String.valueOf(a.charAt(x)));
            if ((x + 1) % 2 != 0) {
                num *= 2;
            }
            if (num > 9) {
                num -= 9;
            }
            list.add(num);
        }
        list.add(Integer.parseInt(String.valueOf(a.charAt(a.length() - 1))));
        int sum = 0;
        for (int x : list) {
            sum += x;
        }
        return sum % 10 == 0;
    }

    // Generates simple 4 digit pin code
    public String pinGenerate() {
        Random random = new Random();
        String four = "";

        for (int x = 0; x < 4; x++) {
            four += String.valueOf(random.nextInt(10));
        }
        return four;
    }
}

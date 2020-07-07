package banking;

import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class LoginInOut {
    public Scanner reader = new Scanner(System.in);
    private boolean login = false;
    private boolean exit = false;
    private String url = "";
    private GenerateCard generate;
    private dbConnections db;

    private String loggedCard = null;
    private String loggedPin = null;

    public LoginInOut(String url) {
        this.url = url;
        this.generate = new GenerateCard();
        this.db = new dbConnections(new Random());
        db.createDB(url);
    }

    public Boolean getLogin() {
        return this.login;
    }

    public Boolean getExit() {
        return this.exit;
    }

    // Shows menu when logged
    public void loggedOut() throws SQLException {
        while (true) {
            System.out.println("\n1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            int input = Integer.parseInt(reader.nextLine());
            if (input == 1) {
                String card = generate.cardGenerate();
                while (db.checkCardExists(card, url)) {
                    card = generate.cardGenerate();
                }
                String pin = generate.pinGenerate();
                System.out.println("Your card has been created\nYour " +
                        "card number: \n" + card + "\nYour card PIN:\n" +
                        pin);
                db.insert(card, pin, url);
            } else if (input == 2) {
                System.out.println("Enter your card number:");
                String caard = reader.nextLine();
                System.out.println("Enter your PIN:");
                String piin = reader.nextLine();

                if (db.connectCard(caard, piin, url)) {
                    System.out.println("You have successfully logged in!");
                    login = true;
                    loggedCard = caard;
                    loggedPin = piin;
                    break;
                } else {
                    System.out.println("Wrong card number or PIN!");
                }
            } else if (input == 0) {
                System.out.println("\nBye!");
                exit = true;
                break;
            }
        }
    }

    // shows menu when logged in
    public void loggedIn() {
        while (true) {
            System.out.println("\n1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");
            int input = Integer.parseInt(reader.nextLine());
            if (input == 1) {
                System.out.println("Balance: " + db.balanceQuery(loggedCard, url));
            } else if (input == 2) {
                System.out.println("Enter income: ");
                int income = Integer.parseInt(reader.nextLine());
                db.addBalance(income, loggedCard, url);
                System.out.println("Income was added!");
            } else if (input == 3) {
                System.out.println("Transfer");
                System.out.println("Enter card number: ");
                String tcard = reader.nextLine();

                if (generate.checkCard(tcard)) {
                    if (db.checkCardExists(tcard, url)) {
                        System.out.println("Enter how much money you want to transfer: ");
                        int num = Integer.parseInt(reader.nextLine());
                        if (db.balanceQuery(loggedCard, url) - num < 0) {
                            System.out.println("Not enough money!");
                        } else {
                            db.addBalance(num * -1, loggedCard, url);
                            db.addBalance(num, tcard, url);
                            System.out.println("Success!");
                        }
                    } else {
                        System.out.println("Such a card does not exist.");
                    }
                } else {
                    System.out.println("Probably you made mistake in the card number. Please try again!");
                }
            } else if (input == 4) {
                db.destroyAccount(loggedCard, loggedPin, url);
                System.out.println("The account has been closed!");
                loggedPin = null;
                loggedCard = null;
                login = false;
                break;
            } else if (input == 5) {
                System.out.println("You have successfully logged out!");
                login = false;
                loggedCard = null;
                loggedPin = null;
                break;
            } else if (input == 0) {
                System.out.println("Bye!");
                exit = true;
                break;
            }
        }
    }
}

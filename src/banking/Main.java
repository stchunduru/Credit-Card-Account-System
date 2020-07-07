package banking;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        String url = urlMake(args);
        LoginInOut log = new LoginInOut(url);
        while (!log.getExit()) {
            if (!log.getLogin()) {
                log.loggedOut();
            } else {
                log.loggedIn();
            }
        }
    }

    // gets url
    public static String urlMake(String[] args) {
        String db = "";
        for (int x = 0; x < args.length; x++) {
            if (args[x].equals("-fileName")) {
                db = args[x + 1];
            }
        }
        String url = "jdbc:sqlite:" + db;
        return url;
    }

}

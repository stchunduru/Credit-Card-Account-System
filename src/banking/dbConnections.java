package banking;

import java.sql.*;
import java.util.Random;

public class dbConnections {
    private Random random;

    public dbConnections(Random random) {
        this.random = random;
    }

    //Creates a new database or connected to existing one. Creates a table "card" if it does not exist.
    public void createDB(String url) {
        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                        + "	id INTEGER PRIMARY KEY,\n"
                        + "	number TEXT,\n"
                        + "	pin TEXT,\n"
                        + "	balance INTEGER DEFAULT 0\n"
                        + ");";
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Returns simple connection to db
    public Connection connect(String url) {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Inserts an entry into card table
    public void insert(String card, String pin, String url) throws SQLException {
        String sql = "INSERT INTO card (id, number, pin, balance) VALUES (?,?,?,?);";
        try (Connection conn = connect(url)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int id = random.nextInt(500);
            while (true) {
                if (!checkIdExists(id, url)) {
                    break;
                }
                id = random.nextInt(500);
            }
            pstmt.setInt(1, id);
            pstmt.setString(2, card);
            pstmt.setString(3, pin);
            pstmt.setInt(4, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // checks to make sure card id does not exists
    public boolean checkIdExists(int a, String url) {
        String sql = "SELECT id FROM card WHERE id = ?";
        try (Connection conn = connect(url)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, a);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // checks db if card number exists
    public boolean checkCardExists(String tcard, String url) {
        String sql = "SELECT number FROM card WHERE number = ?";
        try (Connection conn = connect(url)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tcard);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // connects to account with card and returns whether it exists or not
    public boolean connectCard(String card, String pin, String url) {
        String sql = "SELECT * FROM card WHERE number = ? and pin = ?;";
        try (Connection conn = connect(url)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, card);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();
            // loop through the result set
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Wrong card number or PIN!");
            return false;
        }
    }

    // return balance on account
    public int balanceQuery(String loggedCard, String url) {
        String sql = "SELECT * "
                + "FROM card WHERE number = ?";
        try (Connection conn = connect(url)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, loggedCard);

            ResultSet rs = pstmt.executeQuery();
            int balance = rs.getInt("balance");
            return balance;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    //adds money to account
    public void addBalance(int num, String loggedCard, String url) {
        String sql = "UPDATE card SET balance = ? WHERE number = ?";
        try (Connection conn = connect(url)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, balanceQuery(loggedCard, url) + num);
            pstmt.setString(2, loggedCard);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // annihilates card account
    public void destroyAccount(String loggedCard, String loggedPin, String url) {
        String sql = "DELETE FROM card WHERE number = ? and pin = ?";
        try (Connection conn = connect(url)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, loggedCard);
            pstmt.setString(2, loggedPin);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

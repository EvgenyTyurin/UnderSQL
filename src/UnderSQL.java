import java.sql.*;
import java.util.Scanner;

/*
    Tool to run exercises of "Understanding SQL" book by Martin Gruber
    DBF files via JDBC ODBC
*/

public class UnderSQL {

    // Column's width
    private static final int COL_WIDTH = 15;

    // Task queries: [chapter][exercise number]
    private static final int CHAPTER_MAX = 30;
    private static final int EXERCISE_MAX = 10;
    private static String[][] queries = new String[CHAPTER_MAX][EXERCISE_MAX];

    // Run point
    public static void main(String[] args) {
        // Query collection init
        // Chapter 3
        queries[3][1] = "SELECT onum, amt, odate FROM Orders;";
        queries[3][2] = "SELECT * FROM Customer WHERE snum = 1001;";
        queries[3][3] = "SELECT city, sname, snum, comm FROM salesmen;";
        queries[3][4] = "SELECT rating, cname FROM Customer WHERE city = 'San Jose';";
        queries[3][5] = "SELECT DISTINCT snum FROM Orders;";
        // Chapter 4
        queries[4][1] = "SELECT * FROM Orders WHERE amt > 1000;";
        queries[4][2] = "SELECT sname, city FROM Salesmen WHERE comm > 0.1 AND city = 'London';";
        // Chapter 5
        queries[5][1] = "SELECT * FROM Orders WHERE odate IN (10/03/1990, 10/04/1990);";
        queries[5][2] = "SELECT * FROM Customers WHERE snum IN (1001, 1004);";
        queries[5][3] = "SELECT * FROM Customers WHERE cname BETWEEN 'A' AND 'H';";
        // Query collection show
        for (int chapterN = 1; chapterN < CHAPTER_MAX; chapterN++)
            for (int exerciseN = 1; exerciseN < EXERCISE_MAX; exerciseN++)
                if (queries[chapterN][exerciseN] != null)
                    System.out.println("Chapter №" + chapterN + " Exercise №" + exerciseN + "\n" +
                        queries[chapterN][exerciseN]);
        // User input cycle
        do {
            // Get chapter and exercise numbers from user
            Scanner con = new Scanner(System.in);
            System.out.println("Input chapter number");
            int chapterNum = con.nextInt();
            System.out.println("Input exercise number");
            int exerciseNum = con.nextInt();
            // If query exist - exec it
            // else show error
            if (queries[chapterNum][exerciseNum] != null) {
                System.out.println("Executing: " + queries[chapterNum][exerciseNum] + "\n");
                execQuery(queries[chapterNum][exerciseNum]);
            } else {
                System.out.println("No such exercise in collection.");
            }
            // Ask user for another SQL show or quit
            System.out.println("Another query? (y/n)");
            String exit = con.next();
            if (!exit.equals("y"))
                break;
        } while (true);
    }

    // Connect, exec a query to test database
    // and print result
    private static void execQuery(String query) {
        try {
            // Connect DB
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Connection con = DriverManager.getConnection("jdbc:odbc:dBASE Files");
            Statement stmt = con.createStatement();
            // Exec SQL
            ResultSet rs = stmt.executeQuery(query);
            // Show title
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int columnCount = 1; columnCount <= rsmd.getColumnCount(); columnCount++) {
                int colPos = (columnCount - 1) * COL_WIDTH + 1;
                System.out.format("%" + colPos + "s",
                        rsmd.getColumnName(columnCount));
            }
            System.out.print("\n");
            // Show data
            while (rs.next()) {
                for (int columnCount = 1; columnCount <= rsmd.getColumnCount(); columnCount++) {
                    int colPos = (columnCount - 1) * COL_WIDTH + 1;
                    String colName = rsmd.getColumnName(columnCount);
                    System.out.format("%" + colPos + "s",
                            rs.getString(colName));
                }
                System.out.print("\n\n");
            }
            // Job's done
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // static void ExecQuery(String query) {

}

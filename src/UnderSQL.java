import java.io.File;
import java.sql.*;
import java.util.Scanner;

/*
    Tool to run exercises of "Understanding SQL" book by Martin Gruber
    DBF files via JDBC ODBC
*/

public class UnderSQL {

    // Column's width
    private static final int COL_WIDTH = 15;
    // Collection size
    private static final int CHAPTER_MAX = 30;
    private static final int EXERCISE_MAX = 10;
    // File with SQL collection
    private static final String FILE_SQL = "src/sql.txt";
    // Task queries: [chapter number][exercise number]
    private static String[][] queries = new String[CHAPTER_MAX][EXERCISE_MAX];

    // Run point
    public static void main(String[] args) {
        // Load and show queries collection
        queriesReady();
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

    // Connect, exec a query to test database and print result
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
                    System.out.format("%" + colPos + "s",
                            rs.getString(columnCount));
                }
                System.out.print("\n\n");
            }
            // Job's done
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Read SQL queries collection from file and show it
    private static void queriesReady() {
        // Query collection init
        try {
            Scanner scanFile = new Scanner(new File(FILE_SQL));
            scanFile.useDelimiter("\n");
            while (scanFile.hasNext()) {
                String line = scanFile.next();
                String[] words = line.split("~");
                queries[Integer.valueOf(words[0])][Integer.valueOf(words[1])] = words[2];
            }
            scanFile.close();
        } catch (Exception e) {e.printStackTrace();}
        // Query collection show
        for (int chapterN = 1; chapterN < CHAPTER_MAX; chapterN++)
            for (int exerciseN = 1; exerciseN < EXERCISE_MAX; exerciseN++)
                if (queries[chapterN][exerciseN] != null)
                    System.out.println("Ch.№" + chapterN + " Ex.№" + exerciseN + " " +
                            queries[chapterN][exerciseN]);
        System.out.println("\n");
    }

}

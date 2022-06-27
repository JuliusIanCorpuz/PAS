import java.sql.*;

public class CustomDBFunctions extends Database {

    // check if table is empty
    public Boolean checkTableRows(String tableName) {
        Boolean emptyTable = true;
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pas", "root", "admin");) {
            PreparedStatement getRows = conn.prepareStatement("SELECT COUNT(*) as recordsCount FROM " + tableName);
            ResultSet queryRes = getRows.executeQuery();

            while (queryRes.next()) {
                int recordsCount = queryRes.getInt("recordsCount");
                if (recordsCount > 0) {
                    emptyTable = false;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Database error occured upon checking row count");
        }

        return emptyTable;
    }

}

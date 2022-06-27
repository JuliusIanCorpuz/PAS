import java.sql.*;
import java.util.*;

public class Database extends Validations{

    final protected String DB_URL_LOCALCHOST = "jdbc:mysql://localhost/";
    final protected String DB_MYSQLPORT = "jdbc:mysql://localhost:3306/";

    protected String db_username = "";
    protected String db_password = "";
    protected String db_schema_name = "policyandclaims";

    Scanner input = new Scanner(System.in);


    public void loadDBCredentials(){

        Boolean match = false;

        do {
            System.out.println("Please Input your MySQL Connection Credentials to create the Database");

            System.out.print("Input username: ");
            String dbUserName = input.nextLine();

            System.out.print("Input password: ");
            String dbPassword = input.nextLine();

            try (Connection conn = DriverManager.getConnection(DB_URL_LOCALCHOST, dbUserName, dbPassword);
                    Statement stmt = conn.createStatement()) {

                ResultSet resultSet = conn.getMetaData().getCatalogs();


                while(resultSet.next()){
                    String databaseName = resultSet.getString(1);
                    if(!databaseName.toLowerCase().equals("policyandclaims")){
                        String createDBStr = "CREATE DATABASE IF NOT EXISTS " + this.db_schema_name;
                        stmt.execute(createDBStr);
                        break;
                    } 
                    
                }
                
                this.db_username = dbUserName;
                this.db_password = dbPassword;

                match = true;

            } catch (SQLException e) {
                System.out.println("Invalid username or password" + e);
                match = false;
            }
        } while (!match == true);

        
    }

    public void setupDBTables() {

        try (Connection conn = DriverManager.getConnection(DB_MYSQLPORT + db_schema_name, db_username, db_password);
                Statement stmt = conn.createStatement()) {

            System.out.println("Creating tables");

            String createCustomerTable = "CREATE TABLE IF NOT EXISTS `customer` ("
                    + "`id` int(6) unsigned zerofill NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "`date_of_accident` date DEFAULT NULL,"
                    + "`accident_address` varchar(45) DEFAULT NULL,"
                    + "`description` varchar(45) DEFAULT NULL,"
                    + "`damage_to_vehicle` varchar(45) DEFAULT NULL,"
                    + "`repairs_cost` double DEFAULT NULL,"
                    + "`policy_id` int DEFAULT NULL)";

            stmt.executeUpdate(createCustomerTable);

            String createPolicyTable = "CREATE TABLE IF NOT EXISTS `policy` ("
                    + "`id` int(6) unsigned zerofill NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "`effective_date` date DEFAULT NULL,"
                    + "`expiration_date` date DEFAULT NULL,"
                    + "`policy_cost` double DEFAULT NULL,"
                    + "`customer_id` int DEFAULT NULL,"
                    + "`policy_holder_id` int DEFAULT NULL,"
                    + "`cancelled` tinyint DEFAULT NULL)";

            stmt.executeUpdate(createPolicyTable);

            String createPolicyHolderTable = "CREATE TABLE IF NOT EXISTS `policy_holder` ("
                    + "`id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "`last_name` varchar(45) DEFAULT NULL,"
                    + "`date_of_birth` date DEFAULT NULL,"
                    + "`policy_cost` double DEFAULT NULL,"
                    + "`address` varchar(45) DEFAULT NULL,"
                    + "`drivers_license` varchar(45) DEFAULT NULL,"
                    + "`drivers_license_issue_date` date DEFAULT NULL)";

            stmt.executeUpdate(createPolicyHolderTable);

            String createVehicleTable = "CREATE TABLE IF NOT EXISTS `vehicle` ("
                    + "`id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "`make` varchar(45) DEFAULT NULL,"
                    + "`model` varchar(45) DEFAULT NULL,"
                    + "`model_year` int DEFAULT NULL,"
                    + "`vehicle_type` varchar(45) DEFAULT NULL,"
                    + "`fuel_type` varchar(45) DEFAULT NULL,"
                    + "`purchase_price` double DEFAULT NULL,"
                    + "`color` varchar(45) DEFAULT NULL,"
                    + "`premium_charge` double DEFAULT NULL,"
                    + "`policy_id` int DEFAULT NULL)";

            stmt.executeUpdate(createVehicleTable);

            String createClaimTable = "CREATE TABLE IF NOT EXISTS `claim` ("
                    + "`id` int(6) unsigned zerofill NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "`date_of_accident` date DEFAULT NULL,"
                    + "`accident_address` varchar(45) DEFAULT NULL,"
                    + "`description` varchar(45) DEFAULT NULL,"
                    + "`damage_to_vehicle` varchar(45) DEFAULT NULL,"
                    + "`repairs_cost` double DEFAULT NULL,"
                    + "`policy_id` int DEFAULT NULL)";

            stmt.executeUpdate(createClaimTable);

            System.out.println("Tables successfully created.");

        } catch (SQLException ex) {
            System.out.println("Database error occur upon creating tables." + ex);
        }
        

    }

    public String getDBUsername(){
        return this.db_username;
    }

    public String getDBPassword(){
        return this.db_password;
    }

    public void setDBUserName(String dbUserName){
        this.db_username = dbUserName;
    }

    public void setDBPassword(String dbPassword){
        this.db_password = dbPassword;
    }

    public String getDBSchemaNAme(){
        return this.db_schema_name;
    }

    public void setDBSchemaName(String dbSchemaName){
        this.db_schema_name = dbSchemaName;
    }
    

}
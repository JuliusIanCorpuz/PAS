import java.sql.*;

public class Database extends Validations{

    final private String DB_URL_LOCALCHOST = "jdbc:mysql://localhost/";
    final private String DB_MYSQLPORT = "jdbc:mysql://localhost:3306/";
    final private String db_schema_name = "policyandclaims";

    private String db_username = "";
    private String db_password = "";


    //load mysql connection credentials, then create database schema if not existing
    public void loadDBCredentials(){

        Boolean match = false;

        do {
            System.out.println("Please Input the correct MySQL Connection Credentials setup the Database");

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
                        stmt.executeUpdate(createDBStr);
                        break;
                    } 
                    
                }
                
                this.db_username = dbUserName;
                this.db_password = dbPassword;

                match = true;

            } catch (SQLException e) {
                System.out.println("Invalid username or password");
                match = false;
            }
        } while (!match == true);
        
    }

    //create database tables if not existing
    public void setupDBTables() {

        try (Connection conn = DriverManager.getConnection(DB_MYSQLPORT + db_schema_name, db_username, db_password);
                Statement stmt = conn.createStatement()) {

            String createCustomerTable = "CREATE TABLE IF NOT EXISTS `customer` ("
                    + "`id` int(4) unsigned zerofill NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "`first_name` varchar(45) DEFAULT NULL,"
                    + "`last_name` varchar(45) DEFAULT NULL,"
                    + "`address` varchar(200) DEFAULT NULL)";

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
                    + "`first_name` varchar(45) DEFAULT NULL,"
                    + "`last_name` varchar(45) DEFAULT NULL,"
                    + "`date_of_birth` date DEFAULT NULL,"
                    + "`address` varchar(200) DEFAULT NULL,"
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
                    + "`id` int(5) unsigned zerofill NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "`date_of_accident` date DEFAULT NULL,"
                    + "`accident_address` varchar(200) DEFAULT NULL,"
                    + "`description` varchar(200) DEFAULT NULL,"
                    + "`damage_to_vehicle` varchar(200) DEFAULT NULL,"
                    + "`repairs_cost` double DEFAULT NULL,"
                    + "`policy_id` int DEFAULT NULL)";

            stmt.executeUpdate(createClaimTable);

            System.out.println("\nDatabase successfully setup.\n");

        } catch (SQLException ex) {
            System.out.println("Database error occur upon creating tables.");
        }
        
    }

    //return db connection username
    public String getDBUsername(){
        return this.db_username;
    }

    //return db connection password
    public String getDBPassword(){
        return this.db_password;
    }

    //set db connection username
    public void setDBUserName(String dbUserName){
        this.db_username = dbUserName;
    }

    //set db connection password
    public void setDBPassword(String dbPassword){
        this.db_password = dbPassword;
    }

    //return db connection schema name
    public String getDBSchemaNAme(){
        return this.db_schema_name;
    }

    //return mysql port
    public String GET_DB_MYSQLPORT(){
        return this.DB_MYSQLPORT;
    }
    
}
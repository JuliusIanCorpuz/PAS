
import java.sql.*;

public class PolicyHolder extends Customer {
    
    private int policy_holder_id;
    private java.sql.Date date_of_birth;
    private String drivers_license;
    private java.sql.Date drivers_license_issue_date;
    private int drivers_license_age;
    private int customer_id;

    //creating policy holder object
    public void createPolicyHolder(){
        super.createAccount();

        String dateOfBirth = validateDate("Date of Birth: ",dateOfBirth = "","","age");

        String driversLicense = validateEmptyString("Drivers License: ",driversLicense = "");
        
        String driversLicenseIssueDate = "";
        
        Boolean outOfRange = true;
        do{
            driversLicenseIssueDate = validateDate("Drivers License Issue Date: ",driversLicenseIssueDate, dateOfBirth,"licenseAge");
            
            if(checkDateRange(null, getCurrentDate(), driversLicenseIssueDate).equals("after")){
                System.out.println("Date out of range.");
            }else{
                outOfRange = false;
            }
        }while(outOfRange != false);
         

        this.date_of_birth = convertStringToDate(0,dateOfBirth);
        this.drivers_license = driversLicense;
        this.drivers_license_issue_date = convertStringToDate(0,driversLicenseIssueDate);
    }
    
    //saving policy holder to database
    public void savePolicyHolder(int customerID){
        try(
            Connection conn = DriverManager.getConnection(GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword());
            Statement stmt = conn.createStatement()){
            
            PreparedStatement savePolicyHolder = conn.prepareStatement("INSERT INTO policy_holder (first_name,last_name,date_of_birth,"
                                                                        + "address,drivers_license,drivers_license_issue_date,customer_id)"
                                                                        +"VALUES (?,?,?,?,?,?,?)");
            
            setCustomerID(customerID);

            savePolicyHolder.setString(1, super.getFirstName());
            savePolicyHolder.setString(2, super.getLastName());
            savePolicyHolder.setDate(3, this.date_of_birth);
            savePolicyHolder.setString(4, super.getAddress());
            savePolicyHolder.setString(5, this.drivers_license);
            savePolicyHolder.setDate(6, this.drivers_license_issue_date);
            savePolicyHolder.setInt(7, this.customer_id);

            savePolicyHolder.execute(); 

            String getLatestPolicyHolderID = "SELECT id FROM policy_holder ORDER BY id DESC LIMIT 1";
            ResultSet quereyRes = stmt.executeQuery(getLatestPolicyHolderID);

            while(quereyRes.next()){
                setPolicyHolderID(quereyRes.getInt("id"));
            }
            
        } catch (SQLException ex){
            System.out.println("Database error occured upon saving new policy holder" + ex);
        }
    }

    //check if user input policy holder id exists. If exists, allocate the retrieved data to the policy holder object
    public void getPolicyHolderbyID(){
        int policyHolderIDinput = 0;

        try(
            Connection conn = DriverManager.getConnection(GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword())){

            Boolean isExist = false;
            PreparedStatement getPolicyHolderAccount;
            ResultSet queryRes;
            
            do{
                policyHolderIDinput = intValidator("Please input an existing account policy holder id: ",policyHolderIDinput);

                getPolicyHolderAccount = conn.prepareStatement("SELECT * FROM policy_holder where id = " + policyHolderIDinput);
                queryRes = getPolicyHolderAccount.executeQuery(); 
        
                if(queryRes.next()){
                    System.out.println("Account successfully matched!\n");
                     this.policy_holder_id = queryRes.getInt("id");
                     super.setFirstName(queryRes.getString("first_name"));
                     super.setAddress(queryRes.getString("last_name"));
                     super.setLastName(queryRes.getString("address"));
                     this.date_of_birth = queryRes.getDate("date_of_birth");
                     this.drivers_license = queryRes.getString("drivers_license");
                     this.drivers_license_issue_date = queryRes.getDate("drivers_license_issue_date");
                     
                    isExist = true;
                } else {
                    System.out.println("Policy Holder ID = " +  policyHolderIDinput +" doesn't exist");
                }
            }while(!isExist);

        } catch(SQLException ex){
            System.out.println("Database error occured upon checking policy holder existence");
        }
    }
    
    public Boolean printAllPolicyHolderOfCustomer(int customerID){
        Boolean emptyTable = true;

        try(Connection conn = DriverManager.getConnection(GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword())){

            PreparedStatement getPolicyHolderCount = conn.prepareStatement("SELECT COUNT(*) as recordsCount FROM policy_holder WHERE customer_id = " + customerID);
            ResultSet recordsCountRes = getPolicyHolderCount.executeQuery(); 


            while (recordsCountRes.next()) {
                int recordsCount = recordsCountRes.getInt("recordsCount");
                if (recordsCount > 0) {
                    emptyTable = false;
                }
            }
            if(!emptyTable){
                PreparedStatement getPolicyHolder = conn.prepareStatement("SELECT * FROM policy_holder WHERE customer_id = " + customerID);
                ResultSet getPolicyHolderRes = getPolicyHolder.executeQuery(); 

                System.out.println("\nYou may refer for the list below to check the id of your desired Policy Holder\n");
                System.out.println("ID\t First Name\t\t Last Name\t\t License Issue Date");
                while(getPolicyHolderRes.next()){
                    int policyHolderID = getPolicyHolderRes.getInt("id");
                    String policyHolderFirstName = getPolicyHolderRes.getString("first_name");
                    String policyHolderLastName = getPolicyHolderRes.getString("last_name");
                    java.sql.Date DriversLicenseIssueDate = getPolicyHolderRes.getDate("drivers_license_issue_date");
                    System.out.format("%s %8s %23s %32s",policyHolderID, policyHolderFirstName, policyHolderLastName, DriversLicenseIssueDate);
                    System.out.println();
                }
            }
            

        } catch (SQLException ex){
            System.out.println("Database error upon printing all policy holder associated with a customer.");
        }

        return emptyTable;
    }

    //set drivers license age
    public void setDriversLicenseAge(){
        int driversLicenseIssueYear = Integer.parseInt(this.drivers_license_issue_date.toString().substring(0,4));
        this.drivers_license_age = currentYear - driversLicenseIssueYear;
        if(this.drivers_license_age <= 0){
            this.drivers_license_age = 1;
        }
    }

    //return license issue date
    public java.sql.Date getLicenseIssueDate(){
       return this.drivers_license_issue_date;
    }

    //return drivers license age
    public int getDriversLicenseAge(){
       return this.drivers_license_age;
    }
    
    //return policy holder id
    public int getPolicyHolderID(){
        return this.policy_holder_id;
    }

    //set policy holder id
    public void setPolicyHolderID(int id){
         this.policy_holder_id = id;
    }

    public void setCustomerID(int id){
        this.customer_id = id;
    }

}
    
import java.util.*;
import java.sql.*;

public class PolicyHolder extends Customer {
    
    final private Calendar calendar = Calendar.getInstance();
    private int policy_holder_id;
    private java.sql.Date date_of_birth;
    private String drivers_license;
    private java.sql.Date drivers_license_issue_date;
    private int drivers_license_age;

    //creating policy holder object
    public void createPolicyHolder(){
        super.createAccount();

        String dateOfBirth = validateDate("Date of Birth: ",dateOfBirth = "","","age");

        System.out.print("Drivers License: ");
        String driversLicense = validateEmptyString(driversLicense = "");
        
        String driversLicenseIssueDate = "";

        Boolean outOfRange = true;
        do{
            
            driversLicenseIssueDate = validateDate("Drivers License Issue Date: ",driversLicenseIssueDate, dateOfBirth,"licenseAge");
            
            if(checkDateRange(null, getCurrentDate(), driversLicenseIssueDate).equals("after")){
                System.out.println(checkDateRange(null, getCurrentDate(), driversLicenseIssueDate).equals("after"));
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
    public void savePolicyHolder(){
        try(
            Connection conn = DriverManager.getConnection(GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword());
            Statement stmt = conn.createStatement()){

            PreparedStatement savePolicyHolder = conn.prepareStatement("INSERT INTO policy_holder (first_name,last_name,date_of_birth,"
                                                                        + "address,drivers_license,drivers_license_issue_date)"
                                                                        +"VALUES (?,?,?,?,?,?)");

            savePolicyHolder.setString(1, super.getFirstName());
            savePolicyHolder.setString(2, super.getLastName());
            savePolicyHolder.setDate(3, this.date_of_birth);
            savePolicyHolder.setString(4, super.getAddress());
            savePolicyHolder.setString(5, this.drivers_license);
            savePolicyHolder.setDate(6, this.drivers_license_issue_date);
            
            savePolicyHolder.execute(); 

            String getLatestPolicyHolderID = "SELECT id FROM policy_holder ORDER BY id DESC LIMIT 1";
            ResultSet quereyRes = stmt.executeQuery(getLatestPolicyHolderID);

            while(quereyRes.next()){
                setPolicyHolderID(quereyRes.getInt("id"));
            }

            
        } catch (SQLException ex){
            System.out.println("Database error occured upon saving new policy holder");
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
                System.out.println("Please input an existing account policy holder id: ");
                policyHolderIDinput = intValidator(policyHolderIDinput);

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

    //return converted string type date to sql date
    public java.sql.Date convertStringToDate(int forExpDate, String date){   
        String [] dateArr = date.split("-");
        int year = Integer.parseInt(dateArr[0]);
        int month = forExpDate == 1 ?  Integer.parseInt(dateArr[1]) + 5 : Integer.parseInt(dateArr[1]) - 1;
        int day = Integer.parseInt(dateArr[2]);

        calendar.set(year, month, day);

        java.util.Date utilDate = calendar.getTime();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        return sqlDate;
    }

    //return license issue date
    public java.sql.Date getLicenseIssueDate(){
       return this.drivers_license_issue_date;
    }

    //return drivers license age
    public int getDriversLicenseAge(){
       return this.drivers_license_age;
    }

    //set drivers license age
    public void setDriversLicenseAge(){
        int driversLicenseIssueYear = Integer.parseInt(this.drivers_license_issue_date.toString().substring(0,4));
        this.drivers_license_age = currentYear - driversLicenseIssueYear;
        if(this.drivers_license_age <= 0){
            this.drivers_license_age = 1;
        }
    }

    //check date if within the effectivity and expiration
    public String checkDateRange(java.sql.Date date1, java.sql.Date date2, String strDate){
        String status = "";
        String date_1 = strDate.equals("") ?  date1.toString() : strDate ;
        String date_2 = date2.toString();

        if(date_1.compareTo(date_2) > 0){
            status = "after";
        }
        else if(date_1.compareTo(date_2) < 0){
            status = "before";
        }
        else if(date_1.compareTo(date_2) == 0){
            status = "equals";
        }

        return status;
    }
    
    //return policy holder id
    public int getPolicyHolderID(){
        return this.policy_holder_id;
    }

    //set policy holder id
    public void setPolicyHolderID(int id){
         this.policy_holder_id = id;
    }

}

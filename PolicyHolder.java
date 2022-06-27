import java.util.*;
import java.sql.*;
import java.text.*;

public class PolicyHolder extends Customer {
    
    Calendar calendar = Calendar.getInstance();

    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    
    private int policy_holder_id;
    private java.sql.Date date_of_birth;
    private String drivers_license;
    private java.sql.Date drivers_license_issue_date;
    private int drivers_license_age;

    //creating policy holder object
    public void createPolicyHolder(){
        super.createAccount();

        String dateOfBirth = validateDate("Date of Birth: ",dateOfBirth = "",true);

        System.out.print("Drivers License: ");
        String driversLicense = validateEmptyString(driversLicense = "");

        String driversLicenseIssueDate = validateDate("Drivers License Issue Date: "
                                                        ,driversLicenseIssueDate = "",false);

        this.date_of_birth = convertStringToDate(0,dateOfBirth);
        this.drivers_license = driversLicense;
        this.drivers_license_issue_date = convertStringToDate(0,driversLicenseIssueDate);
    }
    
    //saving policy holder to database
    public void savePolicyHolder(){
        try(
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin")){

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
            
        } catch (SQLException ex){
            System.out.println("Database error occured upon saving new policy holder");
        }
    }

    //check if user input policy holder id exists. If exists, allocate the retrieved data to the policy holder object
    public void getPolicyHolderbyID(){
        int policyHolderIDinput = 0;

        try(
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin")){

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
    }

    //check date if within the effectivity and expiration
    public Boolean checkDateRange(java.sql.Date dateVar){
        Boolean outOfRange = true;
        String currentDate = getCurrentDate().toString();
        String dateVarString = dateVar.toString();
        if (currentDate.compareTo(dateVarString) > 0){
                
            }

        return outOfRange;
    }
    
    //return policy holder id
    public int getPolicyHolderID(){
        return this.policy_holder_id;
    }

}

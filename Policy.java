import java.util.*;
import java.sql.*;

public class Policy extends PolicyHolder{

    Scanner input = new Scanner(System.in);

    Calendar calendar = Calendar.getInstance();

    private int policy_id;
    private java.sql.Date effective_date;
    private java.sql.Date expiration_date;
    private double policy_cost;
    private String policy_id_str;
    private String customer_id;
    private int policy_holder_id;
    private boolean cancelled;
    private String policy_status;

    //create policy object
    public void createPolicy(){
         
        Boolean outOfRange = false;

        do{
            String effectiveDateStr = validateDate("Effective Date: ",effectiveDateStr = "",false); 

            if
            
        }while(outOfRange != false);


        java.sql.Date effectiveDate = convertStringToDate(0,effectiveDateStr);
        java.sql.Date expirationDate = convertStringToDate(1,effectiveDateStr);
        
        this.effective_date = effectiveDate;
        this.expiration_date = expirationDate;
         
        System.out.println("Expiration date: " + this.expiration_date);
    }

    //check if user input policy id exists, if exists, allocate the retrieved data to the policy object
    public void checkPolicyIfExists(){
        int policyIdInput = 0;

        try(
            Connection conn = DriverManager.getConnection( GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword())){
            Boolean isExist = false;
            PreparedStatement getPolicy;
            ResultSet queryRes;
            
            do{
                System.out.println("Please input an existing policy id: ");
                policyIdInput = intValidator(policyIdInput = 0);

                getPolicy = conn.prepareStatement("SELECT * FROM policy where id = " + policyIdInput);
                queryRes = getPolicy.executeQuery(); 
        
                if(queryRes.next()){
                    this.policy_id = queryRes.getInt("id");
                    this.policy_id_str = idPadding(queryRes.getInt("id"), 6,0);
                    this.effective_date = queryRes.getDate("effective_date");
                    this.expiration_date = queryRes.getDate("expiration_date");
                    this.policy_cost = queryRes.getDouble("policy_cost");
                    this.customer_id = idPadding(queryRes.getInt("customer_id"), 4,0);
                    this.policy_holder_id = queryRes.getInt("policy_holder_id");
                    this.cancelled = queryRes.getBoolean("cancelled");
                    isExist = true;
                    
                } else {
                    System.out.println("Policy with ID = " +  policyIdInput +" doesn't exist");
                }
            }while(!isExist);

        } catch(SQLException ex){
            System.out.println("Database error occured upon checking policy holder existence");
        }
    }

    public void checkPolicyStatus(){
        String status = "";
        String policyStatus = checkDateRange(getCurrentDate(),this.expiration_date);
        if(this.cancelled){
            status = "cancelled";
            System.out.println("This Policy is already cancelled");
        }
        else if(policyStatus.equals("equal") || policyStatus.equals("after")){
            System.out.println("This Policy is already expired.");
            status = "expired";
        }

        this.policy_status = status;

    }

    //prompt the user for policy id, then update the cancelled field to true
    public void cancelPolicy(int policyID){
        try(
            Connection conn = DriverManager.getConnection(GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword())){

            PreparedStatement updatePolicy = conn.prepareStatement("UPDATE policy set cancelled = 1 where id = " + policyID);
            updatePolicy.executeUpdate();
            System.out.println("You have cancelled the policy with id =" + policyID);

        } catch(SQLException ex){
            System.out.println("Database error occured upon checking policy holder existence");
        }
    }

    //insert policy object to database
    public void savePolicy(int customerID, int policyHolderId){
        try(
            Connection conn = DriverManager.getConnection(GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword());
            Statement stmt = conn.createStatement()
        ){
            PreparedStatement savePolicyHolder = conn.prepareStatement("INSERT INTO policy (effective_date,expiration_date,"
                                                                        + "policy_cost,customer_id,policy_holder_id) VALUES (?,?,?,?,?)");
                                                                        
            savePolicyHolder.setDate(1, this.effective_date);
            savePolicyHolder.setDate(2, this.expiration_date);
            savePolicyHolder.setDouble(3, this.policy_cost);
            savePolicyHolder.setInt(4, customerID);
            savePolicyHolder.setInt(5, policyHolderId);
            
            savePolicyHolder.execute(); 

            String getLatestPolicyID = "SELECT id FROM policy ORDER BY id DESC LIMIT 1";
            ResultSet quereyRes = stmt.executeQuery(getLatestPolicyID);

            while(quereyRes.next()){
                setPolicyId(quereyRes.getInt("id"));
            }
            
        } catch (SQLException ex){
            System.out.println("Database Error occur upon saving the policy! " + ex);
        }
    }

    //print policy details
    public void printPolicyDetails(){
        if(this.cancelled){
            System.out.println("This Policy is already cancelled.");
        }
        System.out.println(this.customer_id);
        System.out.println("ID\t Effective Date\t Expiration Date\t Policy Cost\t Customer ID\t Policy Holder ID");
        System.out.println(this.policy_id_str + "\t " + this.effective_date + "\t " + this.expiration_date + "\t\t " 
                            + this.policy_cost + "\t "+ this.customer_id + "\t\t  " + this.policy_holder_id);
    }

    //set policy cost
    public void setPolicyCost(double policyCost){
        this.policy_cost = policyCost;
    }

    //return policy cost
    public double getPolicyCost(){
        return this.policy_cost;
    }

    //return policy id
    public int getPolicyId(){
        return this.policy_id;
    }
    
    //set policy id
    public void setPolicyId(int id){
        this.policy_id = id;
    }

    public String getPolicyStatus(){
        return this.policy_status;
    }

    //return policy effective date
    public java.sql.Date getPolicyEffectiveDate(){
        return this.effective_date;
    } 

    //return policy expiration date
    public java.sql.Date getPolicyExpirationDate(){
        return this.expiration_date;
    } 

}

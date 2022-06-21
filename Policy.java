import java.util.*;
import java.sql.*;

public class Policy extends PolicyHolder{

    Scanner input = new Scanner(System.in);

    Calendar calendar = Calendar.getInstance();

    private java.sql.Date effective_date;
    private java.sql.Date expiration_date;
    private double policy_cost;
    private String policy_id;
    private String customer_id;
    private int policy_holder_id;
    private boolean cancelled;

    public void createPolicy()
    {
        printSampleDateFormat();
        String effectiveDateStr = dateValidator("Effective Date: ",effectiveDateStr = "",false);  

        java.sql.Date effectiveDate = convertStringToDate(0,effectiveDateStr);
        java.sql.Date expirationDate = convertStringToDate(1,effectiveDateStr);
        
        this.effective_date = effectiveDate;
        this.expiration_date = expirationDate;
         
        System.out.println("Expiration date: " + this.expiration_date);
    }

    public int checkPolicyIfExists()
    {
        int policyIdInput = 0;

        try(
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin"); )
        {
            Boolean isExist = false;
            PreparedStatement getPolicy;
            ResultSet queryRes;
            
            do{
                System.out.println("Please input an existing policy id: ");
                policyIdInput = (int)Math.round(dataTypeValidator(policyIdInput = 0));

                getPolicy = conn.prepareStatement("SELECT * FROM policy where id = " + policyIdInput);
                queryRes = getPolicy.executeQuery(); 
        
                if(queryRes.next()){
                    policyIdInput = queryRes.getInt("id");
                    isExist = true;
                } else {
                    System.out.println("Policy with ID = " +  policyIdInput +" doesn't exist");
                }
            }while(!isExist);

        } catch(SQLException ex){
            System.out.println("Database error occured upon checking policy holder existence");
        }

        return policyIdInput; 
    }

    public void cancelPolicy(int policyID)
    {
        try(
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin"); )
        {

            PreparedStatement updatePolicy = conn.prepareStatement("UPDATE policy set cancelled = 1 where id = " + policyID);
            updatePolicy.executeUpdate();
            System.out.println("You have cancelled the policy with id =" + policyID);

        } catch(SQLException ex){
            System.out.println("Database error occured upon checking policy holder existence");
        }
    }

    public void savePolicy(int customerID, int policyHolderId)
    {
        try(
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin")
        ){
            PreparedStatement savePolicyHolder = conn.prepareStatement("INSERT INTO policy (effective_date,expiration_date,"
                                                                        + "policy_cost,customer_id,policy_holder_id) VALUES (?,?,?,?,?)");
                                                                        
            savePolicyHolder.setDate(1, this.effective_date);
            savePolicyHolder.setDate(2, this.expiration_date);
            savePolicyHolder.setDouble(3, this.policy_cost);
            savePolicyHolder.setInt(4, customerID);
            savePolicyHolder.setInt(5, policyHolderId);
            
            savePolicyHolder.execute(); 
            
        } catch (SQLException ex){
            System.out.println("Database Error occur upon saving the policy! " + ex);
        }
    }

    public void searchPolicyByID(int policyID)
    {
        try(Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
            ,"root", "admin"))
        {
            PreparedStatement getPolicy = conn.prepareStatement("SELECT * FROM policy where id = " + policyID);
            ResultSet queryRes = getPolicy.executeQuery(); 
            if(queryRes.next()){
                this.policy_id = idPadding(queryRes.getInt("id"), 6,0);
                this.effective_date = queryRes.getDate("effective_date");
                this.expiration_date = queryRes.getDate("expiration_date");
                this.policy_cost = queryRes.getDouble("policy_cost");
                this.customer_id = idPadding(queryRes.getInt("customer_id"), 4,0);
                this.policy_holder_id = queryRes.getInt("policy_holder_id");
                this.cancelled = queryRes.getBoolean("cancelled");
            }    

        }catch(SQLException ex){
            System.out.println("Database error occured upon searching policy by ID");
        }
    }

    public void printPolicyDetails()
    {
        if(this.cancelled){
            System.out.println("This Policy is already cancelled.");
        }
        System.out.println(this.customer_id);
        System.out.println("ID\t Effective Date\t Expiration Date\t Policy Cost\t Customer ID\t Policy Holder ID");
        System.out.println(this.policy_id + "\t " + this.effective_date + "\t " + this.expiration_date + "\t\t " 
                            + this.policy_cost + "\t "+ this.customer_id + "\t\t  " + this.policy_holder_id);
    }

    public void setPolicyCost(double policyCost)
    {
        this.policy_cost = policyCost;
    }

    
}

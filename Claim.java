import java.sql.*;
import java.util.*;

public class Claim extends Policy{

    private java.sql.Date date_of_accident;
    private String accident_address;
    private String description;
    private String damage_to_vehicle;
    private double cost_of_repairs;

    public void createClaim()
    {
        String dateOfAccident = dateValidator("Date of Accident: ",dateOfAccident = "");

        System.out.print("Accident Address: ");
        String accidentAddress = input.nextLine();

        System.out.print("Description: ");
        String description = input.nextLine();
        
        System.out.print("Damage to vehicle: ");
        String damageToVehicle = input.nextLine();

        System.out.print("Cost of repair: ");
        double costOfRepair = dataTypeValidator(costOfRepair = 0);

        this.date_of_accident = convertStringToDate(0,dateOfAccident);
        this.accident_address = accidentAddress;
        this.description = description;
        this.damage_to_vehicle = damageToVehicle;
        this.cost_of_repairs = costOfRepair;
    }

    public void fileClaim(int policyID)
    {
        try(Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
        ,"root", "admin"))
        {
            PreparedStatement fileClaim = conn.prepareStatement("INSERT INTO claim (date_of_accident,accident_address,description,damage_to_vehicle"
                                                                +",repairs_cost,policy_id) VALUES (?,?,?,?,?,?)");

            fileClaim.setDate(1, this.date_of_accident);
            fileClaim.setString(2, this.accident_address);
            fileClaim.setString(3, this.description);
            fileClaim.setString(4, this.damage_to_vehicle);
            fileClaim.setDouble(5, this.cost_of_repairs);
            fileClaim.setInt(6, policyID);

            fileClaim.execute();

            System.out.println("You have successfully claimed the policy.");

        } catch(SQLException ex){
            System.out.println("Database error occured upon filing a claim."+ex);
        }
    }

    public int checkClaimIfExists()
    {
        int claimID = 0;

        try(
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin"); )
        {
            Boolean isExist = false;
            PreparedStatement getClaim;
            ResultSet queryRes;
            
            do{
                System.out.println("Please input an existing claim id (Ex. C000006): ");
                String claimIDS = "";
                int claimIdInput = parseStrtoInt(claimIDS);
                
                getClaim = conn.prepareStatement("SELECT * FROM claim where id = " + claimIdInput);
                queryRes = getClaim.executeQuery(); 
                
                if(queryRes.next()){
                    claimID = queryRes.getInt("id");
                    isExist = true;
                } else {
                    System.out.println("Policy with ID = " +  claimID +" doesn't exist");
                }
            }while(!isExist);

        } catch(SQLException ex){
            System.out.println("Database error occured upon checking policy holder existence");
        }

        return claimID; 
    }
    
    

}

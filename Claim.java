import java.sql.*;

public class Claim extends Policy{

    private int claim_id;
    private java.sql.Date date_of_accident;
    private String accident_address;
    private String description;
    private String damage_to_vehicle;
    private double cost_of_repairs;
    private String claim_id_str;
    private int policy_id;

    //create claim object
    public void createClaim(java.sql.Date policyEffectiveDate, java.sql.Date policyExpirationDate){

        String dateOfAccident = "";
        
        printSampleDateFormat();

        Boolean outOfRange = true;
        do{
            dateOfAccident = validateDate("Date of Accident: ", dateOfAccident,"","");

            if(checkDateRange(null, policyEffectiveDate, dateOfAccident).equals("before")
            || checkDateRange(null, policyExpirationDate, dateOfAccident).equals("equals")
            || checkDateRange(null, policyExpirationDate, dateOfAccident).equals("after")
            || checkDateRange(null, getCurrentDate(), dateOfAccident).equals("after")){
                System.out.println("\nDate is out of range.\nDate of Accident must be dated between\nPolicy Effective Date and Expiration Date.\n" +
                                    "\nAnd must not be a future date.");
                outOfRange = true;
            } else {
                outOfRange = false;
            }
        }while(outOfRange != false);

        String accidentAddress = validateEmptyString("Accident Address: ",accidentAddress = "");

        String description = validateEmptyString("Description: ",description = "");
        
        String damageToVehicle = validateEmptyString("Damage to vehicle: ", damageToVehicle = "");

        double costOfRepair = doubleValidator("Cost of repair: ", costOfRepair = 0);

        this.date_of_accident = convertStringToDate(0,dateOfAccident);
        this.accident_address = accidentAddress;
        this.description = description;
        this.damage_to_vehicle = damageToVehicle;
        this.cost_of_repairs = costOfRepair;
    }

    //save claim object to database
    public void saveClaim(int policyID){
        
        setPolicyID(policyID);

        try(Connection conn = DriverManager.getConnection(GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword())){

            PreparedStatement saveClaim = conn.prepareStatement("INSERT INTO claim (date_of_accident,accident_address,description,damage_to_vehicle"
                                                                +",repairs_cost,policy_id) VALUES (?,?,?,?,?,?)");

            saveClaim.setDate(1, this.date_of_accident);
            saveClaim.setString(2, this.accident_address);
            saveClaim.setString(3, this.description);
            saveClaim.setString(4, this.damage_to_vehicle);
            saveClaim.setDouble(5, this.cost_of_repairs);
            saveClaim.setInt(6, policyID);

            saveClaim.execute();

            System.out.println("\nYou have successfully claimed your insurance.");

            PreparedStatement getLatestClaimInsert = conn.prepareStatement("SELECT * FROM claim ORDER BY id DESC LIMIT 1");
            ResultSet queryRes = getLatestClaimInsert.executeQuery(); 

            while(queryRes.next()){
                setClaimID(queryRes.getInt("id"));
            }

        } catch(SQLException ex){
            System.out.println("Database error occured upon filing a claim.");
        }
    }

    //check if user input claim id exists. If exists, allocate the retrieved data to the claim object
    public int searchClaim(){

        int tries = -1;

        try(
            Connection conn = DriverManager.getConnection( GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword()))
        {
            Boolean isExist = false;
            PreparedStatement getClaim;
            ResultSet queryRes;
            
            do{
                tries++;

                System.out.println("Maximum input trials = " + (5-tries));
                if(tries == 5){
                    System.out.println("\nYou have reached the max trials for searching a claim.\n"
                                        +"You can select 4 on the Menu to file a new claim. Thank you.\n");
                    return tries;
                }

                System.out.print("Please input an existing claim id (Ex. C00006): ");
                String claimIDS = "";
                int claimIdInput = parseIdStrtoInt(claimIDS);
                
                getClaim = conn.prepareStatement("SELECT * FROM claim where id = " + claimIdInput);
                queryRes = getClaim.executeQuery(); 
                
                if(queryRes.next()){
                    this.claim_id = queryRes.getInt("id");
                    this.claim_id_str = idPadding(queryRes.getInt("id"),5,1);
                    this.date_of_accident = queryRes.getDate("date_of_accident");
                    this.accident_address = queryRes.getString("accident_address");
                    this.description = queryRes.getString("description");
                    this.damage_to_vehicle = queryRes.getString("damage_to_vehicle");
                    this.cost_of_repairs  = queryRes.getDouble("repairs_cost");
                    this.policy_id = queryRes.getInt("policy_id");
                    isExist = true;
                } else {
                    System.out.println("\nClaim with ID = " +  claimIdInput +" doesn't exist\n");
                }
            }while(!isExist);

        } catch(SQLException ex){
            System.out.println("Database error occured upon searching a claim");
        }

        return tries;
    }

    //print claim object details
    public void printClaimDetails(){
        System.out.println("\nClaim ID: " + this.claim_id_str + "\n"
                        + "Date of Accident: " + this.date_of_accident + "\n"
                        + "Accident Address: " + this.accident_address + "\n"
                        + "Description: " + this.description + "\n"
                        + "Damage to Vehicle: " + this.damage_to_vehicle + "\n"
                        + "Cost of repairs: $" + this.cost_of_repairs + "\n"
                        + "Policy ID: " + idPadding(this.policy_id, 6, 0)  + "\n");
    }

    //return int type claim id
    public int getClaimId(){
        return this.claim_id;
    }

    //set claim object policy id
    public void setPolicyID(int id){
        this.policy_id = id;
    }

    //set claim object id
    public void setClaimID(int id){
        this.claim_id = id;
        this.claim_id_str = idPadding(this.claim_id, 5, 1);
    }


}

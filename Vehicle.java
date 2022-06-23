import java.util.*;
import java.sql.*;

public class Vehicle extends Policy{

    Scanner input = new Scanner(System.in);

    final static String [] colorArr = {"Red","Orange","Yellow","Green","Blue","Indigo","Violet"
                                        ,"Silver","White","Black","Pink","Gray","Gold","Brown"};
    final static String [] fuelTypeArr = {"Diesel","Electric","Petrol"};
    final static String [] vehicleTypeArr = {"Sedan","Sports","SUV","Truck"};

    private String make;
    private String model;
    private int model_year;
    private String type;
    private String fuel_type;
    private double purchase_price;
    private String color;
    private double premium_charge;

    public void createVehicle(){
        System.out.print("Make: ");
        String make = input.nextLine();

        System.out.print("Model: ");
        String model = input.nextLine();

        System.out.print("Purchased Year: ");
        int modelYear = intValidator(modelYear = 0);

        String type = validateChoice(vehicleTypeArr,type = "","Vehicle type");

        String fuelType = validateChoice(fuelTypeArr,fuelType = "","Fuel type");
        
        System.out.print("Purchase Price: ");
        double purchasePrice = doubleValidator(purchasePrice = 0);

        String color = validateChoice(colorArr,color = "","Color");

        this.make = make;
        this.model = model;
        this.model_year = modelYear;
        this.type = type;
        this.fuel_type = fuelType;
        this.purchase_price = purchasePrice;
        this.color = color;
    }

    public void saveVehicle(){
        try (
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin")){
                PreparedStatement statement = conn.prepareStatement("INSERT INTO vehicle (make,model,model_year,vehicle_type,fuel_type"
                                                                    + ",purchase_price,color,premium_charge,policy_id) VALUES (?,?,?,?,?,?,?,?,?)");

                statement.setString(1, this.make);
                statement.setString(2, this.model);
                statement.setInt(3, this.model_year);
                statement.setString(4, this.type);
                statement.setString(5, this.fuel_type);
                statement.setDouble(6, this.purchase_price);
                statement.setString(7, this.color);
                statement.setDouble(8, this.premium_charge);
                statement.setDouble(9, getLatestPolicyID());

                statement.execute();

            } catch (SQLException ex){
                System.out.println("Database error occured upon saving vehicle");
            }
    }

    public int getLatestPolicyID(){
        int lastPolicyId = 0;

        try (Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                            ,"root", "admin")){
            PreparedStatement getPolicyHolderID = conn.prepareStatement("SELECT id FROM policy ORDER BY id DESC LIMIT 1");
            ResultSet queryRes = getPolicyHolderID.executeQuery(); 

            while(queryRes.next()){
                lastPolicyId = queryRes.getInt("id");
            }
        } catch (SQLException ex){
            System.out.println("Database error occured upon getting the latest policy id");
        }

        return lastPolicyId;
    }

    public double getVehiclePrice(){
        return this.purchase_price;
    }

    public int getVehicleModelYear(){
        return this.model_year;
    }

    public void setPremiumCharge(double premiumCharge){
        this.premium_charge = premiumCharge;
    }

    public double getPremiumCharge(){
       return this.premium_charge;
    }

    public String validateChoice(String [] strArr, String choiceInput, String fieldName){
        Boolean choiceMatch = false;

        do{
            System.out.println("Input '" + fieldName + "' if you want to see the supported vehicle "+fieldName);
            System.out.print(fieldName + ": ");
            choiceInput = input.nextLine();

            for(int index = 0; index < strArr.length; index++){
                if(choiceInput.toLowerCase().equals(strArr[index].toLowerCase())){
                    choiceMatch = true;
                }
            }

            if(choiceInput.toLowerCase().equals(fieldName.toLowerCase())){
                printChoices(strArr, fieldName);
            }
            else if(choiceMatch == false){
                System.out.println("Input not matched. Please try again.");
            }
            
        }while(!choiceMatch);
        
        return color;
    }

    public void printChoices(String [] strArr, String field){

        System.out.println("\n"+field.toUpperCase());
        for(int index = 0; index < strArr.length; index++){
            System.out.println(strArr[index]);
        }
        System.out.println();
    }

}

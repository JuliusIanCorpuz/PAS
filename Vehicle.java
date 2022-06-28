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

    //create vehicle object
    public void createVehicle(){
        System.out.print("Make: ");
        String make = validateEmptyString(make = "");

        System.out.print("Model: ");
        String model = validateEmptyString(model = "");

        System.out.print("Model Year: ");
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

    //insert vehicle object to database
    public void saveVehicle(int policyId){
        try (
            Connection conn = DriverManager.getConnection(GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword())){
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
                statement.setDouble(9, policyId);

                statement.execute();

            } catch (SQLException ex){
                System.out.println("Database error occured upon saving vehicle" + ex);
            }
    }

    //return vehicle purchase price
    public double getVehiclePrice(){
        return this.purchase_price;
    }

    //return vehicle model year
    public int getVehicleModelYear(){
        return this.model_year;
    }

    //set vehicle premium charge
    public void setPremiumCharge(double premiumCharge){
        this.premium_charge = premiumCharge;
    }

    //return vehicle premium charge
    public double getPremiumCharge(){
       return this.premium_charge;
    }

    //validate user input string if within the given choices. If has match, return the user input
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
        
        return choiceInput;
    }

    //print choices for specified field
    public void printChoices(String [] strArr, String field){

        System.out.println("\n"+field.toUpperCase());
        for(int index = 0; index < strArr.length; index++){
            System.out.println(strArr[index]);
        }
        System.out.println();
    }

}

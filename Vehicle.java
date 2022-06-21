import java.util.*;
import java.sql.*;

public class Vehicle extends Policy{

    Scanner input = new Scanner(System.in);

    final static String [] colorArr = {"red","orange","yellow","green","blue","indigo","violet"
                                        ,"silver","white","black","pink","gray","gold"};

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
        int modelYear = (int)Math.round(dataTypeValidator(modelYear = 0));

        input.nextLine();
        System.out.print("Type: ");
        String type = input.nextLine();

        System.out.print("Fuel type: ");
        String fuelType = input.nextLine();
        
        System.out.print("Purchase Price: ");
        double purchasePrice = dataTypeValidator(purchasePrice = 0);

        input.nextLine();
        System.out.print("Color: ");
        String color = validateColor(color = "");

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
            ,"root", "admin")
        
            )
            {
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

        try (Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas","root", "admin"))
        {
            PreparedStatement getPolicyHolderID = conn.prepareStatement("SELECT id FROM policy ORDER BY id DESC LIMIT 1");
            ResultSet queryRes = getPolicyHolderID.executeQuery(); 

            while(queryRes.next()){
                lastPolicyId = queryRes.getInt("id");
            }
        } catch (SQLException ex)
        {
            System.out.println("Database error occured upon getting the latest policy id");
        }

        return lastPolicyId;
    }

    public  double dataTypeValidator(double num){
        boolean invalid;
        do{
            try{
                num = input.nextInt();
                invalid = false;
            } catch(InputMismatchException e){
                input.nextLine();
                System.out.println("Invalid Input");
                invalid = true;
            }
        }while(invalid == true);
        return num;
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

    public  String validateColor(String color){
        Boolean colorMatch = false;
        do{
            System.out.println("Input 'color' if you want to see the supported vehicle colors");

            color = input.nextLine();

            

            for(int index = 0; index < colorArr.length; index++){
                if(color.equals(colorArr[index])){
                    colorMatch = true;
                }
            }

            if(color.toLowerCase().equals("color")){
                printVehicleColors();
            }
            else if(colorMatch == false){
                System.out.println("No color matched. Please try again.");
            }

        }while(!colorMatch);
        
        return color;
    }

    public void printVehicleColors(){
        for(int index = 0; index < colorArr.length; index++){
            System.out.print(colorArr[index] + "\t");
        }
        
    }

}

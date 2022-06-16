import java.util.*;
import java.sql.*;

public class Vehicle extends Policy{

    Scanner input = new Scanner(System.in);

    private String make;
    private String model;
    private int purchased_year;
    private String type;
    private String fuel_type;
    private double purchase_price;
    private String color;
    private double premium_charge;

    public void createVehicle()
    {
        System.out.print("Make: ");
        String make = input.nextLine();

        System.out.print("Model: ");
        String model = input.nextLine();

        System.out.print("Purchased Year: ");
        int purchasedYear = (int)Math.round(dataTypeValidator(purchasedYear = 0));

        input.nextLine();
        System.out.print("Type: ");
        String type = input.nextLine();

        System.out.print("Fuel type: ");
        String fuelType = input.nextLine();
        
        System.out.print("Purchase Price: ");
        double purchasePrice = dataTypeValidator(purchasePrice = 0);

        input.nextLine();
        System.out.print("Color: ");
        String color = input.nextLine();

        this.make = make;
        this.model = model;
        this.purchased_year = purchasedYear;
        this.type = type;
        this.fuel_type = fuelType;
        this.purchase_price = purchasePrice;
        this.color = color;
    }

    public void saveVehicle()
    {
        try (
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
            ,"root", "admin")
        
            )
            {
                PreparedStatement statement = conn.prepareStatement("INSERT INTO vehicle (make,model,purchase_year,vehicle_type,fuel_type"
                                                                    + ",purchase_price,color,premium_charge,policy_id) VALUES (?,?,?,?,?,?,?,?,?)");

                statement.setString(1, this.make);
                statement.setString(2, this.model);
                statement.setInt(3, this.purchased_year);
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

    public int getLatestPolicyID()
    {
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

    public double getVehiclePrice()
    {
        return this.purchase_price;
    }

    public int getVehiclePurchasedYear()
    {
        return this.purchased_year;
    }

    public void setPremiumCharge(double premiumCharge)
    {
        this.premium_charge = premiumCharge;
    }

    public double getPremiumCharge()
    {
       return this.premium_charge;
    }

}

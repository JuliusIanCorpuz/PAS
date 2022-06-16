import java.sql.Date;
import java.util.Calendar;

public class RatingEngine extends DatabaseController{

    Calendar calendar = Calendar.getInstance();

    final int currentYear = calendar.get(Calendar.YEAR);

    public double getVehiclePremium(double vehiclePurchase, int yearPurchased, Date driversLicenseIssueDate)
    {
        int driversLicenseIssueYear = Integer.parseInt(driversLicenseIssueDate.toString().substring(0,4));
        double vehiclePremium = 0;
        int driversLicenseAge = currentYear - driversLicenseIssueYear;
        vehiclePremium = (vehiclePurchase * getVehiclePriceFactor(yearPurchased)) + ((vehiclePurchase/100)/driversLicenseAge);
        
        return vehiclePremium;
    }

    public double getVehiclePriceFactor(int yearPurchased)
    {
        double vehiclePriceFactor = 0;
        int vehicleAge = currentYear - yearPurchased;

        if(vehicleAge < 1){
            vehiclePriceFactor = 0.01;
        }

        else if(vehicleAge < 3){
            vehiclePriceFactor = 0.008;
        }

        else if(vehicleAge < 5){
            vehiclePriceFactor = 0.007;
        }

        else if(vehicleAge < 10){
            vehiclePriceFactor = 0.006;
        }

        else if(vehicleAge < 15){
            vehiclePriceFactor = 0.004;
        }

        else if(vehicleAge < 20){
            vehiclePriceFactor = 0.002;

        } else {
            vehiclePriceFactor = 0.001;
        }

        return vehiclePriceFactor;
    }

}

import java.util.Calendar;

public class RatingEngine extends CustomDBFunctions{

    Calendar calendar = Calendar.getInstance();

    final int currentYear = calendar.get(Calendar.YEAR);

    //calculate vehicle premium base on the given parameters
    public double getVehiclePremium(double vehiclePurchase, int yearPurchased, int driversLicenseAge){
        
        double vehiclePremium = 0;
        vehiclePremium = (vehiclePurchase * getVehiclePriceFactor(yearPurchased)) + ((vehiclePurchase/100)/driversLicenseAge);
        
        return vehiclePremium;
    }

    //return vehicle price factor depending on vehicle age
    public double getVehiclePriceFactor(int yearPurchased){
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

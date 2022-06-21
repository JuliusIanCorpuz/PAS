import java.util.*;

public class PASDriver {

    public static Scanner input = new Scanner(System.in);
    
    public static final StringBuilder res = new StringBuilder();

    public static void main(String[] args){

        System.out.println("Automobile Insurance Policy and Claims Administration System\n");

        int choice = 0;

        do{

            Customer customer = new Customer();
            Policy policy = new Policy();
            int policyID = 0;
            
            choice = menu();

            switch(choice){ 
                
                case 1:
                    addAccount(customer);
                    break;
                case 2:
                    if(!customer.checkTableRow("customer")){
                        int customerAccountID = 0;
                        int policyHolderId = 0;
                        double policyPrice = 0;

                        customerAccountID = customer.checkAccountIfExist(customerAccountID);
                        System.out.println("Creating Policy");
                        policy.createPolicy();
                        PolicyHolder policyHolder = new PolicyHolder();
                        
                        if(policyHolderChoice().equals("new")) {
                            System.out.println("Create a new Policy Holder");
                            policyHolder.createPolicyHolder();
                        } else {
                            if(!policyHolder.checkPolicyHolderRow()){
                                int policyHolderIDinput = 0;
                                policyHolderId = policyHolder.getPolicyHolderbyID(policyHolderIDinput);
                            } else {
                                System.out.println("There are no policy Holder saved. Do you want to create new Policy Holder?");
                                System.out.println("Input 'yes' to CREATE new, input any key to EXIT Policy Holder creation.");
                                String confirmStr = input.next();
                                if(confirmStr.equals("yes")){
                                    policyHolder.createPolicyHolder();
                                } else {
                                    System.out.println("Policy Holder creation cancelled.");
                                    break;
                                }
                            }
                        }

                        policyHolder.setDriversLicenseAge();
                        
                        System.out.print("Number of vehicles: ");
                        int numOfVehicles = (int)Math.round(dataTypeValidator(numOfVehicles = 0));

                        Vehicle[] vehiclesArr = new Vehicle[numOfVehicles];

                        policyPrice = addVehicle(vehiclesArr, policyHolder.getDriversLicenseAge());

                        System.out.println("Policy details are now COMPLETE. Are you sure you want to BUY this policy?");
                        System.out.println("Derived policy premium: " + policyPrice);
                        System.out.println("Input 'yes' to BUY the policy, input any key to CANCEL the policy creation.");
                        System.out.println("Note: Newly input Policy, Policy Holder and Vehicle/s will NOT be saved when you CANCEL buying the policy.");
                        String confirmStr = input.next();
                        if(confirmStr.toLowerCase().equals("yes")){
                            if(policyHolderId == 0){
                                policyHolder.savePolicyHolder();
                            }
                            policyHolderId = policyHolderId == 0 ? policyHolder.getLatestPolicyHolder() : policyHolderId;
                            policy.setPolicyCost(policyPrice);
                            policy.savePolicy(customerAccountID, policyHolderId);
                            saveVehicletoDB(vehiclesArr);

                            System.out.println("CONGRATULATIONS! YOU HAVE SUCCESSFULLY BOUGHT A POLICY.\n");
                        } else {
                            System.out.println("Transaction Cancelled.");
                            break;
                        }

                    } else {
                        printEmptyTable("Customer");
                    }
                    break;
                case 3:
                    System.out.println("Cancel a Policy");
                    if(!policy.checkTableRow("policy")){
                        policyID = policy.checkPolicyIfExists();
                        policy.cancelPolicy(policyID);
                    } else {
                        printEmptyTable("Policy");
                    }
                    break;
                case 4:
                    Claim claim = new Claim();
                    System.out.println("File a Claim");
                    if(!policy.checkTableRow("policy")){

                        policyID = policy.checkPolicyIfExists();

                        if(policyID > 0 ){
                            claim.createClaim();
                            claim.fileClaim(policyID);
                        }
                    } else {
                        printEmptyTable("Policy");
                    }
                    
                    break;
                case 5:
                    System.out.println("Search Customer");
                    if(!customer.checkTableRow("customer")){
                        String customerIdStr = customer.searchCustomerByName();

                        if(customerIdStr != ""){
                            customer.printCustomerDetails(customerIdStr);
                        }
                    } else {
                        printEmptyTable("Customer");
                    }
                    break;
                case 6:
                    System.out.println("Search Policy");
                    if(!policy.checkTableRow("policy")){
                        policyID = policy.checkPolicyIfExists();

                        if(policyID > 0){
                            policy.searchPolicyByID(policyID);
                            policy.printPolicyDetails();
                        }

                    } else {
                        printEmptyTable("Policy");
                    }
                    break;
                case 7:
                    
                    claim = new Claim();

                    System.out.println("Search Claim\n");
                    
                    if(!claim.checkTableRow("claim")){
                        claim.searchClaim();
                        claim.printClaimDetails();
                    } else {
                        printEmptyTable("Claim");
                    }

                    break;
                case 8:
                    System.out.println("Exiting Application ");
                    printProgressBar();
                    System.exit(0);
            }

        }while(choice != 8);
    }

    public static int menu(){

        String [] choiceList = {
            "Create a new Customer Account",
            "Get a policy quote and buy the policy.",
            "Cancel a specific policy.",
            "File an accident claim against a policy.",
            "Search for a Customer account",
            "Search for and display a specific policy",
            "Search for and display a specific claim",
            "Exit the PAS System"
        };

        int menuChoice = 0;
        
        System.out.println("Policy and Claims Administration System Menu:\n");

        for(int index = 0; index < choiceList.length; index++)
        {   
            System.out.println((index+1) + ". " + choiceList[index]);
        }

        System.out.print("\nPlease input the number of your desired transaction: ");

        do{
            menuChoice = (int)Math.round(dataTypeValidator(menuChoice));
            if(menuChoice < 0 || menuChoice > 8){
                System.out.println("Input choice is out of range, please try again.");
            }
        } while((menuChoice < 1) || (menuChoice > choiceList.length));

        return menuChoice;
    }

    //validation for user input data type
    public static double dataTypeValidator(double num){
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

    public static void addAccount(Customer customer){
            customer.createAccount();
                
            System.out.println("Are you sure you want to save this customer?");
            System.out.println("Input 'yes' to save. Input any key to exit customer creation.");
            String confirmStr = input.next();
            if(confirmStr.equals("yes")){
                customer.saveCustomer();
            } else {
                System.out.println("Customer Account creation cancelled.");
            }
    }

    public static String policyHolderChoice(){
    	String choice;
        input.nextLine();
    	do {
    		System.out.println("\nInput 'new' to create new Policy Holder. Input 'link' to link existing Policy Holder your policy. ");
    		
    		choice = input.nextLine(); 
    		if(!choice.equals("new") && !choice.equals("link")) {
    			System.out.println("Invalid input");
    		}
    	}while(!choice.equals("new") && !choice.equals("link"));

    	return choice;
    }

    public static double  addVehicle(Vehicle[] vehiclesArr,int driversLicenceAge){
        double policyPrice = 0;

        for(int index = 0; index < vehiclesArr.length; index++)
        {
            vehiclesArr[index]  = new Vehicle();
            System.out.println("Creating vehicle #"+ (index + 1));
            vehiclesArr[index].createVehicle();
            vehiclesArr[index].setPremiumCharge(vehiclesArr[index].getVehiclePremium(vehiclesArr[index].getVehiclePrice(), vehiclesArr[index].getVehicleModelYear(), driversLicenceAge));
            System.out.println("Vehicle premium cost:" + vehiclesArr[index].getPremiumCharge());
            System.out.println("Are you sure you want to add and save this to your Policy?");
            System.out.println("Input 'yes' to add and save. Input any key to re-input the vehicle.");
            policyPrice += vehiclesArr[index].getPremiumCharge();
            String confirmStr = input.next();
            if(confirmStr.toLowerCase().equals("yes")){
                System.out.println("Vehicle Saved!\n" );
            } else {
                System.out.println("Vehicle Removed!\n" );
                index -= 1;
            }
        }

        return policyPrice;
    }

    public static void saveVehicletoDB(Vehicle[] vehiclesArr){
        for(int index = 0; index < vehiclesArr.length; index++)
        {
            vehiclesArr[index].saveVehicle();
        }
    }

    public static String progress(int pct){
        res.delete(0, res.length());
        int numPounds = (pct + 9) / 10;
        for (int progress = 0 ; progress != numPounds ; progress++) {
            res.append('#');
        }
        while (res.length() != 10) {
            res.append(' ');
        }
        return res.toString();
    }

    public static void printProgressBar(){
        for (int counter = 0 ; counter <= 100 ; counter++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(String.format("[%s]%d%%\r", progress(counter), counter));
             if(counter == 100){
                System.out.print("\033[H\033[2J");  
                System.out.flush();
             }
        }
    }

    public static void printEmptyTable(String tableName){
        System.out.println("There are no "+ tableName +" saved. Please create a "+tableName+" first.\n");
    }


}

import java.util.*;

public class PASDriver {

    public static Scanner input = new Scanner(System.in);
    
    public static void main(String[] args){

        Customer customer;
        
        int choice = menu();

        switch(choice){
            case 1:
                customer = new Customer();
                customer.createAccount();
                break;
            case 2:
                System.out.println("get a policy");
                break;
            case 3:
                System.out.println("cancel a policy");
                break;
            case 4:
                System.out.println("file claim");
                break;
            case 5:
                System.out.println("search customer");
                break;
            case 6:
                System.out.println("search policy");
                break;
            case 7:
                System.out.println("search claim");
        }
    }

    public static int menu()
    {
        String [] choiceList = {
            "Create a new Customer Account",
            "Get a policy quote and buy the policy.",
            "Cancel a specific policy (i.e change the expiration date of a policy to an earlier date than originally specified)",
            "File an accident claim against a policy. All claims must be maintained by system and should be searchable.",
            "Search for a Customer account",
            "Search for and display a specific policy",
            "Search for and display a specific claim",
            "Exit the PAS System"
        };
        int menuChoice = 0;

        System.out.println("Please enter the number of your choice");
        for(int index = 0; index < choiceList.length; index++)
        
        {   
            System.out.println((index+1) + ". " + choiceList[index]);
        }

        do{
            menuChoice = (int)Math.round(dataTypeValidator(menuChoice));
            if(menuChoice < 0 || menuChoice > 8){
                System.out.println("Input choice is out of range, please try again.");
            }
        } while((menuChoice < 1) || (menuChoice > 8));

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

    //validation for user input choice
    //  public static boolean choiceChecker(String [] choiceArr, double choiceInput)
    //  {   
    //      Boolean isValid = false;
    //      for(int index = 0; index < choiceArr.length; index ++){
    //          if(choiceInput == choiceArr[index]){
    //              isValid = true;
    //          } 
    //      }
    //      if(!isValid){
    //          System.out.println("Invalid Input, please try again");
    //      }
    //      return isValid;
    //  }
}

import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Validations {
    
    public Scanner input = new Scanner(System.in);

    public Boolean checkIfMinor(String dateStr){

        Boolean minor = true;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateLD = LocalDate.parse(dateStr, dateFormat);
        Period age = Period.between(dateLD, LocalDate.now());
        if (age.getYears() < 17) {
            minor =  true;
        } else {
            minor =  false;
        }

        return minor;
    }

    public String idPadding(int id, int maxDigits, int claim){
        String stringId = "";

        if(claim > 0){
            stringId = String.format("C%0"+maxDigits+"d", id);
        } else {
            stringId = String.format("%0"+maxDigits+"d", id);
        }
        return stringId;
    }

    public int parseIdStrtoInt(String idStr){
        int idStrIntVal = 0;
        Boolean invalid;

        do{
            try{
                idStr = input.nextLine();
                if(idStr.substring(0,1).toUpperCase().equals("C")){
                    if(idStr.length() == 7){
                        idStrIntVal = Integer.parseInt(idStr.substring(1));
                        invalid = false;
                    } else {
                        System.out.println("Claim ID must be 6 digits of length");
                        invalid = true;
                    }
                } else {
                    System.out.println("Claim ID must start with 'C'");
                    invalid = true;
                }
                
            } catch(NumberFormatException ex){
                System.out.println("Invalid Input");
                invalid = true;
            } catch(StringIndexOutOfBoundsException e){
                System.out.println("Invalid Input");
                invalid = true;
            }
        } while(invalid == true);

        return idStrIntVal;
    }

    public String validateDate(String dateLabel, String inputDate, Boolean forAgechecking){
        String dateFormat = "^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$";
        Boolean invalidDate = true;
        
        if(forAgechecking){
            do{
                printSampleDateFormat();
                System.out.print(dateLabel);

                inputDate = input.nextLine();
                
                if(inputDate.matches(dateFormat)){
                    if(!checkIfMinor(inputDate)){
                        invalidDate = false;
                    } else {
                        System.out.println("Policy Holder must be 17 years old or above.");
                    }
                } else {
                    System.out.println("Invalid input1");
                }
            }while(invalidDate);
        } else {
            do{
                System.out.print(dateLabel);
                inputDate = input.nextLine();
                if(inputDate.matches(dateFormat)){
                    invalidDate = false;
                } else {
                    System.out.println("Invalid input2");
                }
            }while(invalidDate);
        }

        return inputDate;
    }
    
    //validation double data input by the user
    public double doubleValidator(double num){
        boolean invalid;
        do{
            try{
                num = input.nextDouble();
                invalid = false;
            } catch(InputMismatchException e){
                input.nextLine();
                System.out.println("Invalid Input");
                invalid = true;
            }
        }while(invalid == true);
        return num;
    }

    //validation int data input by the user
    public int intValidator(int num){
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

    public void printSampleDateFormat(){
        System.out.println("Please input date in this format 'YYYY-MM-DD'");
    }

}

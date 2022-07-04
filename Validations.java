import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Validations {

    final private Calendar calendar = Calendar.getInstance();
    
    public Scanner input = new Scanner(System.in);

    //validate policy holder age
    public Boolean invalidAge(String dateStr, String birthDate, String dateUse){

        Boolean invalidAge = true;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateLD = LocalDate.parse(dateStr, dateFormat);
        LocalDate birthdDateLD = !birthDate.equals("") ? LocalDate.parse(birthDate, dateFormat) : null;
        Period age = null;
        if(dateUse.equals("age")){
            age = Period.between(dateLD, LocalDate.now());
            int ageYears = age.getYears();
            if (ageYears <= 17) {
                System.out.println("\nInvalid Input Date. Age must be 17 years or older to acquire Drivers Licensed. \n"); 
                invalidAge =  true;
            } else {
                invalidAge =  false;
            }
        }

        if(dateUse.equals("licenseAge")){
            age = Period.between(birthdDateLD, dateLD);
            int ageYears = age.getYears();
            if (ageYears < 17) {
                System.out.println("\nInvalid Input Date. Age must be 17 years or older to acquire Drivers Licensed. \n"); 
                invalidAge =  true;
            } else {
                invalidAge =  false;
            }
        }
        

        return invalidAge;
    }

    //return string type id with padding
    public String idPadding(int id, int maxDigits, int claim){
        String stringId = "";

        if(claim > 0){
            stringId = String.format("C%0"+maxDigits+"d", id);
        } else {
            stringId = String.format("%0"+maxDigits+"d", id);
        }
        return stringId;
    }

    //parse string type id to int type id
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

    //validate user input string date 
    public String validateDate(String dateLabel, String inputDate, String birthDate, String dateUse){
        String dateFormat = "^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$";
        Boolean invalidDate = true;
        
        if(!dateUse.equals("")){
            do{
                printSampleDateFormat();
                System.out.print(dateLabel);

                inputDate = input.nextLine();
                
                if(inputDate.matches(dateFormat)){
                    if(!invalidAge(inputDate, birthDate,dateUse)){
                        invalidDate = false;
                    } else {
                        invalidDate = true;
                    }
                } else {
                    System.out.println("Invalid input");
                }
            }while(invalidDate);
        } else {
            do{
                System.out.print(dateLabel);
                inputDate = input.nextLine();
                if(inputDate.matches(dateFormat)){
                    invalidDate = false;
                } else {
                    System.out.println("Invalid input");
                }
            }while(invalidDate);
        }

        return inputDate;
    }

     //check date if within the effectivity and expiration
     public String checkDateRange(java.sql.Date date1, java.sql.Date date2, String strDate){
        String status = "";
        String date_1 = strDate.equals("") ?  date1.toString() : strDate ;
        String date_2 = date2.toString();
        
        if(date_1.compareTo(date_2) > 0){
            status = "after";
        }
        else if(date_1.compareTo(date_2) < 0){
            status = "before";
        }
        else if(date_1.compareTo(date_2) == 0){
            status = "equals";
        }

        return status;
    }

    //return converted string type date to sql date
    public java.sql.Date convertStringToDate(int forExpDate, String date){   
        String [] dateArr = date.split("-");
        int year = Integer.parseInt(dateArr[0]);
        int month = forExpDate == 1 ?  Integer.parseInt(dateArr[1]) + 5 : Integer.parseInt(dateArr[1]) - 1;
        int day = Integer.parseInt(dateArr[2]);

        calendar.set(year, month, day);

        java.util.Date utilDate = calendar.getTime();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        return sqlDate;
    }
    
    //validation double data input by the user
    public double doubleValidator(double num){
        boolean invalid;
        do{
            try{
                num = input.nextDouble();

                if(num <= 0){
                    System.out.println("Invalid Input");
                    invalid = true;
                } else {
                    invalid = false;
                }
                
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
                
                if(num <= 0){
                    System.out.println("Invalid Input");
                    invalid = true;
                } else {
                    invalid = false;
                }
                
            } catch(InputMismatchException e){
                input.nextLine();
                System.out.println("Invalid Input");
                invalid = true;
            }
        }while(invalid == true);
        return num;
    }

    //print sample date format
    public void printSampleDateFormat(){
        System.out.println("Please input date in this format 'YYYY-MM-DD'");
    }

    //return current date
    public java.sql.Date getCurrentDate(){

        int month = calendar.get(Calendar.MONTH);
        int day =  calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);
        
        calendar.set(year, month, day);
        java.util.Date dateUtil = calendar.getTime();
        java.sql.Date currentDate = new java.sql.Date(dateUtil.getTime());

        return currentDate;
    }

    //validate empty string
    public String validateEmptyString(String str){

        Boolean isNotEmpty = false;

        do{
            str = input.nextLine();
            if(!str.equals("")){
                isNotEmpty = true;
            } else {
                System.out.println("Empty field");
            }
        }while(!isNotEmpty);
        return str;
    }

}

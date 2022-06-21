import java.util.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Customer extends RatingEngine{

    public Scanner input = new Scanner(System.in);

    private String first_name;
    private String last_name;
    private String address;

    public void createAccount(){
        System.out.print("First Name: ");
        String firstName = input.nextLine();

        System.out.print("Last Name: ");
        String lastName = input.nextLine();
        
        System.out.print("Address: ");
        String address = input.nextLine();

        this.first_name = firstName;
        this.last_name = lastName;
        this.address = address;
    }

    public void saveCustomer(){
        try ( Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                            ,"root", "admin"); 
        ){
            PreparedStatement insertCustomer = conn.prepareStatement("INSERT INTO customer (first_name,last_name,address) VALUES (?,?,?)");
           
            insertCustomer.setString(1, this.first_name);
            insertCustomer.setString(2, this.last_name);
            insertCustomer.setString(3, this.address);

            insertCustomer.execute();
            System.out.println("Account successfully created!");

            PreparedStatement getLatestInsert = conn.prepareStatement("SELECT * FROM customer ORDER BY id DESC LIMIT 1");
            ResultSet queryRes = getLatestInsert.executeQuery(); 
            ResultSetMetaData rsmd = queryRes.getMetaData();

            for (int counter = 1; counter <= rsmd.getColumnCount(); counter++ ) {
                String field = rsmd.getColumnName(counter);
                System.out.print(field + "\t");
            }

            System.out.println("");
            while(queryRes.next()){  
                System.out.println(idPadding(queryRes.getInt(1),4,0)+"\t"
                                            +queryRes.getString(2)+"\t\t"
                                            +queryRes.getString(3)+"\t\t"
                                            +queryRes.getString(4));  
            }  
        } catch(SQLException ex){
            System.out.println("Database error occured upon saving new customer");
        }
    }

    public Boolean checkTableRow(String tableName){
        Boolean emptyTable = true;
        try(
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin"); )
        {
            PreparedStatement getRows = conn.prepareStatement("SELECT COUNT(*) as recordsCount FROM "+ tableName);
            ResultSet queryRes = getRows.executeQuery(); 

            while(queryRes.next()){
                int recordsCount = queryRes.getInt("recordsCount");
                if(recordsCount > 0){
                    emptyTable = false;
                }
            }
        } catch(SQLException ex){
            System.out.println("Database error occured upon checking row count");
        }

        return emptyTable;
    }

    public int checkAccountIfExist(int accountNum){
        int account_id = 0;
        Boolean isExist = false;
        try(
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin"); )
        {
            
            PreparedStatement getCustomerAccount;
            ResultSet queryRes;

            do{
                System.out.println("Please input an existing account number/id: ");
                accountNum = (int)Math.round(dataTypeValidator(accountNum));

                getCustomerAccount = conn.prepareStatement("SELECT id FROM customer where id = " + accountNum);
                queryRes = getCustomerAccount.executeQuery(); 

                if(queryRes.next()){
                    System.out.println("Account successfully matched!\n");
                     account_id = queryRes.getInt("id");
                    isExist = true;
                } else {
                    System.out.println("Account id/number = " +  accountNum +" doesn't exist");
                }
            }while(!isExist);

        } catch(SQLException ex){
            System.out.println("Database error upon checking customer id existence");
        }

        return account_id;
    }

    public String searchCustomerByName(){
        String idStr = "";
        try(
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin"); )
        {
            Boolean isExist = false;
            PreparedStatement getCustomerByName;
            ResultSet queryRes;
            String firstName = "";
            String lastName = "";
            do{
                System.out.println("Please input an existing customer account");
                System.out.print("First Name: ");
                firstName = input.nextLine();
                System.out.print("Last Name: ");
                lastName = input.nextLine();

                getCustomerByName = conn.prepareStatement("SELECT * FROM customer WHERE first_name LIKE '%"
                                                            +firstName + "%' AND last_name LIKE '%"+lastName+"%' LIMIT 1");
                queryRes = getCustomerByName.executeQuery(); 
        
                if(queryRes.next()){
                    System.out.println("Account successfully matched!\n");
                     idStr = idPadding(queryRes.getInt("id"),4,0);
                     this.setFirstName(queryRes.getString("first_name"));
                     this.setLastName(queryRes.getString("last_name"));
                     this.setAddress(queryRes.getString("address"));
                     
                    isExist = true;
                } else {
                    System.out.println("\nNo matched found.\n");
                }
            }while(!isExist);

        } catch(SQLException ex){
            System.out.println("Database error occured upon searching customer"+ ex);
        }

        return idStr;
    }

    public double dataTypeValidator(double num){
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

    public String dateValidator(String dateLabel, String inputDate, Boolean forAgechecking){
        String dateFormat = "^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$";
        Boolean invalidDate = true;

        if(forAgechecking){
            do{
                System.out.print(dateLabel);
                printSampleDateFormat();
                inputDate = input.nextLine();
                if(inputDate.matches(dateFormat)){
                    if(!checkIfMinor(inputDate)){
                        invalidDate = false;
                    } else {
                        System.out.println("Policy Holder must be 17 years old or above.");
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

    public void printCustomerDetails(String idStr){
        System.out.println("ID\t First Name\t Last Name\t Address\t");
        System.out.println(idStr + "\t " + this.first_name + "\t " + this.last_name + "\t " + this.address);
    }

    public String getFirstName(){
        return this.first_name;
    }

    public String getLastName(){
        return this.last_name;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setFirstName(String firstName){
        this.first_name = firstName;
    }

    public void setLastName(String lastName){
        this.last_name = lastName;
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

    public void printSampleDateFormat(){
        System.out.println("Please input date in this format 'YYYY-MM-DD'");
    }

}

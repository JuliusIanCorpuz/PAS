import java.util.*;
import java.sql.*;

public class Customer extends RatingEngine{

    public Scanner input = new Scanner(System.in);

    private int account_id;
    private String account_id_str;
    private String first_name;
    private String last_name;
    private String address;

    public void createAccount(){
        System.out.print("First Name: ");
        String firstName = validateEmptyString(firstName = "");

        System.out.print("Last Name: ");
        String lastName = validateEmptyString(lastName = "");
        
        System.out.print("Address: ");
        String address = validateEmptyString(address = "");

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

            for (int counter = 1; counter <= rsmd.getColumnCount(); counter++ ){
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

    public void checkAccountIfExist(){
        int accountNum = 0;
        Boolean isExist = false;

        try(
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin"); ){
            
            PreparedStatement getCustomerAccount;
            ResultSet queryRes;

            do{
                System.out.print("Please input an existing account number/id: ");
                accountNum = intValidator(accountNum);

                getCustomerAccount = conn.prepareStatement("SELECT id FROM customer where id = " + accountNum);
                queryRes = getCustomerAccount.executeQuery(); 

                if(queryRes.next()){
                    System.out.println("Account successfully matched!\n");
                     this.account_id = queryRes.getInt("id");
                    isExist = true;
                } else {
                    System.out.println("Account id/number = " +  accountNum +" doesn't exist");
                }
            }while(!isExist);

        } catch(SQLException ex){
            System.out.println("Database error upon checking customer id existence");
        }
    }

    public String searchCustomerByName(){
        String idStr = "";
        try(
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/pas"
                                                         ,"root", "admin")){
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
                    this.account_id = queryRes.getInt("id");
                    this.account_id_str = idPadding(queryRes.getInt("id"),4,0);
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

    public int getCustomerID(){
        return this.account_id;
    }

    public String getCustomerIDStr(){
        return this.account_id_str;
    }

    public String getFirstName(){
        return this.first_name;
    }

    public int getCustomerId(){
        return this.account_id;
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

    public void printCustomerDetails(String idStr){
        System.out.println("ID\t First Name\t Last Name\t Address\t");
        System.out.println(idStr + "\t " + this.first_name + "\t " + this.last_name + "\t " + this.address);
    }

    
    
}

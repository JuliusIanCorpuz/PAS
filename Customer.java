import java.util.*;
import java.sql.*;

public class Customer extends RatingEngine{

    public Scanner input = new Scanner(System.in);

    private int account_id;
    private String account_id_str;
    private String first_name;
    private String last_name;
    private String address;

    //create an account/customer object
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

    //insert customer/account object to database
    public void saveCustomer(){
        try ( Connection conn = DriverManager.getConnection(GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword()); 
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
            System.out.println("Database error occured upon saving new customer" + ex);
        }
    }

    //prompt user for account/customer id to check if the account exists on the db
    public void checkAccountIfExist(){
        int accountNum = 0;
        Boolean isExist = false;

        try(
            Connection conn = DriverManager.getConnection( GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword())){
            
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

    /**Prompt the user for first name and last name and check if it has match from the Database. 
     *If matched, load the customer object with the query result
     */
    public void searchCustomerByName(){
        try(
            Connection conn = DriverManager.getConnection(GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword())){
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
    }

    //return int type customer/account id
    public int getCustomerID(){
        return this.account_id;
    }

    //return string type customer/account id
    public String getCustomerIDStr(){
        return this.account_id_str;
    }

    //return string type customer/account first name
    public String getFirstName(){
        return this.first_name;
    }

    //return string type customer/account last name
    public String getLastName(){
        return this.last_name;
    }

    //return string type customer/account address
    public String getAddress(){
        return this.address;
    }

    //set customer/account address
    public void setAddress(String address){
        this.address = address;
    }

    //set customer/account first name
    public void setFirstName(String firstName){
        this.first_name = firstName;
    }

    //set customer/account last name
    public void setLastName(String lastName){
        this.last_name = lastName;
    }

    //return string type customer full name
    public String getFullCustomerFullName(){
        String fullName = this.first_name + " " + this.last_name;
        return fullName;
    }

    //print customer details
    public void printCustomerDetails(){
        System.out.println("ID: "+ this.account_id_str + "\n"
                        + "Name: "+ getFullCustomerFullName() +"\n"
                        + "Address: "+ this.address + "\n");
    }

}

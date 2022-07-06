import java.sql.*;

public class Customer extends RatingEngine{

    private int account_id;
    private String account_id_str;
    private String first_name;
    private String last_name;
    private String address;

    //create an account/customer object
    public void createAccount(){

        String firstName = validateEmptyString("First Name: ", firstName = "");

        String lastName = validateEmptyString("Last Name: ",lastName = "");
        
        String address = validateEmptyString("Address: ",address = "");

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
            System.out.println("\nAccount successfully created!\n");

            PreparedStatement getLatestInsert = conn.prepareStatement("SELECT id FROM customer ORDER BY id DESC LIMIT 1");
            ResultSet queryRes = getLatestInsert.executeQuery(); 
            
            while(queryRes.next()){  
                setCustomerIDStr(queryRes.getInt(1)); 
            }  

            printCustomerDetails();
            
        } catch(SQLException ex){
            System.out.println("Database error occured upon saving new customer");
        }
    }

    //prompt user for account/customer id to check if the account exists on the db
    public int checkAccountIfExist(){
        int accountNum = 0;
        Boolean isExist = false;

        int tries = -1;

        try(
            Connection conn = DriverManager.getConnection( GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword())){
            
            PreparedStatement getCustomerAccount;
            ResultSet queryRes;

            do{
                tries++;

                System.out.println("\nMaximum input trials = " + (5-tries));
                if(tries == 5){
                    System.out.println("\nYou have reached the max trials for selecting a customer.\n"
                                        +"You can select 1 on the Menu to create a new customer. Thank you.\n");
                    return tries;
                }
                
                accountNum = intValidator("Please input an existing account number/id: ",accountNum);

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

        return tries;
    }

    /**Prompt the user for first name and last name and check if it has match from the Database. 
     *If matched, load the customer object with the query result
     */
    public int searchCustomerByName(){

        int tries = -1;

        try(
            Connection conn = DriverManager.getConnection(GET_DB_MYSQLPORT() + getDBSchemaNAme(), getDBUsername(), getDBPassword())){
            Boolean isExist = false;
            PreparedStatement getCustomerByName;
            ResultSet queryRes;
            String firstName = "";
            String lastName = "";
            do{
                tries++;

                System.out.println("\nMaximum input trials = " + (5-tries));
                if(tries == 5){
                    System.out.println("\nYou have reached the max trials for selecting a customer.\n"
                                        +"You can select 1 on the Menu to create a new customer. Thank you.\n");
                    return tries;
                }

                System.out.println("Please input an existing customer account");
                firstName = validateEmptyString("First Name: ", firstName = "");
                lastName = validateEmptyString("Last Name: ",lastName = "");

                getCustomerByName = conn.prepareStatement("SELECT * FROM customer WHERE first_name = '" + firstName + "' AND last_name = '" + lastName + "'");

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
            System.out.println("Database error occured upon searching customer"+ex);
        }

        return tries;
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

    //set string type customer/account id
    public void setCustomerIDStr(int id){
        this.account_id_str = idPadding(id, 4, 0);
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

import java.util.*;
import java.sql.*;

public class Customer  {

    public Scanner input = new Scanner(System.in);

    private String first_name;
    private String last_name;
    private String address;

    public void createAccount()
    {
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

    public void saveCustomer(String firstName, String lastName, String address)
    {
        try ( Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/policyandclaimsadmin"
                                                            ,"root", "admin"); 
        ){
            PreparedStatement insertCustomer = conn.prepareStatement("INSERT INTO customer (first_name,last_name,address) VALUES (?,?,?)");
           
            insertCustomer.setString(1, firstName);
            insertCustomer.setString(1, lastName);
            insertCustomer.setString(1, address);

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
                System.out.println(idPadding(queryRes.getInt(1))+"\t"
                                            +queryRes.getString(2)+"\t\t"
                                            +queryRes.getString(3)+"\t\t"
                                            +queryRes.getString(4));  
            }  
        }catch (SQLException ex){
            System.out.println(ex);
        }
    }


    public String getFirstName()
    {
        return this.first_name;
    }

    public String getLastName()
    {
        return this.last_name;
    }

    public String getAddress()
    {
        return this.address;
    }

    public String idPadding(int id)
    {
        {
            String stringId = "";
    
            if(id < 10){
                stringId = String.format("%04d", id);
            }
            else if(id < 100){
                stringId = String.format("%03d", id);
            }
            else if (id < 1000)
            {
                stringId = String.format("%02d", id);
            }
    
            return stringId;
        }
    }


}

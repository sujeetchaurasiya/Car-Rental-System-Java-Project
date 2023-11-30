//import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;


public class Car_Rental_System{
    private static final String url = "jdbc:mysql://localhost:3306/car_rental_system";
    private static final String username = "root";
    private static final String password = "root";
	
    public static void main(String args[]) throws ClassNotFoundException, SQLException{
       try{
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, username, password);
        Scanner scanner = new Scanner(System.in);
        int choice;
        do{
            System.out.println();
            System.out.println();
            System.out.println("____________J11 Car Services____________");
            System.out.println();
            System.out.println("1. Add a Car");
            System.out.println("2. View Cars");
            System.out.println("3. Delete a Car");
            System.out.println("4. Rent a Car");
            System.out.println("5. Update Cars");
            System.out.println("6. Return a Car");
            System.out.println("0. Exit");
            System.out.println();
            System.out.print("Please Choose an option: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addCar(connection, scanner);
                    break;
                case 2:
                	viewCar(connection);        
                    break;
                case 3:
                    deleteCar(connection, scanner);
                    break;
                case 4:
                	rentCar(connection, scanner);
                	break;
                case 5:
                    updateCar(connection, scanner);
                    break;
                case 6:
                	returnCar(connection,scanner);
                	break;
                case 0:
                    exit();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice!!! ");
                    System.out.print("Please try again...");
            }

        }while(choice!=0);
       }
       catch( SQLException e){
        System.out.println(e.getMessage());
       }
       catch (Exception e) {
        throw new RuntimeException(e);
       }
    }
    public static void addCar(Connection connection, Scanner scanner){
    	try {
            System.out.print("Car Name: ");
            String car_name = scanner.next();
            scanner.nextLine();
            System.out.print("Rent Price: ");
            int rent_price = scanner.nextInt();
//            System.out.print("Enter car Id: ");
//            String car_id = scanner.next();
            System.out.print("Enter the numbers of Cars");
            int num_car = scanner.nextInt();

            String sql = "INSERT INTO car (car_name, rent_price,num_car) " +
                    "VALUES ('" + car_name + "', " + rent_price + ","+ num_car +")";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                	System.out.println();
                    System.out.println("Added successfully!");
                } else {
                	System.out.println();
                    System.out.println("Adding failed!!!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void rentCar(Connection connection, Scanner scanner){
            try{
                System.out.print("Enter Car id: ");
                int car_idd=scanner.nextInt();
                if(!carIdMatch(connection , car_idd)) {
                	System.out.println("Invalid Car id !!!");
                	return;
                }
//                System.out.println("Enter Car name");
//                String car_name=scanner.next();
                System.out.print("Enter numbers of cars You want to rent: ");
                int num= scanner.nextInt();
                System.out.print("Enter number of days to rent the cars: ");
                int day = scanner.nextInt();
                String sql = "SELECT rent_price,num_car , car_name,car_id FROM car " +
                        "WHERE car_id = " + car_idd +"";
                try(Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql)){
                    
                    
                    if(resultSet.next()){
                   	    int num_car = resultSet.getInt("num_car");
                        int rent_price = resultSet.getInt("rent_price");
                        if (num<=num_car) {
                        String car_name=resultSet.getNString("car_name");
                        System.out.println("-----------------_________Success Page__________--------------------");
                        System.out.println();
                        System.out.println("Congratulations you booked "+ " "+num +" " +car_name+" "+"cars for"+ " "+day+"Days");
                        System.out.println();
                        System.out.println("__________________________________________________________________________________");
                        int price = rent_price*day*num;
                        System.out.println("Total Cost: "+price);
                        int num_carr=num_car-num;
                        String nsql="UPDATE car SET num_car = "+num_carr+" Where car_id= "+ car_idd+" ";
                        statement.executeUpdate(nsql);
                        System.out.println("Thank You...");
                        }
                        else{
                            System.out.println("No more Car Availables");
                        }
                    }
                   
                }

            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    
    private static void deleteCar(Connection connection, Scanner scanner){
    	try {
    		System.out.print("Enter car Id to Delete: ");
    		int D_car= scanner.nextInt() ;
    		
    		if(!carIdMatch(connection, D_car)) {
    			System.out.println();
    			System.out.println("Invalid Car Id!!!");
    			return;
    		}
    		String sql="Delete from car where car_id ="+D_car+"";
    		try(Statement statement =connection.createStatement()){
    			int affectRows= statement.executeUpdate(sql);
    			if(affectRows>0) {
    				System.out.println();
    				System.out.println("Successfully Deleted");
    			}
    			else {
    				System.out.println();
    				System.out.println("Failed ");
    			}
    		}
    	}
    	catch(Exception e){
    		System.out.println(e.getMessage());
    	}
    }
    public static void viewCar(Connection connection){
    	String sql="SELECT car_name , rent_price , car_id,num_car from car";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql) ) {
        	    System.out.println();//not necessary to built same structure like this you can go on youe own
                System.out.println(" --------------------------------Available Cars--------------------------- ");
                System.out.println("+----------------+-----------------+---------------+----------------------+");
                System.out.println("| Car Id         |       Name      | Price Per Day |     Availibility     |");
                System.out.println("+----------------+-----------------+---------------+----------------------");
                while (resultSet.next()) {
//                	char name[]= new  char [25];
                    int car_id = resultSet.getInt("car_id");
                    String car_name = resultSet.getString("car_name");
//                    name= car_name.toCharArray();
                    int rent_price = resultSet.getInt("rent_price");
                    int Availibility = resultSet.getInt("num_car");
                  //this is used to display the data in desired format
                    System.out.printf("| %-10d | %-20s | %-13d | %-15s |\n",
                            car_id, car_name, rent_price, Availibility);
                }

        }
        catch(Exception e){

        }
    }
    private static void updateCar(Connection connection, Scanner scanner){
    	try {
    		
    		System.out.print("Enter Car Id: ");
    		int car_idd=scanner.nextInt();
    		if(!carIdMatch(connection , car_idd)) {
    		System.out.println();
    		System.out.println("Invalid Selection!!!");
    		}
    		System.out.print("Enter Car Name: ");
    		String N_car_name=scanner.next();
    		System.out.print("Enter Price for One day: ");
    		int N_rent_price=scanner.nextInt();
    		System.out.print("Enter number of Cars Available: ");
    		int N_num_car= scanner.nextInt();
        	String sql ="UPDATE car SET car_name= '"+N_car_name+"' ,rent_price= "+N_rent_price+",num_car="+N_num_car+" WHERE car_id="+car_idd+"";
        	try(Statement statement=connection.createStatement()) {
        		int affectRows= statement.executeUpdate(sql);
        		if(affectRows>0) {
        			System.out.println("Successfully Updated");
        		}
        		else {
        			System.out.println("Failed");
        		}
        		
        	}
        	
    	
    	}
    	catch(SQLException e){
        		System.out.print(e.getMessage());
        	}
    	}
    	
    	
    	
    	
    	
    
    public static void exit() throws InterruptedException{
    	System.out.println();
    	System.out.print("Please Wait we are loging Out");
    	int i=0;
    	while(i!=5) {
    		System.out.print(".");
    		Thread.sleep(800);
    		i++;
    	}
    	System.out.println();
    	System.out.println("Thank you for choosing J11 Services...");
    }
    
    private static void returnCar(Connection connection, Scanner scanner) {
    	try {
    		System.out.print("Enter car id to return: ");
    		int R_car_id= scanner.nextInt();
    		if(!carIdMatch(connection,R_car_id)) {
    			System.out.println("Invalid Car Id!!!");
    			return;
    		}
    		System.out.print("Enter number of Cars you want to return: ");
    		int R_num_car=scanner.nextInt();
    		System.out.println("Have you rented the car more than you paid for it...");
    		System.out.print("Enter 'Y' for Yes or 'N' for no:- ");
    		char inp=scanner.next().charAt(0);
    		
    		if(inp=='Y') {
    		String sqll=" select rent_price,num_car from car where car_id= "+R_car_id+"";
    		try(Statement statement=connection.createStatement();
    			ResultSet resultSet= statement.executeQuery(sqll)){
    			if(resultSet.next()) {
    			int rent_price=resultSet.getInt("rent_price");
    			int N_num_car=resultSet.getInt("num_car");
    			System.out.print("Enter number of Extra days you rented the Car: ");
    			int E_day=scanner.nextInt();
    			int total= E_day * rent_price;
    			int T_num_car= R_num_car+N_num_car;
    			System.out.println();
    			System.out.print("Please pay Rupees: "+total);
//    			System.out.println("Cars"+T_num_car);
    			String sql="update car set num_car="+T_num_car +" where car_id ="+R_car_id+"";
    			
                

    			
    		    int count=statement.executeUpdate(sql);
    		    if(count>0) {
    		    	System.out.println();
    		    	System.out.println("You Successfully returned the Cars");
    		    	System.out.println();
    		    	System.out.println("Thank You for Choosing Our Services");
    		    }
    		    else {
    		    	System.out.println("Failed to Return");
    		    }
    			}
    			
    		}
    		}
    		else if(inp== 'N'|| inp=='n') {
    			String sqll=" select num_car from car where car_id= "+R_car_id+"";
				try(Statement statement=connection.createStatement();
    				ResultSet resultSet= statement.executeQuery(sqll)) {
					resultSet.next();
    				int num_car=resultSet.getInt("num_car");
    				int N_num_car= R_num_car+num_car;
    				String sql="update car set num_car="+N_num_car +" where car_id ="+R_car_id+"";
    				int affectRows =statement.executeUpdate(sql);
    				if(affectRows>0) {
    					System.out.println();
        		    	System.out.println("You Successfully returned the Cars");
        		    	System.out.println();
        		    	System.out.println("Thank You for Choosing Our Services");
        		    }
        		    else {
        		    	System.out.println("Failed to Return");
        		    }
    			}
    		}
    		else {
    			System.out.print("Sorry Plese enter either Y or N");
    		}
    	}
    	catch (SQLException e) {
    		System.out.print(e.getMessage());
    	}
    }
    private static boolean carIdMatch(Connection connection, int car_idd) {
    	try {
            String sql = "SELECT car_id FROM car WHERE car_id = "+ car_idd+"";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }
    }

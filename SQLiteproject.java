import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileWriter;
//importing necessary modules
public class SQLiteproject {
private static Connection con;
private static boolean hasData = false;
//function that selects all data and displays first 50 entries
public void selectAll(){
    String sql = "SELECT A, B, C, D, E, F, G, H, I, J FROM people";
    int counter = 0;
    try (Connection conn = this.connect();
         Statement stmt  = conn.createStatement();
         ResultSet rs    = stmt.executeQuery(sql)){
        
        // loop through the result set
        while (rs.next()) {
        	counter +=1;
            if(counter < 50) {
            	System.out.println(rs.getString("A") +  "\t" + 
                        rs.getString("B") + "\t" +
                        rs.getString("C") + "\t" +
                        rs.getString("D") + "\t" +
                        rs.getString("E") + "\t" +
                        rs.getString("F") + "\t" +
                        rs.getString("G") + "\t" +
                        rs.getString("H") + "\t" +
                        rs.getString("I") + "\t" +
                        rs.getString("J")
                        );
            }
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}
// function for creating database
public static void createNewDatabase(String fileName) {

    String url = "jdbc:sqlite:" + fileName;

    try (Connection conn = DriverManager.getConnection(url)) {
        if (conn != null) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A new database has been created.");
        }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}
//function for creating table
public static void createNewTable() {
    // SQLite connection string
    String url = "jdbc:sqlite:users.db";
    
    // SQL statement for creating a new table
    String sql = "CREATE TABLE IF NOT EXISTS people (\n"
            + "	A text NOT NULL,\n"
            + "	B text NOT NULL,\n"
            + "	C text NOT NULL,\n"
            + "	D text NOT NULL,\n"
            + "	E text NOT NULL,\n"
            + "	F text NOT NULL,\n"
            + "	G text NOT NULL,\n"
            + "	H text NOT NULL,\n"
            + "	I text NOT NULL,\n"
            + "	J text NOT NULL\n"
            + ");";
    
    try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
        // create a new table
        stmt.execute(sql);
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}
//gets connection to database
private Connection connect() {
    // SQLite connection string
    String url = "jdbc:sqlite:users.db";
    Connection conn = null;
    try {
        conn = DriverManager.getConnection(url);
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return conn;
}
//inserts into database for problem format
public  void insert(String A,String B,String C,String D,String E,String F,String G,String H,String I,String J) {
    String sql = "INSERT INTO people(A,B,C,D,E,F,G,H,I,J) VALUES(?,?,?,?,?,?,?,?,?,?)";

    try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1,A);
        pstmt.setString(2,B);
        pstmt.setString(3,C);
        pstmt.setString(4,D);
        pstmt.setString(5,E);
        pstmt.setString(6,F);
        pstmt.setString(7,G);
        pstmt.setString(8,H);
        pstmt.setString(9,I);
        pstmt.setString(10,J);
        
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}

//main
public static void main(String[] args) throws Exception  
{  
//parsing a CSV file into Scanner class constructor  
Scanner sc = new Scanner(new File("ms3Interview - Jr Challenge 2.csv"));
sc.useDelimiter("\n");
//sets the delimiter pattern  
//instantiating app for calling of non static functions
SQLiteproject app = new SQLiteproject();
//creating database and subsequent table
createNewDatabase("users.db");
createNewTable();
//variables for tracking
int counter = 0;
String current = "";
boolean empty = false;
int received = 0;
int success = 0;
int failed = 0;
int tracker = 0;
//failed string used to write incomplete entries to failed.csv file
String failed_string = "";
String run_off = "";
//getting rid of header
run_off = sc.next();

//will loop through until no more lines in csv file
while (sc.hasNext())   {  
	
	//current line
current = sc.next();
//current line split into array
String[] curr_arr = current.split(",");
//looping through array to check for empty entries
for(int i = 0; i < curr_arr.length; i+=1) {
	if(curr_arr[i].equals("")) {
		empty = true;
	}
}
//if an empty entry is found it will be written to the failed string
if(empty == true) {
failed+=1;
received +=1;
for(int i = 0; i<curr_arr.length; i+=1) {
	failed_string += curr_arr[i]+",";
}
failed_string+="\n";
empty = false;

//write to other csv file
}//end of if
else {
	//otherwise it is inserted into the table
success +=1;
received += 1;

if(curr_arr.length >10) {
	tracker+=1;
	if(tracker < 50) {
		System.out.println(Arrays.toString(curr_arr));
	}
	app.insert(curr_arr[0],curr_arr[1],curr_arr[2],curr_arr[3],curr_arr[4]+curr_arr[5],curr_arr[6],curr_arr[7],curr_arr[8],curr_arr[9],curr_arr[10]);
}

//resetting empty variable when finished
empty = false;
//write to database

} //end of else

//if(current.equals("")) {
//	
//		
//	
//	empty = true;
//}
//if(counter == 4 && empty ==false) {
//	arr[counter] = current+sc.next();
//	
//}
//
//else {
//	arr[counter] = current;
//}
//if(counter == 9) {
//	if(empty == true) {
//		failed+=1;
//		received +=1;
//		for(int i = 0; i<arr.length; i+=1) {
//			failed_string += arr[i]+",";
//		}
//		failed_string+="\n";
//		
//		//write to other csv file
//	}
//	else {
//		success +=1;
//		received += 1;
//		app.insert(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6],arr[7],arr[8],arr[9]);
//		//write to database
//		
//	}
//	counter = 0;
//	empty = false;
//}
//counter +=1;

}  

sc.close();  //closes the scanner 
//writing to csv
FileWriter failure = new FileWriter("failed.csv");
failure.write(failed_string);
failure.close();
//writing to log file for stats
FileWriter numbers = new FileWriter("numbers.log");
numbers.write(String.valueOf(received)+ "\n" + String.valueOf(success) +"\n" +String.valueOf(failed) + "\n");
numbers.close();
app.selectAll();
//showing entries from database
System.out.println("program finished");


}// end of main 

}

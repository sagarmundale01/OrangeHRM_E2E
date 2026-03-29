package utilities;

public class DBConnection {

	private static final String DB_URL= "jdbc:mysql://localhost:3306/orangehrm";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD= "";
	
	public static void getConnection() {
		
		System.out.println("Starting DB COnnection...");
	}
	
	
}

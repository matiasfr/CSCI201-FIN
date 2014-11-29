import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;

public class SQLCommand implements Runnable{
		//TODO: pull this data from gameServer
		//TODO: make sure to import driver from lab into project directory
		public static final String DB_ADDRESS = "jdbc:mysql://localhost/";
		public static final String DB_NAME = "lab11";
		public static final String DRIVER = "com.mysql.jdbc.Driver";
		public static final String USER = "root";
		public static final String PASSWORD = "";
		
		protected ReentrantLock queryLock;
		String originalName = null;
		ServerLobbyThread parent;
		public SQLCommand(ReentrantLock queryLock, ServerLobbyThread p, String name )
		{
			this.queryLock = queryLock;
			this.parent = p;
			this.originalName = name;
		}
		@Override
		public void run() {
			queryLock.lock();
			System.out.print("Executing... ");
			execute();
			System.out.println("Done");
			queryLock.unlock();
		}
		public void execute() {
			String name = originalName;
			Connection conn;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(DB_ADDRESS+DB_NAME, USER, PASSWORD);
			Statement stmp = conn.createStatement();
			ResultSet rs = stmp.executeQuery("select name from names");
			String record;
			boolean clear = false;
			while(!clear) {
				while(rs.next()) {
					record = rs.getString("name");
					if(record.equals(name)) {
						clear = true;
						//create entry
						PreparedStatement stmt = conn.prepareStatement("INSERT INTO names VALUES (?) ");
						stmt.setString(1, name);
						stmt.execute();
						break;
					}
					System.out.println(record);
				}
				rs.beforeFirst();//reset resultSet
				if(clear == false) {
					int randomNum = 0 + (int)(Math.random()*99);//0-99 
					name += "#" + randomNum;
				}
				
			}
				
			} catch (SQLException e) {} catch (ClassNotFoundException e) {}
			parent.finalUsername = name;
		}
		
}

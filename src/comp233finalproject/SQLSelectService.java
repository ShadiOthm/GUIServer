
package comp233finalproject;
import java.io.DataOutputStream;//see responder class
import java.sql.Connection;//this imports or driver for our connection to SQL
//could have also used Java.sql.* to download all sql drivers
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author othman9953
 */
public class SQLSelectService extends Service {//by extending the class we are inheriting properties from the service class such as the response writer and do work method

	private String requestString;
	private String SQLCommand;

	//This construcotr will be called from the run method of a
	//Responder.  It passes the HTTP request info, and the output
	//object to the service.
	public SQLSelectService(DataOutputStream responseWriter, String requestString){

		super(responseWriter);
		this.requestString=requestString;
	}

	public void setSQLCommand(){

		int start = requestString.indexOf("Criteria=")+9;
		int end = requestString.indexOf("&Field=");
		String criteria = requestString.substring(start,end);
		String fieldName = requestString.substring(start,end);

		start = requestString.indexOf("&Field=")+7;
		end = requestString.indexOf("&submit");
		criteria = requestString.substring(start,end);

		SQLCommand = "SELECT * FROM EMPLOYEE WHERE "+criteria+" = '"+ fieldName +"'";

		System.out.println(SQLCommand);



		//This method extracts the criteria and field name from the
		//request string (using indexOf)and builds a valid SQL query.
		//something like:

		//”SELECT * FROM EMPLOYEE WHERE “+fieldName+” = ‘“ criteria +”’”
	}


	public SQLSelectService(DataOutputStream responseWriter) {
		super(responseWriter);

	}

	public void doWork() {

		setSQLCommand();

		  Statement stmt=null;
	          ResultSet rset=null;
	          ResultSetMetaData rsmd=null;
	          Connection conn=null;

	      try{

	            DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());//Java Build Path Project--Properties--Java Build Path --Add External Jars this is easily missed on the walkthrough
											//adding Jars will fix the oracle.jdbc
	  	    conn = DriverManager.getConnection("jdbc:oracle:thin:@bisoracle.siast.sk.ca:1521:ACAD", "CISTU024", "CISTU024");

	  	    stmt = conn.createStatement();
	      	    rset = stmt.executeQuery(SQLCommand);
	       	    rsmd = rset.getMetaData();


	       	    int columnCount = rsmd.getColumnCount();

	       	//responseWriter.writeBytes("<html><head><title>"</head></title><body);
	      //tabletag here
	        while (rset.next()){

	        	for(int i = 0; i< columnCount; i++ ){
                         //html formatting can goes in the responsewriter tags we can do what we want
	                 //close all html tags body
	        		responseWriter.writeBytes ("<head>\n" +
                                   "<title>Comp 233 Query Page</title>\n" +
                                "</head>"
                          +"<li>"+rsmd.getColumnLabel(i+1)+"  :     "+rset.getString(i+1)); //in walkthrough this should be nicely formatted

	        	}

	        	responseWriter.writeBytes ("<br/>");
	        }
	        responseWriter.writeBytes ("<a ref='Query.htm/>");
	        stmt.close();
	      }

	      catch(SQLException sqle){
	    		System.out.println("A SQL error has occurred.");
	    		System.out.println(sqle.toString());
	    	}

	      catch(Exception E){
	    	  	System.out.println("Unknown error has occurred.");
	    	  	System.out.println("Exception!"+E);

	        }
	        finally{
	        	try
	        	{
	        		rset.close();
	        		stmt.close();
	        		conn.close();
	        	}
	        	catch(Exception E)
	        	{
	        		System.out.println("Warning.");
	        System.out.println("Failed to get to system resources");
	        }
	        }
	}


}

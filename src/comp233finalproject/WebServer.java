
package comp233finalproject;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author othman9953
 */
public class WebServer {

        private ServerSocket requestListener; // Listens for connection requests.
	private Socket requestHandler; // A socket for communicating with a client
	private ExecutorService runThreads;
	public  int HTTP_PORT = 12346;
        public boolean stop;

        //Creates and instance of WebServer and starts it.
        public static void main(String[] args) {
        WebServer webserver = new WebServer();
        webserver.start();
    }

    //First constructor that:
    public WebServer()
    {

        	try{

	        //1- Creates an instance of RequestListener
		requestListener = new ServerSocket(HTTP_PORT);
                
                //2- The parameter 'timeout' represents a specific timeout in milliseconds
                //to stop the server
                requestListener.setSoTimeout(1000);
                
               //3- Creates an instance of ExecutorService, threadpool size 100
		runThreads = Executors.newFixedThreadPool(100);

                }
	catch(Exception e){

		System.out.println(e.toString());
		System.out.println("\n");
		e.printStackTrace();
	}
    }
    
    //2nd. constructor to connect to the Server by Port number
    public WebServer(int portNumber)
    {

        	try{

	        //Create an instance of RequestListener
                HTTP_PORT = portNumber;
		requestListener = new ServerSocket(HTTP_PORT);
                requestListener.setSoTimeout(1000);
		
		runThreads = Executors.newFixedThreadPool(100);

                }
	catch(Exception e){

		System.out.println(e.toString());
		System.out.println("\n");
		e.printStackTrace();
	}
    }
    
    //Stop method to stop the server from working
    public void stop ()
    {
        stop =true;
        System.out.println("Stopping ....");
    }
    
    
    // Enters an infinite while loop and accepts connections 
    public void start ()
    {
        stop = false;
                    System.out.println("Waiting For IE to request a page:");
        
        //because the server is waiting to request a page
        while(!stop)
                {
                   try 
                   {
                     // Each connection is used to create a new responder:
                     requestHandler = requestListener.accept();
                		Responder r = new Responder(requestHandler);
                     // “r” is then passed to responses to execute                
                     runThreads.execute(r);
                   } 
                   catch (IOException ex) 
                   {
               
                   }
                }
        System.out.println("Server Stopped Successfully");
    }
}

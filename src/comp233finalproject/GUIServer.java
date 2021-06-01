
package comp233finalproject;

// Client portion of a stream-socket connection between client and server.
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author othman9953
 */
public class GUIServer extends JFrame{

    // private JTextArea chatInput;
    private JTextArea chatOutput; //display information to user
    private JButton start;
    private JButton stop;
    private JPanel chatInputContainer; 
    private ObjectOutputStream output;
    private ObjectInputStream input;
    Log log;
    ExecutorService startserver;
    //private Socket connection;
    WebServer webserver ;
    
    
    public GUIServer(){
        //set title bar
        super("Client");
        
        //set diemensions
        setSize(500,500);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        log = new Log();
        chatOutput = new JTextArea();
        
        //Start and Stop buttons
        start = new JButton("Start");
        stop  = new JButton("Stop");
        add(chatOutput,BorderLayout.CENTER);
        
        //JPanel layout
        chatInputContainer = new JPanel();
        chatInputContainer.add(start);
        chatInputContainer.add(stop); 
        add(chatInputContainer,BorderLayout.SOUTH);

        start.addActionListener
        (
 	new ActionListener() 
        {
            //// send message to server to start
            public void actionPerformed(ActionEvent ae) 
            {
                
            //Execute the Webserver with pool 3    
            startserver = Executors.newFixedThreadPool(3);
            StartServer ss = new StartServer(webserver);
            startserver.execute(ss);
            
            }// end method actionPerformed
        }// end anonymous inner class
        ); // end call to addActionListener
        
        stop.addActionListener
        (
 	new ActionListener() 
            {
            public void actionPerformed(ActionEvent ae) 
            {
                    webserver.stop();
            }
            
            }
        );
    }
    
    //connect to server method
    public void connectToServer() {

        try {
            //Use a JOptionPane to prompt the user for server port no.
            int portNo = Integer.parseInt(JOptionPane.showInputDialog("Enter Server Port NO."));
            
              //used the second constructor in Webserver class
              webserver = new WebServer(portNo);
           
              //tell it to appear
            setVisible(true);
            
            //Create an ExecutorService called listen with a thread pool of 1
            ExecutorService chat = Executors.newFixedThreadPool(2);
            
            //Create a new ChatListener called cl and pass input and chatOutput into the constructor
            ChatListener cl = new ChatListener(chatOutput);
            
            //Pass cl into listen's execute method
            chat.execute(cl);
        
        } 
            catch (Exception e) {
            System.out.println("Oops! : " + e.toString());
        }    
    }
    
    //threaded object that will listen to the server, 
    //to update the screen with the log file
    class ChatListener implements Runnable{
        
        private JTextArea chatOutput;

	public ChatListener( JTextArea chatOutput) {
         
            this.chatOutput = chatOutput;
        }
	
	public ChatListener(){}
			
	public void run(){
		try{ 
                    //infinite loop that listens for incoming messages and puts them into chatOutput:
                    while(true)
                      {
                     
                     Log log = new Log();
                     //log file s string
                     String s = log.ReadFile();
                        chatOutput.setText(s);
                      }
		    }
		catch(Exception e)
                    {
			System.out.println(e.toString());
		    }
        }
    }
       
       //threaded object that will listen to the server,
       class StartServer implements Runnable{
        //private ObjectInputStream input;
        //
        private WebServer webserver;

	public StartServer( WebServer webserver) {
         //   this.input = input;
            this.webserver = webserver;
        }
	
	public StartServer(){}
			
	public void run(){
		try{ 
                    webserver.start();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
        }
        
    }
       
    public static void main(String[] args) throws IOException, InterruptedException
    {
        
        GUIServer chatter = new GUIServer();
        chatter.connectToServer();   
    }   

}


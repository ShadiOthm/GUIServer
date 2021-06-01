
package comp233finalproject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author othman9953
 */
public class Responder implements Runnable {

    private Socket requestHandler;
    private Scanner requestReader;
    private Scanner pageReader;
    private DataOutputStream pageWriter;
    private String HTTPMessage;
    private String requestedFile;
    private Log log;
    
    
    public Responder() {
        log=new Log();
    }

    public Responder(Socket requestHandler) {
        System.out.println("Page Requested: Request Header:");
        this.requestHandler = requestHandler;
        log=new Log();
    }
    
    //handles all of the response code from the one shot server 
    public void run() {
        //Declare a requested page variable
        String requestedPage;
        try {
            
            requestReader = new Scanner(
                    new InputStreamReader(requestHandler.getInputStream()));

            int lineCount = 0;
            pageWriter = new DataOutputStream(requestHandler.getOutputStream());
            do {

                lineCount++; //This will be used later
                if (requestReader.hasNextLine()) {
                    
                    // read the first line of the HTTP request, 
                    // we extract the requested file from that line.
                    HTTPMessage = requestReader.nextLine();
                    System.out.println(HTTPMessage);
                    
                    // to check for URL with  parameters because > 6
                    if (HTTPMessage.indexOf("HTTP/1.1") > 6) {
                        requestedPage = HTTPMessage.substring(5, HTTPMessage.indexOf("HTTP/1.1") - 1);
                        
                        //To check the Query webpage
                        if (requestedPage.startsWith("doSERVICE")) {
                            System.out.println("Hi Requested page is:" + HTTPMessage);

                            SQLSelectService sqlservice = new SQLSelectService(pageWriter, HTTPMessage);
                            sqlservice.doWork();
                            pageWriter.close();
                            return;
                        
                        //To check if the user requesting a file 
                        //which you are not responsible for handling or
                        //They have requested the sub-default page in a sub folder
                        } else {
                            if (!requestedPage.endsWith(".htm")) {
                                if (requestedPage.endsWith("\\")) {
                                    requestedPage += "default.htm";
                                } else {
                                    requestedPage += "\\default.htm";
                                }

                            }
                        }
                    } else // for url with no parameters, default page loaded
                    {
                        requestedPage = "default.htm";
                    }

                    if (lineCount == 1) {
                        requestedFile = "WebRoot\\" + requestedPage;
                        
                        //trim any trailing or leading spaces:  
                        requestedFile = requestedFile.trim();
                    }
                }
            //
            } while (HTTPMessage.length() != 0);
                //to write in the logfile
                log.writeToFile("Requested File: "+requestedFile);
                try 
                {
                    pageReader = new Scanner(new File(requestedFile));
                } 
                catch (FileNotFoundException fileNotFoundException) 
                {
                   requestedFile = "WebRoot\\Util\\Error404.htm";
                   pageReader = new Scanner(new File(requestedFile));
                
                //to write in the logfile
                log.writeToFile("Error. Can't find file.  Showed "+requestedFile+" instead");
                }

            //Scanner that reads a file and loops through the file 
            //and outputs it to the console.
            while (pageReader.hasNext()) { 
                String s = pageReader.nextLine();
                pageWriter.writeBytes(s);
            }

            //Tells the Browser we’re done sending
            pageReader.close();
            pageWriter.close();

            requestHandler.close();
            } 
        
        catch (IOException ex) {
            Logger.getLogger(Responder.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

}

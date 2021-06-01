
package comp233finalproject;
import java.io.*;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author othman9953
 */
public class Log {
    private File log = new File("LogFile.txt");//this creates the log file
    private boolean append = true;

 public Log(){}


  public Log(File log, boolean append) {
   super();
   this.log = log;
   this.append = append;
  }


  public File getLog() {
   return log;
  }


  public void setLog(File log) {
   this.log = log;
  }


  public boolean isAppend() {
   return append;
  }


  public void setAppend(boolean append) {
   this.append = append;
  }



 public void writeToFile(String string) throws IOException{

  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
   System.out.println(timeStamp );

  FileWriter writer = new FileWriter(log,append);
   PrintWriter print_line = new PrintWriter(writer);

  print_line.println("Time:" +timeStamp+ "   "+string);
   writer.close();
   print_line.close();




  }
 
 public String ReadFile()
 {
     String s="";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(log.getName()));
            String line;
         try {
             while((line = reader.readLine()) != null){
                 //process the line
                 s += line +"\n";
             }        } catch (IOException ex) {
             Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
         }
        }catch (FileNotFoundException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
 }

}

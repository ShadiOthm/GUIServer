
package comp233finalproject;
import java.io.DataOutputStream;
/**
 *
 * @author othman9953
 */
public abstract class Service {

	protected DataOutputStream responseWriter;

		public Service( DataOutputStream responseWriter){
			this.responseWriter=responseWriter;
		}
		public DataOutputStream getresponseWriter() {
			   return this.responseWriter;
			}

public abstract void doWork();

}



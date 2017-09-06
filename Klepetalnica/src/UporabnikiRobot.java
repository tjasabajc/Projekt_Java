import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.fluent.Request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class UporabnikiRobot extends TimerTask{
	private ChatFrame klepetalnica;
	
	public void pozeni() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, 1000, 2500);
	}
	
	public UporabnikiRobot(ChatFrame klepetalnica) {
		this.klepetalnica = klepetalnica;

}

	@Override
	public void run() {
		try {String users = Request.Get("http://chitchat.andrej.com/users")
	    		   .execute()
	    		   .returnContent()
	    		   .asString();
	       		ObjectMapper mapper = new ObjectMapper();
	       		mapper.setDateFormat(new ISO8601DateFormat());
	       		
	       		TypeReference<List<Uporabnik>> t = new TypeReference<List<Uporabnik>>() { };
	    		List<Uporabnik> prijavljeni = mapper.readValue(users, t);
	    		klepetalnica.pobrisiPrijavljene();
	    		
	    		if (prijavljeni.isEmpty()){
	    			klepetalnica.niUporabnikov();
	    			System.out.println("NI");
	    		} else   {
	    		for (Uporabnik oseba : prijavljeni) {
	    			klepetalnica.zapisiUporabnika(oseba);
	    		}}

	    		
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
		
	}
}
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class SporocilaRobot extends TimerTask{
	private ChatFrame klepetalnica;
	
	public SporocilaRobot(ChatFrame klepetalnica) {
		this.klepetalnica = klepetalnica;
	}

	public void pozeni() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, 2000, 1000);
	}

	@Override
	public void run() {
		if (klepetalnica.getStatus()) {
			 try {
		        	URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
		        	          .addParameter("username", klepetalnica.getUsername())
		        	          .build();

		        	String responseBody = Request.Get(uri)
		        	          .execute()
		        	          .returnContent()
		        	          .asString();
		        	ObjectMapper mapper = new ObjectMapper();
		      		mapper.setDateFormat(new ISO8601DateFormat());
		            TypeReference<List<Prejeto>> t = new TypeReference<List<Prejeto>>() { };
		      		List<Prejeto> prejetaSporocila = mapper.readValue(responseBody, t);
		      		
		      		/*if (prejetaSporocila.isEmpty()) {
		      			klepetalnica.niSporocil();
		      		}
		      		else*/ {
		      			for (Prejeto sporocilo : prejetaSporocila){
			    			klepetalnica.zapisiPrejetoSporocilo(sporocilo);
			    			}
		      		}
		      		
		        	  //System.out.println(responseBody);
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
		
}}


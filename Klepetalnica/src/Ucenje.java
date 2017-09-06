
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Hello ChitChat!
 */

public class Ucenje {
	public static void main(String[] args) {
        //get_index();
        get_users();
        //log_in("Enka");
        //log_in("Dvojka");
		//send("Enka", "BajcT15", "orja.");
        //recive("Oka");
        log_out("Enka");
    }
	public static void get_index(){
        try {
            String hello = Request.Get("http://chitchat.andrej.com")
                                  .execute()
                                  .returnContent().asString();
            System.out.println(hello);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	public static void get_users() {
		try {
			  String responseBody = Request.Get("http://chitchat.andrej.com/users")
		          .execute()
		          .returnContent()
		          .asString();
			  System.out.println(responseBody);
		} catch (IOException e) {
            e.printStackTrace();
        } 
	}
	
	public static void log_in(String username) {  
	URI uri;
	try {
		uri = new URIBuilder("http://chitchat.andrej.com/users")
		          .addParameter("username", username)
		          .build();
		String responseBody;
		responseBody = Request.Post(uri)
		      .execute()
		      .returnContent()
		      .asString();
		System.out.println(responseBody);
		
	} catch (URISyntaxException e) {
		e.printStackTrace();
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

	}
	public static void log_out(String username){
		URI uri;
		try {
			uri = new URIBuilder("http://chitchat.andrej.com/users")
					.addParameter("username", username)
					.build();
			String responseBody = Request.Delete(uri)
					.execute()
					.returnContent()
					.asString();
			System.out.println(responseBody);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void send (String me, String myMessage ){
		  URI uri;
		try {
			uri = new URIBuilder("http://chitchat.andrej.com/messages")
			          .addParameter("username", me)
			          .build();

			String message = "{ \"global\" : true, \"text\" : \""+myMessage+"\"}";
			  
			String responseBody = Request.Post(uri)
			          .bodyString(message, ContentType.APPLICATION_JSON)
			          .execute()
			          .returnContent()
			          .asString();
			System.out.println(responseBody);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void send (String me, String friend, String myMessage ){
		 URI uri;
			try {
				uri = new URIBuilder("http://chitchat.andrej.com/messages")
				          .addParameter("username", me)
				          .build();

				String message = "{ \"global\" : false, \"recipient\" : \""+friend+"\", \"text\" : \""+myMessage+"\"}";
				  
				String responseBody = Request.Post(uri)
				          .bodyString(message, ContentType.APPLICATION_JSON)
				          .execute()
				          .returnContent()
				          .asString();
				System.out.println(responseBody);
				
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void recive (String me) {
		 URI uri;
		try {
			uri = new URIBuilder("http://chitchat.andrej.com/messages")
			          .addParameter("username", me)
			          .build();
			String responseBody;
			responseBody = Request.Get(uri)
                    .execute()
                    .returnContent()
                    .asString();
			System.out.println(responseBody);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		  
	}
	}
	

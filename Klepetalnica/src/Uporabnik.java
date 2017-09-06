import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Uporabnik {
	private String uporabniskoIme;
	private Date zadnjicPrijavljen;
	
	private Uporabnik() { }
	
	public Uporabnik(String uporabniskoIme, Date zadnjicPrijavljen) {
		this.uporabniskoIme = uporabniskoIme;
		this.zadnjicPrijavljen = zadnjicPrijavljen;
	}
	
	@Override
	public String toString() {
		return "Uporabnik [username=" + uporabniskoIme + ", lastActive=" + zadnjicPrijavljen + "]";
	}

	@JsonProperty("username")
	public String getUsername() {
		return uporabniskoIme;
	}

	public void setUsername(String uporabniskoIme) {
		this.uporabniskoIme = uporabniskoIme;
	}

	@JsonProperty("last_active")
	public Date zadnjicPrijavljen() {
		return this.zadnjicPrijavljen;
	}

	public void setLastActive(Date zadnjicPrijavljen) {
		this.zadnjicPrijavljen = zadnjicPrijavljen;
}

}

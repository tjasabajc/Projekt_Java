import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

@SuppressWarnings("serial")
public class ChatFrame extends JFrame implements ActionListener, KeyListener {
	
	private JTextArea prikazSporocil;
	private JTextField vnosSporocila;
	private JTextArea prijavljeniUporabniki;
	private JTextField vnosVzdevka;
	
	private JTextArea prejetaSporocila;
	private JTextField prejemnik;
	
	private JButton prijava;
	private JButton odjava;
	
	private JSplitPane splitPaneVelik;
	
	private boolean prijavljen=false;
	
	private boolean prvic=true;
	
	SporocilaRobot novaSporocila; 
	UporabnikiRobot noviPrijavljeniUporabniki;

	public ChatFrame() {
		super();
		
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		setTitle("Klepetalnica");
		
		novaSporocila = new SporocilaRobot(this);
		noviPrijavljeniUporabniki = new UporabnikiRobot(this);
		
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
		JLabel napisOsnovni = new JLabel("Za pošiljanje sporoèil se morate prijaviti.");
		// TODO Èe je samo en prijavljen ?
		JPanel osnovni = new JPanel();
		osnovni.setPreferredSize(new Dimension(400,50));
		
		prijava = new JButton("Prijava");
		odjava = new JButton("Odjava");
		
		prijava.addActionListener(this);
		odjava.addActionListener(this);
		odjava.setEnabled(false);
		
		osnovni.setLayout(layout);
		osnovni.add(napisOsnovni);
		
		osnovni.add(prijava);
		osnovni.add(odjava);
		
		GridBagConstraints conOsnovni = new GridBagConstraints();
		conOsnovni.weightx = 1.0;
		conOsnovni.gridx = 0;
		conOsnovni.gridy = 0;
		conOsnovni.fill = GridBagConstraints.HORIZONTAL;
		
		//pane.add(osnovni, conOsnovni);
		
			
		// Obmoèje, kjer se prikazujejo poslana in prejeta sporoèila.
		
		this.prikazSporocil = new JTextArea(30, 40);
		this.prikazSporocil.setEditable(false);
		GridBagConstraints conSporocila = new GridBagConstraints();
		conSporocila.weighty = 1.0;
		conSporocila.gridx = 0;
		conSporocila.gridy = 1;
		JScrollPane scrollPaneSporocila = new JScrollPane(prikazSporocil);
		//pane.add(scrollPaneSporocila, conSporocila);

		
		// Okence, kamor vnesemo sporoèilo in 
		// ga pošljemo s pritiskom na tipko enter.
		
		JPanel vnosni = new JPanel();
		vnosni.setLayout(layout);
		GridBagConstraints conVnosni = new GridBagConstraints();
		conVnosni.weighty = 0.5;
		conVnosni.gridx = 0;
		conVnosni.gridy = 2;
		conVnosni.fill = GridBagConstraints.HORIZONTAL;
		
		this.vnosSporocila = new JTextField(29);
		this.vnosSporocila.setEditable(false);
		GridBagConstraints conVnos = new GridBagConstraints();
		conVnos.gridx = 1;
		conVnos.gridy = 0;
		vnosSporocila.addKeyListener(this);
		//pane.add(vnosSporocila, conVnos);

		
		this.vnosVzdevka = new JTextField(System.getProperty("user.name"), 10);
		GridBagConstraints conVzdevek = new GridBagConstraints();
		conVzdevek.weightx = 0.5;
		//conVzdevek.weighty = 0.5;
		conVzdevek.gridx = 0;
		conVzdevek.gridy = 0;
		//conVzdevek.fill = GridBagConstraints.HORIZONTAL;
		//osnovni.add(vnosVzdevka);
		//pane.add(vnosVzdevka, conVzdevek);
		vnosVzdevka.addKeyListener(this);
		
		vnosni.add(vnosVzdevka, conVzdevek);
		vnosni.add(vnosSporocila, conVnos);
		
		//pane.add(vnosni, conVnosni);
		
		JPanel levi = new JPanel();
		//levi.setLayout(layout);
		levi.setPreferredSize(new Dimension(470,600));
		GridBagConstraints conLevi = new GridBagConstraints();
		conLevi.gridx = 0;
		conLevi.gridy = 0;
		conLevi.fill = GridBagConstraints.VERTICAL;
		
		levi.add(osnovni, conOsnovni);
		levi.add(scrollPaneSporocila, conSporocila);
		levi.add(vnosni, conVnosni);
		
		//pane.add(levi, conLevi);
		
		JPanel desni = new JPanel();
		desni.setPreferredSize(new Dimension(300,600));
		GridBagConstraints conDesni = new GridBagConstraints();
		conDesni.gridx = 1;
		conDesni.gridy = 0;
		conDesni.fill = GridBagConstraints.VERTICAL;
			
		this.prijavljeniUporabniki = new JTextArea(10,25);
		this.prijavljeniUporabniki.setEditable(false);
		GridBagConstraints conUporabniki = new GridBagConstraints();
		//conUporabniki.weightx =0.5;
		conUporabniki.weighty =0.5;
		conUporabniki.gridx = 0;
		conUporabniki.gridy = 0;
		conUporabniki.fill = GridBagConstraints.HORIZONTAL;
		JScrollPane scrollPaneUporabniki = new JScrollPane(prijavljeniUporabniki);
		desni.add(scrollPaneUporabniki, conUporabniki);
		
		this.prejetaSporocila = new JTextArea(23, 25);
		this.prejetaSporocila.setEditable(false);
		GridBagConstraints conPreSporocila = new GridBagConstraints();
		//conPreSporocila.weightx =0.5;
		conPreSporocila.weighty =0.5;
		conPreSporocila.gridx = 0;
		conPreSporocila.gridy = 1;
		conPreSporocila.fill = GridBagConstraints.HORIZONTAL;
		JScrollPane scrollPanePreSporocila = new JScrollPane(prejetaSporocila);
		desni.add(scrollPanePreSporocila, conPreSporocila);
		
		JLabel navodiloPrejemnik = new JLabel("Prejemnik sporoèila: ");
		desni.add(navodiloPrejemnik);
		
		this.prejemnik = new JTextField(15);
		this.prejemnik.setEditable(false);
		GridBagConstraints conPrejemnik = new GridBagConstraints();
		//conVzdevek.weightx = 0.5;
		//conVzdevek.weighty = 0.5;
		conPrejemnik.gridx = 0;
		conPrejemnik.gridy = 3;
		conPrejemnik.fill = GridBagConstraints.HORIZONTAL;
		//osnovni.add(vnosVzdevka);
		//pane.add(vnosVzdevka, conVzdevek);
		prejemnik.addKeyListener(this);
		desni.add(prejemnik, conPrejemnik);
		
		//pane.add(desni, conDesni);
		
		splitPaneVelik = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, levi, desni);
		splitPaneVelik.setOneTouchExpandable(true);
		splitPaneVelik.setDividerLocation(470);
		levi.setMinimumSize(new Dimension(400,600));
		desni.setMinimumSize(new Dimension(150,600));
		
		pane.add(splitPaneVelik);
		
		// Ko odpremo okno, je fokus na polju za vnos sporoèila,
		// tako da lahko takoj zaènemo tipkati.
		
		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				vnosSporocila.requestFocusInWindow();
			}
			public void windowClosing(WindowEvent e){
				try {
					odjava();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);}
		});
	}

	public void dodajSporocilo(String oseba, String vsebina) {
		String klepetalnica = this.prikazSporocil.getText();
		this.prikazSporocil.setText(klepetalnica + oseba + ": " + vsebina + "\n");		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == prijava) {
	        try {
	        	prijava();
	        	//this.dodajSporocilo("Klepetalnica", "Pozdravljeni, " + this.vnosVzdevka.getText() + " !" + "\n" + "*****");
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }if (e.getSource() == odjava){
        	try {
        		this.dodajSporocilo("Klepetalnica", "***** O D J A V A *****");
				odjava();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.vnosSporocila && !this.vnosSporocila.getText().equals("")) {
			if (e.getKeyChar() == '\n') {
				this.dodajSporocilo(this.vnosVzdevka.getText(), this.vnosSporocila.getText());
				Prejeto sporocilo = new Prejeto();
				if (this.prejemnik.getText().equals("")) {
					sporocilo.setGlobal(true);
				} else {
					sporocilo.setGlobal(false);
					sporocilo.setRecipient(this.prejemnik.getText());
				}
				sporocilo.setText(this.vnosSporocila.getText());
				this.vnosSporocila.setText("");
				this.prejemnik.setText("");
				
				ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(new ISO8601DateFormat());
				String vsebina;
				try {
					vsebina = mapper.writeValueAsString(sporocilo);
					try {
			        	URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
			        	          .addParameter("username", this.vnosVzdevka.getText())
			        	          .build();

			        	     	  String responseBody = Request.Post(uri)
			        	          .bodyString(vsebina, ContentType.APPLICATION_JSON)
			        	          .execute()
			        	          .returnContent()
			        	          .asString();

			        	  System.out.println(responseBody);
			        } catch (IOException e1) {
			            e1.printStackTrace();
			        } catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (JsonProcessingException e2) {
					e2.printStackTrace();
				}	}
		}		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	

	public void pobrisiPrijavljene() {
		this.prijavljeniUporabniki.setText(null);
		
	}

	public void zapisiUporabnika(Uporabnik oseba) {
		this.prijavljeniUporabniki.append(oseba.getUsername() + "\n");
		
	}

	public boolean getStatus() {
		return prijavljen;
	}

	public void zapisiPrejetoSporocilo(Prejeto sporocilo) {
		if (sporocilo.getGlobal()) {
			this.prikazSporocil.append((sporocilo.getSender() + ": " + sporocilo.getText()) + "\n");
		} else {
			this.prejetaSporocila.append((sporocilo.getSender() + ": " + sporocilo.getText() + "    Poslano ob: " + sporocilo.getSent_at()) + "\n");
		
		}
		
	}

	public String getUsername() {
		return this.vnosVzdevka.getText();
	}
	
	public void prijava() throws URISyntaxException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/users")
				.addParameter("username", getUsername()).build();
		HttpResponse response = Request.Post(uri).execute().returnResponse();
		InputStream responseText = null;
		if (response.getStatusLine().getStatusCode()==200) {
		
			if (prvic) {
				noviPrijavljeniUporabniki.pozeni();
				novaSporocila.pozeni();
				prvic=false;

			}
			prijavljen=true;
			this.odjava.setEnabled(true);
			this.prijava.setEnabled(false);
			this.vnosSporocila.setEditable(true);
			this.prejemnik.setEditable(true);
			responseText=response.getEntity().getContent();
						}else if(response.getStatusLine().getStatusCode()==403){

							
			responseText=response.getEntity().getContent();
		}
		System.out.println("responseText: " + getStringFromInputStream(responseText));
	} ;
	
	public void odjava() throws URISyntaxException {
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/users")
					.addParameter("username", getUsername()).build();

			String responseBody = Request.Delete(uri).execute().returnContent().asString();

			System.out.println(responseBody);
			prijavljen=false;
			this.odjava.setEnabled(false);
			this.prijava.setEnabled(true);
			this.vnosSporocila.setText("");
			this.vnosSporocila.setEditable(false);
			this.prejemnik.setEditable(false);
			this.prijavljeniUporabniki.setText("");
			//this.novaSporocila.cancel();
			//this.noviPrijavljeniUporabniki.cancel();
		} catch (IOException e) {
			e.printStackTrace();
			}
}

	
	private static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
return sb.toString();
}

	public void zapisiPrejetoSporocilo(String obvestilo) {
		this.prejetaSporocila.append(obvestilo);
	}

	public void niUporabnikov() {
		this.prijavljeniUporabniki.setText("Trenutno ni prijavljenih uporabnikov");
		
	}

	public void niSporocil() {
		this.prejetaSporocila.setText("Od vaše zadnje prijave ni novih sporoèil.");
		
	}
}

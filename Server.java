import java.awt.List;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;


public class Server {

	private enum goodGuysNames {
		SUPERMAN, BATMAN, SPIDER_MAN, THOR, CAPTAIN_AMERICA, WONDER_WOMAN, HULK;

		public static goodGuysNames getRandomNames() {
			Random rand = new Random();
			return values()[rand.nextInt(values().length)];
		}
	}
	
	private ObjectOutputStream outputToFile;
	private ObjectInputStream inputFromClient;

	public static void main(String[] args) {
		new Server();
	}

public Server() {
	int count = 0;
	Object object;
	 ArrayList<Person> receiveQueue = new ArrayList<Person>();
	try {     
	
	// Create a server socket
	@SuppressWarnings("resource")
	ServerSocket serverSocket = new ServerSocket(8000);
	System.out.println("\t\tServer started ");
	ObjectOutputStream toClient;
	
	// Create an object ouput stream
	outputToFile = new ObjectOutputStream (new FileOutputStream("villain.dat", true));
	while (true) {
		// Listen for a new connection request
		Socket socket = serverSocket.accept();
		System.out.println("\nClient #" + (++count) + " has been connected.\n");
		// Create an input stream from the socket
		inputFromClient = new ObjectInputStream(socket.getInputStream());
		// Read from input
		object = inputFromClient.readObject();
		System.out.println("\nServer receice an Object:\n" + object.toString());
		receiveQueue.clear();
		receiveQueue.add((Person) object);
		// Write to the file
		outputToFile.writeObject(object);
		System.out.println("\nA new villain object is stored");
		
		for(Person person : receiveQueue) {
			if (person.getType().equalsIgnoreCase("villain")) {
				SuperThing evilPerson = new SuperThing();
				Person thing = evilPerson.createThing("hero");
				thing.setName(goodGuysNames.getRandomNames().toString());
				thing.setType("hero");
				if (person.getSuperPower().equals("strong person")) {
					thing.setSuperPower("strong person");
				} else {
					thing.setSuperPower("flying person");
				}
				System.out.println("\nHero has bean created !!!\n" + thing.toString());
				
				// Stream object for sending object 
				toClient = new ObjectOutputStream(socket.getOutputStream());
				toClient.writeObject(thing);
			}
		}
		
		}
	}catch(ClassNotFoundException ex) {
		ex.printStackTrace();
	}catch(IOException ex) {
		ex.printStackTrace();
	}finally {
		try {
			inputFromClient.close();
			outputToFile.close();
		}catch (Exception ex) {
			ex.printStackTrace();
			}
		}
}

}


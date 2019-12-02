import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

// Enum Generate random names
enum badGuysNames {
	MAGNETO, JOKER, KINGPIN, LOKI, MYSTIQUE, VENOM, SHREDDER, DOCTOR_DOOM, NORMAN_OSBORN, RED_SKULL, GALACTUS, ULTRON,
	THANOS, APOCALYPSE, DOCTOR_OCTAPUS, ANNIHILUS, JUGGERNAUT;

	public static badGuysNames getRandomNames() {
		Random rand = new Random();
		return values()[rand.nextInt(values().length)];
	}
}

// Thread sending objects to the server
class MyThread extends Thread {
	Socket clientSocket;
	ObjectOutputStream toServer;
	boolean isConnected = false;

	public void run() {

		// synchronized void waitFor() throws Exception {
		while (!isConnected) {
			// System.out.println("Waiting for connection");
			// wait();
			// }
			// }

			// synchronized void start() {

			try {
				clientSocket = new Socket("127.0.0.1", 8000);
				System.out.println("Connected to the server\n");
				isConnected = true;
				//notify();

				// Create object of Villain
				SuperThing evilPerson = new SuperThing();
				Person thing = evilPerson.createThing("villain");

				thing.setName(badGuysNames.getRandomNames().toString());
				thing.setType("villain");

				int random = (int) (Math.random() * 2);
				if (random == 1) {
					thing.setSuperPower("strong person");
				} else {
					thing.setSuperPower("flying person");
				}

				System.out.println("Villain created:\n" + thing.toString() + "\nSending villain to the server\n");

				// Create an outputstream to the server
				toServer = new ObjectOutputStream(clientSocket.getOutputStream());
				toServer.writeObject(thing);
				
				System.out.println("Waiting for result from Server");
				
				InputStream toClient = clientSocket.getInputStream();
		        ObjectInputStream fromServer = new ObjectInputStream(toClient);
		        Object object = fromServer.readObject();
		        
		        System.out.println("Object received from Server:\n" + object.toString() + "\n");
				

			} catch (IOException e) {
				e.getStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (clientSocket != null) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (toServer != null) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}
		// }
	}
}

// Main Class
public class Client {

	public static void main(String[] args) {

		MyThread thread1 = new MyThread();
		MyThread thread2 = new MyThread();
		thread1.start();
		try {
			thread1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread2.start();
		try {
			thread2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
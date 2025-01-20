
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class ChatServerDriver {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        try {
            Naming.rebind("RMIChatServer", new ChatServer());

            // Nie dziala wypisanie
            System.out.println("ChatServer is running and waiting for clients...");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

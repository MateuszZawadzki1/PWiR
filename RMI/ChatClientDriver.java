import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChatClientDriver {
        public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
            String chatServerURL = "rmi://localhost/RMIChatServer";
            ChatServerIF chatServer = (ChatServerIF) Naming.lookup(chatServerURL);
            new Thread(new ChatClient(args[0], chatServer)).start();
        }
}

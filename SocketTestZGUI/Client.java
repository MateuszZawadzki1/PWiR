import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;


public class Client {
    
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
    
            // Wysyłamy nazwę użytkownika natychmiast po połączeniu
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
    
            // Inicjalizacja GUI
            initializeGUI();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void initializeGUI() {
        frame = new JFrame("Chat Client - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());
    
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        frame.add(scrollPane, BorderLayout.CENTER);
    
        // Panel na pole tekstowe i przycisk
        JPanel inputPanel = new JPanel(new BorderLayout());
    
        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);
    
        sendButton = new JButton("Send");
        
        // Ograniczamy rozmiar przycisku "Send"
        sendButton.setPreferredSize(new Dimension(80, 30)); 
    
        inputPanel.add(sendButton, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);
    
        // Obsługa przycisku "Send"
        sendButton.addActionListener(e -> sendMessage());
    
        // Obsługa klawisza Enter w polu tekstowym
        messageField.addActionListener(e -> sendMessage());
    
        frame.setVisible(true);
    }
    

    public void sendMessage() {
        try {
            String messageToSend = messageField.getText();
            if (!messageToSend.isEmpty()) {
                String formattedMessage = username + ": " + messageToSend;
                
                chatArea.append(formattedMessage + "\n");
                

                bufferedWriter.write(formattedMessage);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                
                messageField.setText("");
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessages() {
        new Thread(() -> {
            String msgFromGroupChat;
            while (socket.isConnected()) {
                try {
                    msgFromGroupChat = bufferedReader.readLine();
                    if (msgFromGroupChat != null) {
                        if (!msgFromGroupChat.startsWith(username + ":")) {
                            msgFromGroupChat = ">>> " + msgFromGroupChat; // Dodanie strzałek dla cudzych wiadomości
                        }
                        chatArea.append(msgFromGroupChat + "\n");
                    }
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        String username = JOptionPane.showInputDialog("Enter your username:");
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username cannot be empty!");
            return;
        }
        
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, username);
        client.listenForMessages();
    }
    
}

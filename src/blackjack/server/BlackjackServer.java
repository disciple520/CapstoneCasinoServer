package blackjack.server;

import blackjack.gui.CapstoneCasinoBlackjackUI;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


/**
 *
 * @author Jerry
 */
public class BlackjackServer {
    /**
     * Runs the server.
     */
    
    public static void main(String[] args) throws IOException {
        InetAddress ip;
        ServerSocket listener = new ServerSocket(9090);
        try {
            while (true) {
                ip = InetAddress.getLocalHost();
		System.out.println("Current IP address : " + ip.getHostAddress());
                Socket socket = listener.accept();
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("Successfully connected to blackjack server");
                } finally {
                    socket.close();
                }
            }
        }
        finally {
            listener.close();
        }
    }
    public void testMethod(){
        System.out.println("you accessed this from the gui class");
    }
}

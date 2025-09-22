import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.Scanner;

public class Interface extends JFrame {
    private int width = 800;
    private int height = 600;
    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ExecutorService networkExecutor;
    private boolean isConnected = false;

    private boolean roomNum;    

    public Interface() {
        setTitle("Windowed Game"); // Set window title

        setSize(width, height); // Set preferred size
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close properly
        setResizable(true); // Prevent resizing

        // Add KeyListener to detect ESC key to exit

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isConnected) {
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                width = getWidth();
                height = getHeight();
            }
        });

        Timer timer = new Timer(17, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint(); // Redraw the screen
            }
        });
        timer.start();

        setFocusable(true); // Ensure key events are captured
        setVisible(true); // Show the window
        
        // Initialize network connection
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            networkExecutor = Executors.newSingleThreadExecutor();
            isConnected = true;
            
            // Start listening for server updates
            networkExecutor.execute(() -> {
                try {
                    while (isConnected) {
                        HashMap<Integer, String[]> serverState = (HashMap<Integer, String[]>) in.readObject();
                        updateGameState(serverState);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    if (isConnected) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("Could not connect to server. Running in single-player mode.");
            e.printStackTrace();
        }
    }

    private void disconnect() {
        isConnected = false;
        if (networkExecutor != null) {
            networkExecutor.shutdown();
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateGameState(HashMap<Integer, String[]> newState) {
        
    }

    public static void main(String[] args) {
        new Interface();

        // DisplayMode dm = new DisplayMode(800, 600, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
        // bucky b = new bucky();
        // b.run(dm);

        JFrame frame = new JFrame("Two Images Example");
    }

    public void run(DisplayMode dm) {
        setBackground(Color.PINK);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.PLAIN, 24));

        Screen s = new Screen();
        try {
            s.setFullScreen(dm, this);
            try {
                Thread.sleep(5000);
            } catch (Exception ex) {}
        } finally {
            s.restoreScreen();
        }
    }
}
package com.example.oxdfog.Server;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Socket {
    private ArrayList<Thread> clients = new ArrayList<>();
    private static  int PORT;
    public Socket(int port) {
        this.PORT = port;
    }
    public void firstTime()
    {
        File directory = new File("src/main/java/com/example/oxdfog/Server/Resources");
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        });
        ArrayList<String> queries = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                try {
                    Scanner scanner = new Scanner(file);
                    String id = scanner.nextLine();
                    String title = scanner.nextLine();
                    String developer = scanner.nextLine();
                    String genre = scanner.nextLine();
                    String filepath = id+".png";
                    double price = scanner.nextDouble();
                    int year = scanner.nextInt();
                    boolean controller = scanner.nextBoolean();
                    int reviews = scanner.nextInt();
                    int size = scanner.nextInt();
                    int minAge = 15;
                    queries.add("INSERT INTO games (id, title, developer, genre, price, release_year, controller_support, reviews, size, file_path, minage) VALUES ('"+id+"' , '"+title+"' , '"+developer+"' , '"+genre+"' , "+price+" , "+year+", "+controller+" , "+reviews+" , "+size+" , '"+filepath+"' , "+minAge+");");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //addToDb(queries);
    }
    public void addToDb(ArrayList<String> queries) {
        String url = "jdbc:postgresql://localhost:5433/OXDFog";
        String username = "oxd";
        String password = "password";
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            try {
                for (String query : queries) {
                    PreparedStatement stmt = conn.prepareStatement(query);
                    try (ResultSet rs = stmt.executeQuery()) {

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        Socket socket = new Socket(1056);
        //socket.firstTime();
       socket.start();
    }
    public void start() {
        try {

            ServerSocket serverSocket = new ServerSocket(1056);
        System.out.println("Server started. Listening on port " + PORT);

        // Listen for incoming connections
        while (true) {
            java.net.Socket clientSocket = serverSocket.accept();

            // Create a new thread to handle the client connection
            Thread clientThread = new Thread(new Session(clientSocket));
            clientThread.start();
        }
        } catch (IOException e) {
        e.printStackTrace();
        }
    }
}

package com.example.oxdfog.Server;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Session implements Runnable{
    private final java.net.Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private User user;
    private Connection connection;
    private Game currentGame;
    private Gson gson;

    public Session(java.net.Socket socket) throws IOException {
        this.socket = socket;
        in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out= new PrintWriter(socket.getOutputStream(),true);
        String url = "jdbc:postgresql://localhost:5433/OXDFog";
        String username = "oxd";
        String password = "password";
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        gson = new GsonBuilder().create();
    }

    /*
        sql table creating
    -- Create the Games table
    CREATE TABLE Games (
      id TEXT PRIMARY KEY,
      title TEXT,
      developer TEXT,
      genre TEXT,
      price DOUBLE PRECISION,
      release_year INTEGER,
      controller_support BOOLEAN,
      reviews INTEGER,
      size INTEGER,
      file_path TEXT,
      minAge INTEGER
    );

    -- Create the Accounts table
    CREATE TABLE Accounts (
      id TEXT PRIMARY KEY,
      username TEXT,
      password TEXT,
      age INTEGER
          );

    -- Create the Downloads table
    CREATE TABLE Downloads (
      account_id TEXT,
      game_id TEXT,
      download_count INTEGER,
      FOREIGN KEY (account_id) REFERENCES Accounts (id),
      FOREIGN KEY (game_id) REFERENCES Games (id)
    );

         */
    @Override
    public void run() {
        while(true)
        {
            try {
            String job = in.readLine();
            if(Objects.equals(job, "login")) {
                String id;
                String password;
                JSONObject json = new JSONObject(in.readLine());
                id = json.getString("id");
                password = json.getString("password");
                if ((user = checkUser(id, password)) != null) {
                    out.println(gson.toJson(user));
                    break;
                }
                out.println("login failed");
            }else if(Objects.equals(job, "sign up"))
            {
                String id;
                String username;
                String password;
                int age;
                JSONObject json = new JSONObject(in.readLine());
                username=json.getString("username");
                password=json.getString("password");
                age=json.getInt("age");
                id= UUID.randomUUID().toString();
                if((user=addUser(id,username,password,age))!=null)
                {
                    out.println(id);
                    break;
                }
                out.println("this id has been added before");
            }
            }catch (IOException e)
            {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        while(true) {
            try {
                String job;
                job = in.readLine();
                if (Objects.equals(job, "update user")) {
                    String id;
                    String username;
                    String password;
                    int age;
                    id = in.readLine();
                    username = in.readLine();
                    password = in.readLine();
                    age = in.read();
                    if ((user = updateUser(id, username, password, age)) == null)
                        out.println("update failed");
                    else
                        out.println("update done");
                }else if(Objects.equals(job, "view game"))
                {
                    String id;
                    id=in.readLine();
                    Game game;
                    if((game=getGame(id))==null)
                        out.println("game not found");
                    else {
                        out.println(gson.toJson(game));
                    }
                }else if(Objects.equals(job, "games list"))
                {
                    out.println(gson.toJson(gameList()));
                }
                else if(Objects.equals(job, "download game"))
                {
                    String id;
                    id =in.readLine();
                    Game game;
                    if((game=getGame(id))==null)
                    {
                        out.println("game not found");
                    }else
                    {
                       download(user.getId(),id);
                        File imageFile = new File("G:\\code\\java\\OXDFog\\src\\main\\java\\com\\example\\oxdfog\\Server\\Resources\\"+game.getFilePath());
                        byte[] imageData = new byte[(int) imageFile.length()];
                        FileInputStream fis = new FileInputStream(imageFile);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        bis.read(imageData, 0, imageData.length);
                        OutputStream os = socket.getOutputStream();
                        os.write(imageData, 0, imageData.length);
                        os.flush();
                        os.close();
                        bis.close();
                        fis.close();
                    }
                }else if(Objects.equals(job, "play game"))
                {
                    String id;
                    id= in.readLine();
                    Game game;
                    if((game=getGame(id))==null)
                        out.println("game not found");
                    currentGame=game;
                    out.println("game started");
                }else if(Objects.equals(job, "stop game"))
                {
                    currentGame=null;
                    out.println("game have been stopped");
                }else if(Objects.equals(job, "close"))
                    break;
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            socket.close();
            in.close();
            out.close();
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void download(String id,String gameId)
    {
        ResultSet res=sendQuery("SELECT * from downloads where account_id = '"+id+"' and game_id = '"+gameId+"';");
        if(res==null)
        {
            sendQuery("INSERT INTO downloads (account_id , game_id , download_count) VALUES ('"+id+"' , '"+gameId+"' , 1 )");
        }else {
            sendQuery("UPDATE downloads SET download_count = download_count+1 WHERE account_id = '"+id+"' and game_id = '"+gameId+"';");
        }
    }
    private User checkUser(String id,String password) throws SQLException {
        ResultSet res = sendQuery("SELECT * FROM accounts where id ='" +id+"';");
        if(res == null)
            return null;
        if(!checkPassword(password,res.getString("password")))
            return null;
        return new User(res.getString("id"),res.getString("username"),res.getString("password"),res.getInt("age"));
    }
    private User addUser(String id,String username,String password,int age) throws SQLException {
        ResultSet res = sendQuery("SELECT * FROM accounts where id ='" +id+"';");
        if(res != null)
            return null;
        password= hash(password);
        sendQuery("INSERT INTO accounts (id, username, password, age) values ('"+id+"','"+username+"','"+password+"',"+age+");");
        return new User(id,username,password,age);
    }
    private User updateUser(String id,String username,String password,int age) throws SQLException {
        User user;
        if((user=checkUser(id,password))==null)
            return null;
        password=hash(password);
        user.setAge(age);
        user.setPassword(password);
        user.setUsername(username);
        sendQuery("UPDATE accounts SET username = '"+username+"', password = '"+password+"', age = "+age+" WHERE id = '"+id+"';");
        return user;
    }
    private Game getGame(String id) throws SQLException {
        ResultSet res = sendQuery("SELECT * FROM Games WHERE id = '"+id+"'");
        if(res==null)
            return null;
        return new Game(res.getString("id"),res.getString("title"),res.getString("developer"),res.getString("genre"),res.getString("file_path"),res.getDouble("price"), res.getInt("release_year"), res.getInt("reviews"), res.getInt("size"), res.getInt("minage"),res.getBoolean("controller_support"));
    }
    private ArrayList<Game> gameList()
    {
        ArrayList<Game> ret = new ArrayList<>();
        ResultSet res=sendQuery("SELECT * FROM Games;");
        while(true) {
            try {
                if (!res.next()) break;
                ret.add(new Game(res.getString("id"),res.getString("title"),res.getString("developer"),res.getString("genre"),res.getString("file_path"),res.getDouble("price"), res.getInt("release_year"), res.getInt("reviews"), res.getInt("size"), res.getInt("minage"),res.getBoolean("controller_support")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return ret;
    }
    private String hash(String input)
    {
        return BCrypt.hashpw(input,BCrypt.gensalt());
    }
    private boolean checkPassword(String input,String hashed)
    {
        return BCrypt.checkpw(input,hashed);
    }
    private synchronized ResultSet sendQuery(String query)
    {
        try {
             PreparedStatement stmt = connection.prepareStatement(query);
            try {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs;
                }
            }catch (SQLException e)
            {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

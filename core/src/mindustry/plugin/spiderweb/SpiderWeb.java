package mindustry.plugin.spiderweb;

import arc.*;
import arc.struct.*;
import arc.struct.Array;
import arc.util.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.io.*;

import java.sql.*;

public class SpiderWeb implements ApplicationListener{

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private ObjectSet<String> uuids = new ObjectSet<>();

    public SpiderWeb(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/nydus?user=root&password=root&useSSL=false");
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public boolean has(String uuid){
        return get(uuid) != null;
    }

    public Spiderling get(String uuid){
        try{
            preparedStatement = connect.prepareStatement("SELECT * FROM uuids WHERE uuid = ?");
            preparedStatement.setString(1, uuid);
            resultSet = preparedStatement.executeQuery();

            uuids.clear();
            if(resultSet.next()){
                Spiderling sl = new Spiderling();
                sl.uuid = resultSet.getString("uuid");
                return sl;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void add(String uuid){
        try{
            preparedStatement = connect.prepareStatement("INSERT INTO uuids VALUES (?)");
            preparedStatement.setString(1, uuid);
            preparedStatement.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void loadNames(Spiderling spiderling){
        try{
            preparedStatement = connect.prepareStatement("SELECT * FROM names WHERE uuid = ?");
            preparedStatement.setString(1, spiderling.uuid);
            resultSet = preparedStatement.executeQuery();
            spiderling.names.clear();

            while(resultSet.next()){
                spiderling.names.add(resultSet.getString("name"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void saveNames(Spiderling spiderling){
        spiderling.names.each(name -> {
            try{
                preparedStatement = connect.prepareStatement("INSERT INTO names VALUES(?, ?) ON DUPLICATE KEY UPDATE uuid = uuid");
                preparedStatement.setString(1, spiderling.uuid);
                preparedStatement.setString(2, name);
                preparedStatement.execute();
            }catch(SQLException e){
                e.printStackTrace();
            }
        });
    }


    public void loadUnlockedBlocks(Spiderling spiderling){
        try{
            preparedStatement = connect.prepareStatement("SELECT * FROM unlocked_blocks WHERE uuid = ?");
            preparedStatement.setString(1, spiderling.uuid);
            resultSet = preparedStatement.executeQuery();
            spiderling.unlockedBlocks.clear();

            while(resultSet.next()){
                spiderling.unlockedBlocks.add(Vars.content.getByName(ContentType.block, resultSet.getString("block")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void saveUnlockedBlocks(Spiderling spiderling){
        spiderling.unlockedBlocks.each(block -> {
            try{
                preparedStatement = connect.prepareStatement("INSERT INTO unlocked_blocks VALUES(?, ?) ON DUPLICATE KEY UPDATE uuid = uuid");
                preparedStatement.setString(1, spiderling.uuid);
                preparedStatement.setString(2, block.name);
                preparedStatement.execute();
            }catch(SQLException e){
                e.printStackTrace();
            }
        });
    }

    @Override
    public void init(){
        Events.on(BlockBuildEndEvent.class, event -> {
            if(event.breaking) return;
            if(event.player == null) return;
            if(event.player.spiderling.unlockedBlocks.contains(event.tile.block)) return;
            event.player.spiderling.unlockedBlocks.add(event.tile.block);
            event.player.spiderling.save();
        });
    }
}

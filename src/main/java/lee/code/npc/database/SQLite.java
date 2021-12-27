package lee.code.npc.database;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.PU;
import lee.code.npc.lists.SupportedVillagerProfessions;
import lee.code.npc.lists.SupportedVillagerTypes;
import lee.code.npc.nms.VillagerNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class SQLite {

    private Connection connection;
    private Statement statement;

    public void connect() {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        connection = null;

        try {
            if (!plugin.getDataFolder().exists()) {
                boolean created = plugin.getDataFolder().mkdir();
            }
            File dbFile = new File(plugin.getDataFolder(), "database.db");
            if (!dbFile.exists()) {
                boolean created = dbFile.createNewFile();
            }
            String url = "jdbc:sqlite:" + dbFile.getPath();

            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();

        } catch (IOException | SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void update(String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public ResultSet getResult(String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void loadTables() {
        update("CREATE TABLE IF NOT EXISTS npc (" +
                "`name` varchar PRIMARY KEY," +
                "`location` varchar NOT NULL," +
                "`profession` varchar NOT NULL," +
                "`type` varchar NOT NULL," +
                "`command` varchar NOT NULL," +
                "`command_type` varchar NOT NULL" +
                ");");
    }

    public void createNPC(String name, String location, String profession, String type, String command, String commandType) {
        update("INSERT INTO npc (name, location, profession, type, command, command_type) VALUES( '" + name + "','" + location + "','" + profession + "','" + type + "','" + command + "','" + commandType + "');");
    }

    public void removeNPC(String name) {
        update("DELETE FROM npc WHERE name = '" + name + "';");
    }

    public void setName(String oldName, String newName, String location, String profession, String type, String command, String commandType) {
        update("DELETE FROM npc WHERE name = '" + oldName + "';");
        update("INSERT INTO npc (name, location, profession, type, command, command_type) VALUES( '" + newName + "','" + location + "','" + profession + "','" + type + "','" + command + "','" + commandType + "');");
    }

    public void setLocation(String name, String location) {
        update("UPDATE npc SET location ='" + location + "' WHERE name ='" + name + "';");
    }

    public void setType(String name, String type) {
        update("UPDATE npc SET type ='" + type + "' WHERE name ='" + name + "';");
    }

    public void setProfession(String name, String profession) {
        update("UPDATE npc SET profession ='" + profession + "' WHERE name ='" + name + "';");
    }

    public void setCommand(String name, String command) {
        update("UPDATE npc SET command ='" + command + "' WHERE name ='" + name + "';");
    }

    public void setCommandType(String name, String commandType) {
        update("UPDATE npc SET command_type ='" + commandType + "' WHERE name ='" + name + "';");
    }

    public void loadNPCs() {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        PU pu = plugin.getPU();
        Cache cache = plugin.getCache();

        try {
            ResultSet rs = getResult("SELECT * FROM npc;");
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            int count = 0;
            while (rs.next()) {
                String nameString = rs.getString("name");
                String locationString = rs.getString("location");
                String professionString = rs.getString("profession");
                String typeString = rs.getString("type");
                String commandString = rs.getString("command");
                String commandTypeString = rs.getString("command_type");

                cache.setNPC(nameString, locationString, professionString, typeString, commandString, commandTypeString);

                String name = pu.format(nameString);
                Location location = pu.unFormatEntityLocation(locationString);
                ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();

                location.getWorld().getChunkAtAsync(location, false);
                location.getChunk().setForceLoaded(true);

                VillagerType type = SupportedVillagerTypes.valueOf(typeString).getType();
                VillagerProfession profession = SupportedVillagerProfessions.valueOf(professionString).getProfession();

                VillagerNPC villager = new VillagerNPC(location, type, profession, name);
                scheduler.runTaskLater(plugin, () -> world.addFreshEntity(villager), 60);
                count++;
            }
            Bukkit.getLogger().log(Level.INFO, pu.format("&3NPCs Loaded: &b" + count));
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
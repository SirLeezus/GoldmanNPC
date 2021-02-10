package lee.code.npc.database;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.lists.SupportedVillagerProfessions;
import lee.code.npc.lists.SupportedVillagerTypes;
import lee.code.npc.nms.VillagerNPC;
import lombok.SneakyThrows;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLite {

    private Connection connection;
    private Statement statement;

    public void connect() {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        connection = null;

        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdir();
            }
            File dbFile = new File(plugin.getDataFolder(), "database.db");
            if (!dbFile.exists()) {
                dbFile.createNewFile();
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
            statement.execute(sql);
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
        //chunk table
        update("CREATE TABLE IF NOT EXISTS npc (" +
                "`name` varchar PRIMARY KEY," +
                "`location` varchar NOT NULL," +
                "`profession` varchar NOT NULL," +
                "`type` varchar NOT NULL," +
                "`command` varchar NOT NULL" +
                ");");
    }

    public void createNPC(String name, String location, String profession, String type, String command) {
        update("INSERT INTO npc (name, location, profession, type, command) VALUES( '" + name + "','" + location + "','" + profession + "','" + type + "','" + command + "');");
    }

    @SneakyThrows
    public boolean isNameTaken(String name) {
        ResultSet rs = getResult("SELECT name FROM npc WHERE name = '" + name + "';");
        return rs.next();
    }

    public void setName(String oldName, String newName) {
        update("UPDATE npc SET name ='" + newName + "' WHERE name ='" + oldName + "';");
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

    @SneakyThrows
    public List<Location> getNPCLocations() {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        ResultSet rs = getResult("SELECT * FROM npc;");

        List<Location> locations = new ArrayList<>();

        while (rs.next()) {
            String locationString = rs.getString("location");
            Location location = plugin.getPU().unFormatEntityLocation(locationString);
            locations.add(location);
        }
        return locations;
    }

    @SneakyThrows
    public Location getNPCLocation(String name) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        ResultSet rs = getResult("SELECT * FROM npc WHERE name = '" + name + "';");
        String locationString = rs.getString("location");
        return plugin.getPU().unFormatEntityLocation(locationString);
    }

    @SneakyThrows
    public List<String> getNPCNames() {
        ResultSet rs = getResult("SELECT * FROM npc;");
        List<String> names = new ArrayList<>();

        while (rs.next()) {
            String nameString = rs.getString("name");
            names.add(nameString);
        }
        return names;
    }

    @SneakyThrows
    public void loadNPC(String npc) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();

        ResultSet rs = getResult("SELECT * FROM npc WHERE name = '" + npc + "';");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        String nameString = rs.getString("name");
        String locationString = rs.getString("location");
        String professionString = rs.getString("profession");
        String typeString = rs.getString("type");

        String name = plugin.getPU().format(nameString);
        Location location = plugin.getPU().unFormatEntityLocation(locationString);
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();

        VillagerType type = SupportedVillagerTypes.valueOf(typeString).getType();
        VillagerProfession profession = SupportedVillagerProfessions.valueOf(professionString).getProfession();
        VillagerNPC villager = new VillagerNPC(location, type, profession, name);

        scheduler.runTaskLater(plugin, () -> world.addEntity(villager), 10);
    }

    @SneakyThrows
    public void loadNPCs() {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();

        ResultSet rs = getResult("SELECT * FROM npc;");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        while (rs.next()) {

            String nameString = rs.getString("name");
            String locationString = rs.getString("location");
            String professionString = rs.getString("profession");
            String typeString = rs.getString("type");

            String name = plugin.getPU().format(nameString);
            Location location = plugin.getPU().unFormatEntityLocation(locationString);
            WorldServer world = ((CraftWorld) location.getWorld()).getHandle();

            VillagerType type = SupportedVillagerTypes.valueOf(typeString).getType();
            VillagerProfession profession = SupportedVillagerProfessions.valueOf(professionString).getProfession();
            VillagerNPC villager = new VillagerNPC(location, type, profession, name);

            scheduler.runTaskLater(plugin, () -> world.addEntity(villager), 60);
        }
    }
}
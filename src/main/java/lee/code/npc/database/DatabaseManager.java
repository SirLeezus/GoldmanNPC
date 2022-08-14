package lee.code.npc.database;

import lee.code.core.ormlite.dao.Dao;
import lee.code.core.ormlite.dao.DaoManager;
import lee.code.core.ormlite.jdbc.JdbcConnectionSource;
import lee.code.core.ormlite.jdbc.db.DatabaseTypeUtils;
import lee.code.core.ormlite.logger.LogBackendType;
import lee.code.core.ormlite.logger.LoggerFactory;
import lee.code.core.ormlite.support.ConnectionSource;
import lee.code.core.ormlite.table.TableUtils;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.database.tables.NPCTable;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.SQLException;

public class DatabaseManager {

    private Dao<NPCTable, Integer> npcDao;

    @Getter(AccessLevel.NONE)
    private ConnectionSource connectionSource;

    private String getDatabaseURL() {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
        return "jdbc:sqlite:" + new File(plugin.getDataFolder(), "database.db");
    }

    public void initialize() {
        LoggerFactory.setLogBackendFactory(LogBackendType.NULL);

        try {
            String databaseURL = getDatabaseURL();
            connectionSource = new JdbcConnectionSource(
                    databaseURL,
                    "test",
                    "test",
                    DatabaseTypeUtils.createDatabaseType(databaseURL));
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            npcDao.executeRaw("ALTER TABLE `npc` ADD COLUMN placeholders BOOLEAN NOT NULL DEFAULT 'false';");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        CacheManager cacheManager = GoldmanNPC.getPlugin().getCacheManager();

        TableUtils.createTableIfNotExists(connectionSource, NPCTable.class);
        npcDao = DaoManager.createDao(connectionSource, NPCTable.class);

        for (NPCTable npcTable : npcDao.queryForAll()) cacheManager.setNPCData(npcTable);
    }

    public void closeConnection() {
        try {
            connectionSource.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void createNPCTable(NPCTable npcTable) {
        Bukkit.getScheduler().runTaskAsynchronously(GoldmanNPC.getPlugin(), () -> {
            try {
                npcDao.create(npcTable);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void deleteNPCTable(NPCTable npcTable) {
        Bukkit.getScheduler().runTaskAsynchronously(GoldmanNPC.getPlugin(), () -> {
            try {
                npcDao.delete(npcTable);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void updateNPCTable(NPCTable npcTable) {
        Bukkit.getScheduler().runTaskAsynchronously(GoldmanNPC.getPlugin(), () -> {
            try {
                npcDao.update(npcTable);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}

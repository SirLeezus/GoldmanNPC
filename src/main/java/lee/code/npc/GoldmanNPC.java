package lee.code.npc;

import lee.code.npc.commands.CommandManager;
import lee.code.npc.commands.TabCompletion;
import lee.code.npc.database.CacheManager;
import lee.code.npc.database.DatabaseManager;
import lee.code.npc.listeners.DamageListener;
import lee.code.npc.listeners.InteractListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class GoldmanNPC extends JavaPlugin {

    @Getter private DatabaseManager databaseManager;
    @Getter private CacheManager cacheManager;
    @Getter private Data data;
    @Getter private PU pU;

    @Override
    public void onEnable() {
        this.pU = new PU();
        this.databaseManager = new DatabaseManager();
        this.data = new Data();
        this.cacheManager = new CacheManager();

        registerCommands();
        registerListeners();

        databaseManager.initialize();
        data.loadData();
        pU.removeNPCs();
        pU.schedulePlaceholderChecker();
    }

    @Override
    public void onDisable() {
        pU.removeNPCs();
        databaseManager.closeConnection();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
    }

    private void registerCommands() {
        getCommand("npc").setExecutor(new CommandManager());
        getCommand("npc").setTabCompleter(new TabCompletion());
    }

    public static GoldmanNPC getPlugin() {
        return GoldmanNPC.getPlugin(GoldmanNPC.class);
    }
}

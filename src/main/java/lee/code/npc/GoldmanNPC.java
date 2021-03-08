package lee.code.npc;

import lee.code.cache.CacheAPI;
import lee.code.npc.commands.CommandManager;
import lee.code.npc.commands.TabCompletion;
import lee.code.npc.database.Cache;
import lee.code.npc.database.SQLite;
import lee.code.npc.listeners.DamageListener;
import lee.code.npc.listeners.InteractListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class GoldmanNPC extends JavaPlugin {

    @Getter private PU pU;
    @Getter private SQLite sqLite;
    @Getter private Data data;
    @Getter private CacheAPI cacheAPI;
    @Getter private Cache cache;

    @Override
    public void onEnable() {

        this.pU = new PU();
        this.sqLite = new SQLite();
        this.data = new Data();
        this.cacheAPI = new CacheAPI();
        this.cache = new Cache();

        registerCommands();
        registerListeners();

        sqLite.connect();
        sqLite.loadTables();

        pU.removeNPCs();
        data.cacheDatabase();
    }

    @Override
    public void onDisable() {
        pU.removeNPCs();
        sqLite.disconnect();
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

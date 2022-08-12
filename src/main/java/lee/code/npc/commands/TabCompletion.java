package lee.code.npc.commands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.database.CacheManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TabCompletion implements TabCompleter {

    private final List<String> subCommands = Arrays.asList("create", "remove", "list", "select", "tp", "location", "type", "profession", "name", "command");
    private final List<String> blank = new ArrayList<>();

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,@NotNull Command command,@NotNull String alias, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();

        if (sender instanceof Player) {
            if (args.length == 1) {
                List<String> hasCommand = new ArrayList<>();
                for (String pluginCommand : subCommands)
                    if (sender.hasPermission("npc.command." + pluginCommand)) hasCommand.add(pluginCommand);
                return StringUtil.copyPartialMatches(args[0], hasCommand, new ArrayList<>());
            } else if (args[0].equals("type")) {
                if (args.length == 2) return StringUtil.copyPartialMatches(args[1], plugin.getData().getVillagerTypes(), new ArrayList<>());
            } else if (args[0].equals("profession")) {
                if (args.length == 2) return StringUtil.copyPartialMatches(args[1], plugin.getData().getVillagerProfessions(), new ArrayList<>());
            } else if (args[0].equals("command")) {
                if (args.length == 2) return StringUtil.copyPartialMatches(args[1], Arrays.asList("CONSOLE", "PLAYER", "NETWORK"), new ArrayList<>());
            } else if (args[0].equals("tp")) {
                if (args.length == 2) return StringUtil.copyPartialMatches(args[1], cacheManager.getAllIDString(), new ArrayList<>());
            }
        }
        return blank;
    }
}
package lee.code.npc.commands;

import lee.code.npc.GoldmanNPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class TabCompletion implements TabCompleter {

    private final List<String> subCommands = Arrays.asList("create", "select", "type", "profession", "name");
    private final List<String> blank = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();

        if (sender instanceof Player) {
            if (args.length == 1) {
                List<String> hasCommand = new ArrayList<>();
                for (String pluginCommand : subCommands)
                    if (sender.hasPermission("npc.command." + pluginCommand)) hasCommand.add(pluginCommand);
                return StringUtil.copyPartialMatches(args[0], hasCommand, new ArrayList<>());
            } else if (args[0].equals("type")) {
                if (args.length == 2) return StringUtil.copyPartialMatches(args[1], plugin.getPU().getVillagerTypes(), new ArrayList<>());
            } else if (args[0].equals("profession")) {
                if (args.length == 2) return StringUtil.copyPartialMatches(args[1], plugin.getPU().getVillagerProfessions(), new ArrayList<>());
            }
        }
        return blank;
    }
}
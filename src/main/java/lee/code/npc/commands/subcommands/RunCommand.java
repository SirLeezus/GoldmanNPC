package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.Cache;
import lee.code.npc.lists.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RunCommand extends SubCommand {

    @Override
    public String getName() {
        return "command";
    }

    @Override
    public String getDescription() {
        return "Set the selected NPC command.";
    }

    @Override
    public String getSyntax() {
        return "/npc command &f<type> <command>";
    }

    @Override
    public String getPermission() {
        return "npc.command.command";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        Cache cache = plugin.getCache();
        if (args.length > 1) {
            if (args.length > 2) {
                UUID uuid = player.getUniqueId();
                if (cache.hasNPCSelected(uuid)) {
                    String commandType = args[1];
                    String command = plugin.getPU().buildStringFromArgs(args, 2);
                    String npcName = cache.getNPCSelected(uuid);
                    cache.setNPCCommand(npcName, command, commandType);
                    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_COMMAND_ADDED_SUCCESSFUL.getComponent(new String[] { command, npcName })));
                } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_NPC.getComponent(null)));
            } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_COMMAND_ARG.getComponent(null)));
        }
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

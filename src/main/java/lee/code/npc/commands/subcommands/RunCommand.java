package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.SQLite;
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
        return "/npc command <type> <command>";
    }

    @Override
    public String getPermission() {
        return "npc.command.command";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        SQLite SQL = plugin.getSqLite();
        UUID uuid = player.getUniqueId();

        if (args.length > 2) {
            if (plugin.getData().hasSelectedNPC(uuid)) {
                String commandType = args[1];
                String command = args[2];
                String npc = plugin.getData().getSelectedNPC(uuid);
                SQL.setCommand(npc, command);
                SQL.setCommandType(npc, commandType);
                player.sendMessage(Lang.PREFIX.getString(null) + Lang.COMMAND_COMMAND_ADDED_SUCCESSFUL.getString(new String[] { command, npc }));
            } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NO_SELECTED_NPC.getString(null));
        } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_COMMAND_COMMAND_ARG.getString(null));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {

    }
}

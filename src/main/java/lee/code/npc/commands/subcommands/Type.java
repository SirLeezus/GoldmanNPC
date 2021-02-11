package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.SQLite;
import lee.code.npc.lists.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Type extends SubCommand {

    @Override
    public String getName() {
        return "type";
    }

    @Override
    public String getDescription() {
        return "Set the selected NPC type.";
    }

    @Override
    public String getSyntax() {
        return "/npc type <type>";
    }

    @Override
    public String getPermission() {
        return "npc.command.type";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        SQLite SQL = plugin.getSqLite();
        UUID uuid = player.getUniqueId();

        if (args.length > 1) {
            if (plugin.getData().hasSelectedNPC(uuid)) {
                String type = args[1];
                if (plugin.getPU().getVillagerTypes().contains(type)) {
                    String npc = plugin.getData().getSelectedNPC(uuid);
                    SQL.setType(npc, args[1]);
                    plugin.getPU().removeNPC(npc);
                    SQL.loadNPC(npc);
                } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_COMMAND_TYPE_NOT_FOUND.getString(new String[] { type }));
            } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NO_SELECTED_NPC.getString(null));
        } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_COMMAND_TYPE_ARG.getString(null));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {

    }
}

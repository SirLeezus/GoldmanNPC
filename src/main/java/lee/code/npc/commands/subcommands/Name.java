package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.SQLite;
import lee.code.npc.lists.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Name extends SubCommand {

    @Override
    public String getName() {
        return "name";
    }

    @Override
    public String getDescription() {
        return "Set the selected NPC name.";
    }

    @Override
    public String getSyntax() {
        return "/npc name <name>";
    }

    @Override
    public String getPermission() {
        return "npc.command.name";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        SQLite SQL = plugin.getSqLite();
        UUID uuid = player.getUniqueId();

        if (args.length > 1) {
            if (plugin.getData().hasSelectedNPC(uuid)) {
                String npc = plugin.getData().getSelectedNPC(uuid);
                String name = plugin.getPU().buildStringFromArgs(args, 1);

                if (!SQL.isNameTaken(name)) {
                    plugin.getPU().removeNPC(npc);
                    SQL.setName(npc, name);
                    SQL.loadNPC(name);
                    plugin.getData().setSelectedNPC(uuid, name);
                    plugin.getData().removeActiveNPC(npc);
                    plugin.getData().addActiveNPC(name);
                } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NAME_TAKEN.getString(new String[] { name }));
            } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NO_SELECTED_NPC.getString(null));
        } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_COMMAND_NAME_ARG.getString(null));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {

    }
}

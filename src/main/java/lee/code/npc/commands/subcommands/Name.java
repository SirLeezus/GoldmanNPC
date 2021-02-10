package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.SQLite;
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
                plugin.getPU().removeNPC(npc);

                SQL.setName(npc, name);
                SQL.loadNPC(name);
                plugin.getData().setSelectedNPC(uuid, name);
            } else player.sendMessage("You need to select a NPC.");
        }
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {

    }
}

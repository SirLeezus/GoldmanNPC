package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.SQLite;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profession extends SubCommand {

    @Override
    public String getName() {
        return "profession";
    }

    @Override
    public String getDescription() {
        return "Set the selected NPC profession.";
    }

    @Override
    public String getSyntax() {
        return "/npc profession <profession>";
    }

    @Override
    public String getPermission() {
        return "npc.command.profession";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        SQLite SQL = plugin.getSqLite();
        UUID uuid = player.getUniqueId();

        if (args.length > 1) {

            if (plugin.getData().hasSelectedNPC(uuid)) {
                if (plugin.getPU().getVillagerProfessions().contains(args[1])) {
                    String npc = plugin.getData().getSelectedNPC(uuid);
                    SQL.setProfession(npc, args[1]);
                    plugin.getPU().removeNPC(npc);
                    SQL.loadNPC(npc);
                }
            } else player.sendMessage("You need to select a NPC.");
        }
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {

    }
}

package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.Cache;
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
        Cache cache = plugin.getCache();
        UUID uuid = player.getUniqueId();

        if (args.length > 1) {
            if (cache.hasNPCSelected(uuid)) {
                String type = args[1];
                if (plugin.getPU().getVillagerTypes().contains(type)) {
                    String npcName = cache.getNPCSelected(uuid);
                    cache.setNPCType(npcName, type);
                    //SQL.setType(npcName, type);
                    //plugin.getPU().removeNPC(npcName);
                    //SQL.loadNPC(npcName);
                } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_COMMAND_TYPE_NOT_FOUND.getString(new String[] { type }));
            } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NO_SELECTED_NPC.getString(null));
        } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_COMMAND_TYPE_ARG.getString(null));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {

    }
}

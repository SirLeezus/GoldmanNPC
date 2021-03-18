package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.Cache;
import lee.code.npc.lists.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Remove extends SubCommand {

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove the NPC you have selected.";
    }

    @Override
    public String getSyntax() {
        return "/npc remove";
    }

    @Override
    public String getPermission() {
        return "npc.command.remove";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        Cache cache = plugin.getCache();
        UUID uuid = player.getUniqueId();

        if (cache.hasNPCSelected(uuid)) {
            String npcName = cache.getNPCSelected(uuid);
            cache.removeNPC(npcName);
            cache.removeNPCSelected(uuid);
        } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NO_SELECTED_NPC.getString(null));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NOT_A_CONSOLE_COMMAND.getString(null));
    }
}

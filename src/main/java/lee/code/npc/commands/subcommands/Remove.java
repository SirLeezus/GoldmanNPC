package lee.code.npc.commands.subcommands;

import lee.code.npc.Data;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.CacheManager;
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
        CacheManager cacheManager = plugin.getCacheManager();
        Data data = plugin.getData();
        UUID uuid = player.getUniqueId();

        if (data.hasSelectedNPC(uuid)) {
            int id = data.getSelectedNPC(uuid);
            cacheManager.deleteNPC(id);
            data.removeSelectedNPC(uuid);
        } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_NPC.getComponent(null)));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

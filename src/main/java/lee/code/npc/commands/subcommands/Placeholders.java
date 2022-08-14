package lee.code.npc.commands.subcommands;

import lee.code.npc.Data;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.CacheManager;
import lee.code.npc.lists.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Placeholders extends SubCommand {

    @Override
    public String getName() {
        return "placeholders";
    }

    @Override
    public String getDescription() {
        return "Toggle if npc has placeholders so it can be updated.";
    }

    @Override
    public String getSyntax() {
        return "/npc placeholders";
    }

    @Override
    public String getPermission() {
        return "npc.command.placeholders";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();
        Data data = plugin.getData();
        UUID uuid = player.getUniqueId();

        if (data.hasSelectedNPC(uuid)) {
            int id = data.getSelectedNPC(uuid);
            if (cacheManager.hasPlaceholders(id)) {
                cacheManager.setPlaceholders(id, false);
                player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_COMMAND_PLACEHOLDERS_SUCCESSFUL.getComponent(new String[] { Lang.OFF.getString(), cacheManager.getNPCDisplayName(id) })));
            } else {
                cacheManager.setPlaceholders(id, true);
                player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_COMMAND_PLACEHOLDERS_SUCCESSFUL.getComponent(new String[] { Lang.ON.getString(), cacheManager.getNPCDisplayName(id) })));
            }
        } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_NPC.getComponent(null)));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

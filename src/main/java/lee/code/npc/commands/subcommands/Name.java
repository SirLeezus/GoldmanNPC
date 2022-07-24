package lee.code.npc.commands.subcommands;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.npc.Data;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.CacheManager;
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
        return "/npc name &f<name>";
    }

    @Override
    public String getPermission() {
        return "npc.command.name";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();
        Data data = plugin.getData();

        if (args.length > 1) {
            UUID uuid = player.getUniqueId();
            if (data.hasSelectedNPC(uuid)) {
                int id = data.getSelectedNPC(uuid);
                String newName = BukkitUtils.buildStringFromArgs(args, 1);
                cacheManager.setNPCDisplayName(id, newName);
            } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_NPC.getComponent(null)));
        } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_NAME_ARG.getComponent(null)));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

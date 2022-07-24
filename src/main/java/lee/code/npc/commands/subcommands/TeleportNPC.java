package lee.code.npc.commands.subcommands;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.CacheManager;
import lee.code.npc.lists.Lang;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportNPC extends SubCommand {

    @Override
    public String getName() {
        return "tp";
    }

    @Override
    public String getDescription() {
        return "Teleport to a NPC that is created.";
    }

    @Override
    public String getSyntax() {
        return "/npc tp &f<id>";
    }

    @Override
    public String getPermission() {
        return "npc.command.teleport";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();

        if (args.length > 1) {
            if (BukkitUtils.containOnlyNumbers(args[1])) {
                int id = Integer.parseInt(args[1]);
                if (cacheManager.isNPC(id)) {
                    Location location = cacheManager.getNPCLocation(id);
                    player.teleportAsync(location);
                }
            }
        }
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

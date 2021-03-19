package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.Cache;
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
        return "/npc tp &f<name>";
    }

    @Override
    public String getPermission() {
        return "npc.command.teleport";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        Cache cache = plugin.getCache();

        if (args.length > 1) {
            String targetName = plugin.getPU().buildStringFromArgs(args, 1);
            if (cache.isNPC(targetName)) {
                Location location = cache.getNPCLocation(targetName);
                player.teleportAsync(location);
            }
        }
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NOT_A_CONSOLE_COMMAND.getString(null));
    }
}

package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.Cache;
import lee.code.npc.lists.Lang;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Create extends SubCommand {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Create a new NPC.";
    }

    @Override
    public String getSyntax() {
        return "/npc create <name>";
    }

    @Override
    public String getPermission() {
        return "npc.command.create";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        Cache cache = plugin.getCache();

        if (args.length > 1) {
            UUID uuid = player.getUniqueId();
            String name = plugin.getPU().buildStringFromArgs(args, 1);
            Location location = player.getLocation();

            if (!cache.isNPC(name)) {
                cache.createNPC(uuid, name, location, "NONE", "PLAINS", "n", "n");
                player.sendMessage(Lang.PREFIX.getString(null) + Lang.COMMAND_CREATE_NPC_SUCCESSFUL.getString(new String[] { name }));
            } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NAME_TAKEN.getString(new String[] { name }));
        } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_COMMAND_CREATE_ARG.getString(null));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NOT_A_CONSOLE_COMMAND.getString(null));
    }
}

package lee.code.npc.commands.subcommands;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.CacheManager;
import lee.code.npc.database.tables.NPCTable;
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
        return "/npc create &f<name>";
    }

    @Override
    public String getPermission() {
        return "npc.command.create";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();

        if (args.length > 1) {
            UUID uuid = player.getUniqueId();
            String displayName = BukkitUtils.buildStringFromArgs(args, 1);
            int nextId = cacheManager.getNextID();
            Location location = player.getLocation();

            cacheManager.createNPCData(new NPCTable(nextId, displayName, BukkitUtils.serializeLocation(location)));
            plugin.getData().setSelectedNPC(uuid, nextId);
            player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_CREATE_NPC_SUCCESSFUL.getComponent(new String[] { displayName })));

        } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_CREATE_ARG.getComponent(null)));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

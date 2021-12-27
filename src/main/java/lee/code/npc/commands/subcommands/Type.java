package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.PU;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.Cache;
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
        return "/npc type &f<type>";
    }

    @Override
    public String getPermission() {
        return "npc.command.type";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        PU pu = plugin.getPU();
        Cache cache = plugin.getCache();
        UUID uuid = player.getUniqueId();

        if (args.length > 1) {
            if (cache.hasNPCSelected(uuid)) {
                String type = args[1];
                if (pu.getVillagerTypes().contains(type)) {
                    String npcName = cache.getNPCSelected(uuid);
                    cache.setNPCType(npcName, type);
                } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_TYPE_NOT_FOUND.getComponent(new String[] { type })));
            } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_NPC.getComponent(null)));
        } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_TYPE_ARG.getComponent(null)));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

package lee.code.npc.commands.subcommands;

import lee.code.npc.Data;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.CacheManager;
import lee.code.npc.lists.Lang;
import lee.code.npc.lists.NPCType;
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
        CacheManager cacheManager = plugin.getCacheManager();
        Data data = plugin.getData();
        UUID uuid = player.getUniqueId();

        if (args.length > 1) {
            if (data.hasSelectedNPC(uuid)) {
                String type = args[1];
                if (data.getVillagerTypes().contains(type)) {
                    int id = data.getSelectedNPC(uuid);
                    cacheManager.setNPCType(id, NPCType.valueOf(type));
                } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_TYPE_NOT_FOUND.getComponent(new String[] { type })));
            } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_NPC.getComponent(null)));
        } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_TYPE_ARG.getComponent(null)));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

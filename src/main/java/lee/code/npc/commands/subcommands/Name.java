package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.PU;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.Cache;
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
        PU pu = plugin.getPU();
        Cache cache = plugin.getCache();

        if (args.length > 1) {
            UUID uuid = player.getUniqueId();
            if (cache.hasNPCSelected(uuid)) {
                String oldName = cache.getNPCSelected(uuid);
                String newName = pu.buildStringFromArgs(args, 1);

                if (!cache.isNPC(newName)) {
                    cache.setNPCName(uuid, oldName, newName);
                } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NAME_TAKEN.getComponent(new String[] { newName })));
            } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_NPC.getComponent(null)));
        } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_NAME_ARG.getComponent(null)));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

package lee.code.npc.commands.subcommands;

import lee.code.npc.Data;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.CacheManager;
import lee.code.npc.lists.Lang;
import lee.code.npc.lists.NPCProfession;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profession extends SubCommand {

    @Override
    public String getName() {
        return "profession";
    }

    @Override
    public String getDescription() {
        return "Set the selected NPC profession.";
    }

    @Override
    public String getSyntax() {
        return "/npc profession &f<profession>";
    }

    @Override
    public String getPermission() {
        return "npc.command.profession";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();
        Data data = plugin.getData();

        if (args.length > 1) {
            UUID uuid = player.getUniqueId();
            if (data.hasSelectedNPC(uuid)) {
                String profession = args[1];
                if (data.getVillagerProfessions().contains(profession)) {
                    int id = data.getSelectedNPC(uuid);
                    cacheManager.setNPCProfession(id, NPCProfession.valueOf(profession));
                } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_PROFESSION_NOT_FOUND.getComponent(new String[] { profession })));
            } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_NPC.getComponent(null)));
        } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_PROFESSION_ARG.getComponent(null)));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

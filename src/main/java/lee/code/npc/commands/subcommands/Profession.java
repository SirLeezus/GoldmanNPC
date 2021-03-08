package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.Cache;
import lee.code.npc.lists.Lang;
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
        return "/npc profession <profession>";
    }

    @Override
    public String getPermission() {
        return "npc.command.profession";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        Cache cache = plugin.getCache();

        if (args.length > 1) {
            UUID uuid = player.getUniqueId();
            if (cache.hasNPCSelected(uuid)) {
                String profession = args[1];
                if (plugin.getPU().getVillagerProfessions().contains(profession)) {
                    String npcName = cache.getNPCSelected(uuid);
                    cache.setNPCProfession(npcName, profession);
                } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_COMMAND_PROFESSION_NOT_FOUND.getString(new String[] { profession }));
            } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NO_SELECTED_NPC.getString(null));
        } else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_COMMAND_PROFESSION_ARG.getString(null));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NOT_A_CONSOLE_COMMAND.getString(null));
    }
}

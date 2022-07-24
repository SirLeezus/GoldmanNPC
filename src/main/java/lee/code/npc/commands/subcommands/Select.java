package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.lists.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Select extends SubCommand {

    @Override
    public String getName() {
        return "select";
    }

    @Override
    public String getDescription() {
        return "Select the NPC you're looking at.";
    }

    @Override
    public String getSyntax() {
        return "/npc select";
    }

    @Override
    public String getPermission() {
        return "npc.command.select";
    }

    @Override
    public void perform(Player player, String[] args) {
        int id = GoldmanNPC.getPlugin().getPU().selectNPC(player);
        if (id != 0) player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_SELECT_NPC_SUCCESSFUL.getComponent(new String[] { GoldmanNPC.getPlugin().getCacheManager().getNPCDisplayName(id) })));
        else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_TARGET_NPC.getComponent(null)));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

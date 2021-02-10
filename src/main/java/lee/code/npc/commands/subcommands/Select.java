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
        GoldmanNPC plugin = GoldmanNPC.getPlugin();

        String name = plugin.getPU().selectNPC(player);

        if (!name.equals("n")) player.sendMessage(Lang.PREFIX.getString(null) + Lang.MESSAGE_COMMAND_SELECTED_NPC.getString(new String[] { name }) );
        else player.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_TARGET_NPC.getString(null));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {

    }
}

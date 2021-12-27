package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.PU;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.Cache;
import lee.code.npc.lists.Lang;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListNPCs extends SubCommand {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Lists all active NPCs.";
    }

    @Override
    public String getSyntax() {
        return "/npc list";
    }

    @Override
    public String getPermission() {
        return "npc.command.list";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        PU pu = plugin.getPU();
        Cache cache = plugin.getCache();

        int index;
        int maxDisplayed = 10;
        int page = 0;

        if (args.length > 1) {
            if (pu.containOnlyNumbers(args[1])) {
                page = Integer.parseInt(args[1]);
                if (page < 0) return;
            }
        }

        List<String> npcNames = cache.getNPCNames();
        List<Component> lines = new ArrayList<>();

        lines.add(Lang.MESSAGE_COMMAND_LIST_TITLE.getComponent(null));
        lines.add(Component.text(""));

        if (npcNames != null && !npcNames.isEmpty()) {
            for (int i = 0; i < maxDisplayed; i++) {
                index = maxDisplayed * page + i;
                if (index >= npcNames.size()) break;
                if (npcNames.get(index) != null) {
                    int npcNumber = index + 1;
                    String npcName = npcNames.get(index);
                    lines.add(Lang.MESSAGE_COMMAND_LIST_NPC.getComponent(new String[]{ String.valueOf(npcNumber), npcName }));
                }
            }
        }

        if (lines.size() <= 2) return;

        Component next = Lang.NEXT_PAGE_TEXT.getComponent(null).hoverEvent(Lang.NEXT_PAGE_HOVER.getComponent(null)).clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.RUN_COMMAND, "/npc list " + (page + 1)));
        Component split = Lang.PAGE_SPACER.getComponent(null);
        Component prev = Lang.PREVIOUS_PAGE_TEXT.getComponent(null).hoverEvent(Lang.PREVIOUS_PAGE_HOVER.getComponent(null)).clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.RUN_COMMAND, "/npc list " + (page - 1)));

        lines.add(prev.append(split).append(next));
        for (Component line : lines) player.sendMessage(line);
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

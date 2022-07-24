package lee.code.npc.commands.subcommands;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.CacheManager;
import lee.code.npc.lists.Lang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        CacheManager cacheManager = plugin.getCacheManager();

        int index;
        int maxDisplayed = 10;
        int page = 0;

        if (args.length > 1) {
            if (BukkitUtils.containOnlyNumbers(args[1])) {
                page = Integer.parseInt(args[1]);
                if (page < 0) return;
            }
        }

        Map<Integer, String> npcMap = cacheManager.getNPCNames();
        List<Integer> ids = new ArrayList<>(npcMap.keySet());
        List<Component> lines = new ArrayList<>();

        lines.add(Lang.MESSAGE_COMMAND_LIST_TITLE.getComponent(null));
        lines.add(Component.text(""));

        if (!ids.isEmpty()) {
            for (int i = 0; i < maxDisplayed; i++) {
                index = maxDisplayed * page + i;
                if (index >= ids.size()) break;
                if (ids.get(index) != null) {
                    int id = ids.get(index);
                    lines.add(Lang.MESSAGE_COMMAND_LIST_NPC.getComponent(new String[]{ String.valueOf(id), npcMap.get(id) }).hoverEvent(Lang.MESSAGE_COMMAND_LIST_NPC_HOVER.getComponent(new String[] { String.valueOf(id) })).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/npc tp " + id)));
                }
            }
        }

        if (lines.size() <= 2) return;
        lines.add(Component.text(""));
        Component next = Lang.NEXT_PAGE_TEXT.getComponent(null).hoverEvent(Lang.NEXT_PAGE_HOVER.getComponent(null)).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/npc list " + (page + 1)));
        Component split = Lang.PAGE_SPACER.getComponent(null);
        Component prev = Lang.PREVIOUS_PAGE_TEXT.getComponent(null).hoverEvent(Lang.PREVIOUS_PAGE_HOVER.getComponent(null)).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/npc list " + (page - 1)));

        lines.add(prev.append(split).append(next));
        for (Component line : lines) player.sendMessage(line);
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}

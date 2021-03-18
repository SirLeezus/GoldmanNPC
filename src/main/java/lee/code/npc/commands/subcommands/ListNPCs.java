package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.Cache;
import lee.code.npc.lists.Lang;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        Cache cache = plugin.getCache();

        int index;
        int maxDisplayed = 10;
        int page = 0;

        if (args.length > 1) {
            Scanner numberScanner = new Scanner(args[1]);
            if (numberScanner.hasNextInt()) {
                page = Integer.parseInt(args[1]);
                if (page < 0) return;
            }
        }

        List<String> npcNames = cache.getNPCNames();
        List<String> formattedList = new ArrayList<>();

        if (npcNames != null && !npcNames.isEmpty()) {
            for (int i = 0; i < maxDisplayed; i++) {
                index = maxDisplayed * page + i;
                if (index >= npcNames.size()) break;
                if (npcNames.get(index) != null) {
                    int npcNumber = index + 1;
                    String npcName = npcNames.get(index);
                    formattedList.add(Lang.MESSAGE_COMMAND_LIST_NPC.getString(new String[]{ String.valueOf(npcNumber), npcName }));
                }
            }
        }

        if (formattedList.size() != 0) {
            player.sendMessage(Lang.MESSAGE_COMMAND_LIST_TITLE.getString(null));

            for (String line : formattedList) player.sendMessage(line);

            TextComponent nextPage= new TextComponent(plugin.getPU().format("&e&lNext >>---"));
            nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/npc list " + (page + 1)));
            nextPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(plugin.getPU().format("&6&lNext Page >"))));

            TextComponent previousPage= new TextComponent(plugin.getPU().format("&e&l---<< Prev"));
            previousPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/npc list " + (page - 1)));
            previousPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(plugin.getPU().format("&6&l< Previous Page"))));

            TextComponent spacer = new TextComponent(plugin.getPU().format(" &7| "));

            player.spigot().sendMessage(previousPage, spacer, nextPage);
        }
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getString(null) + Lang.ERROR_NOT_A_CONSOLE_COMMAND.getString(null));
    }
}

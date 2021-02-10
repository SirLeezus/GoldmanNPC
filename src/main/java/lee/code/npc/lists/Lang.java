package lee.code.npc.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
public enum Lang {
    PREFIX("&b&lNPC &3➔ &r"),
    MESSAGE_HELP_DIVIDER("&e▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
    MESSAGE_HELP_TITLE("                      &6-== &e&l&nNPC Help&r &6==-"),
    MESSAGE_HELP_SUB_COMMAND("&3{0}&b. &e{1} &c| &7{2}"),
    ERROR_NO_PERMISSION("&cYou sadly do not have permission for this."),
    ERROR_TARGET_NPC("&cSadly no NPC was detected."),
    MESSAGE_COMMAND_SELECTED_NPC("&aYou successfully selected the NPC &e(&f{0}&e)&a!"),
    ;

    @Getter private final String string;

    public String getString(String[] variables) {
        String value = ChatColor.translateAlternateColorCodes('&', string);
        if (variables == null) return value;
        else if (variables.length == 0) return value;
        for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
        return ChatColor.translateAlternateColorCodes('&', value);
    }
}
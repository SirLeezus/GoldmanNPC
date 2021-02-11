package lee.code.npc.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
public enum Lang {
    PREFIX("&b&lNPC &3➔ &r"),
    MESSAGE_HELP_DIVIDER("&b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
    MESSAGE_HELP_TITLE("                      &6-== &3&l&nNPC Help&r &6==-"),
    MESSAGE_HELP_SUB_COMMAND("&3{0}&b. &e{1} &c| &7{2}"),
    ERROR_NO_PERMISSION("&cYou sadly do not have permission for this."),
    ERROR_NAME_TAKEN("&cThere is already a NPC called &e(&f{0}&e)&c."),
    ERROR_COMMAND_CREATE_ARG("&cYou need to input a name for the new NPC to run this command."),
    ERROR_COMMAND_NAME_ARG("&cYou need to input a name to rename your selected NPC."),
    ERROR_COMMAND_PROFESSION_ARG("&cYou need to input a supported profession to change your selected NPC's profession."),
    ERROR_COMMAND_TYPE_ARG("&cYou need to input a supported type to change your selected NPC's type."),
    ERROR_COMMAND_COMMAND_ARG("&cYou need to input the type of command and the command to change your selected NPC's command."),
    ERROR_COMMAND_PROFESSION_NOT_FOUND("&cYou input &6{0} &cis not a supported profession."),
    ERROR_COMMAND_TYPE_NOT_FOUND("&cYou input &6{0} &cis not a supported type."),
    ERROR_NO_SELECTED_NPC("&cYou need to select a NPC to run this command."),
    ERROR_TARGET_NPC("&cSadly no NPC was detected."),
    COMMAND_SELECT_NPC_SUCCESSFUL("&aYou successfully selected the NPC &e(&f{0}&e)&a!"),
    COMMAND_CREATE_NPC_SUCCESSFUL("&aYou successfully created the NPC &e(&f{0}&e)&a!"),
    COMMAND_COMMAND_ADDED_SUCCESSFUL("&aYou successfully added the command &6{0} &ato the NPC &e(&f{0}&e)&a!"),
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
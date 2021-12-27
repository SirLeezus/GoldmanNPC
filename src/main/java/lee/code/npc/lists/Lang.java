package lee.code.npc.lists;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.PU;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

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
    ERROR_NOT_A_CONSOLE_COMMAND("&cThis is not a console command."),
    MESSAGE_COMMAND_LIST_NPC("&b{0}&7. &6{1}"),
    MESSAGE_COMMAND_LIST_TITLE("&e&l----<< &9&lNPC List &e&l>>----"),
    ERROR_COMMAND_COMMAND_ARG("&cYou need to input the type of command and the command to change your selected NPC's command."),
    ERROR_COMMAND_PROFESSION_NOT_FOUND("&cYou input &6{0} &cis not a supported profession."),
    ERROR_COMMAND_TYPE_NOT_FOUND("&cYou input &6{0} &cis not a supported type."),
    ERROR_NO_SELECTED_NPC("&cYou need to select a NPC to run this command."),
    ERROR_TARGET_NPC("&cSadly no NPC was detected."),
    COMMAND_SELECT_NPC_SUCCESSFUL("&aYou successfully selected the NPC &e(&f{0}&e)&a!"),
    COMMAND_CREATE_NPC_SUCCESSFUL("&aYou successfully created the NPC &e(&f{0}&e)&a!"),
    COMMAND_COMMAND_ADDED_SUCCESSFUL("&aYou successfully set the command &6{0} &ato the NPC &e(&f{1}&e)&a!"),
    PAGE_SPACER(" &e| "),
    NEXT_PAGE_HOVER("&6&lNext Page"),
    PREVIOUS_PAGE_HOVER("&6&lPrevious Page"),
    NEXT_PAGE_TEXT("&2&lNext &a&l>>--------"),
    PREVIOUS_PAGE_TEXT("&a&l--------<< &2&lPrev"),
    ;

    @Getter private final String string;

    public String getString(String[] variables) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        PU pu = plugin.getPU();
        String value = string;
        if (variables == null || variables.length == 0) return pu.format(value);
        for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
        return pu.format(value);
    }

    public Component getComponent(String[] variables) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        PU pu = plugin.getPU();
        String value = string;
        if (variables == null || variables.length == 0) return pu.formatC(value);
        for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
        return pu.formatC(value);
    }
}
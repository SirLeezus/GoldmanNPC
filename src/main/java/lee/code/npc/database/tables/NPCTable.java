package lee.code.npc.database.tables;

import lee.code.core.ormlite.field.DatabaseField;
import lee.code.core.ormlite.table.DatabaseTable;
import lee.code.npc.lists.CommandType;
import lee.code.npc.lists.NPCProfession;
import lee.code.npc.lists.NPCType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "npc")
public class NPCTable {

    @DatabaseField(id = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "display_name", canBeNull = false)
    private String displayName;

    @DatabaseField(columnName = "location", canBeNull = false)
    private String location;

    @DatabaseField(columnName = "profession", canBeNull = false)
    private String profession;

    @DatabaseField(columnName = "type", canBeNull = false)
    private String type;

    @DatabaseField(columnName = "command", canBeNull = false)
    private String command;

    @DatabaseField(columnName = "command_type", canBeNull = false)
    private String commandType;

    @DatabaseField(columnName = "placeholders", canBeNull = false)
    private boolean placeholders;

    public NPCTable(int id, String displayName, String location) {
        this.id = id;
        this.displayName = displayName;
        this.location = location;
        this.profession = NPCProfession.NONE.name();
        this.type = NPCType.PLAINS.name();
        this.command = "0";
        this.commandType = CommandType.PLAYER.name();
        this.placeholders = false;
    }
}
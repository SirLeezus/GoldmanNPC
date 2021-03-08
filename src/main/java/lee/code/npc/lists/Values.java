package lee.code.npc.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Values {
    CLICK_DELAY(5),
    ;

    @Getter private final int value;
}
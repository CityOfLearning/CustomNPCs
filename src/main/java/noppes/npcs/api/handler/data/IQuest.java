
package noppes.npcs.api.handler.data;

import noppes.npcs.api.constants.EnumQuestType;

public interface IQuest {
	int getId();

	String getName();

	EnumQuestType getType();
}

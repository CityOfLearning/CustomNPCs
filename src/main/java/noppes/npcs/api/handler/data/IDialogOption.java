
package noppes.npcs.api.handler.data;

import noppes.npcs.api.constants.EnumOptionType;

public interface IDialogOption {
	String getName();

	int getSlot();

	EnumOptionType getType();
}

package noppes.npcs.api.handler.data;

import noppes.npcs.api.constants.EnumOptionType;

public interface IDialogOption {

   int getSlot();

   String getName();

   EnumOptionType getType();
}

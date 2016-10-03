package noppes.npcs.api.handler.data;

import java.util.List;
import noppes.npcs.api.handler.data.IQuest;

public interface IDialog {

   int getId();

   String getName();

   IQuest getQuest();

   List getOptions();
}

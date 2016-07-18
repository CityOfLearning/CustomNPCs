//

//

package noppes.npcs.api.handler.data;

import java.util.List;

public interface IDialog {
	int getId();

	String getName();

	List<IDialogOption> getOptions();

	IQuest getQuest();
}

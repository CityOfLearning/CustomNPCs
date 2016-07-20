//

//

package noppes.npcs.controllers.script;

import java.util.List;

import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.constants.EnumScriptType;

public interface IScriptHandler {
	boolean getEnabled();

	String getLanguage();

	List<ScriptContainer> getScripts();

	boolean isClient();

	String noticeString();

	void runScript(EnumScriptType p0, Event p1);

	void setEnabled(boolean p0);

	void setLanguage(String p0);
}

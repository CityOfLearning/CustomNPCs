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

	void runScript(final EnumScriptType p0, final Event p1);

	void setEnabled(final boolean p0);

	void setLanguage(final String p0);
}

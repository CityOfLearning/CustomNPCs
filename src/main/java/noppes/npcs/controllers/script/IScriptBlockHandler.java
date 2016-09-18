
package noppes.npcs.controllers.script;

import noppes.npcs.api.block.IBlock;

public interface IScriptBlockHandler extends IScriptHandler {
	IBlock getBlock();
}

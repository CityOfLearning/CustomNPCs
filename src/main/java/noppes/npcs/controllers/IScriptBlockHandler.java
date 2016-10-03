package noppes.npcs.controllers;

import noppes.npcs.api.block.IBlock;
import noppes.npcs.controllers.IScriptHandler;

public interface IScriptBlockHandler extends IScriptHandler {

   IBlock getBlock();
}

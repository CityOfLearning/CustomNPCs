package noppes.npcs.client.gui.player;

import net.minecraft.util.BlockPos;
import noppes.npcs.blocks.tiles.TileTextArea;
import noppes.npcs.client.gui.SubGuiNpcTextArea;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.util.NoppesUtilPlayer;

public class GuiTextSign extends SubGuiNpcTextArea {
	public TileTextArea tile;

	public GuiTextSign(int x, int y, int z) {
		super("");
		tile = ((TileTextArea) player.worldObj.getTileEntity(new BlockPos(x, y, z)));
		text = tile.getText();
	}

	@Override
	public void close() {
		super.close();
		NoppesUtilPlayer.sendData(EnumPlayerPacket.SignSave, new Object[] { Integer.valueOf(tile.getPos().getX()),
				Integer.valueOf(tile.getPos().getY()), Integer.valueOf(tile.getPos().getZ()), text });
	}
}

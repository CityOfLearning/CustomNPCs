//

//

package noppes.npcs.client.gui.player;

import net.minecraft.util.BlockPos;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.blocks.tiles.TileBigSign;
import noppes.npcs.client.gui.SubGuiNpcTextArea;
import noppes.npcs.constants.EnumPlayerPacket;

public class GuiBigSign extends SubGuiNpcTextArea {
	public TileBigSign tile;

	public GuiBigSign(final int x, final int y, final int z) {
		super("");
		tile = (TileBigSign) player.worldObj.getTileEntity(new BlockPos(x, y, z));
		text = tile.getText();
	}

	@Override
	public void close() {
		super.close();
		NoppesUtilPlayer.sendData(EnumPlayerPacket.SignSave, tile.getPos().getX(), tile.getPos().getY(),
				tile.getPos().getZ(), text);
	}
}

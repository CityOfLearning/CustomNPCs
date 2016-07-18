//

//

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;

public class ContainerNPCBankSmall extends ContainerNPCBankInterface {
	public ContainerNPCBankSmall(final EntityPlayer player, final int slot, final int bankid) {
		super(player, slot, bankid);
	}

	@Override
	public int getRowNumber() {
		return 3;
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
}

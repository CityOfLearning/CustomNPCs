//

//

package noppes.npcs.roles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.bank.Bank;
import noppes.npcs.controllers.bank.BankController;
import noppes.npcs.controllers.bank.BankData;
import noppes.npcs.entity.EntityNPCInterface;

public class RoleBank extends RoleInterface {
	public int bankId;

	public RoleBank(EntityNPCInterface npc) {
		super(npc);
		bankId = -1;
	}

	public Bank getBank() {
		Bank bank = BankController.getInstance().banks.get(bankId);
		if (bank != null) {
			return bank;
		}
		return BankController.getInstance().banks.values().iterator().next();
	}

	@Override
	public void interact(EntityPlayer player) {
		BankData data = PlayerDataController.instance.getBankData(player, bankId).getBankOrDefault(bankId);
		data.openBankGui(player, npc, bankId, 0);
		npc.say(player, npc.advanced.getInteractLine());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		bankId = nbttagcompound.getInteger("RoleBankID");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("RoleBankID", bankId);
		return nbttagcompound;
	}
}

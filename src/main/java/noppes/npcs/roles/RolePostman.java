//

//

package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.entity.EntityNPCInterface;

public class RolePostman extends RoleInterface {
	public NpcMiscInventory inventory;
	private List<EntityPlayer> recentlyChecked;
	private List<EntityPlayer> toCheck;

	public RolePostman(EntityNPCInterface npc) {
		super(npc);
		inventory = new NpcMiscInventory(1);
		recentlyChecked = new ArrayList<EntityPlayer>();
	}

	@Override
	public boolean aiContinueExecute() {
		return false;
	}

	@Override
	public boolean aiShouldExecute() {
		if ((npc.ticksExisted % 20) != 0) {
			return false;
		}
		(toCheck = npc.worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
				npc.getEntityBoundingBox().expand(10.0, 10.0, 10.0))).removeAll(recentlyChecked);
		List<EntityPlayer> listMax = npc.worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
				npc.getEntityBoundingBox().expand(20.0, 20.0, 20.0));
		recentlyChecked.retainAll(listMax);
		recentlyChecked.addAll(toCheck);
		for (EntityPlayer player : toCheck) {
			if (PlayerDataController.instance.hasMail(player)) {
				player.addChatMessage(new ChatComponentTranslation("You've got mail", new Object[0]));
			}
		}
		return false;
	}

	@Override
	public void interact(EntityPlayer player) {
		player.openGui(CustomNpcs.instance, EnumGuiType.PlayerMailman.ordinal(), player.worldObj, 1, 1, 0);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		inventory.setFromNBT(nbttagcompound.getCompoundTag("PostInv"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setTag("PostInv", inventory.getToNBT());
		return nbttagcompound;
	}
}

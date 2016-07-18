//

//

package noppes.npcs.api.wrapper;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import noppes.npcs.api.entity.IEntity;

public class WrapperEntityData implements IExtendedEntityProperties {
	public IEntity base;

	public WrapperEntityData(final IEntity base) {
		this.base = base;
	}

	@Override
	public void init(final Entity entity, final World world) {
	}

	@Override
	public void loadNBTData(final NBTTagCompound compound) {
	}

	@Override
	public void saveNBTData(final NBTTagCompound compound) {
	}
}

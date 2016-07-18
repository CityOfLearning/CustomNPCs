//

//

package noppes.npcs;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class CustomTeleporter extends Teleporter {
	public CustomTeleporter(final WorldServer par1WorldServer) {
		super(par1WorldServer);
	}

	@Override
	public void placeInPortal(final Entity entityIn, final float rotationYaw) {
	}
}

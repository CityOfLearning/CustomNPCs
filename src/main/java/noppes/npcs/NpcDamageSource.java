//

//

package noppes.npcs;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class NpcDamageSource extends EntityDamageSource {
	public NpcDamageSource(final String par1Str, final Entity par2Entity) {
		super(par1Str, par2Entity);
	}

	@Override
	public boolean isDifficultyScaled() {
		return false;
	}
}

//

//

package noppes.npcs.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import noppes.npcs.entity.EntityNPCInterface;

public class AniNo {
	public static void setRotationAngles(final float par1, final float par2, final float par3, final float par4,
			final float par5, final float par6, final Entity entity, final ModelBiped model) {
		float ticks = (entity.ticksExisted - ((EntityNPCInterface) entity).animationStart) / 8.0f;
		ticks %= 2.0f;
		float ani = ticks - 0.5f;
		if (ticks > 1.0f) {
			ani = 1.5f - ticks;
		}
		model.bipedHead.rotateAngleY = ani;
	}
}

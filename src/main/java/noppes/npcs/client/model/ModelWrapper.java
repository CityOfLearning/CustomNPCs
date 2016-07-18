//

//

package noppes.npcs.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelWrapper extends ModelBase {
	public ModelBase wrapped;
	public ResourceLocation texture;

	@Override
	public void render(final Entity p_78088_1_, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_,
			final float p_78088_5_, final float p_78088_6_, final float p_78088_7_) {
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(texture);
		wrapped.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
	}
}

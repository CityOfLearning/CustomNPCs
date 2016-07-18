//

//

package noppes.npcs.client.model.part.tails;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import noppes.npcs.client.model.ModelPlaneRenderer;

public class ModelDragonTail extends ModelRenderer {
	public ModelDragonTail(final ModelBiped base) {
		super(base);
		final int x = 52;
		final int y = 16;
		final ModelRenderer dragon = new ModelRenderer(base, x, y);
		dragon.setRotationPoint(0.0f, 0.0f, 3.0f);
		addChild(dragon);
		final ModelRenderer DragonTail2 = new ModelRenderer(base, x, y);
		DragonTail2.setRotationPoint(0.0f, 2.0f, 2.0f);
		final ModelRenderer DragonTail3 = new ModelRenderer(base, x, y);
		DragonTail3.setRotationPoint(0.0f, 4.5f, 4.0f);
		final ModelRenderer DragonTail4 = new ModelRenderer(base, x, y);
		DragonTail4.setRotationPoint(0.0f, 7.0f, 5.75f);
		final ModelRenderer DragonTail5 = new ModelRenderer(base, x, y);
		DragonTail5.setRotationPoint(0.0f, 9.0f, 8.0f);
		final ModelPlaneRenderer planeLeft = new ModelPlaneRenderer(base, x, y);
		planeLeft.addSidePlane(-1.5f, -1.5f, -1.5f, 3, 3);
		final ModelPlaneRenderer planeRight = new ModelPlaneRenderer(base, x, y);
		planeRight.addSidePlane(-1.5f, -1.5f, -1.5f, 3, 3);
		setRotation(planeRight, 3.1415927f, 3.1415927f, 0.0f);
		final ModelPlaneRenderer planeTop = new ModelPlaneRenderer(base, x, y);
		planeTop.addTopPlane(-1.5f, -1.5f, -1.5f, 3, 3);
		setRotation(planeTop, 0.0f, -1.5707964f, 0.0f);
		final ModelPlaneRenderer planeBottom = new ModelPlaneRenderer(base, x, y);
		planeBottom.addTopPlane(-1.5f, -1.5f, -1.5f, 3, 3);
		setRotation(planeBottom, 0.0f, -1.5707964f, 3.1415927f);
		final ModelPlaneRenderer planeBack = new ModelPlaneRenderer(base, x, y);
		planeBack.addBackPlane(-1.5f, -1.5f, -1.5f, 3, 3);
		setRotation(planeBack, 0.0f, 0.0f, 1.5707964f);
		final ModelPlaneRenderer planeFront = new ModelPlaneRenderer(base, x, y);
		planeFront.addBackPlane(-1.5f, -1.5f, -1.5f, 3, 3);
		setRotation(planeFront, 0.0f, 3.1415927f, -1.5707964f);
		dragon.addChild(planeLeft);
		dragon.addChild(planeRight);
		dragon.addChild(planeTop);
		dragon.addChild(planeBottom);
		dragon.addChild(planeFront);
		dragon.addChild(planeBack);
		DragonTail2.addChild(planeLeft);
		DragonTail2.addChild(planeRight);
		DragonTail2.addChild(planeTop);
		DragonTail2.addChild(planeBottom);
		DragonTail2.addChild(planeFront);
		DragonTail2.addChild(planeBack);
		DragonTail3.addChild(planeLeft);
		DragonTail3.addChild(planeRight);
		DragonTail3.addChild(planeTop);
		DragonTail3.addChild(planeBottom);
		DragonTail3.addChild(planeFront);
		DragonTail3.addChild(planeBack);
		DragonTail4.addChild(planeLeft);
		DragonTail4.addChild(planeRight);
		DragonTail4.addChild(planeTop);
		DragonTail4.addChild(planeBottom);
		DragonTail4.addChild(planeFront);
		DragonTail4.addChild(planeBack);
		dragon.addChild(DragonTail2);
		dragon.addChild(DragonTail3);
		dragon.addChild(DragonTail4);
	}

	private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(final float par1, final float par2, final float par3, final float par4,
			final float par5, final float par6, final Entity entity) {
	}
}

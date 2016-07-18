//

//

package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import noppes.npcs.client.renderer.EnumPlanePosition;

public class ModelPlaneRenderer extends ModelRenderer {
	private int textureOffsetX;
	private int textureOffsetY;

	public ModelPlaneRenderer(final ModelBase modelbase, final int i, final int j) {
		super(modelbase, i, j);
		textureOffsetX = i;
		textureOffsetY = j;
	}

	public void addBackPlane(final float f, final float f1, final float f2, final int i, final int j) {
		addPlane(f, f1, f2, i, j, 0, 0.0f, EnumPlanePosition.BACK);
	}

	public void addBackPlane(final float f, final float f1, final float f2, final int i, final int j,
			final float scale) {
		addPlane(f, f1, f2, i, j, 0, scale, EnumPlanePosition.BACK);
	}

	public void addPlane(final float par1, final float par2, final float par3, final int par4, final int par5,
			final int par6, final float f3, final EnumPlanePosition pos) {
		cubeList.add(new ModelPlane(this, textureOffsetX, textureOffsetY, par1, par2, par3, par4, par5, par6, f3, pos));
	}

	public void addSidePlane(final float f, final float f1, final float f2, final int j, final int k) {
		addPlane(f, f1, f2, 0, j, k, 0.0f, EnumPlanePosition.LEFT);
	}

	public void addSidePlane(final float f, final float f1, final float f2, final int j, final int k,
			final float scale) {
		addPlane(f, f1, f2, 0, j, k, scale, EnumPlanePosition.LEFT);
	}

	public void addTopPlane(final float f, final float f1, final float f2, final int i, final int k) {
		addPlane(f, f1, f2, i, 0, k, 0.0f, EnumPlanePosition.TOP);
	}

	public void addTopPlane(final float f, final float f1, final float f2, final int i, final int k,
			final float scale) {
		addPlane(f, f1, f2, i, 0, k, scale, EnumPlanePosition.TOP);
	}
}


package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.ModelData;

public class EntityNpcDragon extends EntityNPCInterface {
	public double[][] field_40162_d;
	public int field_40164_e;
	public float field_40173_aw;
	public float field_40172_ax;
	public int field_40178_aA;
	public boolean isFlying;
	private boolean exploded;

	public EntityNpcDragon(World world) {
		super(world);
		isFlying = false;
		exploded = false;
		field_40162_d = new double[64][3];
		field_40164_e = -1;
		field_40173_aw = 0.0f;
		field_40172_ax = 0.0f;
		field_40178_aA = 0;
		scaleX = 0.4f;
		scaleY = 0.4f;
		scaleZ = 0.4f;
		display.setSkinTexture("customnpcs:textures/entity/dragon/BlackDragon.png");
		width = 1.8f;
		height = 1.4f;
	}

	public double[] func_40160_a(int i, float f) {
		f = 1.0f - f;
		int j = (field_40164_e - (i * 1)) & 0x3F;
		int k = (field_40164_e - (i * 1) - 1) & 0x3F;
		double[] ad = new double[3];
		double d = field_40162_d[j][0];
		double d2;
		for (d2 = field_40162_d[k][0] - d; d2 < -180.0; d2 += 360.0) {
		}
		while (d2 >= 180.0) {
			d2 -= 360.0;
		}
		ad[0] = d + (d2 * f);
		d = field_40162_d[j][1];
		d2 = field_40162_d[k][1] - d;
		ad[1] = d + (d2 * f);
		ad[2] = field_40162_d[j][2] + ((field_40162_d[k][2] - field_40162_d[j][2]) * f);
		return ad;
	}

	@Override
	public double getMountedYOffset() {
		return 1.1;
	}

	@Override
	public void onLivingUpdate() {
		field_40173_aw = field_40172_ax;
		if (worldObj.isRemote && (getHealth() <= 0.0f)) {
			if (!exploded) {
				exploded = true;
				float f = (rand.nextFloat() - 0.5f) * 8.0f;
				float f2 = (rand.nextFloat() - 0.5f) * 4.0f;
				float f3 = (rand.nextFloat() - 0.5f) * 8.0f;
				worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX + f, posY + 2.0 + f2, posZ + f3, 0.0,
						0.0, 0.0, new int[0]);
			}
		} else {
			exploded = false;
			float f4 = 0.2f / ((MathHelper.sqrt_double((motionX * motionX) + (motionZ * motionZ)) * 10.0f) + 1.0f);
			f4 = 0.045f;
			f4 *= (float) Math.pow(2.0, motionY);
			field_40172_ax += f4 * 0.5f;
		}
		super.onLivingUpdate();
	}

	@Override
	public void onUpdate() {
		isDead = true;
		if (!worldObj.isRemote) {
			NBTTagCompound compound = new NBTTagCompound();
			writeToNBT(compound);
			EntityCustomNpc npc = new EntityCustomNpc(worldObj);
			npc.readFromNBT(compound);
			ModelData data = npc.modelData;
			data.setEntityClass(EntityNpcDragon.class);
			worldObj.spawnEntityInWorld(npc);
		}
		super.onUpdate();
	}

	@Override
	public void updateHitbox() {
		width = 1.8f;
		height = 1.4f;
	}
}

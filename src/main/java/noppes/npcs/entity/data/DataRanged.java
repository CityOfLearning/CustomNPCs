//

//

package noppes.npcs.entity.data;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.entity.data.INPCRanged;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class DataRanged implements INPCRanged {
	private EntityNPCInterface npc;
	private int burstCount;
	private int pDamage;
	private int pSpeed;
	private int pImpact;
	private int pSize;
	private int pArea;
	private int pTrail;
	private int minDelay;
	private int maxDelay;
	private int rangedRange;
	private int fireRate;
	private int shotCount;
	private int accuracy;
	private int meleeDistance;
	private int canFireIndirect;
	private boolean pRender3D;
	private boolean pSpin;
	private boolean pStick;
	private boolean pPhysics;
	private boolean pXlr8;
	private boolean pGlows;
	private boolean aimWhileShooting;
	private int pEffect;
	private int pDur;
	private int pEffAmp;
	private String fireSound;
	private String hitSound;
	private String groundSound;

	public DataRanged(EntityNPCInterface npc) {
		burstCount = 1;
		pDamage = 4;
		pSpeed = 10;
		pImpact = 0;
		pSize = 5;
		pArea = 0;
		pTrail = 0;
		minDelay = 20;
		maxDelay = 40;
		rangedRange = 15;
		fireRate = 5;
		shotCount = 1;
		accuracy = 60;
		meleeDistance = 0;
		canFireIndirect = 0;
		pRender3D = false;
		pSpin = false;
		pStick = false;
		pPhysics = true;
		pXlr8 = false;
		pGlows = false;
		aimWhileShooting = false;
		pEffect = 0;
		pDur = 5;
		pEffAmp = 0;
		fireSound = "random.bow";
		hitSound = "random.bowhit";
		groundSound = "random.break";
		this.npc = npc;
	}

	@Override
	public boolean getAccelerate() {
		return pXlr8;
	}

	@Override
	public int getAccuracy() {
		return accuracy;
	}

	@Override
	public int getBurst() {
		return burstCount;
	}

	@Override
	public int getBurstDelay() {
		return fireRate;
	}

	@Override
	public int getDelayMax() {
		return maxDelay;
	}

	@Override
	public int getDelayMin() {
		return minDelay;
	}

	@Override
	public int getDelayRNG() {
		int delay = minDelay;
		if ((maxDelay - minDelay) > 0) {
			delay += npc.worldObj.rand.nextInt(maxDelay - minDelay);
		}
		return delay;
	}

	@Override
	public int getEffectStrength() {
		return pEffAmp;
	}

	@Override
	public int getEffectTime() {
		return pDur;
	}

	@Override
	public int getEffectType() {
		return pEffect;
	}

	@Override
	public int getExplodeSize() {
		return pArea;
	}

	@Override
	public int getFireType() {
		return canFireIndirect;
	}

	@Override
	public boolean getGlows() {
		return pGlows;
	}

	@Override
	public boolean getHasAimAnimation() {
		return aimWhileShooting;
	}

	@Override
	public boolean getHasGravity() {
		return pPhysics;
	}

	@Override
	public int getKnockback() {
		return pImpact;
	}

	@Override
	public int getMeleeRange() {
		return meleeDistance;
	}

	@Override
	public int getParticle() {
		return pTrail;
	}

	@Override
	public int getRange() {
		return rangedRange;
	}

	@Override
	public boolean getRender3D() {
		return pRender3D;
	}

	@Override
	public int getShotCount() {
		return shotCount;
	}

	@Override
	public int getSize() {
		return pSize;
	}

	@Override
	public String getSound(int type) {
		String sound = null;
		if (type == 0) {
			sound = fireSound;
		}
		if (type == 1) {
			sound = hitSound;
		}
		if (type == 2) {
			sound = groundSound;
		}
		if ((sound == null) || sound.isEmpty()) {
			return null;
		}
		return sound;
	}

	@Override
	public int getSpeed() {
		return pSpeed;
	}

	@Override
	public boolean getSpins() {
		return pSpin;
	}

	@Override
	public boolean getSticks() {
		return pStick;
	}

	@Override
	public int getStrength() {
		return pDamage;
	}

	public void readFromNBT(NBTTagCompound compound) {
		pDamage = compound.getInteger("pDamage");
		pSpeed = compound.getInteger("pSpeed");
		burstCount = compound.getInteger("BurstCount");
		pImpact = compound.getInteger("pImpact");
		pSize = compound.getInteger("pSize");
		pArea = compound.getInteger("pArea");
		pTrail = compound.getInteger("pTrail");
		rangedRange = compound.getInteger("MaxFiringRange");
		fireRate = compound.getInteger("FireRate");
		minDelay = ValueUtil.CorrectInt(compound.getInteger("minDelay"), 1, 9999);
		maxDelay = ValueUtil.CorrectInt(compound.getInteger("maxDelay"), 1, 9999);
		shotCount = ValueUtil.CorrectInt(compound.getInteger("ShotCount"), 1, 10);
		accuracy = compound.getInteger("Accuracy");
		pRender3D = compound.getBoolean("pRender3D");
		pSpin = compound.getBoolean("pSpin");
		pStick = compound.getBoolean("pStick");
		pPhysics = compound.getBoolean("pPhysics");
		pXlr8 = compound.getBoolean("pXlr8");
		pGlows = compound.getBoolean("pGlows");
		aimWhileShooting = compound.getBoolean("AimWhileShooting");
		pEffect = compound.getInteger("pEffect");
		pDur = compound.getInteger("pDur");
		pEffAmp = compound.getInteger("pEffAmp");
		fireSound = compound.getString("FiringSound");
		hitSound = compound.getString("HitSound");
		groundSound = compound.getString("GroundSound");
		canFireIndirect = compound.getInteger("FireIndirect");
		meleeDistance = compound.getInteger("DistanceToMelee");
	}

	@Override
	public void setAccelerate(boolean accelerate) {
		pXlr8 = accelerate;
	}

	@Override
	public void setAccuracy(int accuracy) {
		accuracy = ValueUtil.CorrectInt(accuracy, 1, 100);
	}

	@Override
	public void setBurst(int count) {
		burstCount = count;
	}

	@Override
	public void setBurstDelay(int delay) {
		fireRate = delay;
	}

	@Override
	public void setDelay(int min, int max) {
		min = Math.min(min, max);
		minDelay = min;
		maxDelay = max;
	}

	@Override
	public void setEffect(int type, int strength, int time) {
		pEffect = type;
		pDur = time;
		pEffAmp = strength;
	}

	@Override
	public void setExplodeSize(int size) {
		pArea = size;
	}

	@Override
	public void setFireType(int type) {
		canFireIndirect = type;
	}

	@Override
	public void setGlows(boolean glows) {
		pGlows = glows;
	}

	@Override
	public void setHasAimAnimation(boolean aim) {
		aimWhileShooting = aim;
	}

	@Override
	public void setHasGravity(boolean hasGravity) {
		pPhysics = hasGravity;
	}

	@Override
	public void setKnockback(int punch) {
		pImpact = punch;
	}

	@Override
	public void setMeleeRange(int range) {
		meleeDistance = range;
		npc.updateAI = true;
	}

	@Override
	public void setParticle(int type) {
		pTrail = type;
	}

	@Override
	public void setRange(int range) {
		rangedRange = ValueUtil.CorrectInt(range, 1, 64);
	}

	@Override
	public void setRender3D(boolean render3d) {
		pRender3D = render3d;
	}

	@Override
	public void setShotCount(int count) {
		shotCount = count;
	}

	@Override
	public void setSize(int size) {
		pSize = size;
	}

	@Override
	public void setSound(int type, String sound) {
		if (sound == null) {
			sound = "";
		}
		if (type == 0) {
			fireSound = sound;
		}
		if (type == 1) {
			hitSound = sound;
		}
		if (type == 2) {
			groundSound = sound;
		}
		npc.updateClient = true;
	}

	@Override
	public void setSpeed(int speed) {
		pSpeed = ValueUtil.CorrectInt(speed, 0, 100);
	}

	@Override
	public void setSpins(boolean spins) {
		pSpin = spins;
	}

	@Override
	public void setSticks(boolean sticks) {
		pStick = sticks;
	}

	@Override
	public void setStrength(int strength) {
		pDamage = strength;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("BurstCount", burstCount);
		compound.setInteger("pSpeed", pSpeed);
		compound.setInteger("pDamage", pDamage);
		compound.setInteger("pImpact", pImpact);
		compound.setInteger("pSize", pSize);
		compound.setInteger("pArea", pArea);
		compound.setInteger("pTrail", pTrail);
		compound.setInteger("MaxFiringRange", rangedRange);
		compound.setInteger("FireRate", fireRate);
		compound.setInteger("minDelay", minDelay);
		compound.setInteger("maxDelay", maxDelay);
		compound.setInteger("ShotCount", shotCount);
		compound.setInteger("Accuracy", accuracy);
		compound.setBoolean("pRender3D", pRender3D);
		compound.setBoolean("pSpin", pSpin);
		compound.setBoolean("pStick", pStick);
		compound.setBoolean("pPhysics", pPhysics);
		compound.setBoolean("pXlr8", pXlr8);
		compound.setBoolean("pGlows", pGlows);
		compound.setBoolean("AimWhileShooting", aimWhileShooting);
		compound.setInteger("pEffect", pEffect);
		compound.setInteger("pDur", pDur);
		compound.setInteger("pEffAmp", pEffAmp);
		compound.setString("FiringSound", fireSound);
		compound.setString("HitSound", hitSound);
		compound.setString("GroundSound", groundSound);
		compound.setInteger("FireIndirect", canFireIndirect);
		compound.setInteger("DistanceToMelee", meleeDistance);
		return compound;
	}
}

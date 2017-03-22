
package noppes.npcs.ai.roles.companion;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.entity.EntityNPCInterface;

public class CompanionFoodStats {
	private int foodLevel;
	private float foodSaturationLevel;
	private float foodExhaustionLevel;
	private int foodTimer;
	private int prevFoodLevel;

	public CompanionFoodStats() {
		foodLevel = 20;
		foodSaturationLevel = 5.0f;
		prevFoodLevel = 20;
	}

	public void addExhaustion(float p_75113_1_) {
		foodExhaustionLevel = Math.min(foodExhaustionLevel + p_75113_1_, 40.0f);
	}

	private void addStats(int p_75122_1_, float p_75122_2_) {
		foodLevel = Math.min(p_75122_1_ + foodLevel, 20);
		foodSaturationLevel = Math.min(foodSaturationLevel + (p_75122_1_ * p_75122_2_ * 2.0f), foodLevel);
	}

	public int getFoodLevel() {
		return foodLevel;
	}

	@SideOnly(Side.CLIENT)
	public int getPrevFoodLevel() {
		return prevFoodLevel;
	}

	public float getSaturationLevel() {
		return foodSaturationLevel;
	}

	public boolean needFood() {
		return foodLevel < 20;
	}

	public void onFoodEaten(ItemFood food, ItemStack itemstack) {
		addStats(food.getHealAmount(itemstack), food.getSaturationModifier(itemstack));
	}

	public void onUpdate(EntityNPCInterface npc) {
		EnumDifficulty enumdifficulty = npc.worldObj.getDifficulty();
		prevFoodLevel = foodLevel;
		if (foodExhaustionLevel > 4.0f) {
			foodExhaustionLevel -= 4.0f;
			if (foodSaturationLevel > 0.0f) {
				foodSaturationLevel = Math.max(foodSaturationLevel - 1.0f, 0.0f);
			} else if (enumdifficulty != EnumDifficulty.PEACEFUL) {
				foodLevel = Math.max(foodLevel - 1, 0);
			}
		}
		if (npc.worldObj.getGameRules().getBoolean("naturalRegeneration") && (foodLevel >= 18)
				&& (npc.getHealth() > 0.0f) && (npc.getHealth() < npc.getMaxHealth())) {
			++foodTimer;
			if (foodTimer >= 80) {
				npc.heal(1.0f);
				addExhaustion(3.0f);
				foodTimer = 0;
			}
		} else if (foodLevel <= 0) {
			++foodTimer;
			if (foodTimer >= 80) {
				if ((npc.getHealth() > 10.0f) || (enumdifficulty == EnumDifficulty.HARD)
						|| ((npc.getHealth() > 1.0f) && (enumdifficulty == EnumDifficulty.NORMAL))) {
					npc.attackEntityFrom(DamageSource.starve, 1.0f);
				}
				foodTimer = 0;
			}
		} else {
			foodTimer = 0;
		}
	}

	public void readNBT(NBTTagCompound p_75112_1_) {
		if (p_75112_1_.hasKey("foodLevel", 99)) {
			foodLevel = p_75112_1_.getInteger("foodLevel");
			foodTimer = p_75112_1_.getInteger("foodTickTimer");
			foodSaturationLevel = p_75112_1_.getFloat("foodSaturationLevel");
			foodExhaustionLevel = p_75112_1_.getFloat("foodExhaustionLevel");
		}
	}

	@SideOnly(Side.CLIENT)
	public void setFoodLevel(int p_75114_1_) {
		foodLevel = p_75114_1_;
	}

	@SideOnly(Side.CLIENT)
	public void setFoodSaturationLevel(float p_75119_1_) {
		foodSaturationLevel = p_75119_1_;
	}

	public void writeNBT(NBTTagCompound p_75117_1_) {
		p_75117_1_.setInteger("foodLevel", foodLevel);
		p_75117_1_.setInteger("foodTickTimer", foodTimer);
		p_75117_1_.setFloat("foodSaturationLevel", foodSaturationLevel);
		p_75117_1_.setFloat("foodExhaustionLevel", foodExhaustionLevel);
	}
}

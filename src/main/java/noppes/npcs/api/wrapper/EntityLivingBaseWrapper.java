//

//

package noppes.npcs.api.wrapper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLivingBase;

public class EntityLivingBaseWrapper<T extends EntityLivingBase> extends EntityWrapper<T> implements IEntityLivingBase {
	public EntityLivingBaseWrapper(T entity) {
		super(entity);
	}

	@Override
	public void addPotionEffect(int effect, int duration, int strength, boolean hideParticles) {
		if ((effect < 0) || (effect >= Potion.potionTypes.length) || (Potion.potionTypes[effect] == null)) {
			return;
		}
		if (strength < 0) {
			strength = 0;
		} else if (strength > 255) {
			strength = 255;
		}
		if (duration < 0) {
			duration = 0;
		} else if (duration > 1000000) {
			duration = 1000000;
		}
		if (!Potion.potionTypes[effect].isInstant()) {
			duration *= 20;
		}
		if (duration == 0) {
			entity.removePotionEffect(effect);
		} else {
			entity.addPotionEffect(new PotionEffect(effect, duration, strength, false, hideParticles));
		}
	}

	@Override
	public boolean canSeeEntity(IEntity entity) {
		return this.entity.canEntityBeSeen(entity.getMCEntity());
	}

	@Override
	public void clearPotionEffects() {
		entity.clearActivePotions();
	}

	@Override
	public IItemStack getArmor(int slot) {
		ItemStack item = entity.getEquipmentInSlot(slot + 1);
		if (item == null) {
			return null;
		}
		return new ItemStackWrapper(item);
	}

	@Override
	public IEntityLivingBase getAttackTarget() {
		return (IEntityLivingBase) NpcAPI.Instance().getIEntity(entity.getAITarget());
	}

	@Override
	public float getHealth() {
		return entity.getHealth();
	}

	@Override
	public IItemStack getHeldItem() {
		ItemStack item = entity.getHeldItem();
		if (item == null) {
			return null;
		}
		return new ItemStackWrapper(item);
	}

	@Override
	public IEntityLivingBase getLastAttacked() {
		return (IEntityLivingBase) NpcAPI.Instance().getIEntity(entity.getLastAttacker());
	}

	@Override
	public float getMaxHealth() {
		return entity.getMaxHealth();
	}

	@Override
	public int getPotionEffect(int effect) {
		PotionEffect pf = entity.getActivePotionEffect(Potion.potionTypes[effect]);
		if (pf == null) {
			return -1;
		}
		return pf.getAmplifier();
	}

	@Override
	public int getType() {
		return 5;
	}

	@Override
	public boolean isAttacking() {
		return entity.getAITarget() != null;
	}

	@Override
	public boolean isChild() {
		return entity.isChild();
	}

	@Override
	public void setArmor(int slot, IItemStack item) {
		entity.setCurrentItemOrArmor(slot + 1, (item == null) ? null : item.getMCItemStack());
	}

	@Override
	public void setAttackTarget(IEntityLivingBase living) {
		if (living == null) {
			entity.setRevengeTarget((EntityLivingBase) null);
		} else {
			entity.setRevengeTarget(living.getMCEntity());
		}
	}

	@Override
	public void setHealth(float health) {
		entity.setHealth(health);
	}

	@Override
	public void setHeldItem(IItemStack item) {
		entity.setCurrentItemOrArmor(0, (item == null) ? null : item.getMCItemStack());
	}

	@Override
	public void setMaxHealth(float health) {
		if (health < 0.0f) {
			return;
		}
		entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(health);
	}

	@Override
	public void swingHand() {
		entity.swingItem();
	}

	@Override
	public boolean typeOf(int type) {
		return (type == 5) || super.typeOf(type);
	}
}

//

//

package noppes.npcs.constants;

public enum EnumNpcToolMaterial {
	WOOD(0, 59, 2.0f, 0, 15), STONE(1, 131, 4.0f, 1, 5), BRONZE(2, 170, 5.0f, 2, 15), IRON(2, 250, 6.0f, 2, 14), DIA(3,
			1561, 8.0f, 3, 10), GOLD(0, 32, 12.0f, 1, 22), EMERALD(3, 1000, 8.0f, 4,
					10), DEMONIC(3, 100, 8.0f, 6, 10), FROST(2, 59, 6.0f, 3, 5), MITHRIL(3, 3000, 8.0f, 3, 10);

	private int harvestLevel;
	private int maxUses;
	private float efficiencyOnProperMaterial;
	private int damageVsEntity;
	private int enchantability;

	private EnumNpcToolMaterial(int par3, int par4, float par5, int par6, int par7) {
		harvestLevel = par3;
		maxUses = par4;
		efficiencyOnProperMaterial = par5;
		damageVsEntity = par6;
		enchantability = par7;
	}

	public int getDamageVsEntity() {
		return damageVsEntity;
	}

	public float getEfficiencyOnProperMaterial() {
		return efficiencyOnProperMaterial;
	}

	public int getEnchantability() {
		return enchantability;
	}

	public int getHarvestLevel() {
		return harvestLevel;
	}

	public int getMaxUses() {
		return maxUses;
	}
}

//

//

package noppes.npcs.api.wrapper;

import net.minecraft.entity.passive.EntityAnimal;
import noppes.npcs.api.entity.IAnimal;

public class AnimalWrapper<T extends EntityAnimal> extends EntityLivingWrapper<T> implements IAnimal {
	public AnimalWrapper(final T entity) {
		super(entity);
	}

	@Override
	public int getType() {
		return 4;
	}

	@Override
	public boolean typeOf(final int type) {
		return (type == 4) || super.typeOf(type);
	}
}

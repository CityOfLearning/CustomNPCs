//

//

package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAITransform extends EntityAIBase {
	private EntityNPCInterface npc;

	public EntityAITransform(final EntityNPCInterface npc) {
		this.npc = npc;
		setMutexBits(AiMutex.PASSIVE);
	}

	@Override
	public boolean shouldExecute() {
		return !npc.isKilled() && !npc.isAttacking() && !npc.transform.editingModus
				&& (((npc.worldObj.getWorldTime() % 24000L) < 12000L) ? npc.transform.isActive
						: (!npc.transform.isActive));
	}

	@Override
	public void startExecuting() {
		npc.transform.transform(!npc.transform.isActive);
	}
}

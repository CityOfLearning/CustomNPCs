
package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWorldLines extends EntityAIBase {
	private EntityNPCInterface npc;
	private int cooldown;

	public EntityAIWorldLines(EntityNPCInterface npc) {
		cooldown = 100;
		this.npc = npc;
		setMutexBits(AiMutex.PASSIVE);
	}

	@Override
	public boolean shouldExecute() {
		if (cooldown > 0) {
			--cooldown;
		}
		return !npc.isAttacking() && !npc.isKilled() && npc.advanced.hasWorldLines()
				&& (npc.getRNG().nextInt(600) == 1);
		// lets make this happen slightly more often 66% more likely
	}

	@Override
	public void startExecuting() {
		cooldown = 100;
		npc.saySurrounding(npc.advanced.getWorldLine());
	}
}

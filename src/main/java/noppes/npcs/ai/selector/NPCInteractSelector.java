//

//

package noppes.npcs.ai.selector;

import com.google.common.base.Predicate;

import noppes.npcs.entity.EntityNPCInterface;

public class NPCInteractSelector implements Predicate {
	private EntityNPCInterface npc;

	public NPCInteractSelector(EntityNPCInterface npc) {
		this.npc = npc;
	}

	@Override
	public boolean apply(Object ob) {
		return (ob instanceof EntityNPCInterface) && isEntityApplicable((EntityNPCInterface) ob);
	}

	public boolean isEntityApplicable(EntityNPCInterface entity) {
		return (entity != npc) && npc.isEntityAlive() && !entity.isAttacking()
				&& !npc.getFaction().isAggressiveToNpc(entity) && npc.ai.stopAndInteract;
	}
}

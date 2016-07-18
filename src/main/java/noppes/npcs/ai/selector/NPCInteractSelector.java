//

//

package noppes.npcs.ai.selector;

import com.google.common.base.Predicate;

import noppes.npcs.entity.EntityNPCInterface;

public class NPCInteractSelector implements Predicate {
	private EntityNPCInterface npc;

	public NPCInteractSelector(final EntityNPCInterface npc) {
		this.npc = npc;
	}

	@Override
	public boolean apply(final Object ob) {
		return (ob instanceof EntityNPCInterface) && isEntityApplicable((EntityNPCInterface) ob);
	}

	public boolean isEntityApplicable(final EntityNPCInterface entity) {
		return (entity != npc) && npc.isEntityAlive() && !entity.isAttacking()
				&& !npc.getFaction().isAggressiveToNpc(entity) && npc.ai.stopAndInteract;
	}
}

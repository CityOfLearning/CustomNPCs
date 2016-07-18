//

//

package noppes.npcs.util;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import noppes.npcs.entity.EntityNPCInterface;

public class GameProfileAlt extends GameProfile {
	private static final UUID id;
	static {
		id = UUID.randomUUID();
	}

	public EntityNPCInterface npc;

	public GameProfileAlt() {
		super((UUID) null, "customnpc");
	}

	@Override
	public UUID getId() {
		if (npc == null) {
			return GameProfileAlt.id;
		}
		return npc.getPersistentID();
	}

	@Override
	public String getName() {
		if (npc == null) {
			return super.getName();
		}
		return npc.getName();
	}
}

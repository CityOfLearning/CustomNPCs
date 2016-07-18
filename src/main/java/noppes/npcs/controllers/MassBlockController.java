//

//

package noppes.npcs.controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityNPCInterface;

public class MassBlockController {
	public interface IMassBlock {
		EntityNPCInterface getNpc();

		int getRange();

		void processed(final List<BlockData> p0);
	}

	private static Queue<IMassBlock> queue;

	private static MassBlockController Instance;

	public static void Queue(final IMassBlock imb) {
		MassBlockController.queue.add(imb);
	}

	public static void Update() {
		if (MassBlockController.queue.isEmpty()) {
			return;
		}
		final IMassBlock imb = MassBlockController.queue.remove();
		final World world = imb.getNpc().worldObj;
		final BlockPos pos = imb.getNpc().getPosition();
		final int range = imb.getRange();
		final List<BlockData> list = new ArrayList<BlockData>();
		for (int x = -range; x < range; ++x) {
			for (int z = -range; z < range; ++z) {
				if (world.isBlockLoaded(new BlockPos(x + pos.getX(), 64, z + pos.getZ()))) {
					for (int y = 0; y < range; ++y) {
						final BlockPos blockPos = pos.add(x, y - (range / 2), z);
						list.add(new BlockData(blockPos, world.getBlockState(blockPos), null));
					}
				}
			}
		}
		imb.processed(list);
	}

	public MassBlockController() {
		MassBlockController.queue = new LinkedList<IMassBlock>();
		MassBlockController.Instance = this;
	}
}

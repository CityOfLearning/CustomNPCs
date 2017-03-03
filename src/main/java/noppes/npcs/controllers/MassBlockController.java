
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

		void processed(List<BlockData> p0);
	}

	private static Queue<IMassBlock> queue;

	public static void Queue(IMassBlock imb) {
		MassBlockController.queue.add(imb);
	}

	public static void Update() {
		if (MassBlockController.queue.isEmpty()) {
			return;
		}
		IMassBlock imb = MassBlockController.queue.remove();
		World world = imb.getNpc().worldObj;
		BlockPos pos = imb.getNpc().getPosition();
		int range = imb.getRange();
		List<BlockData> list = new ArrayList<>();
		for (int x = -range; x < range; ++x) {
			for (int z = -range; z < range; ++z) {
				if (world.isBlockLoaded(new BlockPos(x + pos.getX(), 64, z + pos.getZ()))) {
					for (int y = 0; y < range; ++y) {
						BlockPos blockPos = pos.add(x, y - (range / 2), z);
						list.add(new BlockData(blockPos, world.getBlockState(blockPos), null));
					}
				}
			}
		}
		imb.processed(list);
	}

	public MassBlockController() {
		MassBlockController.queue = new LinkedList<>();
	}
}


package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobFarmer;

public class GuiJobFarmer extends GuiNPCInterface2 {
	private JobFarmer job;

	public GuiJobFarmer(EntityNPCInterface npc) {
		super(npc);
		job = (JobFarmer) npc.jobInterface;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 0) {
			job.chestMode = ((GuiNpcButton) guibutton).getValue();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addLabel(new GuiNpcLabel(0, "farmer.itempicked", guiLeft + 10, guiTop + 20));
		addButton(new GuiNpcButton(0, guiLeft + 100, guiTop + 15, 160, 20,
				new String[] { "farmer.donothing", "farmer.chest", "farmer.drop" }, job.chestMode));
	}

	@Override
	public void save() {
		Client.sendData(EnumPacketServer.JobSave, job.writeToNBT(new NBTTagCompound()));
	}
}

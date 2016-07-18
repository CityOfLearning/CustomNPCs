//

//

package noppes.npcs.entity.data;

import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.data.INPCDisplay;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class DataDisplay implements INPCDisplay {
	EntityNPCInterface npc;
	private String name;
	private String title;
	public byte skinType;
	private String url;
	public GameProfile playerProfile;
	private String texture;
	private String cloakTexture;
	private String glowTexture;
	private int visible;
	private int modelSize;
	private int showName;
	private int skinColor;
	private boolean disableLivingAnimation;
	private byte showBossBar;

	public DataDisplay(final EntityNPCInterface npc) {
		title = "";
		skinType = 0;
		url = "";
		texture = "customnpcs:textures/entity/humanmale/Steve.png";
		cloakTexture = "";
		glowTexture = "";
		visible = 0;
		modelSize = 5;
		showName = 0;
		skinColor = 16777215;
		disableLivingAnimation = false;
		showBossBar = 0;
		this.npc = npc;
		final String[] names = { "Noppes", "Noppes", "Noppes", "Noppes", "Atesson", "Rothcersul", "Achdranys", "Pegato",
				"Chald", "Gareld", "Nalworche", "Ineald", "Tia'kim", "Torerod", "Turturdar", "Ranler", "Dyntan",
				"Oldrake", "Gharis", "Elmn", "Tanal", "Waran-ess", "Ach-aldhat", "Athi", "Itageray", "Tasr", "Ightech",
				"Gakih", "Adkal", "Qua'an", "Sieq", "Urnp", "Rods", "Vorbani", "Smaik", "Fian", "Hir", "Ristai",
				"Kineth", "Naif", "Issraya", "Arisotura", "Honf", "Rilfom", "Estz", "Ghatroth", "Yosil", "Darage",
				"Aldny", "Tyltran", "Armos", "Loxiku", "Burhat", "Tinlt", "Ightyd", "Mia", "Ken", "Karla", "Lily",
				"Carina", "Daniel", "Slater", "Zidane", "Valentine", "Eirina", "Carnow", "Grave", "Shadow", "Drakken",
				"Kaoz", "Silk", "Drake", "Oldam", "Lynxx", "Lenyx", "Winter", "Seth", "Apolitho", "Amethyst", "Ankin",
				"Seinkan", "Ayumu", "Sakamoto", "Divina", "Div", "Magia", "Magnus", "Tiakono", "Ruin", "Hailinx",
				"Ethan", "Wate", "Carter", "William", "Brion", "Sparrow", "Basrrelen", "Gyaku", "Claire", "Crowfeather",
				"Blackwell", "Raven", "Farcri", "Lucas", "Bangheart", "Kamoku", "Kyoukan", "Blaze", "Benjamin",
				"Larianne", "Kakaragon", "Melancholy", "Epodyno", "Thanato", "Mika", "Dacks", "Ylander", "Neve",
				"Meadow", "Cuero", "Embrera", "Eldamore", "Faolan", "Chim", "Nasu", "Kathrine", "Ariel", "Arei",
				"Demytrix", "Kora", "Ava", "Larson", "Leonardo", "Wyrl", "Sakiama", "Lambton", "Kederath", "Malus",
				"Riplette", "Andern", "Ezall", "Lucien", "Droco", "Cray", "Tymen", "Zenix", "Entranger", "Saenorath",
				"Chris", "Christine", "Marble", "Mable", "Ross", "Rose", "Xalgan ", "Kennet", "Aphmau" };
		name = names[new Random().nextInt(names.length)];
	}

	@Override
	public int getBossbar() {
		return showBossBar;
	}

	@Override
	public String getCapeTexture() {
		return cloakTexture;
	}

	@Override
	public boolean getHasLivingAnimation() {
		return !disableLivingAnimation;
	}

	@Override
	public float[] getModelScale(final int part) {
		final ModelData modeldata = ((EntityCustomNpc) npc).modelData;
		ModelPartConfig model = null;
		if (part == 0) {
			model = modeldata.getPartConfig(EnumParts.HEAD);
		} else if (part == 1) {
			model = modeldata.getPartConfig(EnumParts.BODY);
		} else if (part == 2) {
			model = modeldata.getPartConfig(EnumParts.ARM_LEFT);
		} else if (part == 3) {
			model = modeldata.getPartConfig(EnumParts.ARM_RIGHT);
		} else if (part == 4) {
			model = modeldata.getPartConfig(EnumParts.LEG_LEFT);
		} else if (part == 5) {
			model = modeldata.getPartConfig(EnumParts.LEG_RIGHT);
		}
		if (model == null) {
			throw new CustomNPCsException("Unknown part: " + part, new Object[0]);
		}
		return new float[] { model.scaleX, model.scaleY, model.scaleZ };
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getOverlayTexture() {
		return glowTexture;
	}

	@Override
	public int getShowName() {
		return showName;
	}

	@Override
	public int getSize() {
		return modelSize;
	}

	@Override
	public String getSkinPlayer() {
		return (playerProfile == null) ? "" : playerProfile.getName();
	}

	@Override
	public String getSkinTexture() {
		return texture;
	}

	@Override
	public String getSkinUrl() {
		return url;
	}

	@Override
	public int getTint() {
		return skinColor;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public int getVisible() {
		return visible;
	}

	public void loadProfile() {
		if ((playerProfile != null) && !StringUtils.isNullOrEmpty(playerProfile.getName())
				&& (MinecraftServer.getServer() != null)
				&& (!playerProfile.isComplete() || !playerProfile.getProperties().containsKey("textures"))) {
			GameProfile gameprofile = MinecraftServer.getServer().getPlayerProfileCache()
					.getGameProfileForUsername(playerProfile.getName());
			if (gameprofile != null) {
				final Property property = (Property) Iterables
						.getFirst((Iterable) gameprofile.getProperties().get("textures"), null);
				if (property == null) {
					gameprofile = MinecraftServer.getServer().getMinecraftSessionService()
							.fillProfileProperties(gameprofile, true);
				}
				playerProfile = gameprofile;
			}
		}
	}

	public void readToNBT(final NBTTagCompound nbttagcompound) {
		name = nbttagcompound.getString("Name");
		title = nbttagcompound.getString("Title");
		url = nbttagcompound.getString("SkinUrl");
		final int prevSkinType = skinType;
		final String prevTexture = texture;
		skinType = nbttagcompound.getByte("UsingSkinUrl");
		texture = nbttagcompound.getString("Texture");
		cloakTexture = nbttagcompound.getString("CloakTexture");
		glowTexture = nbttagcompound.getString("GlowTexture");
		playerProfile = null;
		if (skinType == 1) {
			if (nbttagcompound.hasKey("SkinUsername", 10)) {
				playerProfile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkinUsername"));
			} else if (nbttagcompound.hasKey("SkinUsername", 8)
					&& !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkinUsername"))) {
				playerProfile = new GameProfile((UUID) null, nbttagcompound.getString("SkinUsername"));
			}
			loadProfile();
		}
		modelSize = ValueUtil.CorrectInt(nbttagcompound.getInteger("Size"), 1, 30);
		showName = nbttagcompound.getInteger("ShowName");
		if (nbttagcompound.hasKey("SkinColor")) {
			skinColor = nbttagcompound.getInteger("SkinColor");
		}
		visible = nbttagcompound.getInteger("NpcVisible");
		disableLivingAnimation = nbttagcompound.getBoolean("NoLivingAnimation");
		showBossBar = nbttagcompound.getByte("BossBar");
		if ((prevSkinType != skinType) || !texture.equals(prevTexture)) {
			npc.textureLocation = null;
		}
		npc.textureGlowLocation = null;
		npc.textureCloakLocation = null;
		npc.updateHitbox();
	}

	@Override
	public void setBossbar(final int type) {
		if (type == showBossBar) {
			return;
		}
		showBossBar = (byte) ValueUtil.CorrectInt(type, 0, 2);
		npc.updateClient = true;
	}

	@Override
	public void setCapeTexture(final String texture) {
		if (cloakTexture.equals(texture)) {
			return;
		}
		cloakTexture = texture;
		npc.textureCloakLocation = null;
		npc.updateClient = true;
	}

	@Override
	public void setHashLivingAnimation(final boolean enabled) {
		disableLivingAnimation = !enabled;
		npc.updateClient = true;
	}

	@Override
	public void setModelScale(final int part, final float x, final float y, final float z) {
		final ModelData modeldata = ((EntityCustomNpc) npc).modelData;
		ModelPartConfig model = null;
		if (part == 0) {
			model = modeldata.getPartConfig(EnumParts.HEAD);
		} else if (part == 1) {
			model = modeldata.getPartConfig(EnumParts.BODY);
		} else if (part == 2) {
			model = modeldata.getPartConfig(EnumParts.ARM_LEFT);
		} else if (part == 3) {
			model = modeldata.getPartConfig(EnumParts.ARM_RIGHT);
		} else if (part == 4) {
			model = modeldata.getPartConfig(EnumParts.LEG_LEFT);
		} else if (part == 5) {
			model = modeldata.getPartConfig(EnumParts.LEG_RIGHT);
		}
		if (model == null) {
			throw new CustomNPCsException("Unknown part: " + part, new Object[0]);
		}
		model.setScale(x, y, z);
		npc.updateClient = true;
	}

	@Override
	public void setName(final String name) {
		if (this.name.equals(name)) {
			return;
		}
		this.name = name;
		npc.updateClient = true;
	}

	@Override
	public void setOverlayTexture(final String texture) {
		if (glowTexture.equals(texture)) {
			return;
		}
		glowTexture = texture;
		npc.textureGlowLocation = null;
		npc.updateClient = true;
	}

	@Override
	public void setShowName(final int type) {
		if (type == showName) {
			return;
		}
		showName = ValueUtil.CorrectInt(type, 0, 2);
		npc.updateClient = true;
	}

	@Override
	public void setSize(final int size) {
		if (modelSize == size) {
			return;
		}
		modelSize = ValueUtil.CorrectInt(size, 1, 30);
		npc.updateClient = true;
	}

	@Override
	public void setSkinPlayer(final String name) {
		if ((name == null) || name.isEmpty()) {
			playerProfile = null;
			skinType = 0;
		} else {
			playerProfile = new GameProfile((UUID) null, name);
			skinType = 1;
		}
		npc.updateClient = true;
	}

	@Override
	public void setSkinTexture(final String texture) {
		if (this.texture.equals(texture)) {
			return;
		}
		this.texture = texture;
		npc.textureLocation = null;
		skinType = 0;
		npc.updateClient = true;
	}

	@Override
	public void setSkinUrl(final String url) {
		if (this.url.equals(url)) {
			return;
		}
		this.url = url;
		if (url.isEmpty()) {
			skinType = 0;
		} else {
			skinType = 2;
		}
		npc.updateClient = true;
	}

	@Override
	public void setTint(final int color) {
		if (color == skinColor) {
			return;
		}
		skinColor = color;
		npc.updateClient = true;
	}

	@Override
	public void setTitle(final String title) {
		if (this.title.equals(title)) {
			return;
		}
		this.title = title;
		npc.updateClient = true;
	}

	@Override
	public void setVisible(final int type) {
		if (type == visible) {
			return;
		}
		visible = ValueUtil.CorrectInt(type, 0, 2);
		npc.updateClient = true;
	}

	public boolean showName() {
		return !npc.isKilled() && ((showName == 0) || ((showName == 2) && npc.isAttacking()));
	}

	public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("Name", name);
		nbttagcompound.setString("Title", title);
		nbttagcompound.setString("SkinUrl", url);
		nbttagcompound.setString("Texture", texture);
		nbttagcompound.setString("CloakTexture", cloakTexture);
		nbttagcompound.setString("GlowTexture", glowTexture);
		nbttagcompound.setByte("UsingSkinUrl", skinType);
		if (playerProfile != null) {
			final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
			NBTUtil.writeGameProfile(nbttagcompound2, playerProfile);
			nbttagcompound.setTag("SkinUsername", nbttagcompound2);
		}
		nbttagcompound.setInteger("Size", modelSize);
		nbttagcompound.setInteger("ShowName", showName);
		nbttagcompound.setInteger("SkinColor", skinColor);
		nbttagcompound.setInteger("NpcVisible", visible);
		nbttagcompound.setBoolean("NoLivingAnimation", disableLivingAnimation);
		nbttagcompound.setByte("BossBar", showBossBar);
		return nbttagcompound;
	}
}

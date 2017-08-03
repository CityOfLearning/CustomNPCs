package noppes.npcs.entity.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.data.INPCDisplay;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.model.ModelData;
import noppes.npcs.model.ModelPartConfig;
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

	public DataDisplay(EntityNPCInterface npc) {
		title = "";
		skinType = 0;
		url = "";
		boolean gender = new Random().nextBoolean();
		if (gender) {
			texture = "customnpcs:textures/entity/humanmale/Steve.png";
		} else {
			texture = "customnpcs:textures/entity/humanfemale/Stephanie.png";
		}
		cloakTexture = "";
		glowTexture = "";
		visible = 0;
		modelSize = 5;
		showName = 0;
		skinColor = 16777215;
		disableLivingAnimation = false;
		showBossBar = 0;
		this.npc = npc;
		ResourceLocation nameTxt = new ResourceLocation("customnpcs", "male_names.txt");
		
		try {
			if (gender) {
				InputStream in = getClass().getResourceAsStream("/assets/customnpcs/male_names.txt");
				BufferedReader input = new BufferedReader(new InputStreamReader(in));
				List<String> names = new ArrayList();
				String line = input.readLine();

				while (line != null) {
					names.add(line);

					line = input.readLine();

				}
				name = names.get(new Random().nextInt(names.size()));
			} else {
				InputStream in = getClass().getResourceAsStream("/assets/customnpcs/female_names.txt");
				BufferedReader input = new BufferedReader(new InputStreamReader(in));
				List<String> names = new ArrayList();
				String line = input.readLine();

				while (line != null) {
					names.add(line);
					line = input.readLine();
				}
				name = names.get(new Random().nextInt(names.size()));
			}
		} catch (FileNotFoundException e) {
			CustomNpcs.logger.error("Encountered an error", e);
		} catch (IOException e) {
			CustomNpcs.logger.error("Encountered an error", e);
		}
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
	public float[] getModelScale(int part) {
		ModelData modeldata = ((EntityCustomNpc) npc).modelData;
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
				Property property = (Property) Iterables
						.getFirst((Iterable) gameprofile.getProperties().get("textures"), null);
				if (property == null) {
					gameprofile = MinecraftServer.getServer().getMinecraftSessionService()
							.fillProfileProperties(gameprofile, true);
				}
				playerProfile = gameprofile;
			}
		}
	}

	public void readToNBT(NBTTagCompound nbttagcompound) {
		name = nbttagcompound.getString("Name");
		title = nbttagcompound.getString("Title");
		url = nbttagcompound.getString("SkinUrl");
		int prevSkinType = skinType;
		String prevTexture = texture;
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
	public void setBossbar(int type) {
		if (type == showBossBar) {
			return;
		}
		showBossBar = (byte) ValueUtil.CorrectInt(type, 0, 2);
		npc.updateClient = true;
	}

	@Override
	public void setCapeTexture(String texture) {
		if (cloakTexture.equals(texture)) {
			return;
		}
		cloakTexture = texture;
		npc.textureCloakLocation = null;
		npc.updateClient = true;
	}

	@Override
	public void setHashLivingAnimation(boolean enabled) {
		disableLivingAnimation = !enabled;
		npc.updateClient = true;
	}

	@Override
	public void setModelScale(int part, float x, float y, float z) {
		ModelData modeldata = ((EntityCustomNpc) npc).modelData;
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
	public void setName(String name) {
		if (this.name.equals(name)) {
			return;
		}
		this.name = name;
		npc.updateClient = true;
	}

	@Override
	public void setOverlayTexture(String texture) {
		if (glowTexture.equals(texture)) {
			return;
		}
		glowTexture = texture;
		npc.textureGlowLocation = null;
		npc.updateClient = true;
	}

	@Override
	public void setShowName(int type) {
		if (type == showName) {
			return;
		}
		showName = ValueUtil.CorrectInt(type, 0, 2);
		npc.updateClient = true;
	}

	@Override
	public void setSize(int size) {
		if (modelSize == size) {
			return;
		}
		modelSize = ValueUtil.CorrectInt(size, 1, 30);
		npc.updateClient = true;
	}

	@Override
	public void setSkinPlayer(String name) {
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
	public void setSkinTexture(String texture) {
		if (this.texture.equals(texture)) {
			return;
		}
		this.texture = texture;
		npc.textureLocation = null;
		skinType = 0;
		npc.updateClient = true;
	}

	@Override
	public void setSkinUrl(String url) {
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
	public void setTint(int color) {
		if (color == skinColor) {
			return;
		}
		skinColor = color;
		npc.updateClient = true;
	}

	@Override
	public void setTitle(String title) {
		if (this.title.equals(title)) {
			return;
		}
		this.title = title;
		npc.updateClient = true;
	}

	@Override
	public void setVisible(int type) {
		if (type == visible) {
			return;
		}
		visible = ValueUtil.CorrectInt(type, 0, 2);
		npc.updateClient = true;
	}

	public boolean showName() {
		return !npc.isKilled() && ((showName == 0) || ((showName == 2) && npc.isAttacking()));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("Name", name);
		nbttagcompound.setString("Title", title);
		nbttagcompound.setString("SkinUrl", url);
		nbttagcompound.setString("Texture", texture);
		nbttagcompound.setString("CloakTexture", cloakTexture);
		nbttagcompound.setString("GlowTexture", glowTexture);
		nbttagcompound.setByte("UsingSkinUrl", skinType);
		if (playerProfile != null) {
			NBTTagCompound nbttagcompound2 = new NBTTagCompound();
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

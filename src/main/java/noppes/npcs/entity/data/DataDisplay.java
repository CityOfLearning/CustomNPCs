package noppes.npcs.entity.data;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Random;
import java.util.UUID;
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
   private String title = "";
   public byte skinType = 0;
   private String url = "";
   public GameProfile playerProfile;
   private String texture = "customnpcs:textures/entity/humanmale/Steve.png";
   private String cloakTexture = "";
   private String glowTexture = "";
   private int visible = 0;
   private int modelSize = 5;
   private int showName = 0;
   private int skinColor = 16777215;
   private boolean disableLivingAnimation = false;
   private byte showBossBar = 0;


   public DataDisplay(EntityNPCInterface npc) {
      this.npc = npc;
      String[] names = new String[]{"Noppes", "Noppes", "Noppes", "Noppes", "Atesson", "Rothcersul", "Achdranys", "Pegato", "Chald", "Gareld", "Nalworche", "Ineald", "Tia\'kim", "Torerod", "Turturdar", "Ranler", "Dyntan", "Oldrake", "Gharis", "Elmn", "Tanal", "Waran-ess", "Ach-aldhat", "Athi", "Itageray", "Tasr", "Ightech", "Gakih", "Adkal", "Qua\'an", "Sieq", "Urnp", "Rods", "Vorbani", "Smaik", "Fian", "Hir", "Ristai", "Kineth", "Naif", "Issraya", "Arisotura", "Honf", "Rilfom", "Estz", "Ghatroth", "Yosil", "Darage", "Aldny", "Tyltran", "Armos", "Loxiku", "Burhat", "Tinlt", "Ightyd", "Mia", "Ken", "Karla", "Lily", "Carina", "Daniel", "Slater", "Zidane", "Valentine", "Eirina", "Carnow", "Grave", "Shadow", "Drakken", "Kaoz", "Silk", "Drake", "Oldam", "Lynxx", "Lenyx", "Winter", "Seth", "Apolitho", "Amethyst", "Ankin", "Seinkan", "Ayumu", "Sakamoto", "Divina", "Div", "Magia", "Magnus", "Tiakono", "Ruin", "Hailinx", "Ethan", "Wate", "Carter", "William", "Brion", "Sparrow", "Basrrelen", "Gyaku", "Claire", "Crowfeather", "Blackwell", "Raven", "Farcri", "Lucas", "Bangheart", "Kamoku", "Kyoukan", "Blaze", "Benjamin", "Larianne", "Kakaragon", "Melancholy", "Epodyno", "Thanato", "Mika", "Dacks", "Ylander", "Neve", "Meadow", "Cuero", "Embrera", "Eldamore", "Faolan", "Chim", "Nasu", "Kathrine", "Ariel", "Arei", "Demytrix", "Kora", "Ava", "Larson", "Leonardo", "Wyrl", "Sakiama", "Lambton", "Kederath", "Malus", "Riplette", "Andern", "Ezall", "Lucien", "Droco", "Cray", "Tymen", "Zenix", "Entranger", "Saenorath", "Chris", "Christine", "Marble", "Mable", "Ross", "Rose", "Xalgan ", "Kennet", "Aphmau"};
      this.name = names[(new Random()).nextInt(names.length)];
   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setString("Name", this.name);
      nbttagcompound.setString("Title", this.title);
      nbttagcompound.setString("SkinUrl", this.url);
      nbttagcompound.setString("Texture", this.texture);
      nbttagcompound.setString("CloakTexture", this.cloakTexture);
      nbttagcompound.setString("GlowTexture", this.glowTexture);
      nbttagcompound.setByte("UsingSkinUrl", this.skinType);
      if(this.playerProfile != null) {
         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
         NBTUtil.writeGameProfile(nbttagcompound1, this.playerProfile);
         nbttagcompound.setTag("SkinUsername", nbttagcompound1);
      }

      nbttagcompound.setInteger("Size", this.modelSize);
      nbttagcompound.setInteger("ShowName", this.showName);
      nbttagcompound.setInteger("SkinColor", this.skinColor);
      nbttagcompound.setInteger("NpcVisible", this.visible);
      nbttagcompound.setBoolean("NoLivingAnimation", this.disableLivingAnimation);
      nbttagcompound.setByte("BossBar", this.showBossBar);
      return nbttagcompound;
   }

   public void readToNBT(NBTTagCompound nbttagcompound) {
      this.name = nbttagcompound.getString("Name");
      this.title = nbttagcompound.getString("Title");
      this.url = nbttagcompound.getString("SkinUrl");
      byte prevSkinType = this.skinType;
      String prevTexture = this.texture;
      this.skinType = nbttagcompound.getByte("UsingSkinUrl");
      this.texture = nbttagcompound.getString("Texture");
      this.cloakTexture = nbttagcompound.getString("CloakTexture");
      this.glowTexture = nbttagcompound.getString("GlowTexture");
      this.playerProfile = null;
      if(this.skinType == 1) {
         if(nbttagcompound.hasKey("SkinUsername", 10)) {
            this.playerProfile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkinUsername"));
         } else if(nbttagcompound.hasKey("SkinUsername", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkinUsername"))) {
            this.playerProfile = new GameProfile((UUID)null, nbttagcompound.getString("SkinUsername"));
         }

         this.loadProfile();
      }

      this.modelSize = ValueUtil.CorrectInt(nbttagcompound.getInteger("Size"), 1, 30);
      this.showName = nbttagcompound.getInteger("ShowName");
      if(nbttagcompound.hasKey("SkinColor")) {
         this.skinColor = nbttagcompound.getInteger("SkinColor");
      }

      this.visible = nbttagcompound.getInteger("NpcVisible");
      this.disableLivingAnimation = nbttagcompound.getBoolean("NoLivingAnimation");
      this.showBossBar = nbttagcompound.getByte("BossBar");
      if(prevSkinType != this.skinType || !this.texture.equals(prevTexture)) {
         this.npc.textureLocation = null;
      }

      this.npc.textureGlowLocation = null;
      this.npc.textureCloakLocation = null;
      this.npc.updateHitbox();
   }

   public void loadProfile() {
      if(this.playerProfile != null && !StringUtils.isNullOrEmpty(this.playerProfile.getName()) && MinecraftServer.getServer() != null && (!this.playerProfile.isComplete() || !this.playerProfile.getProperties().containsKey("textures"))) {
         GameProfile gameprofile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(this.playerProfile.getName());
         if(gameprofile != null) {
            Property property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), (Object)null);
            if(property == null) {
               gameprofile = MinecraftServer.getServer().getMinecraftSessionService().fillProfileProperties(gameprofile, true);
            }

            this.playerProfile = gameprofile;
         }
      }

   }

   public boolean showName() {
      return this.npc.isKilled()?false:this.showName == 0 || this.showName == 2 && this.npc.isAttacking();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      if(!this.name.equals(name)) {
         this.name = name;
         this.npc.updateClient = true;
      }
   }

   public int getShowName() {
      return this.showName;
   }

   public void setShowName(int type) {
      if(type != this.showName) {
         this.showName = ValueUtil.CorrectInt(type, 0, 2);
         this.npc.updateClient = true;
      }
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      if(!this.title.equals(title)) {
         this.title = title;
         this.npc.updateClient = true;
      }
   }

   public String getSkinUrl() {
      return this.url;
   }

   public void setSkinUrl(String url) {
      if(!this.url.equals(url)) {
         this.url = url;
         if(url.isEmpty()) {
            this.skinType = 0;
         } else {
            this.skinType = 2;
         }

         this.npc.updateClient = true;
      }
   }

   public String getSkinPlayer() {
      return this.playerProfile == null?"":this.playerProfile.getName();
   }

   public void setSkinPlayer(String name) {
      if(name != null && !name.isEmpty()) {
         this.playerProfile = new GameProfile((UUID)null, name);
         this.skinType = 1;
      } else {
         this.playerProfile = null;
         this.skinType = 0;
      }

      this.npc.updateClient = true;
   }

   public String getSkinTexture() {
      return this.texture;
   }

   public void setSkinTexture(String texture) {
      if(!this.texture.equals(texture)) {
         this.texture = texture;
         this.npc.textureLocation = null;
         this.skinType = 0;
         this.npc.updateClient = true;
      }
   }

   public String getOverlayTexture() {
      return this.glowTexture;
   }

   public void setOverlayTexture(String texture) {
      if(!this.glowTexture.equals(texture)) {
         this.glowTexture = texture;
         this.npc.textureGlowLocation = null;
         this.npc.updateClient = true;
      }
   }

   public String getCapeTexture() {
      return this.cloakTexture;
   }

   public void setCapeTexture(String texture) {
      if(!this.cloakTexture.equals(texture)) {
         this.cloakTexture = texture;
         this.npc.textureCloakLocation = null;
         this.npc.updateClient = true;
      }
   }

   public boolean getHasLivingAnimation() {
      return !this.disableLivingAnimation;
   }

   public void setHashLivingAnimation(boolean enabled) {
      this.disableLivingAnimation = !enabled;
      this.npc.updateClient = true;
   }

   public int getBossbar() {
      return this.showBossBar;
   }

   public void setBossbar(int type) {
      if(type != this.showBossBar) {
         this.showBossBar = (byte)ValueUtil.CorrectInt(type, 0, 2);
         this.npc.updateClient = true;
      }
   }

   public int getVisible() {
      return this.visible;
   }

   public void setVisible(int type) {
      if(type != this.visible) {
         this.visible = ValueUtil.CorrectInt(type, 0, 2);
         this.npc.updateClient = true;
      }
   }

   public int getSize() {
      return this.modelSize;
   }

   public void setSize(int size) {
      if(this.modelSize != size) {
         this.modelSize = ValueUtil.CorrectInt(size, 1, 30);
         this.npc.updateClient = true;
      }
   }

   public void setModelScale(int part, float x, float y, float z) {
      ModelData modeldata = ((EntityCustomNpc)this.npc).modelData;
      ModelPartConfig model = null;
      if(part == 0) {
         model = modeldata.getPartConfig(EnumParts.HEAD);
      } else if(part == 1) {
         model = modeldata.getPartConfig(EnumParts.BODY);
      } else if(part == 2) {
         model = modeldata.getPartConfig(EnumParts.ARM_LEFT);
      } else if(part == 3) {
         model = modeldata.getPartConfig(EnumParts.ARM_RIGHT);
      } else if(part == 4) {
         model = modeldata.getPartConfig(EnumParts.LEG_LEFT);
      } else if(part == 5) {
         model = modeldata.getPartConfig(EnumParts.LEG_RIGHT);
      }

      if(model == null) {
         throw new CustomNPCsException("Unknown part: " + part, new Object[0]);
      } else {
         model.setScale(x, y, z);
         this.npc.updateClient = true;
      }
   }

   public float[] getModelScale(int part) {
      ModelData modeldata = ((EntityCustomNpc)this.npc).modelData;
      ModelPartConfig model = null;
      if(part == 0) {
         model = modeldata.getPartConfig(EnumParts.HEAD);
      } else if(part == 1) {
         model = modeldata.getPartConfig(EnumParts.BODY);
      } else if(part == 2) {
         model = modeldata.getPartConfig(EnumParts.ARM_LEFT);
      } else if(part == 3) {
         model = modeldata.getPartConfig(EnumParts.ARM_RIGHT);
      } else if(part == 4) {
         model = modeldata.getPartConfig(EnumParts.LEG_LEFT);
      } else if(part == 5) {
         model = modeldata.getPartConfig(EnumParts.LEG_RIGHT);
      }

      if(model == null) {
         throw new CustomNPCsException("Unknown part: " + part, new Object[0]);
      } else {
         return new float[]{model.scaleX, model.scaleY, model.scaleZ};
      }
   }

   public int getTint() {
      return this.skinColor;
   }

   public void setTint(int color) {
      if(color != this.skinColor) {
         this.skinColor = color;
         this.npc.updateClient = true;
      }
   }
}

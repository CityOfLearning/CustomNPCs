package noppes.npcs.constants;

import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;

public enum EnumPacketServer {

   Delete("Delete", 0, CustomNpcsPermissions.NPC_DELETE, true),
   RemoteMainMenu("RemoteMainMenu", 1, CustomNpcsPermissions.NPC_GUI),
   NpcMenuClose("NpcMenuClose", 2, CustomNpcsPermissions.NPC_GUI, true),
   RemoteDelete("RemoteDelete", 3, CustomNpcsPermissions.NPC_DELETE, true),
   RemoteFreeze("RemoteFreeze", 4, CustomNpcsPermissions.NPC_FREEZE),
   RemoteReset("RemoteReset", 5, CustomNpcsPermissions.NPC_RESET),
   SpawnMob("SpawnMob", 6, CustomNpcsPermissions.SPAWNER_MOB),
   MobSpawner("MobSpawner", 7, CustomNpcsPermissions.SPAWNER_CREATE),
   MainmenuAISave("MainmenuAISave", 8, CustomNpcsPermissions.NPC_ADVANCED, true),
   MainmenuAIGet("MainmenuAIGet", 9, true),
   MainmenuInvSave("MainmenuInvSave", 10, CustomNpcsPermissions.NPC_INVENTORY, true),
   MainmenuInvGet("MainmenuInvGet", 11, true),
   MainmenuStatsSave("MainmenuStatsSave", 12, CustomNpcsPermissions.NPC_STATS, true),
   MainmenuStatsGet("MainmenuStatsGet", 13, true),
   MainmenuDisplaySave("MainmenuDisplaySave", 14, CustomNpcsPermissions.NPC_DISPLAY, true),
   MainmenuDisplayGet("MainmenuDisplayGet", 15, true),
   ModelDataSave("ModelDataSave", 16, CustomNpcsPermissions.NPC_DISPLAY, true),
   MainmenuAdvancedSave("MainmenuAdvancedSave", 17, CustomNpcsPermissions.NPC_ADVANCED, true),
   MainmenuAdvancedGet("MainmenuAdvancedGet", 18, true),
   DialogNpcSet("DialogNpcSet", 19, CustomNpcsPermissions.NPC_ADVANCED),
   DialogNpcRemove("DialogNpcRemove", 20, CustomNpcsPermissions.NPC_ADVANCED, true),
   FactionSet("FactionSet", 21, CustomNpcsPermissions.NPC_ADVANCED, true),
   TransportSave("TransportSave", 22, CustomNpcsPermissions.NPC_ADVANCED, true),
   TransformSave("TransformSave", 23, CustomNpcsPermissions.NPC_ADVANCED, true),
   TransformGet("TransformGet", 24, true),
   TransformLoad("TransformLoad", 25, CustomNpcsPermissions.NPC_ADVANCED, true),
   TraderMarketSave("TraderMarketSave", 26, CustomNpcsPermissions.NPC_ADVANCED, true),
   JobSave("JobSave", 27, CustomNpcsPermissions.NPC_ADVANCED, true),
   JobGet("JobGet", 28, true),
   RoleSave("RoleSave", 29, CustomNpcsPermissions.NPC_ADVANCED, true),
   RoleGet("RoleGet", 30, true),
   JobSpawnerAdd("JobSpawnerAdd", 31, CustomNpcsPermissions.NPC_ADVANCED, true),
   JobSpawnerRemove("JobSpawnerRemove", 32, CustomNpcsPermissions.NPC_ADVANCED, true),
   RoleCompanionUpdate("RoleCompanionUpdate", 33, CustomNpcsPermissions.NPC_ADVANCED, true),
   LinkedSet("LinkedSet", 34, CustomNpcsPermissions.NPC_ADVANCED, true),
   ClonePreSave("ClonePreSave", 35, CustomNpcsPermissions.NPC_CLONE),
   CloneSave("CloneSave", 36, CustomNpcsPermissions.NPC_CLONE),
   CloneRemove("CloneRemove", 37, CustomNpcsPermissions.NPC_CLONE),
   CloneList("CloneList", 38),
   LinkedGetAll("LinkedGetAll", 39),
   LinkedRemove("LinkedRemove", 40, CustomNpcsPermissions.GLOBAL_LINKED),
   LinkedAdd("LinkedAdd", 41, CustomNpcsPermissions.GLOBAL_LINKED),
   PlayerDataRemove("PlayerDataRemove", 42, CustomNpcsPermissions.GLOBAL_PLAYERDATA),
   BankSave("BankSave", 43, CustomNpcsPermissions.GLOBAL_BANK),
   BanksGet("BanksGet", 44),
   BankGet("BankGet", 45),
   BankRemove("BankRemove", 46, CustomNpcsPermissions.GLOBAL_BANK),
   DialogCategorySave("DialogCategorySave", 47, CustomNpcsPermissions.GLOBAL_DIALOG),
   DialogCategoriesGet("DialogCategoriesGet", 48),
   DialogsGetFromDialog("DialogsGetFromDialog", 49),
   DialogCategoryRemove("DialogCategoryRemove", 50, CustomNpcsPermissions.GLOBAL_DIALOG),
   DialogCategoryGet("DialogCategoryGet", 51),
   DialogSave("DialogSave", 52, CustomNpcsPermissions.GLOBAL_DIALOG),
   DialogsGet("DialogsGet", 53),
   DialogGet("DialogGet", 54),
   DialogRemove("DialogRemove", 55, CustomNpcsPermissions.GLOBAL_DIALOG),
   TransportCategoryRemove("TransportCategoryRemove", 56, CustomNpcsPermissions.GLOBAL_TRANSPORT),
   TransportGetLocation("TransportGetLocation", 57, true),
   TransportRemove("TransportRemove", 58, CustomNpcsPermissions.GLOBAL_TRANSPORT),
   TransportsGet("TransportsGet", 59),
   TransportCategorySave("TransportCategorySave", 60, CustomNpcsPermissions.GLOBAL_TRANSPORT),
   TransportCategoriesGet("TransportCategoriesGet", 61),
   FactionRemove("FactionRemove", 62, CustomNpcsPermissions.GLOBAL_FACTION),
   FactionSave("FactionSave", 63, CustomNpcsPermissions.GLOBAL_FACTION),
   FactionsGet("FactionsGet", 64),
   FactionGet("FactionGet", 65),
   QuestCategorySave("QuestCategorySave", 66, CustomNpcsPermissions.GLOBAL_QUEST),
   QuestCategoriesGet("QuestCategoriesGet", 67),
   QuestRemove("QuestRemove", 68, CustomNpcsPermissions.GLOBAL_QUEST),
   QuestCategoryRemove("QuestCategoryRemove", 69, CustomNpcsPermissions.GLOBAL_QUEST),
   QuestRewardSave("QuestRewardSave", 70, CustomNpcsPermissions.GLOBAL_QUEST),
   QuestSave("QuestSave", 71, CustomNpcsPermissions.GLOBAL_QUEST),
   QuestsGetFromQuest("QuestsGetFromQuest", 72),
   QuestsGet("QuestsGet", 73),
   QuestDialogGetTitle("QuestDialogGetTitle", 74, CustomNpcsPermissions.GLOBAL_QUEST),
   RecipeSave("RecipeSave", 75, CustomNpcsPermissions.GLOBAL_RECIPE),
   RecipeRemove("RecipeRemove", 76, CustomNpcsPermissions.GLOBAL_RECIPE),
   NaturalSpawnSave("NaturalSpawnSave", 77, CustomNpcsPermissions.GLOBAL_NATURALSPAWN),
   NaturalSpawnGet("NaturalSpawnGet", 78),
   NaturalSpawnRemove("NaturalSpawnRemove", 79, CustomNpcsPermissions.GLOBAL_NATURALSPAWN),
   MerchantUpdate("MerchantUpdate", 80, CustomNpcsPermissions.EDIT_VILLAGER),
   PlayerRider("PlayerRider", 81, CustomNpcsPermissions.TOOL_MOUNTER),
   SpawnRider("SpawnRider", 82, CustomNpcsPermissions.TOOL_MOUNTER),
   MovingPathSave("MovingPathSave", 83, CustomNpcsPermissions.TOOL_PATHER, true),
   MovingPathGet("MovingPathGet", 84, true),
   DoorSave("DoorSave", 85, CustomNpcsPermissions.TOOL_SCRIPTER),
   DoorGet("DoorGet", 86),
   ScriptDataSave("ScriptDataSave", 87, CustomNpcsPermissions.TOOL_SCRIPTER, true),
   ScriptDataGet("ScriptDataGet", 88, true),
   ScriptBlockDataSave("ScriptBlockDataSave", 89, CustomNpcsPermissions.TOOL_SCRIPTER, false),
   ScriptBlockDataGet("ScriptBlockDataGet", 90, false),
   ScriptDoorDataSave("ScriptDoorDataSave", 91, CustomNpcsPermissions.TOOL_SCRIPTER, false),
   ScriptDoorDataGet("ScriptDoorDataGet", 92, false),
   DialogNpcGet("DialogNpcGet", 93),
   RecipesGet("RecipesGet", 94),
   RecipeGet("RecipeGet", 95),
   QuestOpenGui("QuestOpenGui", 96),
   PlayerDataGet("PlayerDataGet", 97),
   RemoteNpcsGet("RemoteNpcsGet", 98, CustomNpcsPermissions.NPC_GUI),
   RemoteTpToNpc("RemoteTpToNpc", 99),
   QuestGet("QuestGet", 100),
   QuestCategoryGet("QuestCategoryGet", 101),
   SaveTileEntity("SaveTileEntity", 102),
   NaturalSpawnGetAll("NaturalSpawnGetAll", 103),
   MailOpenSetup("MailOpenSetup", 104),
   DimensionsGet("DimensionsGet", 105),
   DimensionTeleport("DimensionTeleport", 106),
   GetTileEntity("GetTileEntity", 107),
   Gui("Gui", 108),
   SchematicsTile("SchematicsTile", 109),
   SchematicsSet("SchematicsSet", 110),
   SchematicsTileSave("SchematicsTileSave", 111),
   SchematicStore("SchematicStore", 112),
   SceneStart("SceneStart", 113, CustomNpcsPermissions.SCENES),
   SceneReset("SceneReset", 114, CustomNpcsPermissions.SCENES);
   public CustomNpcsPermissions.Permission permission;
   public boolean needsNpc;
   private boolean exempt;
   // $FF: synthetic field
   private static final EnumPacketServer[] $VALUES = new EnumPacketServer[]{Delete, RemoteMainMenu, NpcMenuClose, RemoteDelete, RemoteFreeze, RemoteReset, SpawnMob, MobSpawner, MainmenuAISave, MainmenuAIGet, MainmenuInvSave, MainmenuInvGet, MainmenuStatsSave, MainmenuStatsGet, MainmenuDisplaySave, MainmenuDisplayGet, ModelDataSave, MainmenuAdvancedSave, MainmenuAdvancedGet, DialogNpcSet, DialogNpcRemove, FactionSet, TransportSave, TransformSave, TransformGet, TransformLoad, TraderMarketSave, JobSave, JobGet, RoleSave, RoleGet, JobSpawnerAdd, JobSpawnerRemove, RoleCompanionUpdate, LinkedSet, ClonePreSave, CloneSave, CloneRemove, CloneList, LinkedGetAll, LinkedRemove, LinkedAdd, PlayerDataRemove, BankSave, BanksGet, BankGet, BankRemove, DialogCategorySave, DialogCategoriesGet, DialogsGetFromDialog, DialogCategoryRemove, DialogCategoryGet, DialogSave, DialogsGet, DialogGet, DialogRemove, TransportCategoryRemove, TransportGetLocation, TransportRemove, TransportsGet, TransportCategorySave, TransportCategoriesGet, FactionRemove, FactionSave, FactionsGet, FactionGet, QuestCategorySave, QuestCategoriesGet, QuestRemove, QuestCategoryRemove, QuestRewardSave, QuestSave, QuestsGetFromQuest, QuestsGet, QuestDialogGetTitle, RecipeSave, RecipeRemove, NaturalSpawnSave, NaturalSpawnGet, NaturalSpawnRemove, MerchantUpdate, PlayerRider, SpawnRider, MovingPathSave, MovingPathGet, DoorSave, DoorGet, ScriptDataSave, ScriptDataGet, ScriptBlockDataSave, ScriptBlockDataGet, ScriptDoorDataSave, ScriptDoorDataGet, DialogNpcGet, RecipesGet, RecipeGet, QuestOpenGui, PlayerDataGet, RemoteNpcsGet, RemoteTpToNpc, QuestGet, QuestCategoryGet, SaveTileEntity, NaturalSpawnGetAll, MailOpenSetup, DimensionsGet, DimensionTeleport, GetTileEntity, Gui, SchematicsTile, SchematicsSet, SchematicsTileSave, SchematicStore, SceneStart, SceneReset};


   private EnumPacketServer(String var1, int var2) {
      this.needsNpc = false;
      this.exempt = false;
   }

   private EnumPacketServer(String var1, int var2, CustomNpcsPermissions.Permission permission, boolean npc) {
      this(var1, var2, permission);
   }

   private EnumPacketServer(String var1, int var2, boolean npc) {
      this.needsNpc = false;
      this.exempt = false;
      this.needsNpc = npc;
   }

   private EnumPacketServer(String var1, int var2, CustomNpcsPermissions.Permission permission) {
      this.needsNpc = false;
      this.exempt = false;
      this.permission = permission;
   }

   public boolean hasPermission() {
      return this.permission != null;
   }

   public void exempt() {
      this.exempt = true;
   }

   public boolean isExempt() {
      return CustomNpcs.OpsOnly || this.exempt;
   }

   static {
      GetTileEntity.exempt();
      ScriptBlockDataGet.exempt();
      ScriptDoorDataGet.exempt();
      DialogCategoriesGet.exempt();
      DialogsGetFromDialog.exempt();
      DialogsGet.exempt();
      QuestsGetFromQuest.exempt();
      QuestCategoriesGet.exempt();
      QuestsGet.exempt();
      FactionsGet.exempt();
      DialogGet.exempt();
      QuestGet.exempt();
      FactionGet.exempt();
      SceneStart.exempt();
      SceneReset.exempt();
   }
}

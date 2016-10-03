package noppes.npcs.roles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.api.entity.data.role.IRoleDialog;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogOption;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.Quest;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleInterface;

public class RoleDialog extends RoleInterface implements IRoleDialog {

   public String dialog = "";
   public int questId = -1;
   public HashMap options = new HashMap();
   public HashMap optionsTexts = new HashMap();


   public RoleDialog(EntityNPCInterface npc) {
      super(npc);
   }

   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      compound.setInteger("RoleQuestId", this.questId);
      compound.setString("RoleDialog", this.dialog);
      compound.setTag("RoleOptions", NBTTags.nbtIntegerStringMap(this.options));
      compound.setTag("RoleOptionTexts", NBTTags.nbtIntegerStringMap(this.optionsTexts));
      return compound;
   }

   public void readFromNBT(NBTTagCompound compound) {
      this.questId = compound.getInteger("RoleQuestId");
      this.dialog = compound.getString("RoleDialog");
      this.options = NBTTags.getIntegerStringMap(compound.getTagList("RoleOptions", 10));
      this.optionsTexts = NBTTags.getIntegerStringMap(compound.getTagList("RoleOptionTexts", 10));
   }

   public void interact(EntityPlayer player) {
      if(this.dialog.isEmpty()) {
         this.npc.say(player, this.npc.advanced.getInteractLine());
      } else {
         Dialog quest = new Dialog();
         quest.text = this.dialog;
         Iterator var3 = this.options.entrySet().iterator();

         while(var3.hasNext()) {
            Entry entry = (Entry)var3.next();
            if(!((String)entry.getValue()).isEmpty()) {
               DialogOption option = new DialogOption();
               String text = (String)this.optionsTexts.get(entry.getKey());
               if(text != null && !text.isEmpty()) {
                  option.optionType = EnumOptionType.ROLE_OPTION;
               } else {
                  option.optionType = EnumOptionType.QUIT_OPTION;
               }

               option.title = (String)entry.getValue();
               quest.options.put(entry.getKey(), option);
            }
         }

         NoppesUtilServer.openDialog(player, this.npc, quest);
      }

      Quest quest1 = (Quest)QuestController.instance.quests.get(Integer.valueOf(this.questId));
      if(quest1 != null) {
         PlayerQuestController.addActiveQuest(quest1, player);
      }

   }

   public String getDialog() {
      return this.dialog;
   }

   public void setDialog(String text) {
      this.dialog = text;
   }

   public String getOption(int option) {
      return (String)this.options.get(Integer.valueOf(option));
   }

   public void setOption(int option, String text) {
      if(option >= 1 && option <= 6) {
         this.options.put(Integer.valueOf(option), text);
      } else {
         throw new CustomNPCsException("Wrong dialog option slot given: " + option, new Object[0]);
      }
   }

   public String getOptionDialog(int option) {
      return (String)this.optionsTexts.get(Integer.valueOf(option));
   }

   public void setOptionDialog(int option, String text) {
      if(option >= 1 && option <= 6) {
         this.optionsTexts.put(Integer.valueOf(option), text);
      } else {
         throw new CustomNPCsException("Wrong dialog option slot given: " + option, new Object[0]);
      }
   }
}

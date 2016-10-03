package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.api.handler.IRecipeHandler;
import noppes.npcs.api.handler.data.IRecipe;
import noppes.npcs.controllers.RecipeCarpentry;
import noppes.npcs.controllers.RecipesDefault;

public class RecipeController implements IRecipeHandler {

   private static Collection prevRecipes;
   public HashMap globalRecipes = new HashMap();
   public HashMap anvilRecipes = new HashMap();
   public static RecipeController instance;
   public static final int version = 1;
   public int nextId = 1;
   public static HashMap syncRecipes = new HashMap();


   public RecipeController() {
      instance = this;
   }

   public void load() {
      this.loadCategories();
      this.reloadGlobalRecipes();
      EventHooks.onGlobalRecipesLoaded(this);
   }

   public void reloadGlobalRecipes() {
      List list = CraftingManager.getInstance().getRecipeList();
      if(prevRecipes != null) {
         list.removeAll(prevRecipes);
      }

      prevRecipes = new HashSet();
      Iterator var2 = this.globalRecipes.values().iterator();

      while(var2.hasNext()) {
         RecipeCarpentry recipe = (RecipeCarpentry)var2.next();
         if(recipe.isValid()) {
            prevRecipes.add(recipe);
         }
      }

      list.addAll(prevRecipes);
   }

   private void loadCategories() {
      File saveDir = CustomNpcs.getWorldSaveDirectory();

      try {
         File e = new File(saveDir, "recipes.dat");
         if(e.exists()) {
            this.loadCategories(e);
         } else {
            this.globalRecipes.clear();
            this.anvilRecipes.clear();
            this.loadDefaultRecipes(-1);
         }
      } catch (Exception var5) {
         var5.printStackTrace();

         try {
            File ee = new File(saveDir, "recipes.dat_old");
            if(ee.exists()) {
               this.loadCategories(ee);
            }
         } catch (Exception var4) {
            var5.printStackTrace();
         }
      }

   }

   private void loadDefaultRecipes(int i) {
      if(i != 1) {
         RecipesDefault.loadDefaultRecipes(i);
         this.saveCategories();
      }
   }

   private void loadCategories(File file) throws Exception {
      NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
      this.nextId = nbttagcompound1.getInteger("LastId");
      NBTTagList list = nbttagcompound1.getTagList("Data", 10);
      HashMap globalRecipes = new HashMap();
      HashMap anvilRecipes = new HashMap();
      if(list != null) {
         for(int i = 0; i < list.tagCount(); ++i) {
            RecipeCarpentry recipe = RecipeCarpentry.read(list.getCompoundTagAt(i));
            if(recipe.isGlobal) {
               globalRecipes.put(Integer.valueOf(recipe.id), recipe);
            } else {
               anvilRecipes.put(Integer.valueOf(recipe.id), recipe);
            }

            if(recipe.id > this.nextId) {
               this.nextId = recipe.id;
            }
         }
      }

      this.anvilRecipes = anvilRecipes;
      this.globalRecipes = globalRecipes;
      this.loadDefaultRecipes(nbttagcompound1.getInteger("Version"));
   }

   private void saveCategories() {
      try {
         File e = CustomNpcs.getWorldSaveDirectory();
         NBTTagList list = new NBTTagList();
         Iterator nbttagcompound = this.globalRecipes.values().iterator();

         RecipeCarpentry file;
         while(nbttagcompound.hasNext()) {
            file = (RecipeCarpentry)nbttagcompound.next();
            if(file.savesRecipe) {
               list.appendTag(file.writeNBT());
            }
         }

         nbttagcompound = this.anvilRecipes.values().iterator();

         while(nbttagcompound.hasNext()) {
            file = (RecipeCarpentry)nbttagcompound.next();
            if(file.savesRecipe) {
               list.appendTag(file.writeNBT());
            }
         }

         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
         nbttagcompound1.setTag("Data", list);
         nbttagcompound1.setInteger("LastId", this.nextId);
         nbttagcompound1.setInteger("Version", 1);
         File file1 = new File(e, "recipes.dat_new");
         File file1 = new File(e, "recipes.dat_old");
         File file2 = new File(e, "recipes.dat");
         CompressedStreamTools.writeCompressed(nbttagcompound1, new FileOutputStream(file1));
         if(file1.exists()) {
            file1.delete();
         }

         file2.renameTo(file1);
         if(file2.exists()) {
            file2.delete();
         }

         file1.renameTo(file2);
         if(file1.exists()) {
            file1.delete();
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public RecipeCarpentry findMatchingRecipe(InventoryCrafting par1InventoryCrafting) {
      Iterator var2 = this.anvilRecipes.values().iterator();

      RecipeCarpentry recipe;
      do {
         if(!var2.hasNext()) {
            return null;
         }

         recipe = (RecipeCarpentry)var2.next();
      } while(!recipe.isValid() || !recipe.matches(par1InventoryCrafting, (World)null));

      return recipe;
   }

   public RecipeCarpentry getRecipe(int id) {
      return this.globalRecipes.containsKey(Integer.valueOf(id))?(RecipeCarpentry)this.globalRecipes.get(Integer.valueOf(id)):(this.anvilRecipes.containsKey(Integer.valueOf(id))?(RecipeCarpentry)this.anvilRecipes.get(Integer.valueOf(id)):null);
   }

   public RecipeCarpentry saveRecipe(RecipeCarpentry recipe) throws IOException {
      RecipeCarpentry current = this.getRecipe(recipe.id);
      if(current != null && !current.name.equals(recipe.name)) {
         while(this.containsRecipeName(recipe.name)) {
            recipe.name = recipe.name + "_";
         }
      }

      if(recipe.id == -1) {
         for(recipe.id = this.getUniqueId(); this.containsRecipeName(recipe.name); recipe.name = recipe.name + "_") {
            ;
         }
      }

      if(recipe.isGlobal) {
         this.anvilRecipes.remove(Integer.valueOf(recipe.id));
         this.globalRecipes.put(Integer.valueOf(recipe.id), recipe);
      } else {
         this.globalRecipes.remove(Integer.valueOf(recipe.id));
         this.anvilRecipes.put(Integer.valueOf(recipe.id), recipe);
      }

      this.saveCategories();
      this.reloadGlobalRecipes();
      return recipe;
   }

   private int getUniqueId() {
      ++this.nextId;
      return this.nextId;
   }

   private boolean containsRecipeName(String name) {
      name = name.toLowerCase();
      Iterator var2 = this.globalRecipes.values().iterator();

      RecipeCarpentry recipe;
      do {
         if(!var2.hasNext()) {
            var2 = this.anvilRecipes.values().iterator();

            do {
               if(!var2.hasNext()) {
                  return false;
               }

               recipe = (RecipeCarpentry)var2.next();
            } while(!recipe.name.toLowerCase().equals(name));

            return true;
         }

         recipe = (RecipeCarpentry)var2.next();
      } while(!recipe.name.toLowerCase().equals(name));

      return true;
   }

   public RecipeCarpentry delete(int id) {
      RecipeCarpentry recipe = this.getRecipe(id);
      if(recipe == null) {
         return null;
      } else {
         this.globalRecipes.remove(Integer.valueOf(recipe.id));
         this.anvilRecipes.remove(Integer.valueOf(recipe.id));
         this.saveCategories();
         this.reloadGlobalRecipes();
         recipe.id = -1;
         return recipe;
      }
   }

   public List getGlobalList() {
      return new ArrayList(this.globalRecipes.values());
   }

   public List getCarpentryList() {
      return new ArrayList(this.anvilRecipes.values());
   }

   public IRecipe addRecipe(String name, boolean global, ItemStack result, Object ... objects) {
      RecipeCarpentry recipe = new RecipeCarpentry(name);
      recipe.isGlobal = global;
      recipe = RecipeCarpentry.createRecipe(recipe, result, objects);

      try {
         return this.saveRecipe(recipe);
      } catch (IOException var7) {
         var7.printStackTrace();
         return recipe;
      }
   }

   public IRecipe addRecipe(String name, boolean global, ItemStack result, int width, int height, ItemStack ... objects) {
      RecipeCarpentry recipe = new RecipeCarpentry(width, height, objects, result);
      recipe.isGlobal = global;
      recipe.name = name;

      try {
         return this.saveRecipe(recipe);
      } catch (IOException var9) {
         var9.printStackTrace();
         return recipe;
      }
   }

}

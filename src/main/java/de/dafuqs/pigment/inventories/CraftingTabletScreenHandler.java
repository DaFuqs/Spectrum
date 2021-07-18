package de.dafuqs.pigment.inventories;

import de.dafuqs.pigment.inventories.slots.ReadOnlySlot;
import de.dafuqs.pigment.inventories.slots.ShadowSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import java.util.Optional;

public class CraftingTabletScreenHandler extends AbstractRecipeScreenHandler<Inventory> {

   private final CraftingTabletInventory craftingInventory;
   private final ScreenHandlerContext context;
   private final PlayerEntity player;
   private final World world;
   private final ItemStack craftingTabletItemStack;

   public CraftingTabletScreenHandler(int syncId, PlayerInventory playerInventory) {
      this(syncId, playerInventory, ScreenHandlerContext.EMPTY, null);
   }

   public CraftingTabletScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ItemStack craftingTabletItemStack) {
      super(PigmentScreenHandlerTypes.CRAFTING_TABLET, syncId);
      this.craftingInventory = new CraftingTabletInventory(this);
      this.context = context;
      this.world = playerInventory.player.getEntityWorld();
      this.craftingTabletItemStack = craftingTabletItemStack;
      this.player = playerInventory.player;

      // crafting slots
      int m;
      int n;
      for(m = 0; m < 3; ++m) {
         for(n = 0; n < 3; ++n) {
            this.addSlot(new ShadowSlot(craftingInventory, n + m * 3, 30 + n * 18, 19 + m * 18));
         }
      }

      // pigment slots
      this.addSlot(new ReadOnlySlot(craftingInventory, 9,  44, 77));
      this.addSlot(new ReadOnlySlot(craftingInventory, 10, 44 + 18, 77));
      this.addSlot(new ReadOnlySlot(craftingInventory, 11, 44 + 2 * 18, 77));
      this.addSlot(new ReadOnlySlot(craftingInventory, 12, 44 + 3 * 18, 77));
      this.addSlot(new ReadOnlySlot(craftingInventory, 13, 44 + 4 * 18, 77));

      // preview slot
      this.addSlot(new ReadOnlySlot(craftingInventory, 14, 127, 37));

      // player inventory
      int l;
      for(l = 0; l < 3; ++l) {
         for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, 112 + l * 18));
         }
      }

      // player hotbar
      for(l = 0; l < 9; ++l) {
         this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 170));
      }

   }

   protected void updateResult(ScreenHandler handler, World world, PlayerEntity player, CraftingTabletInventory inventory) {
      if (!world.isClient) {
         Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, inventory, world);
         if (optional.isPresent()) {
            CraftingRecipe craftingRecipe = optional.get();

            inventory.setStack(14, craftingRecipe.getOutput().copy());

            NbtCompound nbtCompound = craftingTabletItemStack.getOrCreateTag();
            nbtCompound.putString("recipe", craftingRecipe.getId().toString());
            craftingTabletItemStack.setTag(nbtCompound);
         } else {
            if(!inventory.getStack(14).isEmpty()) {
               inventory.setStack(14, ItemStack.EMPTY);
            }

            NbtCompound nbtCompound = craftingTabletItemStack.getOrCreateTag();
            if(nbtCompound.contains("recipe")) {
               nbtCompound.remove("recipe");
               craftingTabletItemStack.setTag(nbtCompound);
            }
         }
      }
   }

   public void onContentChanged(Inventory inventory) {
      this.context.run((world, pos) -> {
         updateResult(this, world, this.player, this.craftingInventory);
      });
   }

   public void populateRecipeFinder(RecipeMatcher recipeMatcher) {
      if (this.craftingInventory != null) {
         this.craftingInventory.provideRecipeInputs(recipeMatcher);
      }
   }

   public void clearCraftingSlots() {
      this.craftingInventory.clear();
      //this.result.clear();
   }

   public boolean matches(Recipe<? super Inventory> recipe) {
      return recipe.matches(this.craftingInventory, this.world);
   }

   public void close(PlayerEntity player) {
      super.close(player);
   }

   public boolean canUse(PlayerEntity player) {
      return true;
   }

   public ItemStack transferSlot(PlayerEntity player, int index) {
      ItemStack itemStack = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot.hasStack()) {
         ItemStack itemStack2 = slot.getStack();
         itemStack = itemStack2.copy();
         if (index == 0) {
            this.context.run((world, pos) -> {
               itemStack2.getItem().onCraft(itemStack2, world, player);
            });
            if (!this.insertItem(itemStack2, 10, 46, true)) {
               return ItemStack.EMPTY;
            }

            slot.onQuickTransfer(itemStack2, itemStack);
         } else if (index >= 10 && index < 46) {
            if (!this.insertItem(itemStack2, 1, 10, false)) {
               if (index < 37) {
                  if (!this.insertItem(itemStack2, 37, 46, false)) {
                     return ItemStack.EMPTY;
                  }
               } else if (!this.insertItem(itemStack2, 10, 37, false)) {
                  return ItemStack.EMPTY;
               }
            }
         } else if (!this.insertItem(itemStack2, 10, 46, false)) {
            return ItemStack.EMPTY;
         }

         if (itemStack2.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
         } else {
            slot.markDirty();
         }

         if (itemStack2.getCount() == itemStack.getCount()) {
            return ItemStack.EMPTY;
         }

         slot.onTakeItem(player, itemStack2);
         if (index == 0) {
            player.dropItem(itemStack2, false);
         }
      }

      return itemStack;
   }

   public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
      return super.canInsertIntoSlot(stack, slot);
   }

   public int getCraftingResultSlotIndex() {
      return 14;
   }

   public int getCraftingWidth() {
      return 3;
   }

   public int getCraftingHeight() {
      return 3;
   }

   public int getCraftingSlotCount() {
      return 9;
   }

   @Override
   public RecipeBookCategory getCategory() {
      return RecipeBookCategory.CRAFTING;
   }

   public boolean canInsertIntoSlot(int index) {
      return index != this.getCraftingResultSlotIndex();
   }
}

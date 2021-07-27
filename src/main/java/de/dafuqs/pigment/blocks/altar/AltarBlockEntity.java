package de.dafuqs.pigment.blocks.altar;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.Support;
import de.dafuqs.pigment.enums.PigmentColor;
import de.dafuqs.pigment.interfaces.PlayerOwned;
import de.dafuqs.pigment.inventories.AltarScreenHandler;
import de.dafuqs.pigment.inventories.AutoCraftingInventory;
import de.dafuqs.pigment.items.misc.CraftingTabletItem;
import de.dafuqs.pigment.recipe.PigmentRecipeTypes;
import de.dafuqs.pigment.recipe.altar.AltarCraftingRecipe;
import de.dafuqs.pigment.registries.PigmentBlockEntityRegistry;
import de.dafuqs.pigment.registries.PigmentBlocks;
import de.dafuqs.pigment.registries.PigmentItems;
import de.dafuqs.pigment.sound.PigmentSoundEvents;
import net.minecraft.advancement.Advancement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.*;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

public class AltarBlockEntity extends LockableContainerBlockEntity implements RecipeInputProvider, SidedInventory, PlayerOwned {

    private UUID ownerUUID;
    private String ownerName;

    protected DefaultedList<ItemStack> inventory;

    private float storedXP;
    private int craftingTime;
    private int craftingTimeTotal;

    protected final PropertyDelegate propertyDelegate;
    private static final RecipeType<? extends AltarCraftingRecipe> recipeType = PigmentRecipeTypes.ALTAR;
    private Object lastRecipe;

    private static AutoCraftingInventory autoCraftingInventory;

    public static final int INVENTORY_SIZE = 17; // 9 crafting, 5 gems, 1 craftingTablet, 1 preview, 1 output
    public static final int CRAFTING_TABLET_SLOT_ID = 14;
    public static final int PREVIEW_SLOT_ID = 15;
    public static final int OUTPUT_SLOT_ID = 16;

    public AltarBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PigmentBlockEntityRegistry.ALTAR, blockPos, blockState);

        if(autoCraftingInventory == null) {
            autoCraftingInventory = new AutoCraftingInventory(3, 3);
        }

        this.inventory  = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> AltarBlockEntity.this.craftingTime;
                    case 1 -> AltarBlockEntity.this.craftingTimeTotal;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AltarBlockEntity.this.craftingTime = value;
                    case 1 -> AltarBlockEntity.this.craftingTimeTotal = value;
                }
            }

            public int size() {
                return 2;
            }
        };
    }

    @Override
    public Text getContainerName() {
        return new TranslatableText("block.pigment.altar");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new AltarScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = this.inventory.get(slot);
        boolean isSimilarItem = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areTagsEqual(stack, itemStack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        if (slot < CRAFTING_TABLET_SLOT_ID && !isSimilarItem) {
            this.craftingTimeTotal = getCraftingTime(this.world, recipeType, this);
            this.craftingTime = 0;
            this.markDirty();
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher recipeMatcher) {
        for (ItemStack itemStack : this.inventory) {
            recipeMatcher.addInput(itemStack);
        }
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
        Inventories.readNbt(tag, this.inventory);
        this.storedXP = tag.getFloat("StoredXP");
        this.craftingTime = tag.getShort("CraftingTime");
        this.craftingTimeTotal = tag.getShort("CraftingTimeTotal");
        if(tag.contains("OwnerUUID")) {
            this.ownerUUID = tag.getUuid("OwnerUUID");
        } else {
            this.ownerUUID = null;
        }
        if(tag.contains("OwnerName")) {
            this.ownerName = tag.getString("OwnerName");
        } else {
            this.ownerName = "???";
        }
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putFloat("StoredXP", this.storedXP);
        tag.putShort("CraftingTime", (short)this.craftingTime);
        tag.putShort("CraftingTimeTotal", (short)this.craftingTimeTotal);

        if(this.ownerUUID != null) {
            tag.putUuid("OwnerUUID", this.ownerUUID);
        }
        if(this.ownerName != null) {
            tag.putString("OwnerName", this.ownerName);
        }
        Inventories.writeNbt(tag, this.inventory);
        return tag;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    private boolean isCrafting() {
        return this.craftingTime > 0;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, AltarBlockEntity altarBlockEntity) {
        // check recipe crafted last tick => performance
        AltarCraftingRecipe altarCraftingRecipe = null;
        CraftingRecipe craftingRecipe = null;
        boolean shouldMarkDirty = false;

        if (altarBlockEntity.lastRecipe instanceof AltarCraftingRecipe && ((AltarCraftingRecipe) altarBlockEntity.lastRecipe).matches(altarBlockEntity, world)) {
            altarCraftingRecipe = (AltarCraftingRecipe) altarBlockEntity.lastRecipe;
        } else {
            autoCraftingInventory.setInputInventory(altarBlockEntity.inventory.subList(0, 9));
            if(altarBlockEntity.lastRecipe instanceof CraftingRecipe && ((CraftingRecipe) altarBlockEntity.lastRecipe).matches(autoCraftingInventory, world)) {
                craftingRecipe = (CraftingRecipe) altarBlockEntity.lastRecipe;
            } else {
                // current inventory does not match last recipe
                // => search valid recipe
                altarBlockEntity.craftingTime = 0;

                altarCraftingRecipe = world.getRecipeManager().getFirstMatch(recipeType, altarBlockEntity, world).orElse(null);
                if (altarCraftingRecipe != null) {
                    if (altarCraftingRecipe.canCraft(altarBlockEntity.getOwnerUUID())) {
                        altarBlockEntity.lastRecipe = altarCraftingRecipe;
                        altarBlockEntity.craftingTimeTotal = altarCraftingRecipe.getCraftingTime();
                        altarBlockEntity.inventory.set(PREVIEW_SLOT_ID, altarCraftingRecipe.getOutput().copy());
                        shouldMarkDirty = true;
                    } else {
                        altarBlockEntity.lastRecipe = null;
                        if(!altarBlockEntity.inventory.get(PREVIEW_SLOT_ID).isEmpty()) {
                            altarBlockEntity.inventory.set(PREVIEW_SLOT_ID, ItemStack.EMPTY);
                            shouldMarkDirty = true;
                        }
                    }
                } else {
                    craftingRecipe = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, autoCraftingInventory, world).orElse(null);
                    if (craftingRecipe != null) {
                        altarBlockEntity.lastRecipe = craftingRecipe;
                        altarBlockEntity.craftingTimeTotal = 20;
                        altarBlockEntity.inventory.set(PREVIEW_SLOT_ID, craftingRecipe.getOutput().copy());
                        shouldMarkDirty = true;
                    } else {
                        altarBlockEntity.lastRecipe = null;
                        if(!altarBlockEntity.inventory.get(PREVIEW_SLOT_ID).isEmpty()) {
                            altarBlockEntity.inventory.set(PREVIEW_SLOT_ID, ItemStack.EMPTY);
                            shouldMarkDirty = true;
                        }
                    }
                }
            }
        }

        // only craft when there is redstone power
        Block block = world.getBlockState(blockPos).getBlock();
        if(block.equals(PigmentBlocks.ALTAR) && ((AltarBlock)block).isGettingPowered(world, blockPos)) {

            int maxCountPerStack = altarBlockEntity.getMaxCountPerStack();
            boolean crafting = altarBlockEntity.isCrafting();

            // Altar crafting
            boolean craftingFinished = false;
            if (canAcceptRecipeOutput(altarCraftingRecipe, altarBlockEntity.inventory, maxCountPerStack)) {
                altarBlockEntity.craftingTime++;
                if (altarBlockEntity.craftingTime == altarBlockEntity.craftingTimeTotal) {
                    altarBlockEntity.craftingTime = 0;
                    craftingFinished = altarBlockEntity.craftAltarRecipe(altarCraftingRecipe, altarBlockEntity.inventory, maxCountPerStack);
                    shouldMarkDirty = true;
                }
            // Vanilla crafting
            } else if(canAcceptRecipeOutput(craftingRecipe, altarBlockEntity.inventory, maxCountPerStack)) {
                altarBlockEntity.craftingTime++;
                if (altarBlockEntity.craftingTime == altarBlockEntity.craftingTimeTotal) {
                    altarBlockEntity.craftingTime = 0;
                    craftingFinished = altarBlockEntity.craftVanillaRecipe(craftingRecipe, altarBlockEntity.inventory, maxCountPerStack);
                    shouldMarkDirty = true;
                }
            // No crafting
            } else {
                altarBlockEntity.craftingTime = 0;
            }

            if (crafting != altarBlockEntity.isCrafting()) {
                shouldMarkDirty = true;
            }

            // spawn output item stack in world
            ItemStack outputItemStack = altarBlockEntity.inventory.get(OUTPUT_SLOT_ID);
            if (outputItemStack != ItemStack.EMPTY) {
                if (world.getBlockState(blockPos.up()).isAir()) {
                    // spawn crafting output
                    ItemEntity itemEntity = new ItemEntity(world, altarBlockEntity.pos.getX() + 0.5, altarBlockEntity.pos.getY() + 1, altarBlockEntity.pos.getZ() + 0.5, outputItemStack);
                    itemEntity.addVelocity(0, 0.1, 0);
                    world.spawnEntity(itemEntity);
                    altarBlockEntity.inventory.set(OUTPUT_SLOT_ID, ItemStack.EMPTY);

                    // spawn XP
                    if(altarBlockEntity.storedXP > 0) {
                        int spawnedXPAmount = Support.getWholeIntFromFloatWithChance(altarBlockEntity.storedXP, altarBlockEntity.getWorld().random);
                        ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, altarBlockEntity.pos.getX() + 0.5, altarBlockEntity.pos.getY() + 1, altarBlockEntity.pos.getZ() + 0.5, spawnedXPAmount);
                        world.spawnEntity(experienceOrbEntity);
                        altarBlockEntity.storedXP = 0;
                    }

                    altarBlockEntity.spawnCraftingFinishedParticles(world, blockPos);

                    if(craftingRecipe != null) {
                        altarBlockEntity.playSound(PigmentSoundEvents.ALTAR_CRAFT_GENERIC);
                    } else if(altarCraftingRecipe != null) {
                        altarBlockEntity.playSound(altarCraftingRecipe.getSoundEvent(world.random));
                    }
                } else {
                    if(craftingFinished) {
                        altarBlockEntity.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH);
                    }
                }
            }

            if (shouldMarkDirty) {
                markDirty(world, blockPos, blockState);
            }
        }
    }

    // TODO: doesn't spawn on client?
    public void spawnCraftingFinishedParticles(World world, BlockPos pos) {
        world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, PigmentBlocks.ALTAR.getDefaultState()), pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 0, 0.5D, 0);
    }

    private void playSound(SoundEvent soundEvent) {
        Random random = world.random;
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.BLOCKS, 0.9F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F);
    }

    private static int getCraftingTime(World world, RecipeType<? extends AltarCraftingRecipe> recipeType, Inventory inventory) {
        return world.getRecipeManager().getFirstMatch(recipeType, inventory, world).map(AltarCraftingRecipe::getCraftingTime).orElse(20);
    }

    private static boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> defaultedList, int maxCountPerStack) {
        if (recipe != null) {
            ItemStack output = recipe.getOutput();
            if (output.isEmpty()) {
                return false;
            } else {
                ItemStack existingOutput = defaultedList.get(OUTPUT_SLOT_ID);
                if (existingOutput.isEmpty()) {
                    return true;
                } else if (!existingOutput.isItemEqualIgnoreDamage(output)) {
                    return false;
                } else if (existingOutput.getCount() < maxCountPerStack && existingOutput.getCount() < existingOutput.getMaxCount()) {
                    return true;
                } else {
                    return existingOutput.getCount() < output.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }

    private boolean craftAltarRecipe(@Nullable AltarCraftingRecipe recipe, DefaultedList<ItemStack> defaultedList, int maxCountPerStack) {
        if (canAcceptRecipeOutput(recipe, defaultedList, maxCountPerStack)) {

            // -1 for all crafting inputs
            for(int i = 0; i < 9; i++) {
                ItemStack itemStack = defaultedList.get(i);
                if(!itemStack.isEmpty()) {
                    Item recipeReminderItem = itemStack.getItem().getRecipeRemainder();
                    if(recipeReminderItem == null) {
                        itemStack.decrement(1);
                    } else {
                        inventory.set(i, new ItemStack(recipeReminderItem, 1));
                    }
                }
            }

            // -X for all the pigment inputs
            for(PigmentColor pigmentColor : PigmentColor.values()) {
                int pigmentAmount = recipe.getPigmentColor(pigmentColor);
                inventory.get(getSlotForPigmentColor(pigmentColor)).decrement(pigmentAmount);
            }

            ItemStack recipeOutput = recipe.getOutput();
            ItemStack existingOutput = defaultedList.get(OUTPUT_SLOT_ID);
            if (existingOutput.isEmpty()) {
                defaultedList.set(OUTPUT_SLOT_ID, recipeOutput.copy());
            } else {
                existingOutput.increment(recipeOutput.getCount());
                defaultedList.set(OUTPUT_SLOT_ID, existingOutput);
            }

            // Add recipe XP
            storedXP = recipe.getExperience();

            // if the recipe unlocks an advancement unlock it
            grantPlayerCraftingAdvancement(recipe);

            return true;
        } else {
            return false;
        }
    }

    private boolean craftVanillaRecipe(@Nullable CraftingRecipe recipe, DefaultedList<ItemStack> defaultedList, int maxCountPerStack) {
        if (canAcceptRecipeOutput(recipe, defaultedList, maxCountPerStack)) {

            // -1 for all crafting inputs
            for(int i = 0; i < 9; i++) {
                ItemStack itemStack = defaultedList.get(i);
                if(!itemStack.isEmpty()) {
                    Item recipeReminderItem = itemStack.getItem().getRecipeRemainder();
                    if(recipeReminderItem == null) {
                        itemStack.decrement(1);
                    } else {
                        inventory.set(i, new ItemStack(recipeReminderItem, 1));
                    }
                }
            }

            ItemStack recipeOutput = recipe.getOutput();
            ItemStack existingOutput = defaultedList.get(OUTPUT_SLOT_ID);
            if (existingOutput.isEmpty()) {
                defaultedList.set(OUTPUT_SLOT_ID, recipeOutput.copy());
            } else {
                existingOutput.increment(recipeOutput.getCount());
                defaultedList.set(OUTPUT_SLOT_ID, existingOutput);
            }

            return true;
        } else {
            return false;
        }
    }

    private void grantPlayerCraftingAdvancement(AltarCraftingRecipe recipe) {
        ServerPlayerEntity serverPlayerEntity = PigmentCommon.minecraftServer.getPlayerManager().getPlayer(this.ownerUUID);

        // General altar crafting advancement
        Advancement craftingAdvancement = PigmentCommon.minecraftServer.getAdvancementLoader().get(new Identifier(PigmentCommon.MOD_ID, "craft_using_altar"));

        if(serverPlayerEntity != null) {
            if(craftingAdvancement != null) {
                serverPlayerEntity.getAdvancementTracker().grantCriterion(craftingAdvancement, "craft");

                // Advancement specific for the crafted item
                if(recipe.unlocksAdvancement() && !Support.hasAdvancement(serverPlayerEntity, recipe.unlockedAdvancement())) {
                    Advancement itemAdvancement = PigmentCommon.minecraftServer.getAdvancementLoader().get(recipe.unlockedAdvancement());
                    if (itemAdvancement != null) {
                        serverPlayerEntity.getAdvancementTracker().grantCriterion(itemAdvancement, "craft");
                    }
                }
            }
        }


    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if(slot < 9) {
            return true;
        } else if(slot == CRAFTING_TABLET_SLOT_ID && stack.isOf(PigmentItems.CRAFTING_TABLET)) {
            return true;
        } else {
            return stack.getItem().equals(getPigmentItemForSlot(slot));
        }
    }

    public static Item getPigmentItemForSlot(int slot) {
        return switch (slot) {
            case 9 -> PigmentItems.AMETHYST_POWDER;
            case 10 -> PigmentItems.CITRINE_POWDER;
            case 11 -> PigmentItems.TOPAZ_POWDER;
            case 12 -> PigmentItems.ONYX_POWDER;
            case 13 -> PigmentItems.MOONSTONE_POWDER;
            default -> Items.AIR;
        };
    }

    public static int getSlotForPigmentColor(PigmentColor pigmentColor) {
        return switch (pigmentColor) {
            case MAGENTA -> 9;
            case YELLOW -> 10;
            case CYAN -> 11;
            case BLACK -> 12;
            default -> 13; // WHITE
        };
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if(side == Direction.DOWN) {
            return new int[]{OUTPUT_SLOT_ID};
        } else if(side == Direction.UP) {
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        } else {
            return new int[]{9, 10, 11, 12, 13};
        }
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        if(stack.isOf(getPigmentItemForSlot(slot))) {
            return true;
        }

        if(slot < 9 && inventory.get(CRAFTING_TABLET_SLOT_ID).isOf(PigmentItems.CRAFTING_TABLET)) {
            ItemStack craftingTabletItem = inventory.get(CRAFTING_TABLET_SLOT_ID);

            if(inventory.get(slot).getCount() > 0) {
                return false;
            }

            Recipe storedRecipe = CraftingTabletItem.getStoredRecipe(this.world, craftingTabletItem);

            int width = 3;
            if(storedRecipe instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe) storedRecipe;
                width = shapedRecipe.getWidth();
                if(slot % 3 >= width) {
                    return false;
                }
            } else if(storedRecipe instanceof AltarCraftingRecipe) {
                AltarCraftingRecipe altarCraftingRecipe = (AltarCraftingRecipe) storedRecipe;
                width = altarCraftingRecipe.getWidth();
                if(slot % 3 >= width) {
                    return false;
                }
            } else if(storedRecipe instanceof ShapelessRecipe) {
                // just put it in already
            } else {
                return false;
            }

            int resultRecipeSlot = getCraftingRecipeSlotDependingOnWidth(slot, width, 3);
            if(resultRecipeSlot < storedRecipe.getIngredients().size()) {
                Ingredient ingredient = (Ingredient) storedRecipe.getIngredients().get(resultRecipeSlot);
                return ingredient.test(stack);
            } else {
                return false;
            }
        } else {
            return slot < CRAFTING_TABLET_SLOT_ID;
        }
    }

    private int getCraftingRecipeSlotDependingOnWidth(int slot, int recipeWidth, int gridWidth) {
        int line = slot / gridWidth;
        int posInLine = slot % gridWidth;
        int result = line * recipeWidth + posInLine;
        return result;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == OUTPUT_SLOT_ID;
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public String getOwnerName() {
        return this.ownerName;
    }

    @Override
    public void setOwner(PlayerEntity playerEntity) {
        this.ownerUUID = playerEntity.getUuid();
        this.ownerName = playerEntity.getName().asString();
        setCustomName(new TranslatableText("block.pigment.altar.title_with_owner", ownerName));
    }

}

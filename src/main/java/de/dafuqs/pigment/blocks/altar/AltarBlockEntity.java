package de.dafuqs.pigment.blocks.altar;

import de.dafuqs.pigment.PigmentBlockEntityType;
import de.dafuqs.pigment.PigmentBlocks;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.enums.PigmentColor;
import de.dafuqs.pigment.interfaces.PlayerOwned;
import de.dafuqs.pigment.inventories.AltarScreenHandler;
import de.dafuqs.pigment.PigmentItems;
import de.dafuqs.pigment.recipe.PigmentRecipeTypes;
import de.dafuqs.pigment.recipe.altar.AltarCraftingRecipe;
import net.minecraft.advancement.Advancement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
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

    private UUID playerUUID;
    protected DefaultedList<ItemStack> inventory;

    private int craftingTime;
    private int craftingTimeTotal;

    protected final PropertyDelegate propertyDelegate;
    private static final RecipeType<? extends AltarCraftingRecipe> recipeType = PigmentRecipeTypes.ALTAR;
    private AltarCraftingRecipe lastRecipe;

    public AltarBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PigmentBlockEntityType.ALTAR_BLOCK_ENTITY_TYPE, blockPos, blockState);

        this.inventory = DefaultedList.ofSize(9+5+1+1, ItemStack.EMPTY); // 9 crafting, 5 gems, 1 preview, 1 output

        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch(index) {
                    case 0:
                        return AltarBlockEntity.this.craftingTime;
                    case 1:
                        return AltarBlockEntity.this.craftingTimeTotal;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0:
                        AltarBlockEntity.this.craftingTime = value;
                        break;
                    case 1:
                        AltarBlockEntity.this.craftingTimeTotal = value;
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

        if (slot < 15 && !isSimilarItem) {
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
    public void provideRecipeInputs(RecipeFinder finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.addItem(itemStack);
        }
    }

    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.inventory = DefaultedList.ofSize(9+5+1+1, ItemStack.EMPTY);
        Inventories.fromTag(tag, this.inventory);
        this.craftingTime = tag.getShort("CraftingTime");
        this.craftingTimeTotal = tag.getShort("CraftingTimeTotal");
        if(tag.contains("UUID")) {
            this.playerUUID = tag.getUuid("UUID");
        } else {
            this.playerUUID = null;
        }
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putShort("CraftingTime", (short)this.craftingTime);
        tag.putShort("CraftingTimeTotal", (short)this.craftingTimeTotal);
        tag.putUuid("UUID", this.playerUUID);
        Inventories.toTag(tag, this.inventory);
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
        AltarCraftingRecipe recipe;
        if (altarBlockEntity.lastRecipe != null && altarBlockEntity.lastRecipe.matches(altarBlockEntity, world)) {
            recipe = altarBlockEntity.lastRecipe;
        } else {
            recipe = world.getRecipeManager().getFirstMatch(recipeType, altarBlockEntity, world).orElse(null);
            altarBlockEntity.lastRecipe = recipe;
            altarBlockEntity.craftingTime = 0;
            if (recipe != null) {
                altarBlockEntity.craftingTimeTotal = recipe.getCraftingTime();
                altarBlockEntity.inventory.set(14, recipe.getOutput());
            } else {
                altarBlockEntity.inventory.set(14, ItemStack.EMPTY);
            }
        }

        // only craft when there is redstone power
        Block block = world.getBlockState(blockPos).getBlock();
        if(block.equals(PigmentBlocks.ALTAR) && ((AltarBlock)block).isGettingPowered(world, blockPos)) {

            int maxCountPerStack = altarBlockEntity.getMaxCountPerStack();
            boolean crafting = altarBlockEntity.isCrafting();
            boolean shouldMarkDirty = false;

            if (canAcceptRecipeOutput(recipe, altarBlockEntity.inventory, maxCountPerStack)) {
                altarBlockEntity.craftingTime++;
                if (altarBlockEntity.craftingTime == altarBlockEntity.craftingTimeTotal) {
                    altarBlockEntity.craftingTime = 0;
                    altarBlockEntity.craftRecipe(recipe, altarBlockEntity.inventory, maxCountPerStack);
                    shouldMarkDirty = true;
                }
            } else {
                altarBlockEntity.craftingTime = 0;
            }

            if (crafting != altarBlockEntity.isCrafting()) {
                shouldMarkDirty = true;
            }

            // spawn output item stack in world
            ItemStack outputItemStack = altarBlockEntity.inventory.get(15);
            if (outputItemStack != ItemStack.EMPTY) {
                if (world.getBlockState(blockPos.up()).isAir()) {
                    ItemEntity itemEntity = new ItemEntity(world, altarBlockEntity.pos.getX() + 0.5, altarBlockEntity.pos.getY() + 1, altarBlockEntity.pos.getZ() + 0.5, outputItemStack);
                    itemEntity.addVelocity(0, 0.1, 0);
                    world.spawnEntity(itemEntity);
                    altarBlockEntity.inventory.set(15, ItemStack.EMPTY);
                    altarBlockEntity.spawnCraftingFinishedParticles(world, blockPos);
                    altarBlockEntity.playSound(SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL); // TODO: custom sound
                } else {
                    altarBlockEntity.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH); // TODO: custom sound
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
                ItemStack existingOutput = defaultedList.get(15);
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

    private boolean craftRecipe(@Nullable AltarCraftingRecipe recipe, DefaultedList<ItemStack> defaultedList, int maxCountPerStack) {
        if (canAcceptRecipeOutput(recipe, defaultedList, maxCountPerStack)) {

            // -1 for all crafting inputs
            for(int i = 0; i < 9; i++) {
                ItemStack itemStack = defaultedList.get(i);
                itemStack.decrement(1);
            }

            // -X for all the pigment inputs
            for(PigmentColor pigmentColor : PigmentColor.values()) {
                int pigmentAmount = recipe.getPigmentColor(pigmentColor);
                inventory.get(getSlotForPigmentColor(pigmentColor)).decrement(pigmentAmount);
            }

            ItemStack output = recipe.getOutput();

            ItemStack existingOutput = defaultedList.get(15);
            if (existingOutput.isEmpty()) {
                defaultedList.set(15, output.copy());
            } else if (existingOutput.isOf(output.getItem())) {
                existingOutput.increment(output.getCount());
            }

            grantPlayerCraftingAdvancement();

            return true;
        } else {
            return false;
        }
    }

    private void grantPlayerCraftingAdvancement() {
        Advancement advancement = PigmentCommon.minecraftServer.getAdvancementLoader().get(new Identifier(PigmentCommon.MOD_ID, "craft_using_altar"));
        ServerPlayerEntity serverPlayerEntity = PigmentCommon.minecraftServer.getPlayerManager().getPlayer(this.playerUUID);

        if(advancement != null && serverPlayerEntity != null) {
            serverPlayerEntity.getAdvancementTracker().grantCriterion(advancement, "craft");
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if(slot < 9) {
            return true;
        }

        return stack.getItem().equals(getPigmentItemForSlot(slot));
    }

    public static Item getPigmentItemForSlot(int slot) {
        switch (slot) {
            case 9:
                return PigmentItems.MAGENTA_PIGMENT;
            case 10:
                return PigmentItems.YELLOW_PIGMENT;
            case 11:
                return PigmentItems.CYAN_PIGMENT;
            case 12:
                return PigmentItems.BLACK_PIGMENT;
            case 13:
                return PigmentItems.WHITE_PIGMENT;
            default:
                return Items.AIR;
        }
    }

    public static int getSlotForPigmentColor(PigmentColor pigmentColor) {
        switch (pigmentColor) {
            case MAGENTA:
                return 9;
            case YELLOW:
                return 10;
            case CYAN:
                return 11;
            case BLACK:
                return 12;
            default: // WHITE
                return 13;
        }
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if(side == Direction.DOWN) {
            return new int[]{15};
        } else if(side == Direction.UP) {
            return new int[]{9, 10, 11, 12, 13};
        } else {
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        }
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        if(slot < 14) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 15;
    }

    @Override
    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Override
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public void setPlayerData(UUID uuid, Text name) {
        this.setPlayerUUID(uuid);
        setCustomName(new TranslatableText("block.pigment.altar.title_with_owner", name.asString()));
    }
}

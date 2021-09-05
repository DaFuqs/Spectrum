package de.dafuqs.spectrum.blocks.fusion_shrine;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FusionShrineBlockEntity extends BlockEntity implements RecipeInputProvider, PlayerOwned {

    private UUID ownerUUID;
    private String ownerName;

    protected int INVENTORY_SIZE = 8;
    protected SimpleInventory inventory;
    protected @NotNull Fluid storedFluid;
    private FusionShrineRecipe cachedRecipe;

    public FusionShrineBlockEntity(BlockPos pos, BlockState state) {
        super(SpectrumBlockEntityRegistry.FUSION_SHRINE, pos, state);
        this.inventory = new SimpleInventory(INVENTORY_SIZE);
        storedFluid = Fluids.EMPTY;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = new SimpleInventory(INVENTORY_SIZE);
        this.inventory.readNbtList(nbt.getList("inventory", 10));
        this.storedFluid = Registry.FLUID.get(Identifier.tryParse(nbt.getString("fluid")));
        if(nbt.contains("OwnerUUID")) {
            this.ownerUUID = nbt.getUuid("OwnerUUID");
        } else {
            this.ownerUUID = null;
        }
        if(nbt.contains("OwnerName")) {
            this.ownerName = nbt.getString("OwnerName");
        } else {
            this.ownerName = "???";
        }
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("inventory", this.inventory.toNbtList());
        nbt.putString("fluid", Registry.FLUID.getId(this.storedFluid).toString());
        if(this.ownerUUID != null) {
            nbt.putUuid("OwnerUUID", this.ownerUUID);
        }
        if(this.ownerName != null) {
            nbt.putString("OwnerName", this.ownerName);
        }
        return nbt;
    }

    public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, FusionShrineBlockEntity fusionShrineBlockEntity) {
        FusionShrineRecipe recipe = getCurrentRecipe(world, fusionShrineBlockEntity);
        if(recipe != null) {
            if(recipe.getFluidInput().equals(fusionShrineBlockEntity.storedFluid)
                    && world.getBlockState(blockPos.up()).isAir()
                    && world.isSkyVisible(blockPos)
                    && recipe.areConditionMetCurrently((ServerWorld) world)) {
                craft(world, blockPos, fusionShrineBlockEntity, recipe);
            }
        }
    }

    private static FusionShrineRecipe getCurrentRecipe(@NotNull World world, FusionShrineBlockEntity fusionShrineBlockEntity) {
        if(fusionShrineBlockEntity.cachedRecipe != null) {
            if(fusionShrineBlockEntity.cachedRecipe.matches(fusionShrineBlockEntity.inventory, world)) {
                return fusionShrineBlockEntity.cachedRecipe;
            }
        }

        FusionShrineRecipe recipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.FUSION_SHRINE, fusionShrineBlockEntity.inventory, world).orElse(null);
        fusionShrineBlockEntity.cachedRecipe = recipe;
        return recipe;
    }

    private static void craft(World world, BlockPos blockPos, FusionShrineBlockEntity fusionShrineBlockEntity, FusionShrineRecipe recipe) {
        // calculate the max amount of items that will be crafted
        // note that we only check each ingredient once, if a match was found
        // custom recipes therefore should not use items / tags that match multiple items
        // at once, since we can not rely on positions in a grid like vanilla does
        // in it's crafting table
        int maxAmount = recipe.getOutput().getMaxCount();
        for(Ingredient ingredient : recipe.getIngredients()) {
            for(int i = 0; i < fusionShrineBlockEntity.INVENTORY_SIZE; i++) {
                ItemStack currentStack = fusionShrineBlockEntity.inventory.getStack(i);
                if(ingredient.test(currentStack)) {
                    maxAmount = Math.min(maxAmount, currentStack.getCount());
                    break;
                }
            }
        }

        if(maxAmount > 0) {
            for(Ingredient ingredient : recipe.getIngredients()) {
                for(int i = 0; i < fusionShrineBlockEntity.INVENTORY_SIZE; i++) {
                    ItemStack currentStack = fusionShrineBlockEntity.inventory.getStack(i);
                    if(ingredient.test(currentStack)) {
                        if(currentStack.getCount() - maxAmount < 1) {
                            fusionShrineBlockEntity.inventory.setStack(i, ItemStack.EMPTY);
                        } else {
                            currentStack.decrement(maxAmount);
                        }
                        break;
                    }
                }
            }

            fusionShrineBlockEntity.setFluid(Fluids.EMPTY); // empty the shrine
            scatterContents(world, blockPos.up(), fusionShrineBlockEntity); // drop remaining items
            spawnCraftingResultAndXP(world, fusionShrineBlockEntity, recipe, maxAmount); // spawn results

            //TODO
            //doEffects();
            //spawnParticles()
            // only triggered on server side. Therefore, has to be sent to client via S2C packet
            // SpectrumS2CPackets.sendPlayPedestalCraftingFinishedParticle(world, blockPos, outputItemStack);
            //takeTime()
            //playCraftingFinishedSoundEffect();
            fusionShrineBlockEntity.grantPlayerFusionCraftingAdvancement(recipe);
        }
    }

    private void grantPlayerFusionCraftingAdvancement(FusionShrineRecipe recipe) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(this.world, this.ownerUUID);
        if(serverPlayerEntity != null) {
            SpectrumAdvancementCriteria.FUSION_SHRINE_CRAFTING.trigger(serverPlayerEntity, recipe.getOutput());
        }
    }

    public static void spawnCraftingResultAndXP(World world, FusionShrineBlockEntity blockEntity, FusionShrineRecipe recipe, int amount) {
        int resultAmount = amount * recipe.getOutput().getCount();
        while(resultAmount > 0) {
            int currentAmount = Math.min(amount, recipe.getOutput().getMaxCount());

            ItemEntity itemEntity = new ItemEntity(world, blockEntity.pos.getX() + 0.5, blockEntity.pos.getY() + 1, blockEntity.pos.getZ() + 0.5, new ItemStack(recipe.getOutput().getItem(), currentAmount));
            itemEntity.setVelocity(0, 0.2, 0);
            world.spawnEntity(itemEntity);

            resultAmount -= currentAmount;
        }

        if (recipe.getExperience() > 0) {
            int spawnedXPAmount = Support.getWholeIntFromFloatWithChance(recipe.getExperience(), blockEntity.world.random);
            ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, blockEntity.pos.getX() + 0.5, blockEntity.pos.getY() + 1, blockEntity.pos.getZ() + 0.5, spawnedXPAmount);
            world.spawnEntity(experienceOrbEntity);
        }
    }

    public static void scatterContents(World world, BlockPos pos, FusionShrineBlockEntity blockEntity) {
        ItemScatterer.spawn(world, pos, blockEntity.inventory);
        world.updateComparators(pos, world.getBlockState(pos).getBlock());
    }

    public @NotNull Fluid getFluid() {
        return this.storedFluid;
    }

    public void setFluid(@NotNull Fluid fluid) {
        this.storedFluid = fluid;
        setLightForFluid(world, pos, fluid);
        this.markDirty();
        updateInClientWorld();
    }

    public void updateInClientWorld() {
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NO_REDRAW);
        world.addSyncedBlockEvent(pos, SpectrumBlocks.FUSION_SHRINE, 1, 0);
    }

    public void setLightForFluid(World world, BlockPos blockPos, Fluid fluid) {
        if(SpectrumCommon.fluidLuminance.containsKey(fluid)) {
            int light = SpectrumCommon.fluidLuminance.get(fluid);
            world.setBlockState(blockPos, world.getBlockState(blockPos).with(FusionShrineBlock.LIGHT_LEVEL, light), 3);
        }
    }

    // Called when the chunk is first loaded to initialize this be
    public NbtCompound toInitialChunkDataNbt() {
        return this.writeNbt(new NbtCompound());
    }

    // when marked dirty this is called to send updates to clients
    // see also MobSpawnerBlockEntity for a vanilla version of this
    @Nullable
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        SpectrumS2CPackets.sendBlockEntityUpdate(this, SpectrumS2CPackets.BlockEntityUpdatePacketID.FUSION_SHRINE);
        return null;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    // RECIPE INPUT PROVIDER

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {

    }

    // PLAYER OWNED
    // "owned" is not to be taken literally here. The owner
    // is always set to the last player interacting with
    // the fusion shrine to trigger advancements

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
    }

}
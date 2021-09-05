package de.dafuqs.spectrum.blocks.fusion_shrine;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FusionShrineBlockEntity extends BlockEntity implements RecipeInputProvider {

    protected int INVENTORY_SIZE = 8;
    protected SimpleInventory inventory;
    Fluid storedFluid;

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
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("inventory", this.inventory.toNbtList());
        nbt.putString("fluid", Registry.FLUID.getId(this.storedFluid).toString());
        return nbt;
    }

    public static void tick(@NotNull World world, BlockPos blockPos, BlockState blockState, FusionShrineBlockEntity fusionShrineBlockEntity) {

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

}
package de.dafuqs.spectrum.compat.create;

import com.simibubi.create.api.event.*;
import de.dafuqs.fractal.api.*;
import de.dafuqs.spectrum.api.item_group.*;
import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.fabricmc.fabric.api.object.builder.v1.block.*;
import net.minecraft.block.*;
import net.minecraft.block.piston.*;
import net.minecraft.client.render.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import static de.dafuqs.spectrum.registries.SpectrumBlocks.*;

public class CreateCompat extends SpectrumIntegrationPacks.ModIntegrationPack {

    public static Block SMALL_ZINC_BUD;
    public static Block LARGE_ZINC_BUD;
    public static Block ZINC_CLUSTER;
    public static Block PURE_ZINC_BLOCK;
    public static Item PURE_ZINC;

    @Override
    public void register() {
        SMALL_ZINC_BUD = new SpectrumClusterBlock(FabricBlockSettings.create().pistonBehavior(PistonBehavior.DESTROY).hardness(1.0f).mapColor(Blocks.LIGHT_GRAY_CONCRETE.getDefaultMapColor()).requiresTool().nonOpaque(), SpectrumClusterBlock.GrowthStage.SMALL);
        LARGE_ZINC_BUD = new SpectrumClusterBlock(FabricBlockSettings.copyOf(SMALL_ZINC_BUD), SpectrumClusterBlock.GrowthStage.LARGE);
        ZINC_CLUSTER = new SpectrumClusterBlock(FabricBlockSettings.copyOf(SMALL_ZINC_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER);
        PURE_ZINC_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK));
        PURE_ZINC = new Item(SpectrumItems.IS.of());
        FabricItemSettings settings = SpectrumItems.IS.of();
        registerBlockWithItem("small_zinc_bud", SMALL_ZINC_BUD, settings, DyeColor.BROWN);
        registerBlockWithItem("large_zinc_bud", LARGE_ZINC_BUD, settings, DyeColor.BROWN);
        registerBlockWithItem("zinc_cluster", ZINC_CLUSTER, settings, DyeColor.BROWN);
        registerBlockWithItem("pure_zinc_block", PURE_ZINC_BLOCK, settings, DyeColor.BROWN);
        SpectrumItems.register("pure_zinc", PURE_ZINC, DyeColor.BROWN);
		
		ItemSubGroupEvents.modifyEntriesEvent(ItemGroupIDs.SUBTAB_PURE_RESOURCES).register(entries -> {
			entries.add(PURE_ZINC);
			entries.add(SMALL_ZINC_BUD);
			entries.add(LARGE_ZINC_BUD);
			entries.add(ZINC_CLUSTER);
			entries.add(PURE_ZINC_BLOCK);
		});
		
        PipeCollisionEvent.FLOW.register(event -> {
            final BlockState result = handleBidirectionalCollision(event.getLevel(), event.getFirstFluid(), event.getSecondFluid());
            if (result != null) event.setState(result);
        });

        PipeCollisionEvent.SPILL.register(event -> {
            final BlockState result = handleBidirectionalCollision(event.getLevel(), event.getPipeFluid(), event.getWorldFluid());
            if (result != null) event.setState(result);
        });
		
	}

    // NOTE: firstFluid and secondFluid are assumed to be not null without checking,
    // since the default Create event handlers for pipe collisions would throw a NullPointerException otherwise.
    private BlockState handleBidirectionalCollision(World world, @NotNull Fluid firstFluid, @NotNull Fluid secondFluid) {
        final FluidState firstState = firstFluid.getDefaultState();
        final FluidState secondState = secondFluid.getDefaultState();

        // Handle fluid 1
        final BlockState result = spectrumFluidCollision(world, firstState, secondState);
        if (result != null) return result;

        // Handle fluid 2
        return spectrumFluidCollision(world, secondState, firstState);
    }

    private BlockState spectrumFluidCollision(World world, FluidState state, FluidState otherState) {
        if (state.getBlockState().getBlock() instanceof SpectrumFluidBlock spectrumFluid)
            return spectrumFluid.handleFluidCollision(world, state, otherState);
        return null;
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public void registerClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(SMALL_ZINC_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LARGE_ZINC_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ZINC_CLUSTER, RenderLayer.getCutout());
    }

}

package de.dafuqs.spectrum.compat.create;

import com.simibubi.create.api.event.*;
import de.dafuqs.fractal.api.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item_group.*;
import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.SpectrumItems.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.data.models.model.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import org.jetbrains.annotations.*;

import static de.dafuqs.spectrum.registries.SpectrumBlocks.*;
import static de.dafuqs.spectrum.registries.SpectrumBlocks.simple;
import static de.dafuqs.spectrum.registries.SpectrumItems.simple;
import static de.dafuqs.spectrum.registries.SpectrumItems.item;

public class CreateCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static Block SMALL_ZINC_BUD = SpectrumBlocks.register(cluster(blockWithItem("small_zinc_bud", new SpectrumClusterBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).destroyTime(1.0f).mapColor(Blocks.LIGHT_GRAY_CONCRETE.defaultMapColor()).requiresCorrectToolForDrops().noOcclusion(), SpectrumClusterBlock.GrowthStage.SMALL), InkColors.BROWN), ModelTemplates.CROSS));
	public static Block LARGE_ZINC_BUD = SpectrumBlocks.register(cluster(blockWithItem("large_zinc_bud", new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_ZINC_BUD), SpectrumClusterBlock.GrowthStage.LARGE), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static Block ZINC_CLUSTER = SpectrumBlocks.register(cluster(blockWithItem("zinc_cluster", new SpectrumClusterBlock(BlockBehaviour.Properties.ofFullCopy(SMALL_ZINC_BUD), SpectrumClusterBlock.GrowthStage.CLUSTER), InkColors.BROWN), SpectrumModels.CRYSTALLARIEUM_FARMABLE));
	public static Block PURE_ZINC_BLOCK = SpectrumBlocks.register(simple(blockWithItem("pure_zinc_block", new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)), InkColors.BROWN)));
	
	public static Item PURE_ZINC = SpectrumItems.register(simple(item("pure_zinc", new Item(IS.of()), InkColors.BROWN)));
	
	@Override
	public void register() {
		SpectrumItems.ITEM_REGISTRAR.flush();
		SpectrumBlocks.COMMON_REGISTRAR.flush();
		
		ItemSubGroupEvents.modifyEntriesEvent(ItemGroupIDs.SUBTAB_PURE_RESOURCES).register(entries -> {
			entries.accept(PURE_ZINC);
			entries.accept(SMALL_ZINC_BUD);
			entries.accept(LARGE_ZINC_BUD);
			entries.accept(ZINC_CLUSTER);
			entries.accept(PURE_ZINC_BLOCK);
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
	private BlockState handleBidirectionalCollision(Level world, @NotNull Fluid firstFluid, @NotNull Fluid secondFluid) {
		final FluidState firstState = firstFluid.defaultFluidState();
		final FluidState secondState = secondFluid.defaultFluidState();
		
		// Handle fluid 1
		final BlockState result = spectrumFluidCollision(world, firstState, secondState);
		if (result != null) return result;
		
		// Handle fluid 2
		return spectrumFluidCollision(world, secondState, firstState);
	}
	
	private BlockState spectrumFluidCollision(Level world, FluidState state, FluidState otherState) {
		if (state.createLegacyBlock().getBlock() instanceof SpectrumFluidBlock spectrumFluid)
			return spectrumFluid.handleFluidCollision(world, state, otherState);
		return null;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void registerClient() {
		SpectrumBlocks.CLIENT_REGISTRAR.flush();
	}
	
}

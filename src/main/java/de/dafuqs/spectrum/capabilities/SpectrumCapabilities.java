package de.dafuqs.spectrum.capabilities;

import de.dafuqs.spectrum.api.ink.capability.*;
import de.dafuqs.spectrum.blocks.amphora.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.blocks.fusion_shrine.*;
import de.dafuqs.spectrum.blocks.ink.gen.*;
import de.dafuqs.spectrum.blocks.ink.sink.*;
import de.dafuqs.spectrum.blocks.item_bowl.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.blocks.potion_workshop.*;
import de.dafuqs.spectrum.blocks.redstone.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.fluids.capability.templates.*;
import net.neoforged.neoforge.items.wrapper.*;
import org.jetbrains.annotations.*;

public class SpectrumCapabilities {

    public static void register(RegisterCapabilitiesEvent event) {
		// ItemHandler.BLOCK
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.FUSION_SHRINE.get(), (@NotNull FusionShrineBlockEntity blockEntity, Direction direction) -> new InvWrapper(blockEntity));;
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.PEDESTAL.get(), (@NotNull PedestalBlockEntity blockEntity, Direction direction) -> new SidedInvWrapper(blockEntity, direction));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.SPIRIT_INSTILLER.get(), (@NotNull SpiritInstillerBlockEntity blockEntity, Direction direction) -> new InvWrapper(blockEntity));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.ENCHANTER.get(), (@NotNull EnchanterBlockEntity blockEntity, Direction direction) -> new SidedInvWrapper(blockEntity, direction));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.CINDERHEARTH.get(), (@NotNull CinderhearthBlockEntity blockEntity, Direction direction) -> new SidedInvWrapper(blockEntity, direction));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.CRYSTALLARIEUM.get(), (@NotNull CrystallarieumBlockEntity blockEntity, Direction direction) -> new InvWrapper(blockEntity));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.TITRATION_BARREL.get(), (titrationBarrel, direction) -> {
			if(!titrationBarrel.isInteractionAllowed()) {
				new InvWrapper(titrationBarrel);
			}
			return null;
		});
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.POTION_WORKSHOP.get(), (@NotNull PotionWorkshopBlockEntity blockEntity, Direction direction) -> new SidedInvWrapper(blockEntity, direction));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.BLOCK_PLACER.get(), (@NotNull BlockPlacerBlockEntity blockEntity, Direction direction) -> new InvWrapper(blockEntity));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.BOTTOMLESS_BUNDLE.get(), (@NotNull BottomlessBundleBlockEntity blockEntity, Direction direction) -> blockEntity.storage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.ITEM_BOWL.get(), (@NotNull ItemBowlBlockEntity blockEntity, Direction direction) -> new InvWrapper(blockEntity));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.COLOR_PICKER.get(), (@NotNull ColorPickerBlockEntity blockEntity, Direction direction) -> new InvWrapper(blockEntity));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.CRYSTAL_APOTHECARY.get(), (@NotNull CrystalApothecaryBlockEntity blockEntity, Direction direction) -> new InvWrapper(blockEntity));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.BLACK_HOLE_CHEST.get(), (@NotNull BlackHoleChestBlockEntity blockEntity, Direction direction) -> new SidedInvWrapper(blockEntity, direction));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.FABRICATION_CHEST.get(), (@NotNull FabricationChestBlockEntity blockEntity, Direction direction) -> new SidedInvWrapper(blockEntity, direction));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.COMPACTING_CHEST.get(), (@NotNull CompactingChestBlockEntity blockEntity, Direction direction) -> new InvWrapper(blockEntity));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SpectrumBlockEntities.AMPHORA.get(), (@NotNull AmphoraBlockEntity blockEntity, Direction direction) -> new InvWrapper(blockEntity));
		
		// FluidHandler.BLOCK
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SpectrumBlockEntities.FUSION_SHRINE.get(), (blockEntity, context) -> blockEntity.getFluidTank());
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SpectrumBlockEntities.CRYSTALLARIEUM.get(), (blockEntity, context) -> blockEntity.getFluidTank());
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SpectrumBlockEntities.TITRATION_BARREL.get(), (blockEntity, context) -> blockEntity.getFluidTank());
		
		// ItemHandler.ITEM
		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> BottomlessItemCapability.get(stack), SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem());
		/*
			// BAG_OF_HOLDING only works server side
			// the client does not know about the content of the ender chest, unless opened
			event.registerItem(Capabilities.ItemHandler.ITEM, (ignored, ignored2) -> iterableProvider((player, stack) -> player == null ? List.of() : player.getEnderChestInventory().getItems()), SpectrumItems.BAG_OF_HOLDING);
		*/
		
		// FluidHandler.ITEM
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, v) -> new FluidHandlerItemStackSimple.Consumable(SpectrumDataComponentTypes.FLUID_CONTENT, stack, 1000), SpectrumItems.MERMAIDS_GEM.get());
		
		// Ink Blocks
		event.registerBlockEntity(InkCapabilities.BLOCK, SpectrumBlockEntities.COLOR_PICKER.get(), (blockEntity, context) -> BlockEntityInkCapability.of(blockEntity));
		event.registerBlockEntity(InkCapabilities.BLOCK, SpectrumBlockEntities.TINTING_STATION.get(), (blockEntity, context) -> BlockEntityInkCapability.of(blockEntity));
		event.registerBlockEntity(InkCapabilities.BLOCK, SpectrumBlockEntities.CINDERHEARTH.get(), (blockEntity, context) -> BlockEntityInkCapability.of(blockEntity));
		event.registerBlockEntity(InkCapabilities.BLOCK, SpectrumBlockEntities.CRYSTALLARIEUM.get(), (blockEntity, context) -> BlockEntityInkCapability.of(blockEntity));
		
		// Ink Items
		event.registerItem(InkCapabilities.ITEM, (stack, ctx) -> ItemInkCapability.of(stack),
				SpectrumItems.INK_FLASK, SpectrumItems.INK_ASSORTMENT, SpectrumItems.CREATIVE_INK_ASSORTMENT, SpectrumItems.PIGMENT_PALETTE, SpectrumItems.ARTISTS_PALETTE,
				SpectrumItems.SHIELDGRASP_AMULET, SpectrumItems.HEARTSINGERS_REWARD, SpectrumItems.GLOVES_OF_DAWNS_GRASP, SpectrumItems.RING_OF_PURSUIT, SpectrumItems.RING_OF_DENSER_STEPS, SpectrumItems.RING_OF_AERIAL_GRACE, SpectrumItems.LAURELS_OF_SERENITY);
    }

}
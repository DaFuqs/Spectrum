package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.transfer.v1.context.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;

import java.util.*;

public class MermaidsGemItem extends ItemNameBlockItem implements RevelationAware {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("place_pedestal");
	public static final long ITEM_INTERACTION_WATER_FILL_DROPLETS = FluidConstants.BUCKET;
	
	public MermaidsGemItem(Block block, Properties settings) {
		super(block, settings);
		RevelationAware.register(this);
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of();
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this, Items.KELP);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		var pos = context.getClickedPos();
		var level = context.getLevel();
		var stack = context.getItemInHand();
		var storage = FluidStorage.SIDED.find(level, pos, level.getBlockState(pos), level.getBlockEntity(pos), context.getClickedFace());
		if (storage != null)
			try (Transaction tx = Transaction.openOuter() ) {
				final var water = FluidVariant.of(Fluids.WATER);
				long filled = storage.insert(water, ITEM_INTERACTION_WATER_FILL_DROPLETS, tx);
				stack.shrink((int)Math.ceilDiv(filled, ITEM_INTERACTION_WATER_FILL_DROPLETS));
				tx.commit();
				level.playSound(context.getPlayer(), pos.getX(), pos.getY(), pos.getZ(), FluidVariantAttributes.getEmptySound(water), SoundSource.PLAYERS, 1, 1);
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
		return super.useOn(context);
	}
	
	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
		var storage = ContainerItemContext
				.ofSingleSlot(InventoryStorage.of(slot.container, null).getSlot(slot.getContainerSlot()))
				.find(FluidStorage.ITEM);
		if (storage != null) {
			int usedGems = action == ClickAction.PRIMARY ? stack.getCount() : 1;
			try (Transaction tx = Transaction.openOuter() ) {
				final var water = FluidVariant.of(Fluids.WATER);
				long filled = storage.insert(water, ITEM_INTERACTION_WATER_FILL_DROPLETS * usedGems, tx);
				stack.shrink((int)Math.ceilDiv(filled, ITEM_INTERACTION_WATER_FILL_DROPLETS));
				tx.commit();
				player.level().playSound(player, player.getX(), player.getEyeY(), player.getZ(), FluidVariantAttributes.getEmptySound(water), SoundSource.PLAYERS, 1, 1);
				return true;
			}
		}
		return super.overrideStackedOnOther(stack, slot, action, player);
	}
}

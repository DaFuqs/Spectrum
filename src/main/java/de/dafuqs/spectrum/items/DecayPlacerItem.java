package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.ticks.*;

import java.util.*;

public class DecayPlacerItem extends ItemNameBlockItem {
	
	protected final List<Component> tooltips;
	
	public DecayPlacerItem(Block block, Properties settings, List<Component> tooltips) {
		super(block, settings);
		this.tooltips = tooltips;
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		InteractionResult actionResult = super.useOn(context);
		if (actionResult.consumesAction()) {
			BlockPlaceContext itemPlacementContext = this.updatePlacementContext(new BlockPlaceContext(context));
			if (itemPlacementContext != null) {
				BlockPos blockPos = itemPlacementContext.getClickedPos();
				
				BlockState placedBlockState = context.getLevel().getBlockState(blockPos);
				if (placedBlockState.is(SpectrumBlockTags.DECAY)) {
					context.getLevel().scheduleTick(blockPos, placedBlockState.getBlock(), 40 + world.random.nextInt(200), TickPriority.EXTREMELY_LOW);
				}
			}
		}
		if (!world.isClientSide && actionResult.consumesAction() && context.getPlayer() != null && !context.getPlayer().isCreative()) {
			context.getPlayer().getInventory().placeItemBackInInventory(Items.GLASS_BOTTLE.getDefaultInstance());
		}
		return actionResult;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.addAll(tooltips);
	}
	
}

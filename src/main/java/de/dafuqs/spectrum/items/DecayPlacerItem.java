package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.List;

public class DecayPlacerItem extends AliasedBlockItem {
	
	public DecayPlacerItem(Block block, Settings settings) {
		super(block, settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		ActionResult actionResult = super.useOnBlock(context);
		if (actionResult.isAccepted()) {
			ItemPlacementContext itemPlacementContext = this.getPlacementContext(new ItemPlacementContext(context));
			BlockPos blockPos = itemPlacementContext.getBlockPos();
			
			BlockState placedBlockState = context.getWorld().getBlockState(blockPos);
			if (placedBlockState.isIn(SpectrumBlockTags.DECAY)) {
				context.getWorld().createAndScheduleBlockTick(blockPos, placedBlockState.getBlock(), 40 + context.getWorld().random.nextInt(200), TickPriority.EXTREMELY_LOW);
			}
		}
		if (!context.getWorld().isClient && actionResult.isAccepted() && context.getPlayer() != null && !context.getPlayer().isCreative()) {
			context.getPlayer().giveItemStack(Items.GLASS_BOTTLE.getDefaultStack());
		}
		return actionResult;
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		Item item = itemStack.getItem();
		if (item.equals(SpectrumItems.BOTTLE_OF_FADING)) {
			tooltip.add(new TranslatableText("item.spectrum.bottle_of_fading.tooltip"));
		} else if (item.equals(SpectrumItems.BOTTLE_OF_FAILING)) {
			tooltip.add(new TranslatableText("item.spectrum.bottle_of_failing.tooltip"));
		} else if (item.equals(SpectrumItems.BOTTLE_OF_RUIN)) {
			tooltip.add(new TranslatableText("item.spectrum.bottle_of_ruin.tooltip"));
		} else if (item.equals(SpectrumItems.BOTTLE_OF_DECAY_AWAY)) {
			tooltip.add(new TranslatableText("item.spectrum.bottle_of_decay_away.tooltip"));
		}
	}
	
}

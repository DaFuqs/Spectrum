package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PrimordialLighterItem extends FlintAndSteelItem {

	public PrimordialLighterItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.primordial_lighter.tooltip").formatted(Formatting.GRAY));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity player = context.getPlayer();
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockPos blockOnSide = pos.offset(context.getSide());

		if (PrimordialFireBlock.canPlaceAt(world, blockOnSide, context.getHorizontalPlayerFacing())) {
			world.playSound(player, blockOnSide, SpectrumSoundEvents.ITEM_PRIMORDIAL_LIGHTER_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
			BlockState primordialFireState = SpectrumBlocks.PRIMORDIAL_FIRE.getStateForPosition(world, blockOnSide);
			world.setBlockState(blockOnSide, primordialFireState, 11);
			world.emitGameEvent(player, GameEvent.BLOCK_PLACE, pos);

			ItemStack stack = context.getStack();
			if (player instanceof ServerPlayerEntity serverPlayerEntity) {
				Criteria.PLACED_BLOCK.trigger(serverPlayerEntity, blockOnSide, stack);
				stack.damage(1, serverPlayerEntity, (p) -> {
					p.sendToolBreakStatus(context.getHand());
				});
			}

			return ActionResult.success(world.isClient());
		} else {
			return ActionResult.FAIL;
		}

	}

}

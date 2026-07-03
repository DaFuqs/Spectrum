package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.core.dispenser.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import javax.annotation.*;

import java.util.*;

public class PrimordialLighterItem extends FlintAndSteelItem implements CreativeOnlyItem {
	
	public static final DispenseItemBehavior DISPENSER_BEHAVIOR = new OptionalDispenseItemBehavior() {
		protected ItemStack execute(BlockSource pointer, ItemStack stack) {
			var world = pointer.level();
			this.setSuccess(true);
			Direction direction = pointer.state().getValue(DispenserBlock.FACING);
			BlockPos blockPos = pointer.pos().relative(direction);
			if (PrimordialFireBlock.tryPlacePrimordialFire(world, blockPos, direction)) {
				stack.hurtAndBreak(1, world, null, item -> {
				});
				this.setSuccess(true);
			} else {
				this.setSuccess(false);
			}
			return stack;
		}
	};
	
	public PrimordialLighterItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.primordial_lighter.tooltip").withStyle(ChatFormatting.GRAY));
		CreativeOnlyItem.appendTooltip(tooltip);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockPos blockOnSide = pos.relative(context.getClickedFace());
		
		if (PrimordialFireBlock.canBePlacedAt(world, blockOnSide, context.getHorizontalDirection())) {
			world.playSound(player, blockOnSide, SpectrumSoundEvents.ITEM_PRIMORDIAL_LIGHTER_USE, SoundSource.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
			BlockState primordialFireState = SpectrumBlocks.PRIMORDIAL_FIRE.get().getStateForPosition(world, blockOnSide, context.getClickedFace().getOpposite());
			world.setBlock(blockOnSide, primordialFireState, 11);
			world.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
			
			ItemStack stack = context.getItemInHand();
			if (player instanceof ServerPlayer serverPlayer) {
				CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, blockOnSide, stack);
				stack.hurtAndBreak(1, serverPlayer, LivingEntity.getSlotForHand(context.getHand()));
			}
			
			return InteractionResult.sidedSuccess(world.isClientSide());
		} else {
			return InteractionResult.FAIL;
		}
		
	}
	
}

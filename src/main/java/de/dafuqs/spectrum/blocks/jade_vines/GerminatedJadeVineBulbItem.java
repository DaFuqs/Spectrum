package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.items.conditional.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;

import java.util.*;

public class GerminatedJadeVineBulbItem extends CloakedItem {
	
	public GerminatedJadeVineBulbItem(Properties settings, ResourceLocation cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		
		if (JadeVineRootsBlock.canBePlantedOn(world.getBlockState(pos)) && world.getBlockState(pos.below()).isAir()) {
			if (context.getLevel().isClientSide) {
				for (int i = 0; i < 16; i++) {
					JadeVine.spawnParticlesClient(world, pos);
					JadeVine.spawnParticlesClient(world, pos.below());
				}
				
				return InteractionResult.SUCCESS;
			} else {
				ServerPlayer player = (ServerPlayer) context.getPlayer();
				
				world.setBlockAndUpdate(pos, SpectrumBlocks.JADE_VINE_ROOTS.defaultBlockState());
				BlockState bulbState = SpectrumBlocks.JADE_VINE_BULB.defaultBlockState();
				world.setBlockAndUpdate(pos.below(), bulbState);
				world.playSound(null, pos, SoundEvents.CROP_PLANTED, SoundSource.PLAYERS, 1.0F, 1.0F);
				
				SoundType blockSoundGroup = bulbState.getSoundType();
				world.playSound(player, pos.below(), bulbState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
				world.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
				world.gameEvent(player, GameEvent.BLOCK_PLACE, pos.below());
				
				CriteriaTriggers.PLACED_BLOCK.trigger(player, pos, context.getItemInHand());
				CriteriaTriggers.PLACED_BLOCK.trigger(player, pos.below(), context.getItemInHand());
				
				if (player == null || !player.isCreative()) {
					context.getItemInHand().shrink(1);
				}
				return InteractionResult.CONSUME;
			}
		}
		return super.useOn(context);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		tooltip.add(Component.translatable("item.spectrum.germinated_jade_vine_bulb.tooltip"));
		tooltip.add(Component.translatable("item.spectrum.germinated_jade_vine_bulb.tooltip2"));
		tooltip.add(Component.translatable("item.spectrum.germinated_jade_vine_bulb.tooltip3"));
	}
	
}

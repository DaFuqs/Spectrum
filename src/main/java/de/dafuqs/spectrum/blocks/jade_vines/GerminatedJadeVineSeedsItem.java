package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.items.conditional.CloakedItem;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class GerminatedJadeVineSeedsItem extends CloakedItem {
	
	public GerminatedJadeVineSeedsItem(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		
		if (JadeVineRootsBlock.canBePlantedOn(world.getBlockState(pos)) && world.getBlockState(pos.down()).isAir()) {
			if (context.getWorld().isClient) {
				for (int i = 0; i < 16; i++) {
					JadeVine.spawnParticlesClient(world, pos);
					JadeVine.spawnParticlesClient(world, pos.down());
				}
				
				return ActionResult.SUCCESS;
			} else {
				ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
				
				world.setBlockState(pos, SpectrumBlocks.JADE_VINE_ROOTS.getDefaultState());
				BlockState bulbState = SpectrumBlocks.JADE_VINE_BULB.getDefaultState();
				world.setBlockState(pos.down(), bulbState);
				world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.PLAYERS, 1.0F, 1.0F);
				
				BlockSoundGroup blockSoundGroup = bulbState.getSoundGroup();
				world.playSound(player, pos.down(), bulbState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
				world.emitGameEvent(player, GameEvent.BLOCK_PLACE, pos);
				world.emitGameEvent(player, GameEvent.BLOCK_PLACE, pos.down());
				
				Criteria.PLACED_BLOCK.trigger(player, pos, context.getStack());
				Criteria.PLACED_BLOCK.trigger(player, pos.down(), context.getStack());
				
				if (player == null || !player.isCreative()) {
					context.getStack().decrement(1);
				}
				return ActionResult.CONSUME;
			}
		}
		return super.useOnBlock(context);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		tooltip.add(new TranslatableText("item.spectrum.germinated_jade_vine_seeds.tooltip"));
		tooltip.add(new TranslatableText("item.spectrum.germinated_jade_vine_seeds.tooltip2"));
		tooltip.add(new TranslatableText("item.spectrum.germinated_jade_vine_seeds.tooltip3"));
	}
	
}

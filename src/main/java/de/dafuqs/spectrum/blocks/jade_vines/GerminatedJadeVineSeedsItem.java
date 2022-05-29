package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.items.conditional.CloakedItem;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class GerminatedJadeVineSeedsItem extends CloakedItem {
	
	public GerminatedJadeVineSeedsItem(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();

		if(JadeVineRootsBlock.canBePlantedOn(world.getBlockState(pos)) && world.getBlockState(pos.down()).isAir()) {
			if(context.getWorld().isClient) {
				for(int i = 0; i < 10; i++){
					JadeVine.spawnParticles(world, pos);
					JadeVine.spawnParticles(world, pos.down());
				}
				
				return ActionResult.SUCCESS;
			} else {
				world.setBlockState(pos, SpectrumBlocks.JADE_VINE_ROOTS.getDefaultState());
				world.setBlockState(pos.down(), SpectrumBlocks.JADE_VINE_BULB.getDefaultState());
				world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.PLAYERS, 1.0F, 1.0F);
				
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

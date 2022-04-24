package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.items.CloakedItem;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GerminatedJadeVineSeedsItem extends CloakedItem {
	
	public GerminatedJadeVineSeedsItem(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();

		if(JadeVinesBlock.canBePlacedOn(world.getBlockState(pos))) {
			if(context.getWorld().isClient) {
				return ActionResult.SUCCESS;
			} else {
				world.setBlockState(pos, SpectrumBlocks.JADE_VINES.getDefaultState());
				return ActionResult.CONSUME;
			}
		}
		return super.useOnBlock(context);
	}
	
}

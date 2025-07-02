package de.dafuqs.spectrum.items;

import com.klikli_dev.modonomicon.api.multiblock.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class StructurePlacerItem extends Item implements CreativeOnlyItem {
	
	protected final ResourceLocation multiBlockIdentifier;
	
	public StructurePlacerItem(Properties settings, ResourceLocation multiBlockIdentifier) {
		super(settings);
		this.multiBlockIdentifier = multiBlockIdentifier;
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getPlayer() != null && context.getPlayer().isCreative()) {
			Multiblock multiblock = SpectrumMultiblocks.get(multiBlockIdentifier);
			if (multiblock != null) {
				Rotation blockRotation = Support.rotationFromDirection(context.getHorizontalDirection());
				multiblock.place(context.getLevel(), context.getClickedPos().above(), blockRotation);
				return InteractionResult.CONSUME;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		CreativeOnlyItem.appendTooltip(tooltip);
	}
	
}

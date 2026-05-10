package de.dafuqs.spectrum.items;

import com.klikli_dev.modonomicon.client.gui.book.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.stats.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public class CookbookItem extends Item {
	
	public BookAddress bookAddress;
	private final int toolTipColor;
	
	public CookbookItem(Properties settings, BookAddress bookAddress) {
		this(settings, bookAddress, -1);
	}
	
	public CookbookItem(Properties settings, BookAddress bookAddress, int toolTipColor) {
		super(settings);
		this.bookAddress = bookAddress;
		this.toolTipColor = toolTipColor;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (!world.isClientSide()) {
			user.awardStat(Stats.ITEM_USED.get(this));
			
			return InteractionResultHolder.success(user.getItemInHand(hand));
		} else {
			openGuidebookPage(this.bookAddress);
		}
		
		return InteractionResultHolder.consume(user.getItemInHand(hand));
	}
	
	private void openGuidebookPage(BookAddress address) {
		if (SpectrumItems.GUIDEBOOK instanceof GuidebookItem guidebook) {
			guidebook.openGuidebook(address);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		if (toolTipColor == -1) {
			tooltip.add(Component.translatable(this.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
			return;
		}
		
		tooltip.add(Component.translatable(this.getDescriptionId() + ".tooltip").withStyle(s -> s.withColor(toolTipColor)));
	}
	
}

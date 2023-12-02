package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.client.item.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.stat.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CookbookItem extends Item {
	
	public String guidebookPageToOpen;
	
	public CookbookItem(Settings settings, String guidebookPageToOpen) {
		super(settings);
		this.guidebookPageToOpen = guidebookPageToOpen;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient && user instanceof ServerPlayerEntity serverPlayerEntity) {
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			
			return TypedActionResult.success(user.getStackInHand(hand));
		} else if (user instanceof ClientPlayerEntity clientPlayerEntity) {
			openGuidebookPage(clientPlayerEntity, SpectrumCommon.locate(guidebookPageToOpen), 0);
		}
		
		return TypedActionResult.consume(user.getStackInHand(hand));
	}
	
	private void openGuidebookPage(ClientPlayerEntity serverPlayerEntity, Identifier entry, int page) {
		if (SpectrumItems.GUIDEBOOK instanceof GuidebookItem guidebook) {
			guidebook.openGuidebook(serverPlayerEntity, entry, page);
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable(this.getTranslationKey() + ".tooltip").formatted(Formatting.GRAY));
	}
	
}
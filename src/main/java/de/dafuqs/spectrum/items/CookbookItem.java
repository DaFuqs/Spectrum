package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.stat.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.api.*;

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
			openGuidebookPage(serverPlayerEntity, SpectrumCommon.locate(guidebookPageToOpen), 0);
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			
			return TypedActionResult.success(user.getStackInHand(hand));
		}
		
		return TypedActionResult.consume(user.getStackInHand(hand));
	}
	
	private void openGuidebookPage(ServerPlayerEntity serverPlayerEntity, Identifier entry, int page) {
		PatchouliAPI.get().openBookEntry(serverPlayerEntity, GuidebookItem.GUIDEBOOK_ID, entry, page);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable(this.getTranslationKey() + ".tooltip").formatted(Formatting.GRAY));
	}
	
}
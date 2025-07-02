package de.dafuqs.spectrum.items;

import com.klikli_dev.modonomicon.client.gui.*;
import com.klikli_dev.modonomicon.client.gui.book.*;
import de.dafuqs.revelationary.advancement_criteria.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.stats.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;

import java.util.*;

public class GuidebookItem extends Item implements LoomPatternProvider {
	
	public static final ResourceLocation GUIDEBOOK_ID = SpectrumCommon.locate("guidebook");
	public static final BookAddress GUIDEBOOK_ADDRESS = BookAddress.defaultFor(GUIDEBOOK_ID);
	
	public static final ResourceLocation CUISINE_CATEGORY_ID = SpectrumCommon.locate("cuisine");
	public static final ResourceLocation DIMENSION_CATEGORY_ID = SpectrumCommon.locate("dimension");
	
	
	public static BookAddress addressOf(ResourceLocation category, ResourceLocation entryId) {
		return BookAddress.of(GUIDEBOOK_ID, category, entryId, 0);
	}
	
	public GuidebookItem(Properties settings) {
		super(settings);
	}
	
	
	private static final Set<UUID> alreadyReprocessedPlayers = new HashSet<>();
	
	public static void reprocessAdvancementUnlocks(ServerPlayer serverPlayerEntity) {
		if (serverPlayerEntity.getServer() == null || SpectrumCommon.minecraftServer == null) {
			return;
		}
		
		UUID uuid = serverPlayerEntity.getUUID();
		if (alreadyReprocessedPlayers.contains(uuid)) {
			return;
		}
		alreadyReprocessedPlayers.add(uuid);
		
		PlayerAdvancements tracker = serverPlayerEntity.getAdvancements();
		
		for (var advancement : serverPlayerEntity.getServer().getAdvancements().getAllAdvancements()) {
			var hasAdvancement = tracker.getOrStartProgress(advancement);
			if (!hasAdvancement.isDone()) {
				for (var criterionEntry : advancement.value().criteria().entrySet()) {
					var conditions = criterionEntry.getValue().triggerInstance();
					if (conditions instanceof AdvancementGottenCriterion.Conditions hasAdvancementConditions) {
						var advancementCriterionAdvancement = SpectrumCommon.minecraftServer.getAdvancements().get(hasAdvancementConditions.getAdvancementIdentifier());
						if (advancementCriterionAdvancement != null) {
							var hasAdvancementCriterionAdvancement = tracker.getOrStartProgress(advancementCriterionAdvancement);
							if (hasAdvancementCriterionAdvancement.isDone()) {
								tracker.award(advancement, criterionEntry.getKey());
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (world.isClientSide()) {
			// if the player has never opened the book before
			// automatically open the introduction page
			openGuidebook();
		} else if (user instanceof ServerPlayer serverPlayerEntity) {
			// Process new advancement unlocks that got added
			// after spectrum has been installed / updated
			reprocessAdvancementUnlocks(serverPlayerEntity);
		}
		user.awardStat(Stats.ITEM_USED.get(this));
		
		return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), world.isClientSide);
	}
	
	public void openGuidebook() {
		BookGuiManager.get().openBook(GUIDEBOOK_ADDRESS);
	}
	
	public void openGuidebook(BookAddress address) {
		BookGuiManager.get().openBook(address);
	}
	
	@Override
	public ResourceKey<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.GUIDEBOOK;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		addBannerPatternProviderTooltip(tooltip);
	}
	
}

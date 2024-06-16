package de.dafuqs.spectrum.items;

import de.dafuqs.revelationary.advancement_criteria.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.entry.*;
import net.minecraft.server.network.*;
import net.minecraft.stat.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.api.*;

import java.util.*;

public class GuidebookItem extends Item implements LoomPatternProvider {
	
	public static final Identifier GUIDEBOOK_ID = SpectrumCommon.locate("guidebook");
	
	public GuidebookItem(Settings settings) {
		super(settings);
	}
	
	
	private static final Set<UUID> alreadyReprocessedPlayers = new HashSet<>();
	
	public static void reprocessAdvancementUnlocks(ServerPlayerEntity serverPlayerEntity) {
		if (serverPlayerEntity.getServer() == null) {
			return;
		}
		
		UUID uuid = serverPlayerEntity.getUuid();
		if (alreadyReprocessedPlayers.contains(uuid)) {
			return;
		}
		alreadyReprocessedPlayers.add(uuid);
		
		PlayerAdvancementTracker tracker = serverPlayerEntity.getAdvancementTracker();
		
		for (Advancement advancement : serverPlayerEntity.getServer().getAdvancementLoader().getAdvancements()) {
			AdvancementProgress hasAdvancement = tracker.getProgress(advancement);
			if (!hasAdvancement.isDone()) {
				for (Map.Entry<String, AdvancementCriterion> criterionEntry : advancement.getCriteria().entrySet()) {
					CriterionConditions conditions = criterionEntry.getValue().getConditions();
					if (conditions != null && conditions.getId().equals(AdvancementGottenCriterion.ID) && conditions instanceof AdvancementGottenCriterion.Conditions hasAdvancementConditions) {
						Advancement advancementCriterionAdvancement = SpectrumCommon.minecraftServer.getAdvancementLoader().get(hasAdvancementConditions.getAdvancementIdentifier());
						if (advancementCriterionAdvancement != null) {
							AdvancementProgress hasAdvancementCriterionAdvancement = tracker.getProgress(advancementCriterionAdvancement);
							if (hasAdvancementCriterionAdvancement.isDone()) {
								tracker.grantCriterion(advancement, criterionEntry.getKey());
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient && user instanceof ServerPlayerEntity serverPlayerEntity) {
			
			// Workaround for new advancement unlocks getting added after spectrum has been installed
			reprocessAdvancementUnlocks(serverPlayerEntity);
			
			// if the player has never opened the book before
			// automatically open the introduction page
			if (isNewPlayer(serverPlayerEntity)) {
				openGuidebook(serverPlayerEntity, SpectrumCommon.locate("general/intro"), 0);
			} else {
				openGuidebook(serverPlayerEntity);
			}
			
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			
			return TypedActionResult.success(user.getStackInHand(hand));
		} else {
			return TypedActionResult.consume(user.getStackInHand(hand));
		}
	}
	
	private boolean isNewPlayer(ServerPlayerEntity serverPlayerEntity) {
		return serverPlayerEntity.getStatHandler().getStat(Stats.USED, this) == 0;
	}
	
	private void openGuidebook(ServerPlayerEntity serverPlayerEntity) {
		PatchouliAPI.get().openBookGUI(serverPlayerEntity, GUIDEBOOK_ID);
	}
	
	private void openGuidebook(ServerPlayerEntity serverPlayerEntity, Identifier entry, int page) {
		PatchouliAPI.get().openBookEntry(serverPlayerEntity, GUIDEBOOK_ID, entry, page);
	}
	
	@Override
	public RegistryEntry<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.GUIDEBOOK;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		addBannerPatternProviderTooltip(tooltip);
	}
	
}
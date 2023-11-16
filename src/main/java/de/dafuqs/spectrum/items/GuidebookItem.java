package de.dafuqs.spectrum.items;

import de.dafuqs.revelationary.advancement_criteria.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.entry.*;
import net.minecraft.server.*;
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
	
	public static void reprocessAdvancementUnlocks(ServerPlayerEntity serverPlayerEntity) {
		if (serverPlayerEntity.getServer() == null) {
			return;
		}
		
		PlayerAdvancementTracker tracker = serverPlayerEntity.getAdvancementTracker();
		
		// "has advancement" criteria with nonexistent advancements
		for (Advancement advancement : serverPlayerEntity.getServer().getAdvancementLoader().getAdvancements()) {
			if (advancement.getId().getNamespace().equals(SpectrumCommon.MOD_ID)) {
				AdvancementProgress hasAdvancement = tracker.getProgress(advancement);
				if (!hasAdvancement.isDone()) {
					for (Map.Entry<String, AdvancementCriterion> criterionEntry : advancement.getCriteria().entrySet()) {
						CriterionConditions conditions = criterionEntry.getValue().getConditions();
						if (conditions != null && conditions.getId().equals(AdvancementGottenCriterion.ID) && conditions instanceof AdvancementGottenCriterion.Conditions hasAdvancementConditions) {
							Identifier advancementIdentifier = hasAdvancementConditions.getAdvancementIdentifier();
							Advancement advancementCriterionAdvancement = SpectrumCommon.minecraftServer.getAdvancementLoader().get(advancementIdentifier);
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

		upgradeAdvancementsTo1dot7(serverPlayerEntity, tracker);
	}

	/**
	 * With the chance from 1.6.x => 1.7.x a few advancements have gotten their criteria changed
	 * This method fixes those differences, so the player ends up exactly where they were before in progression
	 * without having to re-do those advancements
	 */
	public static void upgradeAdvancementsTo1dot7(ServerPlayerEntity player, PlayerAdvancementTracker tracker) {
		ServerAdvancementLoader loader = player.getServer().getAdvancementLoader();
		Advancement ruinAdvancement = loader.get(SpectrumCommon.locate("midgame/craft_bottle_of_ruin"));
		boolean hasRuinAdvancement = tracker.getProgress(ruinAdvancement).isDone();

		Advancement pedestalAdvancement = loader.get(SpectrumCommon.locate("craft_cmy_pedestal"));
		boolean hasPedestalAdvancement = tracker.getProgress(pedestalAdvancement).isDone();

		if (hasRuinAdvancement && !hasPedestalAdvancement) {
			Support.grantAdvancementCriterion(player, "craft_cmy_pedestal", "craft_pedestal");
			Support.grantAdvancementCriterion(player, "collect_shimmerstone", "collected_shimmerstone");
			Support.grantAdvancementCriterion(player, "enter_ender_glass", "enter_ender_glass");
			Support.grantAdvancementCriterion(player, "midgame/collect_stratine", "collected_stratine");
			Support.grantAdvancementCriterion(player, "midgame/break_decayed_bedrock", "broken_decayed_bedrock");
			Support.grantAdvancementCriterion(player, "midgame/crumble_midnight_aberration", "have_midnight_aberration_crumble");
			Support.grantAdvancementCriterion(player, "midgame/collect_neolith", "has_neolith");
			Support.grantAdvancementCriterion(player, "midgame/collect_azurite", "collected_azurite");
			Support.grantAdvancementCriterion(player, "collect_quitoxic_reeds", "collected_quitoxic_reeds");
			Support.grantAdvancementCriterion(player, "milestones/reveal_amaranth", "solved_color_mixing_preservation_ruin");
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
		return SpectrumBannerPatterns.MANUAL;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		addBannerPatternProviderTooltip(tooltip);
	}
	
}
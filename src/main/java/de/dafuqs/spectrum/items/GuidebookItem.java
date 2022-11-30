package de.dafuqs.spectrum.items;

import de.dafuqs.revelationary.advancement_criteria.AdvancementGottenCriterion;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBannerPatterns;
import io.wispforest.owo.Owo;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;
import java.util.Map;

public class GuidebookItem extends Item implements LoomPatternProvider {
	
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
							Advancement advancementCriterionAdvancement = Owo.currentServer().getAdvancementLoader().get(advancementIdentifier);
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
		PatchouliAPI.get().openBookGUI(serverPlayerEntity, SpectrumCommon.locate("guidebook"));
	}
	
	private void openGuidebook(ServerPlayerEntity serverPlayerEntity, Identifier entry, int page) {
		PatchouliAPI.get().openBookEntry(serverPlayerEntity, SpectrumCommon.locate("guidebook"), entry, page);
	}
	
	@Override
	public RegistryEntry<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.MANUAL;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
}
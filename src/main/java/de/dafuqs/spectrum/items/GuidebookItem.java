package de.dafuqs.spectrum.items;

import com.klikli_dev.modonomicon.client.gui.*;
import de.dafuqs.revelationary.advancement_criteria.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.client.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.entry.*;
import net.minecraft.server.network.*;
import net.minecraft.stat.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

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
			if (advancement.getId().getNamespace().startsWith(SpectrumCommon.MOD_ID)) {
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
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user instanceof ClientPlayerEntity clientPlayer) {
			// if the player has never opened the book before
			// automatically open the introduction page
			if (isNewPlayer(clientPlayer)) {
				openGuidebook(SpectrumCommon.locate("general/intro"), 0);
			} else {
				openGuidebook();
			}
			
			return TypedActionResult.success(user.getStackInHand(hand));
		} else if (user instanceof ServerPlayerEntity serverPlayerEntity) {
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			
			// Workaround for new advancement unlocks getting added after spectrum has been installed
			reprocessAdvancementUnlocks(serverPlayerEntity);
		}
		
		return TypedActionResult.consume(user.getStackInHand(hand));
	}
	
	private boolean isNewPlayer(ClientPlayerEntity user) {
		return user.getStatHandler().getStat(Stats.USED, this) == 0;
	}
	
	public void openGuidebook() {
		BookGuiManager.get().openBook(GUIDEBOOK_ID);
	}
	
	public void openGuidebook(Identifier entry, int page) {
		BookGuiManager.get().openEntry(GUIDEBOOK_ID, entry, page);
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

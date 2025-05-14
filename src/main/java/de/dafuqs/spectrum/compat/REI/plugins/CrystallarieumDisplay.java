package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CrystallarieumDisplay extends GatedSpectrumDisplay {
	
	protected final List<EntryIngredient> growthStages;
	protected final List<CrystallarieumCatalyst> catalysts;
	protected final InkColor inkColor;
	protected final boolean growsWithoutCatalyst;
	protected final int secondsPerStage;
	
	public CrystallarieumDisplay(@NotNull RecipeHolder<CrystallarieumRecipe> recipe) {
		super(recipe, inputs(recipe.value()), outputs(recipe.value()));
		
		this.growthStages = new ArrayList<>();
		for (BlockState state : recipe.value().getGrowthStages()) {
			growthStages.add(EntryIngredients.of(state.getBlock().asItem()));
		}
		this.catalysts = recipe.value().getCatalysts();
		this.inkColor = recipe.value().getInkColor();
		this.growsWithoutCatalyst = recipe.value().growsWithoutCatalyst();
		this.secondsPerStage = recipe.value().getSecondsPerGrowthStage();
	}
	
	public static List<EntryIngredient> inputs(CrystallarieumRecipe recipe) {
		List<EntryIngredient> inputs = new ArrayList<>();
		inputs.add(EntryIngredients.ofIngredient(recipe.getIngredientStack()));
		
		Item firstBlockStateItem = recipe.getGrowthStages().get(0).getBlock().asItem();
		if (firstBlockStateItem != Items.AIR) {
			inputs.add(EntryIngredients.of(firstBlockStateItem));
		}
		return inputs;
	}
	
	public static List<EntryIngredient> outputs(CrystallarieumRecipe recipe) {
		List<EntryIngredient> outputs = new ArrayList<>();
		outputs.add(EntryIngredients.of(recipe.getResultItem(BasicDisplay.registryAccess())));
		for (ItemStack additionalOutput : recipe.getAdditionalResults()) {
			outputs.add(EntryIngredients.of(additionalOutput));
		}
		
		for (BlockState growthStageState : recipe.getGrowthStages()) {
			Item blockStateItem = growthStageState.getBlock().asItem();
			if (blockStateItem != Items.AIR) {
				outputs.add(EntryIngredients.of(blockStateItem));
			}
		}
		return outputs;
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.CRYSTALLARIEUM;
	}
	
	@Override
    public boolean isUnlocked() {
		Minecraft client = Minecraft.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, CrystallarieumRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}

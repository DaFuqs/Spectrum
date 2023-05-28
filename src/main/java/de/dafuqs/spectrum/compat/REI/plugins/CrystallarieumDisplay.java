package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CrystallarieumDisplay extends GatedSpectrumDisplay {
	
	protected final List<EntryIngredient> growthStages;
	protected final List<CrystallarieumCatalyst> catalysts;
	protected final InkColor inkColor;
	protected final boolean growsWithoutCatalyst;
	protected final int secondsPerStage;
	
	public CrystallarieumDisplay(@NotNull CrystallarieumRecipe recipe) {
		super(recipe, inputs(recipe), outputs(recipe));

		this.growthStages = new ArrayList<>();
		for (BlockState state : recipe.getGrowthStages()) {
			growthStages.add(EntryIngredients.of(state.getBlock().asItem()));
		}
		this.catalysts = recipe.getCatalysts();
		this.inkColor = recipe.getInkColor();
		this.growsWithoutCatalyst = recipe.growsWithoutCatalyst();
		this.secondsPerStage = recipe.getSecondsPerGrowthStage();
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
		outputs.add(EntryIngredients.of(recipe.getOutput(null)));
		for (ItemStack additionalOutput : recipe.getAdditionalOutputs()) {
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
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, CrystallarieumRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}
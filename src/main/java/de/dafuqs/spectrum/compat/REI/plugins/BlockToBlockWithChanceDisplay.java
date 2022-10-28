package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.mixin.accessors.FluidBlockAccessor;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class BlockToBlockWithChanceDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	public static final Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_mob_blocks");
	protected boolean secret;
	public final float chance;
	
	public BlockToBlockWithChanceDisplay(EntryStack<?> in, EntryStack<?> out, float chance) {
		this(Collections.singletonList(EntryIngredient.of(in)), Collections.singletonList(EntryIngredient.of(out)), chance);
	}
	
	public BlockToBlockWithChanceDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, float chance) {
		super(inputs, outputs);
		this.chance = chance;
	}
	
	public static EntryStack blockToEntryStack(Block block) {
		if (block instanceof FluidBlock inFluidBlock) {
			return EntryStacks.of(((FluidBlockAccessor) inFluidBlock).getFlowableFluid());
		} else {
			return EntryStacks.of(block);
		}
		
	}
	
	public final EntryIngredient getIn() {
		return getInputEntries().get(0);
	}
	
	public final EntryIngredient getOut() {
		return getOutputEntries().get(0);
	}
	
	public final float getChance() {
		return chance;
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.FREEZING;
	}
	
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, UNLOCK_ADVANCEMENT_IDENTIFIER);
	}
	
	@Override
	public boolean isSecret() {
		return false;
	}
	
}
package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import javax.annotation.*;

import java.util.*;


public class CrystallarieumCategory extends GatedDisplayCategory<CrystallarieumDisplay> {
	
	public final static ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/crystallarieum.png");
	
	@Override
	public CategoryIdentifier<CrystallarieumDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.CRYSTALLARIEUM;
	}
	
	@Override
	public Component getTitle() {
		return Component.translatable("block.spectrum.crystallarieum");
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.CRYSTALLARIEUM);
	}
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, CrystallarieumDisplay display) {
		// input
		EntryIngredient input = display.getInputEntries().get(0);
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 2, startPoint.y + 1 + 8)).markInput().entries(input));
		
		// crystallarieum
		ItemStack crystallarieumStack = CrystallarieumBlock.withColor(SpectrumBlocks.CRYSTALLARIEUM.toStack(), display.inkColor);
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 20, startPoint.y + 3 + 16)).entries(EntryIngredients.of(crystallarieumStack)).disableBackground());
		
		// output arrow
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 39, startPoint.y + 1 + 8)).animationDurationTicks(display.secondsPerStage));
		
		// growth stages
		Iterator<EntryIngredient> it = display.growthStages.iterator();
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 20, startPoint.y + 3)).markInput().entries(it.next()));
		int x = 0;
		while (it.hasNext()) {
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 66 + x * 20, startPoint.y + 1 + 8)).markInput().entries(it.next()));
			x++;
		}
		
		// catalysts
		widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 44), Component.translatable("container.spectrum.rei.crystallarieum.catalyst")).leftAligned().color(0x3f3f3f).noShadow());
		widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 60), Component.translatable("container.spectrum.rei.crystallarieum.speed")).leftAligned().color(0x3f3f3f).noShadow());
		widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 70), Component.translatable("container.spectrum.rei.crystallarieum.ink_drain")).leftAligned().color(0x3f3f3f).noShadow());
		widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 80), Component.translatable("container.spectrum.rei.crystallarieum.depletion")).leftAligned().color(0x3f3f3f).noShadow());
		
		int i = 0;
		int startX = 46;
		int offsetPerReagent = 18;
		for (CrystallarieumCatalyst catalyst : display.catalysts) {
			int offsetX = startPoint.x + startX + offsetPerReagent * i;
			widgets.add(Widgets.createSlot(new Point(offsetX, startPoint.y + 1 + 38)).markInput().entries(EntryIngredients.ofIngredient(catalyst.ingredient())));
			
			int offsetU = CrystallarieumRecipe.growthSpeedOffsetU(catalyst);
			widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, offsetX + 5, startPoint.y + 60, offsetU, CrystallarieumRecipe.GROWTH_SPEED_V, 7, 7, 128, 128));
			
			offsetU = CrystallarieumRecipe.consumptionOffsetU(catalyst, offsetU);
			widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, offsetX + 5, startPoint.y + 70, offsetU, CrystallarieumRecipe.CONSUMPTION_V, 7, 7, 128, 128));
			
			offsetU = CrystallarieumRecipe.consumeChanceOffsetU(catalyst, offsetU);
			widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, offsetX + 5, startPoint.y + 80, offsetU, CrystallarieumRecipe.CONSUME_CHANCE_V, 7, 7, 128, 128));
			
			i++;
		}
		
		// description texts
		if (display.growsWithoutCatalyst) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 92), Component.translatable("container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds_catalyst_optional", display.secondsPerStage)).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 92), Component.translatable("container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds", display.secondsPerStage)).leftAligned().color(0x3f3f3f).noShadow());
		}
	}
	
	@Override
	public int getDisplayHeight() {
		return 109;
	}
	
}

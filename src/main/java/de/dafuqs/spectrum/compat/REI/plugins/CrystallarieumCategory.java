package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class CrystallarieumCategory extends GatedDisplayCategory<CrystallarieumDisplay> {
	
	public final static Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/crystallarieum.png");
	private static final EntryIngredient CRYSTALLARIEUM = EntryIngredients.of(SpectrumBlocks.CRYSTALLARIEUM);
	
	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.CRYSTALLARIEUM;
	}
	
	@Override
	public Text getTitle() {
		return Text.translatable("block.spectrum.crystallarieum");
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.CRYSTALLARIEUM);
	}
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull CrystallarieumDisplay display) {
		// input
		EntryIngredient input = display.getInputEntries().get(0);
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 2, startPoint.y + 1 + 8)).markInput().entries(input));
		
		// crystallarieum
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 20, startPoint.y + 3 + 16)).entries(CRYSTALLARIEUM).disableBackground());
		
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
		widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 1 + 42), Text.translatable("container.spectrum.rei.crystallarieum.catalyst")).leftAligned().color(0x3f3f3f).noShadow());
		widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 1 + 58), Text.translatable("container.spectrum.rei.crystallarieum.accelerator")).leftAligned().color(0x3f3f3f).noShadow());
		widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 1 + 68), Text.translatable("container.spectrum.rei.crystallarieum.ink_consumption")).leftAligned().color(0x3f3f3f).noShadow());
		widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 1 + 78), Text.translatable("container.spectrum.rei.crystallarieum.used_up")).leftAligned().color(0x3f3f3f).noShadow());
		
		int i = 0;
		int startX = 46;
		int offsetPerReagent = 18;
		for (CrystallarieumCatalyst catalyst : display.catalysts) {
			int offsetX = startPoint.x + startX + offsetPerReagent * i;
			widgets.add(Widgets.createSlot(new Point(offsetX, startPoint.y + 1 + 38)).markInput().entries(EntryIngredients.ofIngredient(catalyst.ingredient)));
			
			float growthAcceleration = catalyst.growthAccelerationMod;
			int offsetU = growthAcceleration == 1 ? 97 : growthAcceleration >= 6 ? 85 : growthAcceleration > 1 ? 67 : growthAcceleration <= 0.25 ? 79 : 73;
			widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, offsetX + 5, startPoint.y + 1 + 59, offsetU, 0, 6, 6, 128, 128));
			
			float inkConsumption = catalyst.inkConsumptionMod;
			offsetU = inkConsumption == 1 ? 97 : inkConsumption >= 8 ? 85 : inkConsumption > 1 ? 67 : inkConsumption <= 0.25 ? 79 : 73;
			widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, offsetX + 5, startPoint.y + 1 + 69, offsetU, 6, 6, 6, 128, 128));
			
			float consumeChance = catalyst.consumeChancePerSecond;
			offsetU = consumeChance == 0 ? 97 : consumeChance >= 0.2 ? 85 : consumeChance >= 0.05 ? 67 : 91;
			widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, offsetX + 5, startPoint.y + 1 + 79, offsetU, 6, 6, 6, 128, 128));
			
			i++;
		}
		
		// description texts
		if (display.growsWithoutCatalyst) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 1 + 90), Text.translatable("container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds", display.secondsPerStage)).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 1 + 90), Text.translatable("container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds_catalyst_required", display.secondsPerStage)).leftAligned().color(0x3f3f3f).noShadow());
		}
	}
	
	@Override
	public int getDisplayHeight() {
		return 108;
	}
	
}

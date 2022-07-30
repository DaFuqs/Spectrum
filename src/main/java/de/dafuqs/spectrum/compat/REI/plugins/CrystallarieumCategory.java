package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumCatalyst;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.fluid.Fluids;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class CrystallarieumCategory implements DisplayCategory<CrystallarieumDisplay> {
	
	public final static Identifier BACKGROUND_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/patchouli/crystallarieum.png");
	private static final EntryIngredient CRYSTALLARIEUM = EntryIngredients.of(SpectrumBlocks.CRYSTALLARIEUM);
	
	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.CRYSTALLARIEUM;
	}
	
	@Override
	public Text getTitle() {
		return new TranslatableText("block.spectrum.crystallarieum");
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.CRYSTALLARIEUM);
	}
	
	@Override
	public List<Widget> setupDisplay(@NotNull CrystallarieumDisplay display, @NotNull Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 68, bounds.getCenterY() - 49);
		List<Widget> widgets = Lists.newArrayList();
		
		widgets.add(Widgets.createRecipeBase(bounds));
		
		if (!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 33), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 43), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			// input
			EntryIngredient input = display.getInputEntries().get(0);
			widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y + 8)).markInput().entries(input));
			
			// crystallarieum
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 20, startPoint.y + 16)).entries(CRYSTALLARIEUM).disableBackground());
			
			// output arrow
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 39, startPoint.y + 8)).animationDurationTicks(display.secondsPerStage));
			
			// growth stages
			Iterator<EntryIngredient> it = display.growthStages.iterator();
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 20, startPoint.y)).markInput().entries(it.next()));
			int x = 0;
			while(it.hasNext()) {
				widgets.add(Widgets.createSlot(new Point(startPoint.x + 66 + x * 20, startPoint.y + 8)).markInput().entries(it.next()));
				x++;
			}
			
			// catalysts
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 42), new TranslatableText("container.spectrum.rei.crystallarieum.catalyst")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 58), new TranslatableText("container.spectrum.rei.crystallarieum.accelerator")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 68), new TranslatableText("container.spectrum.rei.crystallarieum.ink_consumption")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 78), new TranslatableText("container.spectrum.rei.crystallarieum.used_up")).leftAligned().color(0x3f3f3f).noShadow());
			
			int i = 0;
			int startX = 46;
			int offsetPerReagent = 18;
			for(CrystallarieumCatalyst catalyst : display.catalysts) {
				int offsetX = startPoint.x + startX + offsetPerReagent * i;
				widgets.add(Widgets.createSlot(new Point(offsetX, startPoint.y + 38)).markInput().entries(EntryIngredients.ofIngredient(catalyst.ingredient)));
				
				float growthAcceleration = catalyst.growthAccelerationMod;
				int offsetU = growthAcceleration == 1 ? 97 : growthAcceleration >= 6 ? 85 : growthAcceleration > 1 ? 67 : growthAcceleration <= 0.25 ? 79 : 73;
				widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, offsetX + 5, startPoint.y + 59, offsetU, 0, 6, 6, 128, 128));
				
				float inkConsumption = catalyst.inkConsumptionMod;
				offsetU = inkConsumption == 1 ? 97 : inkConsumption >= 8 ? 85 : inkConsumption > 1 ? 67 : inkConsumption <= 0.25 ? 79 : 73;
				widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, offsetX + 5, startPoint.y + 69, offsetU, 6, 6, 6, 128, 128));
				
				float consumeChance = catalyst.consumeChancePerSecond;
				offsetU = consumeChance == 0 ? 97 : consumeChance >= 0.2 ? 85 : consumeChance >= 0.05 ? 67 : 91;
				widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, offsetX + 5, startPoint.y + 79, offsetU, 6, 6, 6, 128, 128));
				
				i++;
			}
			
			// description texts
			if (display.growsWithoutCatalyst) {
				widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 90), new TranslatableText("container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds", display.secondsPerStage)).leftAligned().color(0x3f3f3f).noShadow());
			} else {
				widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 90), new TranslatableText("container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds_catalyst_required", display.secondsPerStage)).leftAligned().color(0x3f3f3f).noShadow());
			}
		}
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		return 112;
	}
	
}

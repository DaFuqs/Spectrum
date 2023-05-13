package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class InkConvertingCategory extends GatedDisplayCategory<InkConvertingDisplay> {
	
	@Override
	public CategoryIdentifier<? extends InkConvertingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.INK_CONVERTING;
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.COLOR_PICKER);
	}
	
	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.ink_converting.title");
	}
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull InkConvertingDisplay display) {
		// input slot
		widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y + 2)).markInput().entries(display.getInputEntries().get(0)));
		
		// output arrow
		widgets.add(Widgets.createArrow(new Point(startPoint.x - 8 + 30, startPoint.y + 2)));
		
		// output amount & required time
		Text colorText = Text.translatable("container.spectrum.rei.ink_converting.color", display.color.getName());
		Text amountText = Text.translatable("container.spectrum.rei.ink_converting.amount", display.amount);
		widgets.add(Widgets.createLabel(new Point(startPoint.x - 8 + 58, startPoint.y + 1), colorText).leftAligned().color(0x3f3f3f).noShadow());
		widgets.add(Widgets.createLabel(new Point(startPoint.x - 8 + 58, startPoint.y + 14), amountText).leftAligned().color(0x3f3f3f).noShadow());
	}
	
	@Override
	public int getDisplayHeight() {
		return 32;
	}
	
}

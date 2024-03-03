package de.dafuqs.spectrum.compat.REI.plugins;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.compat.REI.widgets.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import me.shedaniel.rei.impl.*;
import me.shedaniel.rei.impl.client.gui.widget.basewidgets.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class PrimordialFireBurningCategory extends GatedDisplayCategory<PrimordialFireBurningDisplay> {
	
	private final static Identifier FIRE_TEXTURE = SpectrumCommon.locate("textures/block/primordial_fire_0.png");
	
	@Override
	public CategoryIdentifier<PrimordialFireBurningDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.PRIMORDIAL_FIRE_BURNING;
	}
	
	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.primordial_fire_burning.title");
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumItems.DOOMBLOOM_SEED);
	}
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull PrimordialFireBurningDisplay display) {
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 81, startPoint.y + 9)));
		widgets.add(new AnimatedTexturedWidget(FIRE_TEXTURE, new Rectangle(startPoint.x + 18, startPoint.y + 20, 0, 0), 16, 176).animationDurationMS(1000));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 41, startPoint.y + 8)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 78, startPoint.y + 9)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 18, startPoint.y + 1)).entries(display.getInputEntries().get(0)).markInput());
	}
	
	@Override
	public int getDisplayHeight() {
		return 46;
	}
	
}

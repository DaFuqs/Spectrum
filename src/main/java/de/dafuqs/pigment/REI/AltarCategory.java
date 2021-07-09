package de.dafuqs.pigment.REI;

import com.google.common.collect.Lists;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.inventories.AltarScreen;
import de.dafuqs.pigment.recipe.altar.AltarCraftingRecipe;
import de.dafuqs.pigment.registries.PigmentBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.DisplayRenderer;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.SimpleDisplayRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class AltarCategory<R extends AltarCraftingRecipe> implements DisplayCategory<AltarCraftingRecipeDisplay<R>> {

    final Identifier GUI_TEXTURE = AltarScreen.BACKGROUND;

    public static final CategoryIdentifier<AltarCraftingRecipeDisplay> ID = CategoryIdentifier.of(new Identifier(PigmentCommon.MOD_ID, "altar_crafting"));

    @Override
    public CategoryIdentifier getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("container.pigment.rei.altar_crafting.title");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(PigmentBlocks.ALTAR);
    }

    @Override
    public DisplayRenderer getDisplayRenderer(AltarCraftingRecipeDisplay<R> recipe) {
        return SimpleDisplayRenderer.from(Collections.singletonList(recipe.getInputEntries().get(0)), recipe.getOutputEntries());
    }

    @Override
    public List<Widget> setupDisplay(AltarCraftingRecipeDisplay display, Rectangle bounds) {

        Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 43);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 19)));
        List<? extends List<? extends EntryStack<?>>> input = display.getInputEntries();

        // crafting grid slots
        List<Slot> slots = Lists.newArrayList();
        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++)
                slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18)).markInput());
        for (int i = 0; i < 9; i++) {
            if (!input.get(i).isEmpty()) {
                slots.get(DefaultCraftingDisplay.getSlotWithSize(3, i, 3)).entries(input.get(i));
            }
        }
        // gemstone dust slots
        for (int x = 0; x < 5; x++) {
            slots.add(Widgets.createSlot(new Point(bounds.getCenterX() + x * 18 - 45, startPoint.y + 60)).markInput());
            if (!input.get(9+x).isEmpty()) {
                slots.get(DefaultCraftingDisplay.getSlotWithSize(3, 9 + x, 3)).entries(input.get(9 + x));
            }
        }
        widgets.addAll(slots);

        // Output
        List<EntryIngredient> results = display.getOutputEntries();
        EntryIngredient result = EntryIngredient.of(results.get(0));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 19)).entries(result).disableBackground().markOutput());

        // special handling for "1 second". Looks nicer
        TranslatableText text;
        if(display.craftingTime == 20) {
            text = new TranslatableText("container.pigment.altar.rei.crafting_time_one_second_and_xp", 1, display.experience);
        } else {
            text = new TranslatableText("container.pigment.altar.rei.crafting_time_and_xp", (display.craftingTime / 20), display.experience);
        }
        widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 82), text).leftAligned().color(0x3f3f3f).noShadow());

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 110;
    }

}

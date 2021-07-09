package de.dafuqs.pigment.REI;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.recipe.anvil_crushing.AnvilCrushingRecipe;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;

public class AnvilCrushingCategory<R extends AnvilCrushingRecipe> implements DisplayCategory<AnvilCrushingRecipeDisplay<R>> {

    public static final CategoryIdentifier<AnvilCrushingRecipeDisplay> ID = CategoryIdentifier.of(new Identifier(PigmentCommon.MOD_ID, "anvil_crushing"));

    @Override
    public CategoryIdentifier getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("container.pigment.rei.anvil_crushing.title");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.ANVIL);
    }

    @Override
    public List<Widget> setupDisplay(AnvilCrushingRecipeDisplay display, Rectangle bounds) {
        /*Point origin = bounds.getLocation();

        final var widgets = new ArrayList<Widget>();

        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createTexturedWidget(GUI_TEXTURE, origin.x + 10, origin.y + 18, 42, 5, 124, 58));
        widgets.add(Widgets.createTexturedWidget(GUI_TEXTURE, origin.x + 115, origin.y + 21, 176, 0, 15, 19));

        for (int i = 0; i < display.getInputEntries().size(); i++) {
            final var slotLocation = new Point(origin.x + 12 + i % 5 * 18, origin.y + 40 + (i > 4 ? 1 : 0) * 18);
            widgets.add(Widgets.createSlot(slotLocation).entries(display.getInputEntries().get(i)).markInput().disableBackground());
            widgets.add(Widgets.createTexturedWidget(GUI_TEXTURE, slotLocation.x - 1, slotLocation.y - 1, 208, 0, 18, 18));
        }

        final var resultSlot = Widgets.createSlot(new Point(origin.x + 113, origin.y + 47));
        widgets.add(resultSlot.entries(display.getOutputEntries().get(0)).disableBackground().markOutput());

        final var tierLabel = Widgets.createLabel(new Point(origin.x + 12, origin.y + 11), new TranslatableText("container.alloy_forgery.rei.min_tier", display.minForgeTier));
        widgets.add(tierLabel.leftAligned().color(0x3f3f3f).noShadow());
        widgets.add(Widgets.createLabel(new Point(origin.x + 12, origin.y + 24), new TranslatableText("container.alloy_forgery.rei.fuel_per_tick", display.requiredFuel)).leftAligned().color(0x3f3f3f).noShadow());

        final MutableInt overrideIndex = new MutableInt(1);
        final List<AlloyForgeRecipe.OverrideRange> overrides = new ArrayList<>(display.overrides.keySet());

        widgets.add(Widgets.createButton(new Rectangle(origin.x + 131, origin.y + 6, 12, 12), Text.of("...")).onClick(button -> {
            int index = overrideIndex.intValue();
            tierLabel.setMessage(new TranslatableText("container.alloy_forgery.rei.min_tier", index == 0 ? display.minForgeTier : overrides.get(index - 1)));

            resultSlot.clearEntries();
            resultSlot.entries(index == 0 ? display.getOutputEntries().get(0) : EntryIngredients.of(display.overrides.get(overrides.get(index - 1))));

            overrideIndex.increment();
            if (overrideIndex.intValue() - 1 > overrides.size() - 1) overrideIndex.setValue(0);
        }).tooltipLine(Text.of("Cycle Tier Overrides")).enabled(overrides.size() != 0));

        return widgets;*/
        return null;
    }

}

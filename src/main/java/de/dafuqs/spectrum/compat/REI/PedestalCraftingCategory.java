package de.dafuqs.spectrum.compat.REI;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.inventories.PedestalScreen;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
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

public class PedestalCraftingCategory<R extends PedestalCraftingRecipe> implements DisplayCategory<PedestalCraftingRecipeDisplay<R>> {

    public static final CategoryIdentifier ID = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "pedestal_crafting"));

    @Override
    public CategoryIdentifier getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(SpectrumCommon.MOD_ID, "pedestal_crafting");
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("container.spectrum.rei.pedestal_crafting.title");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST);
    }

    @Override
    public DisplayRenderer getDisplayRenderer(PedestalCraftingRecipeDisplay<R> recipe) {
        return SimpleDisplayRenderer.from(Collections.singletonList(recipe.getInputEntries().get(0)), recipe.getOutputEntries());
    }

    @Override
    public List<Widget> setupDisplay(PedestalCraftingRecipeDisplay display, Rectangle bounds) {

        Identifier backgroundTexture = PedestalScreen.getBackgroundTextureForTier(display.getTier());

        Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 43);
        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(bounds));

        // Searching for the usage or recipes for items will not trigger an
        // pedestal crafting recipe display. Searching for all recipes, that can
        // be triggered with a pedestal will, though.
        //
        // For the sake of not spoiling the surprise there will just be
        // a placeholder displayed instead of the actual recipe.
        //
        // It would be way better to just skip not unlocked recipes altogether.
        // but howwwwww... TODO
        if(!display.isUnlocked()) {
            widgets.add(Widgets.createLabel(new Point(startPoint.x - 6, startPoint.y + 33), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
            widgets.add(Widgets.createLabel(new Point(startPoint.x - 6, startPoint.y + 43), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
        } else {
            widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18)));

            // crafting grid slots
            List<Slot> slots = Lists.newArrayList();
            for (int y = 0; y < 3; y++)
                for (int x = 0; x < 3; x++)
                    slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18)).disableBackground().markInput());

            // set crafting slot contents
            List<? extends List<? extends EntryStack<?>>> input = display.getInputEntries();
            int gemstoneDustStartSlot = display.getHeight() * display.getWidth();
            for (int i = 0; i < gemstoneDustStartSlot; i++) {
                if (!input.get(i).isEmpty()) {
                    slots.get(DefaultCraftingDisplay.getSlotWithSize(display.getWidth(), i, 3)).disableBackground().entries(input.get(i));
                }
            }

            // gemstone dust slots
            int gemstoneSlotCount = display.getTier() == PedestalRecipeTier.COMPLEX ? 5 : display.getTier() == PedestalRecipeTier.ADVANCED ? 4 : 3;
            int gemstoneSlotStartX = gemstoneSlotCount == 5 ? -45 : gemstoneSlotCount == 4 ? -40 : -31;
            int gemstoneSlotTextureStartX = gemstoneSlotCount == 5 ? 43 : gemstoneSlotCount == 4 ? 52 : 61;
            for (int x = 0; x < gemstoneSlotCount; x++) {
                slots.add(Widgets.createSlot(new Point(bounds.getCenterX() + x * 18 + gemstoneSlotStartX, startPoint.y + 60)).disableBackground().markInput());
                if (!input.get(gemstoneDustStartSlot + x).isEmpty()) {
                    slots.get(9 + x).entries(input.get(gemstoneDustStartSlot + x));
                }
            }
            widgets.addAll(slots);

            // output
            List<EntryIngredient> results = display.getOutputEntries();
            EntryIngredient result = EntryIngredient.of(results.get(0));
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 19)).entries(result).disableBackground().markOutput());

            // the gemstone slot background texture                  destinationX                 destinationY       sourceX, sourceY, width, height
            widgets.add(Widgets.createTexturedWidget(backgroundTexture, bounds.getCenterX() + gemstoneSlotStartX - 1, startPoint.y + 59, gemstoneSlotTextureStartX, 76, 18 * gemstoneSlotCount, 18));
            // crafting input texture
            widgets.add(Widgets.createTexturedWidget(backgroundTexture, startPoint.x, startPoint.y, 29, 18, 54, 54));
            // crafting output texture
            widgets.add(Widgets.createTexturedWidget(backgroundTexture, startPoint.x + 94 - 4, startPoint.y + 18 - 4, 122, 32, 26, 26));
            // miniature gemstones texture
            widgets.add(Widgets.createTexturedWidget(backgroundTexture, startPoint.x + 94 - 12, startPoint.y + 18 + 20, 200, 0, 40, 16));

            // description text
            // special handling for "1 second". Looks nicer
            TranslatableText text;
            if (display.craftingTime == 20) {
                text = new TranslatableText("container.spectrum.rei.pedestal_crafting.crafting_time_one_second_and_xp", 1, display.experience);
            } else {
                text = new TranslatableText("container.spectrum.rei.pedestal_crafting.crafting_time_and_xp", (display.craftingTime / 20), display.experience);
            }
            widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 82), text).leftAligned().color(0x3f3f3f).noShadow());
        }
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 110;
    }

}

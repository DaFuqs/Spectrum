package de.dafuqs.pigment;

import net.minecraft.advancement.Advancement;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PigmentAltarCraftingAdvancements {

    private static final Map<Item, Identifier> craftAdvancementIdentifierItems = new HashMap<>();

    public static void setup() {
        registerCraftingAdvancementItem(PigmentItems.DECAY_1_PLACER, new Identifier(PigmentCommon.MOD_ID, "craft_decay"));
        registerCraftingAdvancementItem(PigmentItems.DECAY_2_PLACER, new Identifier(PigmentCommon.MOD_ID, "craft_decay"));
        registerCraftingAdvancementItem(PigmentItems.DECAY_3_PLACER, new Identifier(PigmentCommon.MOD_ID, "craft_decay"));
        registerCraftingAdvancementItem(PigmentBlocks.OMINOUS_SAPLING.asItem(), new Identifier(PigmentCommon.MOD_ID, "craft_decay"));

        // one of the 16 colored saplings
        for(DyeColor dyeColor : DyeColor.values()) {
            Item coloredSapling = PigmentBlocks.getColoredSaplingItem(dyeColor);
            registerCraftingAdvancementItem(coloredSapling, new Identifier(PigmentCommon.MOD_ID, "craft_colored_sapling"));
        }
    }

    public static void registerCraftingAdvancementItem(Item item, Identifier advancementIdentifier) {
        if(!craftAdvancementIdentifierItems.containsKey(item)) {
            craftAdvancementIdentifierItems.put(item, advancementIdentifier);
        } else {
            PigmentCommon.log(Level.ERROR, "Registering an Item as craftingAdvancementIngredient twice!: " + item.getTranslationKey());
        }
    }

    public static boolean hasCraftingAdvancement(Item craftedItem) {
        return craftAdvancementIdentifierItems.containsKey(craftedItem);
    }

    @Nullable
    public static Advancement getCraftingAdvancement(Item craftedItem) {
        Advancement advancement = PigmentCommon.minecraftServer.getAdvancementLoader().get(craftAdvancementIdentifierItems.get(craftedItem));
        if(advancement == null) {
            PigmentCommon.log(Level.ERROR, "Item '" + craftedItem.getTranslationKey() + "'was registered as having an altar crafting advancement, but that does not exist: " + craftAdvancementIdentifierItems.get(craftedItem).toString());
        }
        return advancement;
    }
}

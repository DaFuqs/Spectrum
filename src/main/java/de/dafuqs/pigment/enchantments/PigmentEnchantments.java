package de.dafuqs.pigment.enchantments;

import de.dafuqs.pigment.PigmentCommon;
import net.minecraft.enchantment.BindingCurseEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PigmentEnchantments {

    public static final Enchantment RESONANCE = new ResonanceEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND); // Silk Touch, just for different blocks
    public static final Enchantment PEST_CONTROL = new PestControlEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND); // Kills silverfish when mining infested blocks
    public static final Enchantment AUTO_SMELT = new AutoSmeltEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // applies smelting recipe before dropping items after mining
    public static final Enchantment INVENTORY_INSERTION = new InventoryInsertionEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // don't drop items into the world, add to inv instead
    public static final Enchantment VOIDING = new VoidingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // Voids all items mined
    public static final Enchantment EXUBERANCE = new ExuberanceEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND); // Drops more XP on kill
    public static final Enchantment TREASURE_HUNTER = new TreasureHunterEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // Drops mob heads

    public static void register() {
        Registry.register(Registry.ENCHANTMENT, new Identifier(PigmentCommon.MOD_ID, "resonance"), RESONANCE);
        Registry.register(Registry.ENCHANTMENT, new Identifier(PigmentCommon.MOD_ID, "pest_control"), PEST_CONTROL);
        Registry.register(Registry.ENCHANTMENT, new Identifier(PigmentCommon.MOD_ID, "autosmelt"), AUTO_SMELT);
        Registry.register(Registry.ENCHANTMENT, new Identifier(PigmentCommon.MOD_ID, "inventory_insertion"), INVENTORY_INSERTION);
        Registry.register(Registry.ENCHANTMENT, new Identifier(PigmentCommon.MOD_ID, "voiding"), VOIDING);
        Registry.register(Registry.ENCHANTMENT, new Identifier(PigmentCommon.MOD_ID, "exuberance"), EXUBERANCE);
        Registry.register(Registry.ENCHANTMENT, new Identifier(PigmentCommon.MOD_ID, "treasure_hunter"), TREASURE_HUNTER);
    }

}

package de.dafuqs.spectrum.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import de.dafuqs.spectrum.api.item.SplittableItem;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DragonTalonItem extends SwordItem implements SplittableItem {

    private final Multimap<EntityAttribute, EntityAttributeModifier> phantomModifiers;

    public DragonTalonItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);

        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> phantom = ImmutableMultimap.builder();
        phantom.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", -4.0, EntityAttributeModifier.Operation.ADDITION));
        this.phantomModifiers = phantom.build();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        if (slot != EquipmentSlot.MAINHAND)
            return super.getAttributeModifiers(slot);

        var nbt = stack.getOrCreateNbt();
        if (nbt.getBoolean("cooldown"))
            return phantomModifiers;

        return super.getAttributeModifiers(slot);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.spectrum.dragon_talon.tooltip").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.spectrum.dragon_talon.tooltip2").formatted(Formatting.GRAY));
    }

    @Override
    public ItemStack getResult(ServerPlayerEntity player, ItemStack parent) {
        var stack = new ItemStack(SpectrumItems.DRAGON_NEEDLE);
        stack.setNbt(parent.getNbt());
        sign(player, stack);
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            var nbt = stack.getOrCreateNbt();
            if (player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
                if (!nbt.getBoolean("cooldown")) {
                    nbt.putBoolean("cooldown", true);
                }
            }
            else if(nbt.contains("cooldown")) {
                nbt.remove("cooldown");
            }
        }
    }

    @Override
    public boolean canSplit(ServerPlayerEntity player, Hand occupiedHand, ItemStack stack) {
        if (player.getItemCooldownManager().isCoolingDown(stack.getItem()))
            return false;

        return switch (occupiedHand) {
            case MAIN_HAND -> player.getStackInHand(Hand.OFF_HAND).isEmpty();
            case OFF_HAND -> player.getStackInHand(Hand.MAIN_HAND).isEmpty();
        };
    }

    @Override
    public SoundEvent getSplitSound() {
        return SoundEvents.ITEM_LODESTONE_COMPASS_LOCK;
    }
}

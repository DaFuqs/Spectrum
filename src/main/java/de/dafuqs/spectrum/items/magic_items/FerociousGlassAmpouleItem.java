package de.dafuqs.spectrum.items.magic_items;

import com.google.common.collect.*;
import com.jamieswhiteshirt.reachentityattributes.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FerociousGlassAmpouleItem extends Item {
    
    protected static final float EXTRA_REACH = 8.0F;
    protected static final UUID REACH_MODIFIER_ID = UUID.fromString("c81a7152-313c-452f-b15e-fcf51322ccc0");
    
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    
    public FerociousGlassAmpouleItem(Settings settings) {
        super(settings);
        
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(REACH_MODIFIER_ID, "Weapon modifier", EXTRA_REACH, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }
    
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getAttributeModifiers(slot);
    }
    
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = attacker.getWorld();
        if (!world.isClient) {
            LightSpearEntity.summonBarrage(world, attacker, target);
        }
        
        return true;
    }
    
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();
        if (!world.isClient) {
            LightSpearEntity.summonBarrage(world, user, entity);
        }
        if (!user.isCreative()) {
            stack.decrement(1);
        }
        
        return ActionResult.success(world.isClient);
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.spectrum.ferocious_glass_ampoule.tooltip").formatted(Formatting.GRAY));
    }
    
}

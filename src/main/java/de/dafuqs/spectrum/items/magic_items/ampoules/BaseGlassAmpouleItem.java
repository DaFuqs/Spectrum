package de.dafuqs.spectrum.items.magic_items.ampoules;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public abstract class BaseGlassAmpouleItem extends Item {
    
    public BaseGlassAmpouleItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (trigger(stack, user, null)) {
            if (!user.isCreative()) {
                stack.decrement(1);
            }
        }
        return user.isCreative() ? super.use(world, user, hand) : TypedActionResult.consume(stack);
    }
    
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (trigger(stack, attacker, target)) {
            if (!(attacker instanceof PlayerEntity player && player.isCreative())) {
                stack.decrement(1);
            }
        }
        return super.postHit(stack, target, attacker);
    }
    
    public abstract boolean trigger(ItemStack stack, LivingEntity attacker, @Nullable LivingEntity target);
    
}

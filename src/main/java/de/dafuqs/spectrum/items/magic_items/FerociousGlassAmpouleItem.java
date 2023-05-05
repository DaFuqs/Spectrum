package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class FerociousGlassAmpouleItem extends Item {
    
    public FerociousGlassAmpouleItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        
        if (!world.isClient) {
            LightSpearEntity.summonBarrage(world, user, null);
        }
        
        return user.isCreative() ? super.use(world, user, hand) : TypedActionResult.consume(stack);
    }
    
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        LightSpearEntity.summonBarrage(attacker.getWorld(), attacker, target);
        
        if (!(attacker instanceof PlayerEntity player && player.isCreative()))
            stack.decrement(1);
        
        return super.postHit(stack, target, attacker);
    }
    
}

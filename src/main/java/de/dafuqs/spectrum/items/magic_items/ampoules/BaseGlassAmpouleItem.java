package de.dafuqs.spectrum.items.magic_items.ampoules;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public abstract class BaseGlassAmpouleItem extends Item {
    
    public BaseGlassAmpouleItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
		if (trigger(user.getWorld(), stack, user, null, user.getEyePos())) {
            if (!user.isCreative()) {
                stack.decrement(1);
            }
			return TypedActionResult.success(stack);
        }
		
		return world.isClient() ? TypedActionResult.fail(stack) : TypedActionResult.pass(stack);
    }
    
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		World world = user.getWorld();
		if (trigger(user.getWorld(), stack, user, entity, user.getEyePos())) {
			if (!user.getWorld().isClient) {
				if (!(user.isCreative())) {
					stack.decrement(1);
				}
			}
			return ActionResult.success(world.isClient);
		}
		
		return world.isClient() ? ActionResult.FAIL : ActionResult.PASS;
    }
	
	public abstract boolean trigger(World world, ItemStack stack, LivingEntity attacker, @Nullable LivingEntity target, Vec3d position);
    
}

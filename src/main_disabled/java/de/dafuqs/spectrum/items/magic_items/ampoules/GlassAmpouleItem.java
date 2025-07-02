package de.dafuqs.spectrum.items.magic_items.ampoules;

import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public abstract class GlassAmpouleItem extends Item {
	
	public GlassAmpouleItem(Properties settings) {
        super(settings);
    }
    
    @Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);
		if (trigger(user.level(), stack, user, null, user.getEyePosition())) {
			if (!user.isCreative()) {
				stack.shrink(1);
			}
			return InteractionResultHolder.success(stack);
		}
		
		return world.isClientSide() ? InteractionResultHolder.fail(stack) : InteractionResultHolder.pass(stack);
    }
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
		Level world = user.level();
		if (trigger(user.level(), stack, user, entity, user.getEyePosition())) {
			if (!user.level().isClientSide) {
				if (!(user.isCreative())) {
					stack.shrink(1);
				}
			}
			return InteractionResult.sidedSuccess(world.isClientSide);
		}
		
		return world.isClientSide() ? InteractionResult.FAIL : InteractionResult.PASS;
	}
	
	public abstract boolean trigger(Level world, ItemStack stack, LivingEntity attacker, @Nullable LivingEntity target, Vec3 position);
    
}

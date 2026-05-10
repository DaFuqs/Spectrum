package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import javax.annotation.*;

import java.util.*;

public class PipeBombItem extends Item implements DamageAwareItem, TickAwareItem {
	
	public PipeBombItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (isPrimed(user.getItemInHand(hand))) {
			return super.use(world, user, hand);
		}

        if (world.isClientSide()) {
            startSoundInstance(user);
        }
		return ItemUtils.startUsingInstantly(world, user, hand);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void startSoundInstance(Player user) {
		Minecraft.getInstance().getSoundManager().play(new PipeBombChargingSoundInstance(user));
	}
	
	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 55;
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		prime(stack, world, user.position(), user);
		return stack;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (world instanceof ServerLevel serverWorld) {
			if (isPrimeTimeElapsed(world, stack)) {
				explode(stack, serverWorld, entity.position(), entity);
			}
		}
	}
	
	@Override
	public void onItemEntityTicked(ItemEntity itemEntity) {
		var stack = itemEntity.getItem();
		if (itemEntity.level() instanceof ServerLevel world) {
			if (isPrimeTimeElapsed(world, stack))
				explode(stack, world, itemEntity.getEyePosition(), null);
		}
	}
	
	@Override
	public void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity) {
		if (itemEntity.level() instanceof ServerLevel world) {
			if (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_EXPLOSION))
				explode(itemEntity.getItem(), world, itemEntity.position(), null);
		}
	}
	
	private void explode(ItemStack stack, ServerLevel world, Vec3 pos, @Nullable Entity target) {
		stack.shrink(1);
		Entity owner = tryGetOwner(stack, world);
		
		if (target != null)
			target.hurt(SpectrumDamageTypes.incandescence(world, owner instanceof LivingEntity living ? living : null), 200F);
		world.explode(null, SpectrumDamageTypes.incandescence(world), new ExplosionDamageCalculator(), pos.x(), pos.y(), pos.z(), 7.5F, true, Level.ExplosionInteraction.NONE);
	}
	
	public Entity tryGetOwner(ItemStack stack, ServerLevel world) {
		var profile = stack.get(DataComponents.PROFILE);
		if (profile == null || profile.id().isEmpty())
			return null;
		return world.getEntity(profile.id().get());
	}
	
	public static void prime(ItemStack stack, Level world, Vec3 pos, @Nullable Entity user) {
		world.playSound(null, pos.x(), pos.y(), pos.z(), SpectrumSoundEvents.INCANDESCENT_ARM, SoundSource.PLAYERS, 2F, 0.9F);
		stack.set(SpectrumDataComponentTypes.TIMESTAMP, world.getGameTime());
		if (user instanceof Player player) {
			stack.set(DataComponents.PROFILE, new ResolvableProfile(player.getGameProfile()));
		}
	}
	
	public static boolean isPrimed(ItemStack stack) {
		return stack.get(SpectrumDataComponentTypes.TIMESTAMP) != null;
	}
	
	public static boolean isPrimeTimeElapsed(Level world, ItemStack stack) {
		Optional<Long> timestamp = getPrimeTime(stack);
		if (timestamp.isEmpty()) {
			return false;
		}
		return world.getGameTime() - timestamp.get() >= 100;
	}
	
	private static Optional<Long> getPrimeTime(ItemStack stack) {
		if (stack.has(SpectrumDataComponentTypes.TIMESTAMP)) {
			return Optional.of(stack.get(SpectrumDataComponentTypes.TIMESTAMP));
		}
		return Optional.empty();
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.pipe_bomb.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.pipe_bomb.tooltip2").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.pipe_bomb.tooltip3").withStyle(ChatFormatting.GRAY));
	}
	
}

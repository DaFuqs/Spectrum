package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import de.dafuqs.spectrum.spells.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GlassCrestGreatswordItem extends GreatswordItem implements SplitDamageItem {
	
	private static final InkCost GROUND_SLAM_COST_PER_TICK = new InkCost(InkColors.WHITE, 2);
	public static final float MAGIC_DAMAGE_SHARE = 0.25F;
	public final int GROUND_SLAM_CHARGE_TICKS = 32;
	
	public GlassCrestGreatswordItem(ToolMaterial material, int attackDamage, float attackSpeed, float extraReach, Settings settings) {
		super(material, attackDamage, attackSpeed, extraReach, settings);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.glass_crest_ultra_greatsword.tooltip", (int) (MAGIC_DAMAGE_SHARE * 100)));
		tooltip.add(Text.translatable("item.spectrum.glass_crest_ultra_greatsword.tooltip2"));
		tooltip.add(Text.translatable("spectrum.tooltip.ink_powered.white"));
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (world.isClient) {
			startSoundInstance(user);
		}
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return GROUND_SLAM_CHARGE_TICKS;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.SPEAR;
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		super.usageTick(world, user, stack, remainingUseTicks);
		if (world.isClient && getGroundSlamStrength(stack) > 0 && user instanceof PlayerEntity player && InkPowered.tryDrainEnergy(player, GROUND_SLAM_COST_PER_TICK)) {
			Random random = world.random;
			for (int i = 0; i < (GROUND_SLAM_CHARGE_TICKS - remainingUseTicks) / 8; i++) {
				world.addParticle(ParticleTypes.INSTANT_EFFECT,
						user.getParticleX(1.0), user.getY(), user.getParticleZ(1.0),
						random.nextDouble() * 5.0D - 2.5D, random.nextDouble() * 1.2D, random.nextDouble() * 5.0D - 2.5D);
			}
		}
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient) {
			int groundSlamStrength = getGroundSlamStrength(stack);
			if (groundSlamStrength > 0) {
				performGroundSlam(world, user.getPos(), user, groundSlamStrength);
				stack.damage(1, user, (p) -> p.sendToolBreakStatus(user.getActiveHand()));
			}
		}
		
		return stack;
	}
	
	public int getGroundSlamStrength(ItemStack stack) {
		return EnchantmentHelper.getLevel(Enchantments.SWEEPING, stack);
	}
	
	public void performGroundSlam(World world, Vec3d pos, LivingEntity attacker, float strength) {
		world.emitGameEvent(attacker, GameEvent.ENTITY_ROAR, BlockPos.ofFloored(pos.x, pos.y, pos.z));
		MoonstoneStrike.create(world, attacker, null, attacker.getX(), attacker.getY(), attacker.getZ(), strength, 1.75F);
		world.playSound(null, attacker.getBlockPos(), SpectrumSoundEvents.GROUND_SLAM, SoundCategory.PLAYERS, 1.0F, 1.0F);
		if (attacker instanceof ServerPlayerEntity serverPlayer) {
			serverPlayer.incrementStat(Stats.USED.getOrCreateStat(this));
		}
	}

	@Environment(EnvType.CLIENT)
	public void startSoundInstance(PlayerEntity user) {
		MinecraftClient.getInstance().getSoundManager().play(new GreatswordChargingSoundInstance(user, this.GROUND_SLAM_CHARGE_TICKS));
	}

	@Override
	public DamageComposition getDamageComposition(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
		DamageComposition composition = new DamageComposition();
		composition.addPlayerOrEntity(attacker, damage * (1 - MAGIC_DAMAGE_SHARE));
		composition.add(attacker.getDamageSources().magic(), damage * MAGIC_DAMAGE_SHARE);
		return composition;
	}

}

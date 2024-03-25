package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.ExtendedItemBarProvider;
import de.dafuqs.spectrum.api.render.SlotBackgroundEffectProvider;
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

public class GlassCrestGreatswordItem extends GreatswordItem implements SplitDamageItem, ExtendedItemBarProvider, SlotBackgroundEffectProvider {
	
	private static final InkCost GROUND_SLAM_COST = new InkCost(InkColors.WHITE, 25);
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
		if (getGroundSlamStrength(user.getStackInHand(hand)) > 0 && InkPowered.tryDrainEnergy(user, GROUND_SLAM_COST)) {
			if (world.isClient) {
				startSoundInstance(user);
			}
			return ItemUsage.consumeHeldItem(world, user, hand);
		}
		return TypedActionResult.pass(user.getStackInHand(hand));
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
		if (world.isClient) {
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
		world.playSound(null, attacker.getBlockPos(), SpectrumSoundEvents.GROUND_SLAM, SoundCategory.PLAYERS, 0.7F, 1.0F);
		world.playSound(null, attacker.getBlockPos(), SpectrumSoundEvents.DEEP_CRYSTAL_RING, SoundCategory.PLAYERS, 0.7F, 1.0F);
		world.playSound(null, attacker.getBlockPos(), SpectrumSoundEvents.DEEP_CRYSTAL_RING, SoundCategory.PLAYERS, 0.4F, 0.334F);

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

	@Override
	public SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
		var usable = InkPowered.hasAvailableInk(player, GROUND_SLAM_COST);
		return usable ? SlotEffect.BORDER_FADE : SlotEffect.NONE;
	}

	@Override
	public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		return 0xFFFFFF;
	}

	@Override
	public int barCount(ItemStack stack) {
		return 1;
	}

	@Override
	public boolean allowVanillaDurabilityBarRendering(@Nullable PlayerEntity player, ItemStack stack) {
		if (player == null || player.getStackInHand(player.getActiveHand()) != stack)
			return true;

		return !player.isUsingItem();
	}

	@Override
	public BarSignature getSignature(@Nullable PlayerEntity player, @NotNull ItemStack stack, int index) {
		if (player == null || !player.isUsingItem())
			return ExtendedItemBarProvider.PASS;

		var activeStack = player.getStackInHand(player.getActiveHand());
		if (activeStack != stack)
			return ExtendedItemBarProvider.PASS;


		var progress = Math.round(MathHelper.clampedLerp(0, 13, ((float) player.getItemUseTime() / GROUND_SLAM_CHARGE_TICKS)));
		return new BarSignature(2, 13, 13, progress, 1, 0xFFFFFFFF, 2, ExtendedItemBarProvider.DEFAULT_BACKGROUND_COLOR);
	}
}

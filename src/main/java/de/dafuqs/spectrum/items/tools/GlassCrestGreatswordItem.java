package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import de.dafuqs.spectrum.magic.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GlassCrestGreatswordItem extends GreatswordItem implements SplitDamageItem, ExtendedItemBarProvider, SlotBackgroundEffectProvider, InkPowered {
	
	private static final InkAmount GROUND_SLAM_COST = new InkAmount(InkColors.WHITE, 25);
	public static final float MAGIC_DAMAGE_SHARE = 0.25F;
	public final int GROUND_SLAM_CHARGE_TICKS = 32;
	
	public GlassCrestGreatswordItem(Tier material, int attackDamage, float attackSpeed, float extraReach, Properties settings) {
		super(material, attackDamage, attackSpeed, extraReach, settings);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.glass_crest_ultra_greatsword.tooltip", (int) (MAGIC_DAMAGE_SHARE * 100)));
		tooltip.add(Component.translatable("item.spectrum.glass_crest_ultra_greatsword.tooltip2"));
		addInkPoweredTooltip(tooltip);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (getGroundSlamStrength(world.registryAccess(), user.getItemInHand(hand)) > 0 && InkPowered.tryDrainEnergy(user, GROUND_SLAM_COST)) {
			if (world.isClientSide) {
				startSoundInstance(user);
			}
			return ItemUtils.startUsingInstantly(world, user, hand);
		}
		return InteractionResultHolder.pass(user.getItemInHand(hand));
	}
	
	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return GROUND_SLAM_CHARGE_TICKS;
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.SPEAR;
	}
	
	@Override
	public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		super.onUseTick(world, user, stack, remainingUseTicks);
		if (world.isClientSide) {
			RandomSource random = world.random;
			for (int i = 0; i < (GROUND_SLAM_CHARGE_TICKS - remainingUseTicks) / 8; i++) {
				world.addParticle(ParticleTypes.INSTANT_EFFECT,
						user.getRandomX(1.0), user.getY(), user.getRandomZ(1.0),
						random.nextDouble() * 5.0D - 2.5D, random.nextDouble() * 1.2D, random.nextDouble() * 5.0D - 2.5D);
			}
		}
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (!world.isClientSide) {
			int groundSlamStrength = getGroundSlamStrength(world.registryAccess(), stack);
			if (groundSlamStrength > 0) {
				performGroundSlam(world, user.position(), user, groundSlamStrength);
				stack.hurtAndBreak(1, user, LivingEntity.getSlotForHand(user.getUsedItemHand()));
			}
		}
		
		return stack;
	}
	
	public int getGroundSlamStrength(HolderLookup.Provider lookup, ItemStack stack) {
		return SpectrumEnchantmentHelper.getLevel(lookup, Enchantments.SWEEPING_EDGE, stack);
	}
	
	public void performGroundSlam(Level world, Vec3 pos, LivingEntity attacker, float strength) {
		world.gameEvent(attacker, GameEvent.HIT_GROUND, BlockPos.containing(pos.x, pos.y, pos.z));
		MoonstoneStrike.create(world, attacker, null, attacker.getX(), attacker.getY(), attacker.getZ(), strength, 1.75F);
		world.playSound(null, attacker.blockPosition(), SpectrumSoundEvents.GROUND_SLAM, SoundSource.PLAYERS, 0.7F, 1.0F);
		world.playSound(null, attacker.blockPosition(), SpectrumSoundEvents.DEEP_CRYSTAL_RING, SoundSource.PLAYERS, 0.7F, 1.0F);
		world.playSound(null, attacker.blockPosition(), SpectrumSoundEvents.DEEP_CRYSTAL_RING, SoundSource.PLAYERS, 0.4F, 0.334F);
		
		if (attacker instanceof ServerPlayer serverPlayer) {
			serverPlayer.awardStat(Stats.ITEM_USED.get(this));
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public void startSoundInstance(Player user) {
		Minecraft.getInstance().getSoundManager().play(new GreatswordChargingSoundInstance(user, this.GROUND_SLAM_CHARGE_TICKS));
	}
	
	@Override
	public DamageComposition getDamageComposition(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
		DamageComposition composition = new DamageComposition();
		composition.addPlayerOrEntity(attacker, damage * (1 - MAGIC_DAMAGE_SHARE));
		composition.add(attacker.damageSources().magic(), damage * MAGIC_DAMAGE_SHARE);
		return composition;
	}
	
	@Override
	public SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		var usable = InkPowered.hasAvailableInk(player, GROUND_SLAM_COST);
		return usable ? SlotEffect.BORDER_FADE : SlotEffect.NONE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return 0xFFFFFF;
	}
	
	@Override
	public int barCount(ItemStack stack) {
		return 1;
	}
	
	@Override
	public boolean allowVanillaDurabilityBarRendering(@Nullable Player player, ItemStack stack) {
		if (player == null || player.getItemInHand(player.getUsedItemHand()) != stack)
			return true;
		
		return !player.isUsingItem();
	}
	
	@Override
	public BarSignature getSignature(@Nullable Player player, @NotNull ItemStack stack, int index) {
		if (player == null || !player.isUsingItem())
			return ExtendedItemBarProvider.PASS;
		
		var activeStack = player.getItemInHand(player.getUsedItemHand());
		if (activeStack != stack)
			return ExtendedItemBarProvider.PASS;
		
		
		var progress = Math.round(Mth.clampedLerp(0, 13, ((float) player.getTicksUsingItem() / GROUND_SLAM_CHARGE_TICKS)));
		return new BarSignature(2, 13, 13, progress, 1, 0xFFFFFFFF, 2, ExtendedItemBarProvider.DEFAULT_BACKGROUND_COLOR);
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(GROUND_SLAM_COST.color());
	}
	
}

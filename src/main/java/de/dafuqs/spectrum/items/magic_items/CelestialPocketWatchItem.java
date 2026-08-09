package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public class CelestialPocketWatchItem extends Item implements InkPowered {
	
	// Since the watch can be triggered from an item frame, too
	// and item frames can turn items in 8 directions this fits real fine
	public static final int TIME_STEP_TICKS = 24000 / 8;
	public static final InkAmount COST = new InkAmount(InkColors.MAGENTA, 1000);
	
	enum TimeToggleResult {
		SUCCESS,
		FAILED_FIXED_TIME,
		FAILED_GAME_RULE
	}
	
	public CelestialPocketWatchItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(COST.color());
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		
		if (!world.isClientSide()) {
			if (!tryAdvanceTime((ServerLevel) world, (ServerPlayer) user)) {
				world.playSound(null, user.blockPosition(), SpectrumSoundEvents.USE_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
			}
			
			return InteractionResultHolder.consume(itemStack);
		}
		return InteractionResultHolder.sidedSuccess(itemStack, true);
	}
	
	public static boolean tryAdvanceTime(ServerLevel world, ServerPlayer user) {
		switch (canAdvanceTime(world)) {
			case FAILED_GAME_RULE -> user.displayClientMessage(Component.translatable("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_gamerule"), true);
			case FAILED_FIXED_TIME -> user.displayClientMessage(Component.translatable("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_fixed_time"), true);
			case SUCCESS -> {
				if (InkPowered.tryDrainEnergy(user, COST)) {
					world.playSound(null, user.blockPosition(), SpectrumSoundEvents.CELESTIAL_POCKET_WATCH_TICKING, SoundSource.PLAYERS, 1.0F, 1.0F);
					advanceTime(world, TIME_STEP_TICKS);
				}
				return true;
			}
		}
		return false;
	}
	
	// the clocks use is blocked if the world has a fixed daylight cycle, or gamerule doDayLightCycle is set to false
	private static TimeToggleResult canAdvanceTime(Level world) {
		GameRules.BooleanValue doDaylightCycleRule = world.getGameRules().getRule(GameRules.RULE_DAYLIGHT);
		if (doDaylightCycleRule.get()) {
			if (world.dimensionType().hasFixedTime()) {
				return TimeToggleResult.FAILED_FIXED_TIME;
			} else {
				return TimeToggleResult.SUCCESS;
			}
		} else {
			return TimeToggleResult.FAILED_GAME_RULE;
		}
	}
	
	private static void advanceTime(ServerLevel world, int additionalTime) {
		StartSkyLerpingPayload.startSkyLerping(world, additionalTime);
		long timeOfDay = world.getDayTime();
		world.setDayTime(timeOfDay + additionalTime);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		Level level = context.level();
		if (level != null) {
			switch (canAdvanceTime(level)) {
				case FAILED_GAME_RULE -> tooltip.add(Component.translatable("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_gamerule").withStyle(ChatFormatting.GRAY));
				case FAILED_FIXED_TIME -> tooltip.add(Component.translatable("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_fixed_time").withStyle(ChatFormatting.GRAY));
				case SUCCESS -> tooltip.add(Component.translatable("item.spectrum.celestial_pocketwatch.tooltip.working").withStyle(ChatFormatting.GRAY));
			}
		}
		
		addInkPoweredTooltip(tooltip);
	}
	
}

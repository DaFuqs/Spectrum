package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CelestialPocketWatchItem extends Item {

	// Since the watch can be triggered from an item frame, too
	// and item frames can turn items in 8 directions this fits real fine
	public static final int TIME_STEP_TICKS = 24000 / 8;
	public static final InkCost COST = new InkCost(InkColors.MAGENTA, 10000);

	enum TimeToggleResult {
		SUCCESS,
		FAILED_FIXED_TIME,
		FAILED_GAME_RULE
	}

	public CelestialPocketWatchItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);

		if (!world.isClient) {
			if (!tryAdvanceTime((ServerWorld) world, (ServerPlayerEntity) user)) {
				world.playSound(null, user.getBlockPos(), SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}

			return TypedActionResult.consume(itemStack);
		}
		return TypedActionResult.success(itemStack, true);
	}

	public static boolean tryAdvanceTime(ServerWorld world, ServerPlayerEntity user) {
		switch (canAdvanceTime(world)) {
			case FAILED_GAME_RULE ->
					user.sendMessage(Text.translatable("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_gamerule"), true);
			case FAILED_FIXED_TIME ->
					user.sendMessage(Text.translatable("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_fixed_time"), true);
			case SUCCESS -> {
				if (InkPowered.tryDrainEnergy(user, COST)) {
					world.playSound(null, user.getBlockPos(), SpectrumSoundEvents.CELESTIAL_POCKET_WATCH_TICKING, SoundCategory.PLAYERS, 1.0F, 1.0F);
					advanceTime(world, TIME_STEP_TICKS);
				}
				return true;
			}
		}
		return false;
	}

	// the clocks use is blocked if the world has a fixed daylight cycle, or gamerule doDayLightCycle is set to false
	private static TimeToggleResult canAdvanceTime(@NotNull World world) {
		GameRules.BooleanRule doDaylightCycleRule = world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE);
		if (doDaylightCycleRule.get()) {
			if (world.getDimension().hasFixedTime()) {
				return TimeToggleResult.FAILED_FIXED_TIME;
			} else {
				return TimeToggleResult.SUCCESS;
			}
		} else {
			return TimeToggleResult.FAILED_GAME_RULE;
		}
	}

	private static void advanceTime(@NotNull ServerWorld world, int additionalTime) {
		SpectrumS2CPacketSender.startSkyLerping(world, additionalTime);
		long timeOfDay = world.getTimeOfDay();
		world.setTimeOfDay(timeOfDay + additionalTime);
	}

	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(Text.translatable("spectrum.tooltip.ink_powered.magenta"));

		if (world != null) {
			switch (canAdvanceTime(world)) {
				case FAILED_GAME_RULE ->
						tooltip.add(Text.translatable("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_gamerule").formatted(Formatting.GRAY));
				case FAILED_FIXED_TIME ->
						tooltip.add(Text.translatable("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_fixed_time").formatted(Formatting.GRAY));
				case SUCCESS ->
						tooltip.add(Text.translatable("item.spectrum.celestial_pocketwatch.tooltip.working").formatted(Formatting.GRAY));
			}
		}
	}
	
}

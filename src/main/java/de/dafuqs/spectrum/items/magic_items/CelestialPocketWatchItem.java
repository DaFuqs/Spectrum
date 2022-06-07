package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CelestialPocketWatchItem extends Item {
	
	// Since the watch can be triggered from an item frame, too
	// and item frames can turn items in 8 directions this fits real fine
	public static final int TIME_STEP_TICKS = 24000 / 8;
	
	public CelestialPocketWatchItem(Settings settings) {
		super(settings);
	}
	
	public static boolean advanceTime(ServerPlayerEntity player, @NotNull ServerWorld world) {
		GameRules.BooleanRule doDaylightCycleRule = world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE);
		if (doDaylightCycleRule.get()) {
			if (world.getDimension().hasFixedTime()) {
				player.sendMessage(new TranslatableText("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_fixed_time"), false);
			} else {
				SpectrumS2CPacketSender.startSkyLerping(world, TIME_STEP_TICKS);
				long timeOfDay = world.getTimeOfDay();
				world.setTimeOfDay(timeOfDay + TIME_STEP_TICKS);
				return true;
			}
		} else {
			player.sendMessage(new TranslatableText("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_gamerule"), false);
		}
		return false;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		
		if (!world.isClient) {
			// the clocks use is blocked if the world has a fixed daylight cycle
			if (advanceTime((ServerPlayerEntity) user, (ServerWorld) world)) {
				world.playSound(null, user.getBlockPos(), SpectrumSoundEvents.CELESTIAL_POCKET_WATCH_TICKING, SoundCategory.PLAYERS, 1.0F, 1.0F);
			} else {
				world.playSound(null, user.getBlockPos(), SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
			return TypedActionResult.consume(itemStack);
		}
		return TypedActionResult.success(itemStack, true);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		if (world != null) {
			// the clocks use is blocked if the world has a fixed daylight cycle
			GameRules.BooleanRule doDaylightCycleRule = world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE);
			if (doDaylightCycleRule.get()) {
				if (world.getDimension().hasFixedTime()) {
					tooltip.add(new TranslatableText("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_fixed_time").formatted(Formatting.GRAY));
				} else {
					tooltip.add(new TranslatableText("item.spectrum.celestial_pocketwatch.tooltip.working").formatted(Formatting.GRAY));
				}
			} else {
				tooltip.add(new TranslatableText("item.spectrum.celestial_pocketwatch.tooltip.use_blocked_gamerule").formatted(Formatting.GRAY));
			}
		}
	}
	
}

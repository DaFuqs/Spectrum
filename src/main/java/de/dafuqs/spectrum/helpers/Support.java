package de.dafuqs.spectrum.helpers;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.*;
import net.minecraft.server.network.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.text.*;
import java.util.*;

public class Support {
	
	public static final List<Vec3d> VECTORS_4 = List.of(
			new Vec3d(1.0D, 0, 0.0D),
			new Vec3d(0.0D, 0, 1.0D),
			new Vec3d(-1.0, 0, 0.0D),
			new Vec3d(0.0D, 0, -1.0D)
	);
	public static final List<Vec3d> VECTORS_8 = List.of(
			new Vec3d(1.0D, 0, 0.0D),
			new Vec3d(0.7D, 0, 0.7D),
			new Vec3d(0.0D, 0, 1.0D),
			new Vec3d(-0.7D, 0, 0.7D),
			new Vec3d(-1.0D, 0, 0.0D),
			new Vec3d(-0.7D, 0, -0.7D),
			new Vec3d(0.0D, 0, -1.0D),
			new Vec3d(0.7D, 0, -0.7D)
	);
	// Like eight, just turned clockwise
	public static final List<Vec3d> VECTORS_8_OFFSET = List.of(
			new Vec3d(0.75D, 0, 0.5D),
			new Vec3d(0.5D, 0, 0.75D),
			new Vec3d(-0.5D, 0, 0.75D),
			new Vec3d(-0.75D, 0, 0.5D),
			new Vec3d(-0.75D, 0, 0.5D),
			new Vec3d(-0.5D, 0, -0.75D),
			new Vec3d(0.5D, 0, -0.75D),
			new Vec3d(0.75D, 0, -0.5D)
	);
	public static final List<Vec3d> VECTORS_16 = List.of(
			new Vec3d(1.0D, 0, 0.0D),
			new Vec3d(0.75D, 0, 0.5D),
			new Vec3d(0.7D, 0, 0.7D),
			new Vec3d(0.5D, 0, 0.75D),
			new Vec3d(0.0D, 0, 1.0D),
			new Vec3d(-0.5D, 0, 0.75D),
			new Vec3d(-0.7D, 0, 0.7D),
			new Vec3d(-0.75D, 0, 0.5D),
			new Vec3d(-1.0D, 0, 0.0D),
			new Vec3d(-0.75D, 0, 0.5D),
			new Vec3d(-0.7D, 0, -0.7D),
			new Vec3d(-0.5D, 0, -0.75D),
			new Vec3d(0.0D, 0, -1.0D),
			new Vec3d(0.5D, 0, -0.75D),
			new Vec3d(0.7D, 0, -0.7D),
			new Vec3d(0.75D, 0, -0.5D)
	);
	private static final Identifier PROGRESSION_FINISHED_ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("endgame/finish_progression");
	public static final DecimalFormat DF = new DecimalFormat("0");
	public static final DecimalFormat DF1 = new DecimalFormat("0.0");
	public static final DecimalFormat DF2 = new DecimalFormat("0.00");
	
	public static @NotNull Optional<TagKey<Block>> getFirstMatchingBlockTag(@NotNull BlockState blockState, @NotNull List<TagKey<Block>> tags) {
		return blockState.streamTags().filter(tags::contains).findFirst();
	}
	
	public static String getWithOneDecimalAfterComma(float number) {
		return DF1.format(number);
	}
	
	public static String getShortenedNumberString(double number) {
		if (number > 1000000000D) {
			return DF2.format(number / 1000000000D) + "G";
		} else if (number > 1000000D) {
			return DF2.format(number / 1000000D) + "M";
		} else if (number > 1000D) {
			return DF2.format(number / 1000D) + "K";
		} else {
			return DF.format(number);
		}
	}
	
	public static String getShortenedNumberString(long number) {
		if (number > 1000000000L) {
			return DF2.format(number / 1000000000D) + "G";
		} else if (number > 1000000L) {
			return DF2.format(number / 1000000D) + "M";
		} else if (number > 1000L) {
			return DF2.format(number / 1000D) + "K";
		} else {
			return DF.format(number);
		}
	}
	
	/**
	 * Calculates the percentage of x / y from 0-100, but in a way it feels logical to players
	 * If x > 0 the result is always at least 1%,
	 * If it approaches 100%, but is not exactly 100%, returns 99
	 */
	public static String getSensiblePercent(long x, long y) {
		if (y == 0) {
			return "0";
		}
		
		double result = (double) x / y;
		if (result < 0.01 && x > 0) {
			return "1";
		} else if (result > 0.99 && x != y) {
			return "99";
		} else {
			return DF.format(Math.round(result * 100L));
		}
	}
	
	public static int getIntFromDecimalWithChance(double d, @NotNull Random random) {
		boolean roundUp = (random.nextFloat() < d % 1);
		if (roundUp) {
			return ((int) d) + 1;
		} else {
			return (int) d;
		}
	}
	
	
	/**
	 * Returns a relative new BlockPos based on a facing direction and a vector
	 *
	 * @param origin           the source position
	 * @param forwardUpRight   a vector specifying the amount of blocks forward, up and right
	 * @param horizontalFacing the facing direction
	 * @return the blockpos with forwardUpRight offset from origin when facing horizontalFacing
	 */
	public static BlockPos directionalOffset(BlockPos origin, Vec3i forwardUpRight, @NotNull Direction horizontalFacing) {
		switch (horizontalFacing) {
			case NORTH -> {
				return origin.add(forwardUpRight.getZ(), forwardUpRight.getY(), -forwardUpRight.getX());
			}
			case EAST -> {
				return origin.add(forwardUpRight.getX(), forwardUpRight.getY(), forwardUpRight.getZ());
			}
			case SOUTH -> {
				return origin.add(-forwardUpRight.getZ(), forwardUpRight.getY(), forwardUpRight.getX());
			}
			case WEST -> {
				return origin.add(-forwardUpRight.getX(), forwardUpRight.getY(), -forwardUpRight.getZ());
			}
			default -> {
				SpectrumCommon.logWarning("Called directionalOffset with facing" + horizontalFacing + " this is not supported.");
				return origin;
			}
		}
	}
	
	public static void grantAdvancementCriterion(@NotNull ServerPlayerEntity serverPlayerEntity, Identifier advancementIdentifier, String criterion) {
		if (serverPlayerEntity.getServer() == null) {
			return;
		}
		ServerAdvancementLoader sal = serverPlayerEntity.getServer().getAdvancementLoader();
		PlayerAdvancementTracker tracker = serverPlayerEntity.getAdvancementTracker();
		
		Advancement advancement = sal.get(advancementIdentifier);
		if (advancement == null) {
			SpectrumCommon.logError("Trying to grant a criterion \"" + criterion + "\" for an advancement that does not exist: " + advancementIdentifier);
		} else {
			if (!tracker.getProgress(advancement).isDone()) {
				tracker.grantCriterion(advancement, criterion);
			}
		}
	}
	
	public static void grantAdvancementCriterion(@NotNull ServerPlayerEntity serverPlayerEntity, String advancementString, String criterion) {
		grantAdvancementCriterion(serverPlayerEntity, SpectrumCommon.locate(advancementString), criterion);
	}
	
	public static @NotNull String getReadableDimensionString(@NotNull String dimensionKeyString) {
		switch (dimensionKeyString) {
			case "minecraft:overworld":
				return "Overworld";
			case "minecraft:nether":
				return "Nether";
			case "minecraft:end":
				return "End";
			case "spectrum:deeper_down_dimension":
				return "Deeper Down";
			default:
				if (dimensionKeyString.contains(":")) {
					return dimensionKeyString.substring(dimensionKeyString.indexOf(":") + 1);
				} else {
					return dimensionKeyString;
				}
		}
	}
	
	@Contract(pure = true)
	public static Direction directionFromRotation(@NotNull BlockRotation blockRotation) {
		switch (blockRotation) {
			case NONE -> {
				return Direction.EAST;
			}
			case CLOCKWISE_90 -> {
				return Direction.SOUTH;
			}
			case CLOCKWISE_180 -> {
				return Direction.WEST;
			}
			default -> {
				return Direction.NORTH;
			}
		}
	}
	
	@Contract(pure = true)
	public static BlockRotation rotationFromDirection(@NotNull Direction direction) {
		switch (direction) {
			case EAST -> {
				return BlockRotation.NONE;
			}
			case SOUTH -> {
				return BlockRotation.CLOCKWISE_90;
			}
			case WEST -> {
				return BlockRotation.CLOCKWISE_180;
			}
			default -> {
				return BlockRotation.COUNTERCLOCKWISE_90;
			}
		}
	}
	
	public static boolean hasPlayerFinishedMod(PlayerEntity player) {
		return AdvancementHelper.hasAdvancement(player, PROGRESSION_FINISHED_ADVANCEMENT_IDENTIFIER);
	}
	
	public static Optional<BlockPos> getNexReplaceableBlockPosUpDown(World world, BlockPos blockPos, int maxUpDown) {
		if (world.getBlockState(blockPos).getMaterial().isReplaceable()) {
			// search down
			for (int i = 0; i < maxUpDown; i++) {
				if (!world.getBlockState(blockPos.down(i + 1)).getMaterial().isReplaceable()) {
					return Optional.of(blockPos.down(i));
				}
			}
		} else {
			// search up
			for (int i = 1; i <= maxUpDown; i++) {
				if (world.getBlockState(blockPos.up(i)).getMaterial().isReplaceable()) {
					return Optional.of(blockPos.up(i));
				}
			}
		}
		return Optional.empty();
	}
	
	public static double logBase(double base, double logNumber) {
		return Math.log(logNumber) / Math.log(base);
	}
	
}
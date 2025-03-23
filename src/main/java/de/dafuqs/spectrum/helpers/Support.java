package de.dafuqs.spectrum.helpers;

import com.jamieswhiteshirt.reachentityattributes.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.math.*;
import java.text.*;
import java.util.*;

public class Support {
	
	public static HitResult playerInteractionRaycast(World world, LivingEntity user, PlayerEntity player) {
		double maxDistance = getReachDistance(player);
		Vec3d eyePos = user.getEyePos();
		Vec3d rotationVec = user.getRotationVec(0F);
		Vec3d vec3d3 = eyePos.add(rotationVec.x * maxDistance, rotationVec.y * maxDistance, rotationVec.z * maxDistance);
		return world.raycast(new RaycastContext(eyePos, vec3d3, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
	}
	
	public static float getReachDistance(PlayerEntity player) {
		return (player.isCreative() ? 5.0F : 4.5F) + (float) player.getAttributeValue(ReachEntityAttributes.REACH);
	}
	
	public static final DecimalFormat DF = new DecimalFormat("0");
	public static final DecimalFormat DF1 = new DecimalFormat("0.0");
	public static final DecimalFormat DF2 = new DecimalFormat("0.00");

	static {
		DF.setRoundingMode(RoundingMode.DOWN);
		DF1.setRoundingMode(RoundingMode.DOWN);
		DF2.setRoundingMode(RoundingMode.DOWN);
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
		return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
	}
	
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
	public static String getSensiblePercentString(long x, long y) {
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
	
	public static int getSensiblePercent(long x, long y, int max) {
		if (y == 0) {
			return 0;
		}
		
		int result = (int) MathHelper.clampedLerp(0, max, (double) x / y);
		if (result < 1 && x > 0) {
			return 1;
		} else if (result == max && x != y) {
			return max - 1;
		} else {
			return result;
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
			case "minecraft:overworld" -> {
				return "Overworld";
			}
			case "minecraft:nether" -> {
				return "Nether";
			}
			case "minecraft:end" -> {
				return "End";
			}
			case "spectrum:deeper_down" -> {
				return "Deeper Down";
			}
			default -> {
				if (dimensionKeyString.contains(":")) {
					return dimensionKeyString.substring(dimensionKeyString.indexOf(":") + 1);
				} else {
					return dimensionKeyString;
				}
			}
		}
	}
	
	@Contract(pure = true)
	public static Direction directionFromRotation(@NotNull BlockRotation blockRotation) {
		switch (blockRotation) {
			case NONE -> {
				return Direction.NORTH;
			}
			case CLOCKWISE_90 -> {
				return Direction.EAST;
			}
			case CLOCKWISE_180 -> {
				return Direction.SOUTH;
			}
			default -> {
				return Direction.WEST;
			}
		}
	}
	
	@Contract(pure = true)
	public static BlockRotation rotationFromDirection(@NotNull Direction direction) {
		switch (direction) {
			case EAST -> {
				return BlockRotation.CLOCKWISE_90;
			}
			case SOUTH -> {
				return BlockRotation.CLOCKWISE_180;
			}
			case WEST -> {
				return BlockRotation.COUNTERCLOCKWISE_90;
			}
			default -> {
				return BlockRotation.NONE;
			}
		}
	}
	
	public static Optional<BlockPos> getNexReplaceableBlockPosUpDown(World world, BlockPos blockPos, int maxUpDown) {
		if (world.getBlockState(blockPos).isReplaceable()) {
			// search down
			for (int i = 0; i < maxUpDown; i++) {
				if (!world.getBlockState(blockPos.down(i + 1)).isReplaceable()) {
					return Optional.of(blockPos.down(i));
				}
			}
		} else {
			// search up
			for (int i = 1; i <= maxUpDown; i++) {
				if (world.getBlockState(blockPos.up(i)).isReplaceable()) {
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
package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.SporeBlossomBlock;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ColoredSporeBlossomBlock extends SporeBlossomBlock {
	
	DyeColor dyeColor;
	ParticleEffect fallingParticleType;
	ParticleEffect airParticleType;
	
	public ColoredSporeBlossomBlock(Settings settings, DyeColor dyeColor) {
		super(settings);
		this.dyeColor = dyeColor;
		this.fallingParticleType = getFallingParticleType(dyeColor);
		this.airParticleType = getAirParticleType(dyeColor);
	}
	
	public static ParticleEffect getFallingParticleType(@NotNull DyeColor dyeColor) {
		switch (dyeColor) {
			case BLACK -> {
				return SpectrumParticleTypes.BLACK_FALLING_SPORE_BLOSSOM;
			}
			case BLUE -> {
				return SpectrumParticleTypes.BLUE_FALLING_SPORE_BLOSSOM;
			}
			case BROWN -> {
				return SpectrumParticleTypes.BROWN_FALLING_SPORE_BLOSSOM;
			}
			case CYAN -> {
				return SpectrumParticleTypes.CYAN_FALLING_SPORE_BLOSSOM;
			}
			case GRAY -> {
				return SpectrumParticleTypes.GRAY_FALLING_SPORE_BLOSSOM;
			}
			case GREEN -> {
				return SpectrumParticleTypes.GREEN_FALLING_SPORE_BLOSSOM;
			}
			case LIGHT_BLUE -> {
				return SpectrumParticleTypes.LIGHT_BLUE_FALLING_SPORE_BLOSSOM;
			}
			case LIGHT_GRAY -> {
				return SpectrumParticleTypes.LIGHT_GRAY_FALLING_SPORE_BLOSSOM;
			}
			case LIME -> {
				return SpectrumParticleTypes.LIME_FALLING_SPORE_BLOSSOM;
			}
			case MAGENTA -> {
				return SpectrumParticleTypes.MAGENTA_FALLING_SPORE_BLOSSOM;
			}
			case ORANGE -> {
				return SpectrumParticleTypes.ORANGE_FALLING_SPORE_BLOSSOM;
			}
			case PINK -> {
				return SpectrumParticleTypes.PINK_FALLING_SPORE_BLOSSOM;
			}
			case PURPLE -> {
				return SpectrumParticleTypes.PURPLE_FALLING_SPORE_BLOSSOM;
			}
			case RED -> {
				return SpectrumParticleTypes.RED_FALLING_SPORE_BLOSSOM;
			}
			case WHITE -> {
				return SpectrumParticleTypes.WHITE_FALLING_SPORE_BLOSSOM;
			}
			default -> {
				return SpectrumParticleTypes.YELLOW_FALLING_SPORE_BLOSSOM;
			}
		}
	}
	
	public static ParticleEffect getAirParticleType(@NotNull DyeColor dyeColor) {
		switch (dyeColor) {
			case BLACK -> {
				return SpectrumParticleTypes.BLACK_SPORE_BLOSSOM_AIR;
			}
			case BLUE -> {
				return SpectrumParticleTypes.BLUE_SPORE_BLOSSOM_AIR;
			}
			case BROWN -> {
				return SpectrumParticleTypes.BROWN_SPORE_BLOSSOM_AIR;
			}
			case CYAN -> {
				return SpectrumParticleTypes.CYAN_SPORE_BLOSSOM_AIR;
			}
			case GRAY -> {
				return SpectrumParticleTypes.GRAY_SPORE_BLOSSOM_AIR;
			}
			case GREEN -> {
				return SpectrumParticleTypes.GREEN_SPORE_BLOSSOM_AIR;
			}
			case LIGHT_BLUE -> {
				return SpectrumParticleTypes.LIGHT_BLUE_SPORE_BLOSSOM_AIR;
			}
			case LIGHT_GRAY -> {
				return SpectrumParticleTypes.LIGHT_GRAY_SPORE_BLOSSOM_AIR;
			}
			case LIME -> {
				return SpectrumParticleTypes.LIME_SPORE_BLOSSOM_AIR;
			}
			case MAGENTA -> {
				return SpectrumParticleTypes.MAGENTA_SPORE_BLOSSOM_AIR;
			}
			case ORANGE -> {
				return SpectrumParticleTypes.ORANGE_SPORE_BLOSSOM_AIR;
			}
			case PINK -> {
				return SpectrumParticleTypes.PINK_SPORE_BLOSSOM_AIR;
			}
			case PURPLE -> {
				return SpectrumParticleTypes.PURPLE_SPORE_BLOSSOM_AIR;
			}
			case RED -> {
				return SpectrumParticleTypes.RED_SPORE_BLOSSOM_AIR;
			}
			case WHITE -> {
				return SpectrumParticleTypes.WHITE_SPORE_BLOSSOM_AIR;
			}
			default -> {
				return SpectrumParticleTypes.YELLOW_SPORE_BLOSSOM_AIR;
			}
		}
	}
	
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		double d = (double) i + random.nextDouble();
		double e = (double) j + 0.7D;
		double f = (double) k + random.nextDouble();
		world.addParticle(this.fallingParticleType, d, e, f, 0.0D, 0.0D, 0.0D);
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		
		for (int l = 0; l < 14; ++l) {
			mutable.set(i + MathHelper.nextInt(random, -10, 10), j - random.nextInt(10), k + MathHelper.nextInt(random, -10, 10));
			BlockState blockState = world.getBlockState(mutable);
			if (!blockState.isFullCube(world, mutable)) {
				world.addParticle(this.airParticleType, (double) mutable.getX() + random.nextDouble(), (double) mutable.getY() + random.nextDouble(), (double) mutable.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	
}

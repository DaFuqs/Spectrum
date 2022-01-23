package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.ColorHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.Random;

public class ShootingStarBlock extends Block {
	
	public enum Type {
		GLISTERING("glistering"),
		FIERY("fiery"),
		COLORFUL("colorful"),
		PRISTINE("pristine"),
		GEMSTONE("gemstone");
		
		public static final Identifier COMMON_LOOT_TABLE = new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_common");
		
		private final String name;
		
		Type(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		public Block getBlock() {
			switch (this) {
				case PRISTINE -> {
					return SpectrumBlocks.PRISTINE_SHOOTING_STAR;
				}
				case GEMSTONE -> {
					return SpectrumBlocks.GEMSTONE_SHOOTING_STAR;
				}
				case FIERY -> {
					return SpectrumBlocks.FIERY_SHOOTING_STAR;
				}
				case COLORFUL -> {
					return SpectrumBlocks.COLORFUL_SHOOTING_STAR;
				}
				default -> {
					return SpectrumBlocks.GLISTERING_SHOOTING_STAR;
				}
			}
		}
		
		public static ShootingStarBlock.Type getType(int type) {
			ShootingStarBlock.Type[] types = values();
			if (type < 0 || type >= types.length) {
				type = 0;
			}
			
			return types[type];
		}
		
		public static ShootingStarBlock.Type getType(String name) {
			ShootingStarBlock.Type[] types = values();
			
			for (Type type : types) {
				if (type.getName().equals(name)) {
					return type;
				}
			}
			
			return types[0];
		}
		
		public static Identifier getLootTableIdentifier(int index) {
			return getLootTableIdentifier(values()[index]);
		}
		
		public static Identifier getLootTableIdentifier(Type type) {
			switch (type) {
				case FIERY -> {
					return new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_fiery");
				}
				case COLORFUL -> {
					return new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_colorful");
				}
				case GEMSTONE -> {
					return new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_gemstone");
				}
				case PRISTINE -> {
					return new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_pristine");
				}
				default -> {
					return new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_glistering");
				}
			}
		}
		
		public Vec3f getRandomParticleColor(Random random) {
			switch (this) {
				case GLISTERING -> {
					int r = random.nextInt(5);
					if(r == 0) {
						return ColorHelper.getColor(DyeColor.YELLOW);
					} else if (r == 1) {
						return ColorHelper.getColor(DyeColor.RED);
					} else if (r == 2) {
						return ColorHelper.getColor(DyeColor.LIGHT_BLUE);
					} else if (r == 3) {
						return ColorHelper.getColor(DyeColor.LIME);
					} else {
						return ColorHelper.getColor(DyeColor.BLUE);
					}
				}
				case COLORFUL -> {
					return ColorHelper.getColor(DyeColor.values()[random.nextInt(DyeColor.values().length)]);
				}
				case FIERY -> {
					int r = random.nextInt(2);
					if(r == 0) {
						return ColorHelper.getColor(DyeColor.ORANGE);
					} else {
						return ColorHelper.getColor(DyeColor.RED);
					}
				}
				case PRISTINE -> {
					int r = random.nextInt(3);
					if(r == 0) {
						return ColorHelper.getColor(DyeColor.BLUE);
					} else if(r == 1) {
						return ColorHelper.getColor(DyeColor.LIGHT_BLUE);
					} else {
						return ColorHelper.getColor(DyeColor.CYAN);
					}
				}
				default -> {
					int r = random.nextInt(3);
					if(r == 0) {
						return ColorHelper.getColor(DyeColor.CYAN);
					} else if(r == 1) {
						return ColorHelper.getColor(DyeColor.MAGENTA);
					} else {
						return ColorHelper.getColor(DyeColor.YELLOW);
					}
				}
			}
		}
	}
	
	public final Type shootingStarType;
	
	public ShootingStarBlock(Settings settings, ShootingStarBlock.Type shootingStarType) {
		super(settings);
		this.shootingStarType = shootingStarType;
	}
	
}

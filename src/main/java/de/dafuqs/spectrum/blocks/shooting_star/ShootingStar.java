package de.dafuqs.spectrum.blocks.shooting_star;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.*;
import org.joml.*;

public interface ShootingStar {

	ShootingStar.Type getShootingStarType();

	enum Type {
		GLISTERING("glistering"),
		FIERY("fiery"),
		COLORFUL("colorful"),
		PRISTINE("pristine"),
		GEMSTONE("gemstone");

		public static final Identifier BOUNCE_LOOT_TABLE = SpectrumCommon.locate("entity/shooting_star/shooting_star_bounce");

		private final String name;

		Type(String name) {
			this.name = name;
		}

		public static Type getWeightedRandomType(@NotNull Random random) {
			int r = random.nextInt(8);
			if (r == 0) {
				return FIERY;
			} else if (r == 1) {
				return PRISTINE;
			} else if (r < 3) {
				return GLISTERING;
			} else if (r < 5) {
				return COLORFUL;
			} else {
				return GEMSTONE;
			}
		}

		public static Type getType(int type) {
			Type[] types = values();
			if (type < 0 || type >= types.length) {
				type = 0;
			}

			return types[type];
		}

		public static Type getType(String name) {
			Type[] types = values();

			for (Type type : types) {
				if (type.getName().equals(name)) {
					return type;
				}
			}

			return types[0];
		}

		@Contract("_ -> new")
		public static @NotNull Identifier getLootTableIdentifier(int index) {
			return getLootTableIdentifier(values()[index]);
		}

		@Contract("_ -> new")
		public static @NotNull Identifier getLootTableIdentifier(@NotNull ShootingStar.Type type) {
			switch (type) {
				case FIERY -> {
					return SpectrumCommon.locate("entity/shooting_star/fiery_shooting_star");
				}
				case COLORFUL -> {
					return SpectrumCommon.locate("entity/shooting_star/colorful_shooting_star");
				}
				case GEMSTONE -> {
					return SpectrumCommon.locate("entity/shooting_star/gemstone_shooting_star");
				}
				case PRISTINE -> {
					return SpectrumCommon.locate("entity/shooting_star/pristine_shooting_star");
				}
				default -> {
					return SpectrumCommon.locate("entity/shooting_star/glistering_shooting_star");
				}
			}
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

		public @NotNull Vector3f getRandomParticleColor(Random random) {
			switch (this) {
				case GLISTERING -> {
					int r = random.nextInt(5);
					if (r == 0) {
						return ColorHelper.getRGBVec(DyeColor.YELLOW);
					} else if (r == 1) {
						return ColorHelper.getRGBVec(DyeColor.WHITE);
					} else if (r == 2) {
						return ColorHelper.getRGBVec(DyeColor.ORANGE);
					} else if (r == 3) {
						return ColorHelper.getRGBVec(DyeColor.LIME);
					} else {
						return ColorHelper.getRGBVec(DyeColor.BLUE);
					}
				}
				case COLORFUL -> {
					return ColorHelper.getRGBVec(ColorHelper.VANILLA_DYE_COLORS.get(random.nextInt(ColorHelper.VANILLA_DYE_COLORS.size())));
				}
				case FIERY -> {
					int r = random.nextInt(2);
					if (r == 0) {
						return ColorHelper.getRGBVec(DyeColor.ORANGE);
					} else {
						return ColorHelper.getRGBVec(DyeColor.RED);
					}
				}
				case PRISTINE -> {
					int r = random.nextInt(3);
					if (r == 0) {
						return ColorHelper.getRGBVec(DyeColor.BLUE);
					} else if (r == 1) {
						return ColorHelper.getRGBVec(DyeColor.LIGHT_BLUE);
					} else {
						return ColorHelper.getRGBVec(DyeColor.CYAN);
					}
				}
				default -> {
					int r = random.nextInt(4);
					if (r == 0) {
						return ColorHelper.getRGBVec(DyeColor.CYAN);
					} else if (r == 1) {
						return ColorHelper.getRGBVec(DyeColor.MAGENTA);
					} else if (r == 2) {
						return ColorHelper.getRGBVec(DyeColor.WHITE);
					} else {
						return ColorHelper.getRGBVec(DyeColor.YELLOW);
					}
				}
			}
		}
	}
}

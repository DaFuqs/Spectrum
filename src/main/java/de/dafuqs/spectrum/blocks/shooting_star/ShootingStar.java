package de.dafuqs.spectrum.blocks.shooting_star;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import io.netty.buffer.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.loot.*;
import org.jetbrains.annotations.*;
import org.joml.*;

public interface ShootingStar {
	
	enum Variant implements StringRepresentable {
		GLISTERING("glistering", SpectrumLootTables.GLISTERING_SHOOTING_STAR),
		FIERY("fiery", SpectrumLootTables.FIERY_SHOOTING_STAR),
		COLORFUL("colorful", SpectrumLootTables.COLORFUL_SHOOTING_STAR),
		PRISTINE("pristine", SpectrumLootTables.PRISTINE_SHOOTING_STAR),
		GEMSTONE("gemstone", SpectrumLootTables.GEMSTONE_SHOOTING_STAR);
		
		public static Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);
		public static final StreamCodec<ByteBuf, Variant> PACKET_CODEC = PacketCodecHelper.enumOf(Variant::values);
		
		private final String name;
		private final ResourceKey<LootTable> lootTable;
		
		Variant(String name, ResourceKey<LootTable> lootTable) {
			this.name = name;
			this.lootTable = lootTable;
		}
		
		public static Variant getWeightedRandomType(@NotNull RandomSource random) {
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
		
		public static Variant getType(int type) {
			Variant[] types = values();
			if (type < 0 || type >= types.length) {
				type = 0;
			}
			
			return types[type];
		}
		
		public static Variant getType(String name) {
			Variant[] types = values();
			
			for (Variant type : types) {
				if (type.getName().equals(name)) {
					return type;
				}
			}
			
			return types[0];
		}
		
		@Contract("_ -> new")
		public static @NotNull ResourceKey<LootTable> getLootTable(int index) {
			return values()[index].getLootTable();
		}
		
		public @NotNull ResourceKey<LootTable> getLootTable() {
			return this.lootTable;
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
		
		public @NotNull Vector3f getRandomParticleColor(RandomSource random) {
			switch (this) {
				case GLISTERING -> {
					int r = random.nextInt(5);
					if (r == 0) {
						return SpectrumColorHelper.getRGBVec(DyeColor.YELLOW);
					} else if (r == 1) {
						return SpectrumColorHelper.getRGBVec(DyeColor.WHITE);
					} else if (r == 2) {
						return SpectrumColorHelper.getRGBVec(DyeColor.ORANGE);
					} else if (r == 3) {
						return SpectrumColorHelper.getRGBVec(DyeColor.LIME);
					} else {
						return SpectrumColorHelper.getRGBVec(DyeColor.BLUE);
					}
				}
				case COLORFUL -> {
					return SpectrumColorHelper.getRGBVec(SpectrumColorHelper.VANILLA_DYE_COLORS.get(random.nextInt(SpectrumColorHelper.VANILLA_DYE_COLORS.size())));
				}
				case FIERY -> {
					int r = random.nextInt(2);
					if (r == 0) {
						return SpectrumColorHelper.getRGBVec(DyeColor.ORANGE);
					} else {
						return SpectrumColorHelper.getRGBVec(DyeColor.RED);
					}
				}
				case PRISTINE -> {
					int r = random.nextInt(3);
					if (r == 0) {
						return SpectrumColorHelper.getRGBVec(DyeColor.BLUE);
					} else if (r == 1) {
						return SpectrumColorHelper.getRGBVec(DyeColor.LIGHT_BLUE);
					} else {
						return SpectrumColorHelper.getRGBVec(DyeColor.CYAN);
					}
				}
				default -> {
					int r = random.nextInt(4);
					if (r == 0) {
						return SpectrumColorHelper.getRGBVec(DyeColor.CYAN);
					} else if (r == 1) {
						return SpectrumColorHelper.getRGBVec(DyeColor.MAGENTA);
					} else if (r == 2) {
						return SpectrumColorHelper.getRGBVec(DyeColor.WHITE);
					} else {
						return SpectrumColorHelper.getRGBVec(DyeColor.YELLOW);
					}
				}
			}
		}
		
		@Override
		public String getSerializedName() {
			return this.name;
		}
		
	}
}

package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;

public interface Dragonjag {
	
	enum Variant implements StringRepresentable {
		YELLOW(MapColor.SAND),
		RED(MapColor.NETHER),
		PINK(MapColor.WARPED_HYPHAE),
		PURPLE(MapColor.COLOR_PURPLE),
		BLACK(MapColor.TERRACOTTA_BLACK);
		
		public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);
		
		private final MapColor mapColor;
		
		Variant(MapColor mapColor) {
			this.mapColor = mapColor;
		}
		
		public MapColor getMapColor() {
			return this.mapColor;
		}
		
		@Override
		public String getSerializedName() {
			return name();
		}
	}
	
	Dragonjag.Variant getVariant();
	
	static boolean canPlantOnTop(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.isSolidRender(world, pos);
	}
	
}

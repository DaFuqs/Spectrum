package de.dafuqs.spectrum.worldgen;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.mixin.BlockPlacerTypeAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumBlockPlacerTypes {

	public static final BlockPlacerType<QuitoxicReedsColumnPlacer> QUITOXIC_REEDS_COLUMN_PLACER;

	private static BlockPlacerType register(String name, BlockPlacerType<?> type) {
		return Registry.register(Registry.BLOCK_PLACER_TYPE, new Identifier(SpectrumCommon.MOD_ID, name), type);
	}

	static {
		QUITOXIC_REEDS_COLUMN_PLACER = register("quitoxic_reeds_column_placer", BlockPlacerTypeAccessor.createBlockPlacerType(QuitoxicReedsColumnPlacer.CODEC));
	}

}
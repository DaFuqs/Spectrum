package de.dafuqs.spectrum.compat.alloy_forgery;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;
import vazkii.patchouli.common.multiblock.*;

public class AlloyForgeryCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static final Identifier CALCITE_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("calcite_alloy_forge");
	public static final Identifier BASALT_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("basalt_alloy_forge");
	public static final Identifier BLACKSLAG_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("blackslag_alloy_forge");
	public static final Identifier SHALE_CLAY_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("shale_clay_alloy_forge");
	public static final Identifier PYRITE_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("pyrite_alloy_forge");
	public static final Identifier BONE_ASH_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("bone_ash_alloy_forge");
	
	@Override
	public void register() {
	
	}
	
	@Override
	public void registerClient() {
	
	}
	
	@Override
	public void registerMultiblocks() {
		String[][] structure = {
				{"_P_", "P P", "_P_"},
				{"_P_", "X P", "_P_"},
				{"BBB", "B0B", "BBB"}
		};
		
		SpectrumMultiblocks.registerMultiBlock(BASALT_ALLOY_FORGE_MULTIBLOCK_ID, structure, new Object[]{
				'_', StateMatcher.ANY,
				'X', "alloy_forgery:polished_basalt_forge_controller",
				'P', "spectrum:polished_basalt",
				'B', "spectrum:basalt_bricks",
				'0', "spectrum:basalt_bricks",
		});
		
		SpectrumMultiblocks.registerMultiBlock(CALCITE_ALLOY_FORGE_MULTIBLOCK_ID, structure, new Object[]{
				'_', StateMatcher.ANY,
				'X', "alloy_forgery:polished_calcite_forge_controller",
				'P', "spectrum:polished_calcite",
				'B', "spectrum:calcite_bricks",
				'0', "spectrum:calcite_bricks",
		});
		
		SpectrumMultiblocks.registerMultiBlock(BLACKSLAG_ALLOY_FORGE_MULTIBLOCK_ID, structure, new Object[]{
				'_', StateMatcher.ANY,
				'X', "alloy_forgery:polished_blackslag_forge_controller",
				'P', "spectrum:polished_blackslag",
				'B', "spectrum:blackslag_bricks",
				'0', "spectrum:blackslag_bricks",
		});
		
		SpectrumMultiblocks.registerMultiBlock(SHALE_CLAY_ALLOY_FORGE_MULTIBLOCK_ID, structure, new Object[]{
				'_', StateMatcher.ANY,
				'X', "alloy_forgery:polished_shale_clay_forge_controller",
				'P', "spectrum:polished_shale_clay",
				'B', "spectrum:shale_clay_bricks",
				'0', "spectrum:shale_clay_bricks",
		});
		
		SpectrumMultiblocks.registerMultiBlock(PYRITE_ALLOY_FORGE_MULTIBLOCK_ID, structure, new Object[]{
				'_', StateMatcher.ANY,
				'X', "alloy_forgery:pyrite_forge_controller",
				'P', "spectrum:pyrite",
				'B', "spectrum:pyrite_plating",
				'0', "spectrum:pyrite_plating",
		});
		
		SpectrumMultiblocks.registerMultiBlock(BONE_ASH_ALLOY_FORGE_MULTIBLOCK_ID, structure, new Object[]{
				'_', StateMatcher.ANY,
				'X', "alloy_forgery:polished_bone_ash_forge_controller",
				'P', "spectrum:polished_bone_ash",
				'B', "spectrum:bone_ash_bricks",
				'0', "spectrum:bone_ash_bricks",
		});
	}
	
}

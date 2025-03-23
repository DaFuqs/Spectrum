package de.dafuqs.spectrum.compat.alloy_forgery;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.*;
import net.fabricmc.api.*;
import net.minecraft.util.*;

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
	
	@Environment(EnvType.CLIENT)
	@Override
	public void registerClient() {
	
	}
	
}

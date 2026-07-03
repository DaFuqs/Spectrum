package de.dafuqs.spectrum.compat.alloy_forgery;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.*;
import net.minecraft.resources.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.event.lifecycle.*;

public class AlloyForgeryCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static final ResourceLocation CALCITE_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("calcite_alloy_forge");
	public static final ResourceLocation BASALT_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("basalt_alloy_forge");
	public static final ResourceLocation BLACKSLAG_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("blackslag_alloy_forge");
	public static final ResourceLocation SHALE_CLAY_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("shale_clay_alloy_forge");
	public static final ResourceLocation PYRITE_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("pyrite_alloy_forge");
	public static final ResourceLocation BONE_ASH_ALLOY_FORGE_MULTIBLOCK_ID = SpectrumCommon.locate("bone_ash_alloy_forge");
	
	@Override
	public void register(IEventBus modBus) {
	
	}
	
	@Override
	public void registerClient(FMLClientSetupEvent event) {
	
	}
	
}

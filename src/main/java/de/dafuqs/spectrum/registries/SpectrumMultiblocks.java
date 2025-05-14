package de.dafuqs.spectrum.registries;

import com.klikli_dev.modonomicon.api.*;
import com.klikli_dev.modonomicon.api.multiblock.*;
import de.dafuqs.spectrum.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;

public class SpectrumMultiblocks {
	
	public static final Component PEDESTAL_SIMPLE_TEXT = Component.translatable("multiblock.spectrum.pedestal_simple");
	public static final ResourceLocation PEDESTAL_SIMPLE = SpectrumCommon.locate("pedestal_simple");
	public static final Component PEDESTAL_ADVANCED_TEXT = Component.translatable("multiblock.spectrum.pedestal_advanced");
	public static final ResourceLocation PEDESTAL_ADVANCED = SpectrumCommon.locate("pedestal_advanced");
	public static final Component PEDESTAL_COMPLEX_TEXT = Component.translatable("multiblock.spectrum.pedestal_complex");
	public static final ResourceLocation PEDESTAL_COMPLEX = SpectrumCommon.locate("pedestal_complex");
	public static final ResourceLocation PEDESTAL_COMPLEX_WITHOUT_MOONSTONE = SpectrumCommon.locate("pedestal_complex_without_moonstone");
	
	public static final Component FUSION_SHRINE_TEXT = Component.translatable("multiblock.spectrum.fusion_shrine");
	public static final ResourceLocation FUSION_SHRINE = SpectrumCommon.locate("fusion_shrine");
	
	public static final Component ENCHANTER_TEXT = Component.translatable("multiblock.spectrum.enchanter");
	public static final ResourceLocation ENCHANTER = SpectrumCommon.locate("enchanter");
	
	public static final Component SPIRIT_INSTILLER_TEXT = Component.translatable("multiblock.spectrum.spirit_instiller");
	public static final ResourceLocation SPIRIT_INSTILLER = SpectrumCommon.locate("spirit_instiller");
	
	public static final Component CINDERHEARTH_TEXT = Component.translatable("multiblock.spectrum.cinderhearth");
	public static final ResourceLocation CINDERHEARTH = SpectrumCommon.locate("cinderhearth");
	public static final ResourceLocation CINDERHEARTH_WITHOUT_LAVA = SpectrumCommon.locate("cinderhearth_no_lava");
	
	public static Multiblock get(ResourceLocation id) {
		return ModonomiconAPI.get().getMultiblock(id);
	}
	
}
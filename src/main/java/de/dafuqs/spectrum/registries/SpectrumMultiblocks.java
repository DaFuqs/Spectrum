package de.dafuqs.spectrum.registries;

import com.klikli_dev.modonomicon.api.*;
import com.klikli_dev.modonomicon.api.multiblock.*;
import de.dafuqs.spectrum.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public class SpectrumMultiblocks {
	
	public static final Text PEDESTAL_SIMPLE_TEXT = Text.translatable("multiblock.spectrum.pedestal_simple");
	public static final Identifier PEDESTAL_SIMPLE = SpectrumCommon.locate("pedestal_simple");
	public static final Text PEDESTAL_ADVANCED_TEXT = Text.translatable("multiblock.spectrum.pedestal_advanced");
	public static final Identifier PEDESTAL_ADVANCED = SpectrumCommon.locate("pedestal_advanced");
	public static final Text PEDESTAL_COMPLEX_TEXT = Text.translatable("multiblock.spectrum.pedestal_complex");
	public static final Identifier PEDESTAL_COMPLEX = SpectrumCommon.locate("pedestal_complex");
	public static final Identifier PEDESTAL_COMPLEX_WITHOUT_MOONSTONE = SpectrumCommon.locate("pedestal_complex_without_moonstone");
	
	public static final Text FUSION_SHRINE_TEXT = Text.translatable("multiblock.spectrum.fusion_shrine");
	public static final Identifier FUSION_SHRINE = SpectrumCommon.locate("fusion_shrine");
	
	public static final Text ENCHANTER_TEXT = Text.translatable("multiblock.spectrum.enchanter");
	public static final Identifier ENCHANTER = SpectrumCommon.locate("enchanter");
	
	public static final Text SPIRIT_INSTILLER_TEXT = Text.translatable("multiblock.spectrum.spirit_instiller");
	public static final Identifier SPIRIT_INSTILLER = SpectrumCommon.locate("spirit_instiller");
	
	public static final Text CINDERHEARTH_TEXT = Text.translatable("multiblock.spectrum.cinderhearth");
	public static final Identifier CINDERHEARTH = SpectrumCommon.locate("cinderhearth");
	public static final Identifier CINDERHEARTH_WITHOUT_LAVA = SpectrumCommon.locate("cinderhearth_no_lava");
	
	public static Multiblock get(Identifier id) {
		return ModonomiconAPI.get().getMultiblock(id);
	}
	
}
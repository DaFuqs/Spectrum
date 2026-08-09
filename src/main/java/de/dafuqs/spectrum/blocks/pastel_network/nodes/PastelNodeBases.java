package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.*;
import net.neoforged.api.distmarker.*;

public class PastelNodeBases {
	
	public record PastelNodeBase(ResourceLocation location) {
		
		@OnlyIn(Dist.CLIENT)
		public ModelResourceLocation getModelLocation() {
			return ModelResourceLocation.standalone(location);
		}
		
	}
	
	public static final ResourceLocation BASE_LOCATION = SpectrumCommon.locate("technical/pastel_item_base");
	public static final ResourceLocation FLUID_LOCATION = SpectrumCommon.locate("technical/pastel_fluid_base");
	public static final ResourceLocation INK_LOCATION = SpectrumCommon.locate("technical/pastel_ink_base");
	public static final ResourceLocation OMNI_LOCATION = SpectrumCommon.locate("technical/pastel_omni_base");
	
	public static final PastelNodeBase ITEM = new PastelNodeBase(BASE_LOCATION);
	public static final PastelNodeBase FLUID = new PastelNodeBase(FLUID_LOCATION);
	public static final PastelNodeBase INK = new PastelNodeBase(INK_LOCATION);
	public static final PastelNodeBase OMNI = new PastelNodeBase(OMNI_LOCATION);
	
}

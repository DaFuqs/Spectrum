package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.monster.*;
import net.neoforged.api.distmarker.*;
import javax.annotation.*;

@OnlyIn(Dist.CLIENT)
public class SplinterspawnRenderer extends SilverfishRenderer {
	
	private static final ResourceLocation SPLINTERSPAWN_LOCATION = SpectrumCommon.locate("textures/entity/splinterspawn/splinterspawn.png");
	
	public SplinterspawnRenderer(EntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	public ResourceLocation getTextureLocation(Silverfish entity) {
		return SPLINTERSPAWN_LOCATION;
	}
}

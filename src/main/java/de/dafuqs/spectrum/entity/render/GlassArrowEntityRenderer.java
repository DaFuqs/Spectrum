package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.GlassArrowEntity;
import de.dafuqs.spectrum.items.tools.GlassArrowItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GlassArrowEntityRenderer extends ProjectileEntityRenderer<GlassArrowEntity> {
    
    public static final Identifier TEXTURE_GLASS = SpectrumCommon.locate("textures/entity/projectiles/glass_arrow.png");
    public static final Identifier TEXTURE_TOPAZ = SpectrumCommon.locate("textures/entity/projectiles/topaz_glass_arrow.png");
    public static final Identifier TEXTURE_AMETHYST = SpectrumCommon.locate("textures/entity/projectiles/amethyst_glass_arrow.png");
    public static final Identifier TEXTURE_CITRINE = SpectrumCommon.locate("textures/entity/projectiles/citrine_glass_arrow.png");
    public static final Identifier TEXTURE_ONYX = SpectrumCommon.locate("textures/entity/projectiles/onyx_glass_arrow.png");
    public static final Identifier TEXTURE_MOONSTONE = SpectrumCommon.locate("textures/entity/projectiles/moonstone_glass_arrow.png");

    public GlassArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(GlassArrowEntity arrowEntity) {
        GlassArrowItem.Variant variant = arrowEntity.getVariant();
        switch (variant) {
            case TOPAZ -> {
                return TEXTURE_TOPAZ;
            }
            case AMETHYST -> {
                return TEXTURE_AMETHYST;
            }
            case CITRINE -> {
                return TEXTURE_CITRINE;
            }
            case ONYX -> {
                return TEXTURE_ONYX;
            }
            case MOONSTONE -> {
                return TEXTURE_MOONSTONE;
            }
            default -> {
                return TEXTURE_GLASS;
            }
        }
    }
    
}

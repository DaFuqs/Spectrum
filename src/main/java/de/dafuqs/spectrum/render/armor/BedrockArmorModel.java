package de.dafuqs.spectrum.render.armor;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.armor.BedrockArmorItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BedrockArmorModel extends AnimatedGeoModel<BedrockArmorItem> {

    @Override
    public Identifier getModelLocation(BedrockArmorItem object) {
        return SpectrumCommon.locate("geo/armor/bedrock_armor.geo.json");
    }

    @Override
    public Identifier getTextureLocation(BedrockArmorItem object) {
        return SpectrumCommon.locate("textures/armor/bedrock_armor.png");
    }

    @Override
    public Identifier getAnimationFileLocation(BedrockArmorItem animatable) {
        return SpectrumCommon.locate("animations/armor/bedrock_armor.animation.json");
    }

    @Override
    public void setLivingAnimations(BedrockArmorItem item, Integer uniqueID, AnimationEvent event) {
        super.setLivingAnimations(item, uniqueID, event);
        var wearer = (LivingEntity) event.getExtraDataOfType(LivingEntity.class).get(0);
        var loincloth = getAnimationProcessor().getBone("cock_bone");
        var cape = getAnimationProcessor().getBone("cape_bone");

        if(wearer.isInSneakingPose()) {
            loincloth.setRotationX(loincloth.getPositionX() + 0.55F);
        }

        var velocity = wearer.getVelocity().length();

        if(velocity > 0.05) {
            cape.setRotationX((float) (cape.getPositionX() - velocity));
        }
    }
}

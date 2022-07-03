package de.dafuqs.spectrum.graces;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.azure_dike.AzureDikeComponent;
import de.dafuqs.spectrum.items.CrystalGraceItem;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.util.Identifier;

public class GraceProvider implements ItemComponentInitializer {

    public static final ComponentKey<CrystalGraceEffectHolder> CRYSTAL_GRACE_EFFECT_HOLDER = ComponentRegistry.getOrCreate(new Identifier(SpectrumCommon.MOD_ID, "crystal_grace_effect_holder"), CrystalGraceEffectHolder.class); // See the "Registering your component" section

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(item -> item instanceof CrystalGraceItem, CRYSTAL_GRACE_EFFECT_HOLDER, CrystalGraceEffectHolder::new);
    }
}

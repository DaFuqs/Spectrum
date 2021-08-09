package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class PatchouliPages {

    public static void register() {
        ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(SpectrumCommon.MOD_ID, "altar_crafting"), PageAltarCrafting.class);
    }

}

package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class PatchouliPages {

	public static void register() {
		ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(SpectrumCommon.MOD_ID, "pedestal_crafting"), PagePedestalCrafting.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(SpectrumCommon.MOD_ID, "fusion_shrine_crafting"), PageFusionShrine.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(SpectrumCommon.MOD_ID, "enchanter_crafting"), PageEnchanterRecipe.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(SpectrumCommon.MOD_ID, "enchanter_upgrading"), PageEnchantmentUpgradeRecipe.class);
		
		ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(SpectrumCommon.MOD_ID, "hint"), PageHint.class);
	}

}

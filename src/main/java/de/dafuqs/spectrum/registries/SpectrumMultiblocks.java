package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.enums.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.api.*;
import vazkii.patchouli.common.multiblock.*;

import java.util.*;
import java.util.concurrent.*;

public class SpectrumMultiblocks {
	
	public static final Map<Identifier, IMultiblock> MULTIBLOCKS = new ConcurrentHashMap<>();
	
	public static Identifier PEDESTAL_SIMPLE_STRUCTURE_IDENTIFIER_CHECK = SpectrumCommon.locate("pedestal_simple_structure_check");
	public static Identifier PEDESTAL_SIMPLE_STRUCTURE_IDENTIFIER_PLACE = SpectrumCommon.locate("pedestal_simple_structure_place");
	public static Identifier PEDESTAL_ADVANCED_STRUCTURE_IDENTIFIER_CHECK = SpectrumCommon.locate("pedestal_advanced_structure_check");
	public static Identifier PEDESTAL_ADVANCED_STRUCTURE_IDENTIFIER_PLACE = SpectrumCommon.locate("pedestal_advanced_structure_place");
	public static Identifier PEDESTAL_COMPLEX_STRUCTURE_IDENTIFIER_CHECK = SpectrumCommon.locate("pedestal_complex_structure_check");
	public static Identifier PEDESTAL_COMPLEX_STRUCTURE_IDENTIFIER_PLACE = SpectrumCommon.locate("pedestal_complex_structure_place");
	public static Identifier PEDESTAL_COMPLEX_STRUCTURE_WITHOUT_MOONSTONE_IDENTIFIER_CHECK = SpectrumCommon.locate("pedestal_complex_structure_without_moonstone_check");
	public static Identifier PEDESTAL_COMPLEX_STRUCTURE_WITHOUT_MOONSTONE_IDENTIFIER_PLACE = SpectrumCommon.locate("pedestal_complex_structure_without_moonstone_place");
	
	public static Identifier FUSION_SHRINE_IDENTIFIER = SpectrumCommon.locate("fusion_shrine_structure");
	public static Identifier ENCHANTER_IDENTIFIER = SpectrumCommon.locate("enchanter_structure");
	public static Identifier SPIRIT_INSTILLER_IDENTIFIER = SpectrumCommon.locate("spirit_instiller_structure");
	public static Identifier CINDERHEARTH_IDENTIFIER = SpectrumCommon.locate("cinderhearth_structure");
	public static Identifier CINDERHEARTH_WITHOUT_LAVA_IDENTIFIER = SpectrumCommon.locate("cinderhearth_without_lava_structure");
	
	public static Identifier PEDESTAL_IDENTIFIER_WITH_UPGRADES = SpectrumCommon.locate("pedestal_structure_with_upgrades");
	public static Identifier FUSION_SHRINE_IDENTIFIER_WITH_UPGRADES = SpectrumCommon.locate("fusion_shrine_structure_with_upgrades");
	public static Identifier ENCHANTER_IDENTIFIER_WITH_UPGRADES = SpectrumCommon.locate("enchanter_structure_with_upgrades");
	public static Identifier CINDERHEARTH_IDENTIFIER_WITH_UPGRADES = SpectrumCommon.locate("cinderhearth_structure_with_upgrades");
	public static Identifier SPIRIT_INSTILLER_IDENTIFIER_WITH_UPGRADES = SpectrumCommon.locate("spirit_instiller_structure_with_upgrades");
	
	private static void registerMultiBlock(Identifier identifier, String[][] structure, Object[] targetBlocks) {
		IMultiblock multiblock = PatchouliAPI.get().makeMultiblock(structure, targetBlocks);
		MULTIBLOCKS.put(identifier, PatchouliAPI.get().registerMultiblock(identifier, multiblock));
	}
	
	private static boolean registered = false;
	
	public static void register() {
		if (!registered) {
			registerPedestal();
			registerFusionShrine();
			registerEnchanter();
			registerSpiritInstiller();
			registerCinderhearth();
			registered = true;
		}
	}
	
	private static void registerFusionShrine() {
		Object[] targetBlocks = {
				'X', "#spectrum:polished_base_blocks",
				'P', "#spectrum:pillar_base_blocks",
				'L', "#spectrum:polished_base_block_slabs",
				'S', "#spectrum:gemstone_storage_blocks",
				'C', "#spectrum:chiseled_base_blocks",
				'F', "#spectrum:fusion_shrines",
				'U', "spectrum:upgrade_speed",
				'_', StateMatcher.ANY,
				'0', "#spectrum:polished_base_blocks",
		};
		
		String[][] structure = {
				{"_________", "_L_____L_", "_________", "_________", "_________", "_________", "_________", "_L_____L_", "_________"},
				{"_________", "_S_____S_", "_________", "_________", "_________", "_________", "_________", "_S_____S_", "_________"},
				{"_________", "_P_____P_", "_________", "_________", "_________", "_________", "_________", "_P_____P_", "_________"},
				{"__XXXXX__", "_P_____P_", "X_______X", "X_______X", "X___F___X", "X_______X", "X_______X", "_P_____P_", "__XXXXX__"},
				{"__XXXXX__", "_XXXXXXX_", "XXCXXXCXX", "XXXLLLXXX", "XXXL0LXXX", "XXXLLLXXX", "XXCXXXCXX", "_XXXXXXX_", "__XXXXX__"}
		};
		registerMultiBlock(FUSION_SHRINE_IDENTIFIER, structure, targetBlocks);
		
		String[][] structureUpgrades = {
				{"_________", "_L_____L_", "_________", "_________", "_________", "_________", "_________", "_L_____L_", "_________"},
				{"_________", "_S_____S_", "_________", "_________", "_________", "_________", "_________", "_S_____S_", "_________"},
				{"_________", "_P_____P_", "_________", "_________", "_________", "_________", "_________", "_P_____P_", "_________"},
				{"__XXXXX__", "_P_____P_", "X_U___U_X", "X_______X", "X___F___X", "X_______X", "X_U___U_X", "_P_____P_", "__XXXXX__"},
				{"__XXXXX__", "_XXXXXXX_", "XXCXXXCXX", "XXXLLLXXX", "XXXL0LXXX", "XXXLLLXXX", "XXCXXXCXX", "_XXXXXXX_", "__XXXXX__"}
		};
		registerMultiBlock(FUSION_SHRINE_IDENTIFIER_WITH_UPGRADES, structureUpgrades, targetBlocks);
	}
	
	private static void registerSpiritInstiller() {
		Object[] targetBlocks = {
				'S', "spectrum:spirit_instiller",
				'X', "#spectrum:polished_base_blocks",
				'L', "#spectrum:polished_base_block_slabs",
				'P', "#spectrum:pillar_base_blocks",
				'C', "#spectrum:chiseled_base_blocks",
				'G', "#spectrum:onyx_chiseled_base_blocks",
				'K', "#spectrum:notched_base_blocks",
				'I', "#spectrum:item_bowls",
				'M', "#spectrum:gemstone_chimes",
				'U', "spectrum:upgrade_speed",
				'_', StateMatcher.ANY,
				'0', "#spectrum:polished_base_blocks",
		};
		
		String[][] structure = {
				{"________", "________", "__M_____", "________", "________", "________", "__M_____", "________", "________"},
				{"________", "________", "________", "________", "________", "________", "________", "________", "________"},
				{"________", "K_______", "__I_____", "________", "________", "________", "__I_____", "K_______", "________"},
				{"______G_", "P_______", "__C_____", "_LLLLL__", "_LSLLL__", "_LLLLL__", "__C_____", "P_______", "______G_"},
				{"______P_", "PXXXX___", "XXXXXX__", "XXXXXXXL", "XXXXXX0L", "XXXXXXXL", "XXXXXX__", "PXXXX___", "______P_"}
		};
		registerMultiBlock(SPIRIT_INSTILLER_IDENTIFIER, structure, targetBlocks);
		
		String[][] structureUpgrades = {
				{"________", "________", "__M_____", "________", "________", "________", "__M_____", "________", "________"},
				{"________", "________", "________", "________", "________", "________", "________", "________", "________"},
				{"______U_", "K_______", "__I_____", "________", "________", "________", "__I_____", "K_______", "______U_"},
				{"______G_", "P_______", "__C_____", "_LLLLL__", "_LSLLL__", "_LLLLL__", "__C_____", "P_______", "______G_"},
				{"______P_", "PXXXX___", "XXXXXX__", "XXXXXXXL", "XXXXXX0L", "XXXXXXXL", "XXXXXX__", "PXXXX___", "______P_"}
		};
		registerMultiBlock(SPIRIT_INSTILLER_IDENTIFIER_WITH_UPGRADES, structureUpgrades, targetBlocks);
	}
	
	private static void registerEnchanter() {
		Object[] targetBlocks = {
				'X', "#spectrum:polished_base_blocks",
				'P', "#spectrum:pillar_base_blocks",
				'B', "#spectrum:polished_base_block_slabs",
				'L', "#spectrum:gemstone_lamps",
				'G', "#spectrum:gemstone_storage_blocks",
				'F', "spectrum:enchanter",
				'C', "spectrum:liquid_crystal",
				'I', "#spectrum:item_bowls",
				'U', "spectrum:upgrade_speed",
				'_', StateMatcher.ANY,
				'0', "#spectrum:polished_base_blocks",
		};
		
		String[][] structure = {
				{"__I_____I__", "___________", "I_________I", "___________", "___________", "_____F_____", "___________", "___________", "I_________I", "___________", "__I_____I__",},
				{"__L_____L__", "___________", "L_G_____G_L", "____BBB____", "___BXXXB___", "___BXXXB___", "___BXXXB___", "____BBB____", "L_G_____G_L", "___________", "__L_____L__",},
				{"__P_____P__", "____BBB____", "P_P_XXX_P_P", "____XXX____", "_BXXXXXXXB_", "_BXXXXXXXB_", "_BXXXXXXXB_", "____XXX____", "P_P_XXX_P_P", "____BBB____", "__P_____P__",},
				{"_XXXXXXXXX_", "XCCCXXXCCCX", "XCXCXXXCXCX", "XCCCXXXCCCX", "XXXXXXXXXXX", "XXXXX0XXXXX", "XXXXXXXXXXX", "XCCCXXXCCCX", "XCXCXXXCXCX", "XCCCXXXCCCX", "_XXXXXXXXX_",},
				{"___________", "_XXX___XXX_", "_X_X___X_X_", "_XXX___XXX_", "___________", "___________", "___________", "_XXX___XXX_", "_X_X___X_X_", "_XXX___XXX_", "___________"}
		};
		registerMultiBlock(ENCHANTER_IDENTIFIER, structure, targetBlocks);
		
		String[][] structureUpgrades = {
				{"__I_____I__", "___________", "I_U_____U_I", "___________", "___________", "_____F_____", "___________", "___________", "I_U_____U_I", "___________", "__I_____I__",},
				{"__L_____L__", "___________", "L_G_____G_L", "____BBB____", "___BXXXB___", "___BXXXB___", "___BXXXB___", "____BBB____", "L_G_____G_L", "___________", "__L_____L__",},
				{"__P_____P__", "____BBB____", "P_P_XXX_P_P", "____XXX____", "_BXXXXXXXB_", "_BXXXXXXXB_", "_BXXXXXXXB_", "____XXX____", "P_P_XXX_P_P", "____BBB____", "__P_____P__",},
				{"_XXXXXXXXX_", "XCCCXXXCCCX", "XCXCXXXCXCX", "XCCCXXXCCCX", "XXXXXXXXXXX", "XXXXX0XXXXX", "XXXXXXXXXXX", "XCCCXXXCCCX", "XCXCXXXCXCX", "XCCCXXXCCCX", "_XXXXXXXXX_",},
				{"___________", "_XXX___XXX_", "_X_X___X_X_", "_XXX___XXX_", "___________", "___________", "___________", "_XXX___XXX_", "_X_X___X_X_", "_XXX___XXX_", "___________"}
		};
		registerMultiBlock(ENCHANTER_IDENTIFIER_WITH_UPGRADES, structureUpgrades, targetBlocks);
	}
	
	private static void registerCinderhearth() {
		Object[] targetBlocks = {
				'X', "#spectrum:polished_base_blocks",
				'C', "#spectrum:chiseled_base_blocks",
				'c', "#spectrum:crest_base_blocks",
				'G', "#minecraft:impermeable",
				'L', "minecraft:lava",
				'U', "spectrum:upgrade_speed",
				'_', StateMatcher.ANY,
				'H', "spectrum:cinderhearth[facing=south]",
				'0', "#spectrum:polished_base_blocks",
		};
		
		String[][] structure = {
				{"C___", "____", "_H__", "____", "C___"},
				{"Xcc_", "cGGX", "__GX", "cGGX", "Xcc_"},
				{"_XX_", "XLLX", "XLLX", "XLLX", "_XX_"},
				{"____", "_XX_", "_0X_", "_XX_", "____"}
		};
		registerMultiBlock(CINDERHEARTH_IDENTIFIER, structure, targetBlocks);
		
		String[][] withoutLavaStructure = {
				{"C___", "____", "_H__", "____", "C___"},
				{"Xcc_", "cGGX", "__GX", "cGGX", "Xcc_"},
				{"_XX_", "X__X", "X__X", "X__X", "_XX_"},
				{"____", "_XX_", "_0X_", "_XX_", "____"}
		};
		registerMultiBlock(CINDERHEARTH_WITHOUT_LAVA_IDENTIFIER, withoutLavaStructure, targetBlocks);
		
		String[][] structureUpgrades = {
				{"U___", "____", "_H__", "____", "U___"},
				{"C___", "____", "_H__", "____", "C___"},
				{"Xcc_", "cGGX", "__GX", "cGGX", "Xcc_"},
				{"_XX_", "XLLX", "XLLX", "XLLX", "_XX_"},
				{"____", "_XX_", "_0X_", "_XX_", "____"}
		};
		registerMultiBlock(CINDERHEARTH_IDENTIFIER_WITH_UPGRADES, structureUpgrades, targetBlocks);
	}
	
	private static void registerPedestal() {
		// since the structure is using rotation on blocks from a block tag
		// (that's impossible to define via string) we have to split targets for
		// check (if structure is built) and display (for auto building via debug item and guidebook)
		Object[] targetBlocksCheck = {
				'X', "#spectrum:polished_base_blocks",
				'T', "#spectrum:crest_base_blocks",
				't', "#spectrum:crest_base_blocks",
				'Q', "#spectrum:pillar_base_blocks",
				'L', "#spectrum:gemstone_lamps",
				'S', "#spectrum:gemstone_storage_blocks",
				'C', "#spectrum:chiseled_base_blocks",
				'K', "#spectrum:notched_base_blocks",
				'r', "#spectrum:basic_and_onyx_gemstone_chiseled_base_blocks",
				'O', "#spectrum:onyx_chiseled_base_blocks",
				'm', "#spectrum:moonstone_chiseled_base_blocks",
				'n', "#spectrum:moonstone_chiseled_base_blocks",
				'M', "#spectrum:moonstone_chiseled_base_blocks",
				'N', "#spectrum:moonstone_chiseled_base_blocks",
				'2', "#spectrum:pedestals",
				'3', "#spectrum:pedestals",
				'4', "#spectrum:pedestals",
				'_', StateMatcher.ANY,
				'0', StateMatcher.ANY
		};
		
		Object[] targetBlocksPlace = {
				'X', "spectrum:polished_basalt",
				'T', "spectrum:polished_basalt_crest[cardinal_facing=true]",
				't', "spectrum:polished_basalt_crest[cardinal_facing=false]",
				'Q', "spectrum:polished_basalt_pillar[axis=y]",
				'L', "spectrum:amethyst_basalt_lamp",
				'S', "spectrum:amethyst_storage_block",
				'C', "spectrum:chiseled_polished_basalt",
				'K', "spectrum:notched_polished_basalt",
				'r', "spectrum:amethyst_chiseled_basalt",
				'O', "spectrum:onyx_chiseled_basalt",
				'm', "spectrum:moonstone_chiseled_basalt[facing=east]",
				'n', "spectrum:moonstone_chiseled_basalt[facing=north]",
				'M', "spectrum:moonstone_chiseled_basalt[facing=west]",
				'N', "spectrum:moonstone_chiseled_basalt[facing=south]",
				'2', "spectrum:pedestal_all_basic",
				'3', "spectrum:pedestal_onyx",
				'4', "spectrum:pedestal_moonstone",
				'U', "spectrum:upgrade_speed",
				'_', StateMatcher.ANY,
				'0', StateMatcher.ANY
		};
		
		String[][] tier1Structure = {
				{"C_________C", "___________", "___________", "___________", "___________", "___________", "___________", "___________", "___________", "___________", "C_________C"},
				{"Q_________Q", "___________", "__S_____S__", "___________", "___________", "___________", "___________", "___________", "__S_____S__", "___________", "Q_________Q"},
				{"K_________K", "___________", "__Q_____Q__", "___________", "___________", "_____2_____", "___________", "___________", "__Q_____Q__", "___________", "K_________K"},
				{"___________", "___XXXXX___", "__XXXXXXX__", "_XXXXXXXXX_", "_XXXXXXXXX_", "_XXXX0XXXX_", "_XXXXXXXXX_", "_XXXXXXXXX_", "__XXXXXXX__", "___XXXXX___", "___________"}
		};
		registerMultiBlock(PEDESTAL_SIMPLE_STRUCTURE_IDENTIFIER_CHECK, tier1Structure, targetBlocksCheck);
		registerMultiBlock(PEDESTAL_SIMPLE_STRUCTURE_IDENTIFIER_PLACE, tier1Structure, targetBlocksPlace);
		
		String[][] tier2Structure = {
				{"_____________", "_Sttr___rttS_", "_T_________T_", "_T_________T_", "_r_________r_", "_____________", "_____________", "_____________", "_r_________r_", "_T_________T_", "_T_________T_", "_Sttr___rttS_", "_____________"},
				{"_____________", "_Q__Q___Q__Q_", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_Q__Q___Q__Q_", "_____________"},
				{"_____________", "_C__Q___Q__C_", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_C__Q___Q__C_", "_____________"},
				{"_____________", "_Q__L___L__Q_", "_____________", "___S_____S___", "_L_________L_", "_____________", "_____________", "_____________", "_L_________L_", "___S_____S___", "_____________", "_Q__L___L__Q_", "_____________"},
				{"_____________", "_K__Q___Q__K_", "_____________", "___Q_____Q___", "_Q_________Q_", "_____________", "______3______", "_____________", "_Q_________Q_", "___Q_____Q___", "_____________", "_K__Q___Q__K_", "_____________"},
				{"XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXrXrXXXXX", "XXXXrXXXrXXXX", "XXXXXX0XXXXXX", "XXXXrXXXrXXXX", "XXXXXrXrXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX"}
		};
		registerMultiBlock(PEDESTAL_ADVANCED_STRUCTURE_IDENTIFIER_CHECK, tier2Structure, targetBlocksCheck);
		registerMultiBlock(PEDESTAL_ADVANCED_STRUCTURE_IDENTIFIER_PLACE, tier2Structure, targetBlocksPlace);
		
		String[][] tier2StructureUpgrades = {
				{"_____________", "_Sttr___rttS_", "_T_________T_", "_T_________T_", "_r_________r_", "_____________", "_____________", "_____________", "_r_________r_", "_T_________T_", "_T_________T_", "_Sttr___rttS_", "_____________"},
				{"_____________", "_Q__Q___Q__Q_", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_Q__Q___Q__Q_", "_____________"},
				{"_____________", "_C__Q___Q__C_", "_____________", "___U_____U___", "_Q_________Q_", "_____________", "_____________", "_____________", "_Q_________Q_", "___U_____U___", "_____________", "_C__Q___Q__C_", "_____________"},
				{"_____________", "_Q__L___L__Q_", "_____________", "___S_____S___", "_L_________L_", "_____________", "_____________", "_____________", "_L_________L_", "___S_____S___", "_____________", "_Q__L___L__Q_", "_____________"},
				{"_____________", "_K__Q___Q__K_", "_____________", "___Q_____Q___", "_Q_________Q_", "_____________", "______3______", "_____________", "_Q_________Q_", "___Q_____Q___", "_____________", "_K__Q___Q__K_", "_____________"},
				{"XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXrXrXXXXX", "XXXXrXXXrXXXX", "XXXXXX0XXXXXX", "XXXXrXXXrXXXX", "XXXXXrXrXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX"}
		};
		registerMultiBlock(PEDESTAL_IDENTIFIER_WITH_UPGRADES, tier2StructureUpgrades, targetBlocksPlace);
		
		
		String[][] tier3StructureWithoutMoonstone = {
				{"_____________", "____XtStX____", "_____________", "_____________", "_X__OtttO__X_", "_T__T___T__T_", "_S__T___T__S_", "_T__T___T__T_", "_X__OtttO__X_", "_____________", "_____________", "____XtStX____", "_____________"},
				{"_____________", "_Sttr___rttS_", "_T__T___T__T_", "_T__T___T__T_", "_rttX___Xttr_", "_____________", "_____________", "_____________", "_rttX___Xttr_", "_T__T___T__T_", "_T__T___T__T_", "_Sttr___rttS_", "_____________"},
				{"_____________", "_Q__Q___Q__Q_", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_Q__Q___Q__Q_", "_____________"},
				{"_____________", "_C__Q___Q__C_", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_C__Q___Q__C_", "_____________"},
				{"_____________", "_Q__L___L__Q_", "_____________", "___S_____S___", "_L_________L_", "_____________", "_____________", "_____________", "_L_________L_", "___S_____S___", "_____________", "_Q__L___L__Q_", "_____________"},
				{"_____________", "_K__Q___Q__K_", "_____________", "___Q_____Q___", "_Q_________Q_", "_____________", "______4______", "_____________", "_Q_________Q_", "___Q_____Q___", "_____________", "_K__Q___Q__K_", "_____________"},
				{"XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXrXrXXXXX", "XXXXrXXXrXXXX", "XXXXXX0XXXXXX", "XXXXrXXXrXXXX", "XXXXXrXrXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX"}
		};
		registerMultiBlock(PEDESTAL_COMPLEX_STRUCTURE_WITHOUT_MOONSTONE_IDENTIFIER_CHECK, tier3StructureWithoutMoonstone, targetBlocksCheck);
		registerMultiBlock(PEDESTAL_COMPLEX_STRUCTURE_WITHOUT_MOONSTONE_IDENTIFIER_PLACE, tier3StructureWithoutMoonstone, targetBlocksPlace);
		
		String[][] tier3Structure = {
				{"_____________", "____XtStX____", "_____________", "_____________", "_X__OtttO__X_", "_T__T___T__T_", "_S__T___T__S_", "_T__T___T__T_", "_X__OtttO__X_", "_____________", "_____________", "____XtStX____", "_____________"},
				{"_____________", "_Sttr___rttS_", "_T__T___T__T_", "_T__T___T__T_", "_rttX___Xttr_", "_____________", "_____________", "_____________", "_rttX___Xttr_", "_T__T___T__T_", "_T__T___T__T_", "_Sttr___rttS_", "_____________"},
				{"_____________", "_Q__Q___Q__Q_", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_Q__Q___Q__Q_", "_____________"},
				{"_____________", "_C__Q___Q__C_", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_C__Q___Q__C_", "_____________"},
				{"_____________", "_Q__L___L__Q_", "_____________", "___S_____S___", "_L_________L_", "_____________", "_____________", "_____________", "_L_________L_", "___S_____S___", "_____________", "_Q__L___L__Q_", "_____________"},
				{"_____________", "_K__Q___Q__K_", "_____________", "___Q_____Q___", "_Q_________Q_", "_____________", "______4______", "_____________", "_Q_________Q_", "___Q_____Q___", "_____________", "_K__Q___Q__K_", "_____________"},
				{"XXXXXXXXXXXXX", "XXnnXnnnXnnXX", "XmXXXXXXXXXMX", "XmXXXXXXXXXMX", "XXXXXrXrXXXXX", "XmXXrXXXrXXMX", "XmXXXX0XXXXMX", "XmXXrXXXrXXMX", "XXXXXrXrXXXXX", "XmXXXXXXXXXMX", "XmXXXXXXXXXMX", "XXNNXNNNXNNXX", "XXXXXXXXXXXXX"}
		};
		registerMultiBlock(PEDESTAL_COMPLEX_STRUCTURE_IDENTIFIER_CHECK, tier3Structure, targetBlocksCheck);
		registerMultiBlock(PEDESTAL_COMPLEX_STRUCTURE_IDENTIFIER_PLACE, tier3Structure, targetBlocksPlace);
	}
	
	@Contract(pure = true)
	public static @Nullable Identifier getDisplayStructureIdentifierForTier(@NotNull PedestalRecipeTier pedestalRecipeTier) {
		switch (pedestalRecipeTier) {
			case COMPLEX -> {
				return SpectrumMultiblocks.PEDESTAL_COMPLEX_STRUCTURE_IDENTIFIER_CHECK;
			}
			case ADVANCED -> {
				return SpectrumMultiblocks.PEDESTAL_ADVANCED_STRUCTURE_IDENTIFIER_CHECK;
			}
			case SIMPLE -> {
				return SpectrumMultiblocks.PEDESTAL_SIMPLE_STRUCTURE_IDENTIFIER_CHECK;
			}
		}
		return null;
	}
	
	public static @Nullable Text getPedestalStructureText(@NotNull PedestalRecipeTier pedestalRecipeTier) {
		switch (pedestalRecipeTier) {
			case COMPLEX -> {
				return Text.translatable("multiblock.spectrum.pedestal.complex_structure");
			}
			case ADVANCED -> {
				return Text.translatable("multiblock.spectrum.pedestal.advanced_structure");
			}
			case SIMPLE -> {
				return Text.translatable("multiblock.spectrum.pedestal.simple_structure");
			}
		}
		return null;
	}
	
}
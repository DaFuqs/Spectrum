package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpectrumMultiblocks {

	public static final Map<Identifier, IMultiblock> MULTIBLOCKS = new ConcurrentHashMap<>();
	
	private static IMultiblock registerMultiBlock(String identifierString, String[][] structure, Object[] targetBlocks) {
		Identifier identifier = new Identifier(SpectrumCommon.MOD_ID, identifierString);
		IMultiblock multiblock = PatchouliAPI.get().makeMultiblock(structure, targetBlocks);
		MULTIBLOCKS.put(identifier, PatchouliAPI.get().registerMultiblock(identifier, multiblock));
		return multiblock;
	}

	public static void register() {
		// since the structure is using rotation on blocks from a block tag
		// (that's impossible to define via string) we have to split targets for
		// check (if structure is built) and display (for auto building via debug item and manual)
		Object[] targetBlocksCheck = {
				'X', "#spectrum:polished_base_blocks",
				'P', "#spectrum:pillar_base_blocks",
				'p', "#spectrum:pillar_base_blocks",
				'Q', "#spectrum:pillar_base_blocks",
				'L', "#spectrum:gemstone_lamps",
				'S', "#spectrum:gemstone_storage_blocks",
				'C', "#spectrum:chiseled_base_blocks",
				'R', "#spectrum:basic_gemstone_chiseled_base_blocks",
				'O', "#spectrum:onyx_chiseled_base_blocks",
				'm', "#spectrum:moonstone_chiseled_base_blocks",
				'M', "#spectrum:moonstone_chiseled_base_blocks",
				'A', "spectrum:pedestal_all_basic",
				'a', "spectrum:pedestal_onyx",
				'B', "spectrum:pedestal_moonstone"
		};

		Object[] targetBlocksDisplay = {
			'X', "spectrum:polished_basalt",
			'P', "spectrum:polished_basalt_pillar[axis=x]",
			'p', "spectrum:polished_basalt_pillar[axis=z]",
			'Q', "spectrum:polished_basalt_pillar[axis=y]",
			'L', "spectrum:amethyst_basalt_lamp",
			'S', "spectrum:amethyst_storage_block",
			'C', "spectrum:chiseled_polished_basalt",
			'R', "spectrum:amethyst_chiseled_basalt",
			'O', "spectrum:onyx_chiseled_basalt",
			'm', "spectrum:moonstone_chiseled_basalt[axis=x]",
			'M', "spectrum:moonstone_chiseled_basalt[axis=y]",
			'A', "spectrum:pedestal_all_basic",
			'a', "spectrum:pedestal_onyx",
			'B', "spectrum:pedestal_moonstone"
		};

		String[][] tier1Structure = {
				{ "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           " },
				{ "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           " },
				{ "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           " },
				{ "C         C", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "C         C" },
				{ "Q         Q", "           ", "  S     S  ", "           ", "           ", "           ", "           ", "           ", "  S     S  ", "           ", "Q         Q" },
				{ "X         X", "           ", "  Q     Q  ", "           ", "           ", "     A     ", "           ", "           ", "  Q     Q  ", "           ", "X         X" },
				{ "           ", "   XXXXX   ", "  XXXXXXX  ", " XXXXXXXXX ", " XXXXXXXXX ", " XXXX0XXXX ", " XXXXXXXXX ", " XXXXXXXXX ", "  XXXXXXX  ", "   XXXXX   ", "           " }
		};
		registerMultiBlock("pedestal_basic_structure", tier1Structure, targetBlocksCheck);

		String[][] tier2Structure = {
				{ "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             " },
				{ "             ", " SppR   RppS ", " P         P ", " P         P ", " R         R ", "             ", "             ", "             ", " R         R ", " P         P ", " P         P ", " SppR   RppS ", "             " },
				{ "             ", " Q  Q   Q  Q ", "             ", "             ", " Q         Q ", "             ", "             ", "             ", " Q         Q ", "             ", "             ", " Q  Q   Q  Q ", "             " },
				{ "             ", " C  Q   Q  C ", "             ", "             ", " Q         Q ", "             ", "             ", "             ", " Q         Q ", "             ", "             ", " C  Q   Q  C ", "             " },
				{ "             ", " Q  L   L  Q ", "             ", "   S     S   ", " L         L ", "             ", "             ", "             ", " L         L ", "   S     S   ", "             ", " Q  L   L  Q ", "             " },
				{ "             ", " X  Q   Q  X ", "             ", "   Q     Q   ", " Q         Q ", "             ", "      a      ", "             ", " Q         Q ", "   Q     Q   ", "             ", " X  Q   Q  X ", "             " },
				{ "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXRXRXXXXX", "XXXXRXXXRXXXX", "XXXXXX0XXXXXX", "XXXXRXXXRXXXX", "XXXXXRXRXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX" }
		};
		registerMultiBlock("pedestal_advanced_structure_check", tier2Structure, targetBlocksCheck);
		registerMultiBlock("pedestal_advanced_structure_display", tier2Structure, targetBlocksDisplay);

		String[][] tier3Structure = {
				{ "             ", "    XpSpX    ", "             ", "             ", " X  OpppO  X ", " P  P   P  P ", " S  P   P  S ", " P  P   P  P ", " X  OpppO  X ", "             ", "             ", "    XpSpX    ", "             " },
				{ "             ", " SppR   RppS ", " P  P   P  P ", " P  P   P  P ", " RppX   XppR ", "             ", "             ", "             ", " RppX   XppR ", " P  P   P  P ", " P  P   P  P ", " SppR   RppS ", "             " },
				{ "             ", " Q  Q   Q  Q ", "             ", "             ", " Q         Q ", "             ", "             ", "             ", " Q         Q ", "             ", "             ", " Q  Q   Q  Q ", "             " },
				{ "             ", " C  Q   Q  C ", "             ", "             ", " Q         Q ", "             ", "             ", "             ", " Q         Q ", "             ", "             ", " C  Q   Q  C ", "             " },
				{ "             ", " Q  L   L  Q ", "             ", "   S     S   ", " L         L ", "             ", "             ", "             ", " L         L ", "   S     S   ", "             ", " Q  L   L  Q ", "             " },
				{ "             ", " X  Q   Q  X ", "             ", "   Q     Q   ", " Q         Q ", "             ", "      B      ", "             ", " Q         Q ", "   Q     Q   ", "             ", " X  Q   Q  X ", "             " },
				{ "XXXXXXXXXXXXX", "XXmmXmmmXmmXX", "XMXXXXXXXXXMX", "XMXXXXXXXXXMX", "XXXXXRXRXXXXX", "XMXXRXXXRXXMX", "XMXXXX0XXXXMX", "XMXXRXXXRXXMX", "XXXXXRXRXXXXX", "XMXXXXXXXXXMX", "XMXXXXXXXXXMX", "XXmmXmmmXmmXX", "XXXXXXXXXXXXX" }
		};
		registerMultiBlock("pedestal_complex_structure_check", tier3Structure, targetBlocksCheck);
		registerMultiBlock("pedestal_complex_structure_display", tier3Structure, targetBlocksDisplay);
	}

}
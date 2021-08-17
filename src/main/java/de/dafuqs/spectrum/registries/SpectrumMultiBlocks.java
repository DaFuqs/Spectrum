package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpectrumMultiBlocks {

	public static final Map<Identifier, IMultiblock> MULTIBLOCKS = new ConcurrentHashMap<>();

	public static IMultiblock testMultiblock;

	public static void register() {
		String[][] blocks = {
				{ "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             ", "             " },
				{ "             ", " SPPR   RPPS ", " P         P ", " P         P ", " R         R ", "             ", "             ", "             ", " R         R ", " P         P ", " P         P ", " SPPR   RPPS ", "             " },
				{ "             ", " P  P   P  P ", "             ", "             ", " P         P ", "             ", "             ", "             ", " P         P ", "             ", "             ", " P  P   P  P ", "             " },
				{ "             ", " C  P   P  C ", "             ", "             ", " P         P ", "             ", "             ", "             ", " P         P ", "             ", "             ", " C  P   P  C ", "             " },
				{ "             ", " P  L   L  P ", "             ", "   S     S   ", " L         L ", "             ", "             ", "             ", " L         L ", "   S     S   ", "             ", " P  L   L  P ", "             " },
				{ "             ", " X  P   P  X ", "             ", "   P     P   ", " P         P ", "             ", "      A      ", "             ", " P         P ", "   P     P   ", "             ", " X  P   P  X ", "             " },
				{ "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXRXRXXXXX", "XXXXRXXXRXXXX", "XXXXXX0XXXXXX", "XXXXRXXXRXXXX", "XXXXXRXRXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "XXXXXXXXXXXXX" }
		};
		testMultiblock = PatchouliAPI.get().makeMultiblock(blocks,
				'X', "#spectrum:polished_base_blocks",
				'P', "#spectrum:pillar_base_blocks",
				'L', "#spectrum:gemstone_lamps",
				'S', "#spectrum:gemstone_storage_blocks",
				'C', "#spectrum:chiseled_base_blocks",
				'R', "#spectrum:gemstone_chiseled_base_blocks",
				'A', "spectrum:altar");
		vazkii.patchouli.common.multiblock.MultiblockRegistry.registerMultiblock(new Identifier(SpectrumCommon.MOD_ID, "altar1"), testMultiblock);
	}

}
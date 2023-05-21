package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.*;
import net.minecraft.util.*;

public class SpectrumC2SPackets {
	
	public static final Identifier RENAME_ITEM_IN_BEDROCK_ANVIL_PACKET_ID = SpectrumCommon.locate("rename_item_in_bedrock_anvil");
	public static final Identifier ADD_LORE_IN_BEDROCK_ANVIL_PACKET_ID = SpectrumCommon.locate("add_lore_to_item_in_bedrock_anvil");
	public static final Identifier CHANGE_PARTICLE_SPAWNER_SETTINGS_PACKET_ID = SpectrumCommon.locate("change_particle_spawner_settings");
	public static final Identifier CHANGE_COMPACTING_CHEST_SETTINGS_PACKET_ID = SpectrumCommon.locate("change_compacting_chest_settings");
	public static final Identifier GUIDEBOOK_HINT_BOUGHT = SpectrumCommon.locate("guidebook_tip_used");
	public static final Identifier BIND_ENDER_SPLICE_TO_PLAYER = SpectrumCommon.locate("bind_ender_splice_to_player");
	public static final Identifier INK_COLOR_SELECTED = SpectrumCommon.locate("ink_color_select");
	public static final Identifier WORKSTAFF_TOGGLE_SELECTED = SpectrumCommon.locate("workstaff_toggle_select");
	public static final Identifier CONFIRMATION_BUTTON_PRESSED = SpectrumCommon.locate("confirmation_button_pressed");
	
}

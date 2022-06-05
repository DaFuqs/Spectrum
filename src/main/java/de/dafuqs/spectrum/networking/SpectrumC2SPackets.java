package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;

public class SpectrumC2SPackets {
	
	public static final Identifier RENAME_ITEM_IN_BEDROCK_ANVIL_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "rename_item_in_bedrock_anvil");
	public static final Identifier ADD_LORE_IN_BEDROCK_ANVIL_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "add_lore_to_item_in_bedrock_anvil");
	public static final Identifier CHANGE_PARTICLE_SPAWNER_SETTINGS_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "change_particle_spawner_settings");
	public static final Identifier CHANGE_COMPACTING_CHEST_SETTINGS_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "change_compacting_chest_settings");
	public static final Identifier GUIDEBOOK_HINT_BOUGHT = new Identifier(SpectrumCommon.MOD_ID, "guidebook_tip_used");
	public static final Identifier BIND_ENDER_SPLICE_TO_PLAYER = new Identifier(SpectrumCommon.MOD_ID, "bind_ender_splice_to_player");
	
}

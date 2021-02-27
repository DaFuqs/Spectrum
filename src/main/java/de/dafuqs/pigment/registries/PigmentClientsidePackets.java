package de.dafuqs.pigment.registries;

import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

public class PigmentClientsidePackets {
	//public static final Identifier SPAWN_FLOATING_BLOCK_ENTITY = packet("entity", "spawn", "gravity_block");

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		/*ClientSide
		ClientSidePacketRegistry.INSTANCE.register(SPAWN_FLOATING_BLOCK_ENTITY, GravityBlockEntity::spawn);*/
	}

	private static Identifier packet(String... path) {
		return new Identifier(PigmentCommon.MOD_ID, String.join(".", path));
	}
}
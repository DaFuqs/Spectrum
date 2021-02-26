package de.dafuqs.pigment;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.gravity.GravityBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.util.Identifier;

public class PigmentClientsidePacketRegistry {
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
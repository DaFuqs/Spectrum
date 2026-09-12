package de.dafuqs.spectrum.helpers;

import com.mojang.authlib.*;
import de.dafuqs.spectrum.api.block.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.neoforged.neoforge.common.util.*;
import org.jspecify.annotations.*;

import java.util.*;

public class FakePlayerHelper {
	
	public static @Nullable Player getFakePlayer(ServerLevel level, PlayerOwned playerOwned) {
		UUID ownerUUID = playerOwned.getOwnerUUID();
		if(ownerUUID == null) return null;
		return FakePlayerFactory.get(level, new GameProfile(ownerUUID, "[Spectrum Fake Player of " + ownerUUID + "]"));
	}
	
}

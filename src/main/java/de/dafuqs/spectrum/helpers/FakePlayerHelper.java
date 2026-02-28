package de.dafuqs.spectrum.helpers;

import com.mojang.authlib.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.blocks.redstone.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.common.util.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class FakePlayerHelper {
	
	private static final Map<UUID, FakePlayer> FAKE_PLAYER_CACHE = new Object2ObjectArrayMap<>();
	
	public static @Nullable Player getFakePlayer(ServerLevel level, PlayerOwned playerOwned) {
		UUID ownerUUID = playerOwned.getOwnerUUID();
		if(ownerUUID == null) return null;
		
		return FAKE_PLAYER_CACHE.computeIfAbsent(ownerUUID, uuid -> {
			GameProfile fakeProfile = new GameProfile(ownerUUID, "[Spectrum Fake Player of " + ownerUUID + "]");
			return new FakePlayer(level, fakeProfile);
		});
	}
	
}

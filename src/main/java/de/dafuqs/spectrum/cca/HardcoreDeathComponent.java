package de.dafuqs.spectrum.cca;

import com.mojang.authlib.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import dev.onyxstudios.cca.api.v3.component.*;
import dev.onyxstudios.cca.api.v3.level.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class HardcoreDeathComponent implements Component, LevelComponentInitializer {
	
	public static final ComponentKey<HardcoreDeathComponent> HARDCORE_DEATHS_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("hardcore_deaths"), HardcoreDeathComponent.class);
	
	private final static List<UUID> playersThatDiedInHardcore = new ArrayList<>();
	
	@Override
	public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
		registry.register(HARDCORE_DEATHS_COMPONENT, e -> new HardcoreDeathComponent());
	}
	
	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		NbtList uuidList = new NbtList();
		for (UUID playerThatDiedInHardcore : playersThatDiedInHardcore) {
			uuidList.add(NbtHelper.fromUuid(playerThatDiedInHardcore));
		}
		tag.put("HardcoreDeaths", uuidList);
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		playersThatDiedInHardcore.clear();
		NbtList uuidList = tag.getList("HardcoreDeaths", NbtElement.INT_ARRAY_TYPE);
		for (NbtElement listEntry : uuidList) {
			playersThatDiedInHardcore.add(NbtHelper.toUuid(listEntry));
		}
	}
	
	public static boolean isInHardcore(PlayerEntity player) {
		return player.hasStatusEffect(SpectrumStatusEffects.DIVINITY);
	}
	
	public static void addHardcoreDeath(GameProfile profile) {
		addHardcoreDeath(profile.getId());
	}
	
	public static void removeHardcoreDeath(GameProfile profile) {
		removeHardcoreDeath(profile.getId());
	}
	
	public static boolean hasHardcoreDeath(GameProfile profile) {
		return hasHardcoreDeath(profile.getId());
	}
	
	protected static void addHardcoreDeath(UUID uuid) {
		if (!playersThatDiedInHardcore.contains(uuid)) {
			playersThatDiedInHardcore.add(uuid);
		}
		SpectrumCommon.minecraftServer.getPlayerManager().getPlayer(uuid).changeGameMode(GameMode.SPECTATOR);
	}
	
	protected static boolean hasHardcoreDeath(UUID uuid) {
		return playersThatDiedInHardcore.contains(uuid);
	}
	
	protected static void removeHardcoreDeath(UUID uuid) {
		playersThatDiedInHardcore.remove(uuid);
	}
	
}

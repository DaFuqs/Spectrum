package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HardcoreDeathComponent implements Component, LevelComponentInitializer {
	
	public static final ComponentKey<HardcoreDeathComponent> HARDCORE_DEATHS_COMPONENT = ComponentRegistry.getOrCreate(new Identifier(SpectrumCommon.MOD_ID, "hardcore_deaths"), HardcoreDeathComponent.class);
	
	private final static List<UUID> playersThatDiedInHardcore = new ArrayList<>();
	
	@Override
	public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
		registry.register(HARDCORE_DEATHS_COMPONENT , e -> new HardcoreDeathComponent());
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList uuidList = new NbtList();
		for(UUID playerThatDiedInHardcore : playersThatDiedInHardcore) {
			uuidList.add(NbtHelper.fromUuid(playerThatDiedInHardcore));
		}
		tag.put("HardcoreDeaths", uuidList);
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		playersThatDiedInHardcore.clear();
		NbtList uuidList = tag.getList("HardcoreDeaths", NbtElement.LIST_TYPE);
		for(NbtElement listEntry : uuidList) {
			playersThatDiedInHardcore.add(NbtHelper.toUuid(listEntry));
		}
	}
	
	public static void addHardcoreDeath(UUID uuid) {
		playersThatDiedInHardcore.add(uuid);
	}
	
	public static boolean hasHardcoreDeath(UUID uuid) {
		return playersThatDiedInHardcore.contains(uuid);
	}
	
	public static void removeHardcoreDeath(UUID uuid) {
		playersThatDiedInHardcore.remove(uuid);
	}

	public static boolean isInHardcore(PlayerEntity player) {
		return player.hasStatusEffect(SpectrumStatusEffects.DIVINITY);
	}
	
}

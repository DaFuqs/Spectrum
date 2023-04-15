package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.entity.player.*;
import net.minecraft.particle.*;
import net.minecraft.resource.*;
import net.minecraft.util.*;
import net.minecraft.util.profiler.*;
import net.minecraft.util.registry.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ParticleSpawnerParticlesDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	
	public static final String ID = "particle_spawner_particles";
	public static final ParticleSpawnerParticlesDataLoader INSTANCE = new ParticleSpawnerParticlesDataLoader();
	
	/**
	 * Defines an entry that appears in the particle spawners gui to be selected as particle
	 * Theoretically the particle spawner can spawn all kinds of particle (my modifying its nbt)
	 * But we are limiting us to a few reasonable ones there
	 *
	 * @param particleType      The particle type to dynamically fetch textures from
	 * @param textureIdentifier The texture shown in its gui entry
	 * @param unlockIdentifier  The advancement identifier required to being able to select this entry
	 */
	public record ParticleSpawnerEntry(ParticleType<?> particleType, Identifier textureIdentifier,
									   @Nullable Identifier unlockIdentifier) {
	}
	
	protected static final List<ParticleSpawnerEntry> PARTICLES = new ArrayList<>();
	
	private ParticleSpawnerParticlesDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		PARTICLES.clear();
		prepared.forEach((identifier, jsonElement) -> {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			
			ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(Identifier.tryParse(jsonObject.get("particle_type").getAsString()));
			Identifier guiTexture = Identifier.tryParse(jsonObject.get("gui_texture").getAsString());
			@Nullable Identifier unlockIdentifier = jsonObject.has("unlock_identifier") ? Identifier.tryParse(jsonObject.get("unlock_identifier").getAsString()) : null;
			
			PARTICLES.add(new ParticleSpawnerEntry(particleType, guiTexture, unlockIdentifier));
		});
	}
	
	@Override
	public Identifier getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
	public static List<ParticleSpawnerEntry> getAllUnlocked(PlayerEntity player) {
		List<ParticleSpawnerEntry> list = new ArrayList<>();
		for (ParticleSpawnerParticlesDataLoader.ParticleSpawnerEntry entry : PARTICLES) {
			if (AdvancementHelper.hasAdvancement(player, entry.unlockIdentifier())) {
				list.add(entry);
			}
		}
		return list;
	}
	
}
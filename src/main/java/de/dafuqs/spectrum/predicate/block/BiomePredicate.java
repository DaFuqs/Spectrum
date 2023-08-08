package de.dafuqs.spectrum.predicate.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import org.jetbrains.annotations.Nullable;

public class BiomePredicate {
    public static final BiomePredicate ANY = new BiomePredicate(null, null);
    @Nullable
    private final TagKey<Biome> tag;
    @Nullable
    private final Biome biome;

    public BiomePredicate(@Nullable TagKey<Biome> tag, @Nullable Biome biome) {
        this.tag = tag;
        this.biome = biome;
    }

    public boolean test(ServerWorld world, BlockPos pos) {
        if (this == ANY) {
            return true;
        }
        if (this.tag != null && world.getBiome(pos).isIn(this.tag)) {
            return true;
        }
        if (this.biome != null && world.getBiome(pos).value() == this.biome) {
            return true;
        }
        return false;
    }

    public static BiomePredicate fromJson(@Nullable JsonElement json) {
        if (json == null || json.isJsonNull()) {
            return ANY;
        }
        
        JsonObject biomeObject = JsonHelper.asObject(json, "biome");
        
        Biome biome = null;
        if (biomeObject.has("biome")) {
            Identifier biomeId = new Identifier(JsonHelper.getString(biomeObject, "biome"));
            biome = BuiltinRegistries.BIOME.get(biomeId);
        }
        
        TagKey<Biome> tagKey = null;
        if (biomeObject.has("tag")) {
            Identifier tagId = new Identifier(JsonHelper.getString(biomeObject, "tag"));
            tagKey = TagKey.of(Registry.BIOME_KEY, tagId);
        }
        
        return new BiomePredicate(tagKey, biome);
    }
}

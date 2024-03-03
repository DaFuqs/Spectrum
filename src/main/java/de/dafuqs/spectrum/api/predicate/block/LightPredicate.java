package de.dafuqs.spectrum.api.predicate.block;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.predicate.NumberRange;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

public class LightPredicate {
    public static final LightPredicate ANY = new LightPredicate(null, null);
	
	@Nullable
	private final NumberRange.IntRange blockLight;
	@Nullable
	private final NumberRange.IntRange skyLight;
	
	public LightPredicate(@Nullable NumberRange.IntRange blockLight, @Nullable NumberRange.IntRange skyLight) {
		this.blockLight = blockLight;
		this.skyLight = skyLight;
	}
	
	public boolean test(ServerWorld world, BlockPos pos) {
		if (this == ANY) {
			return true;
		}
		
		if (this.blockLight != null && !this.blockLight.test(world.getLightLevel(LightType.BLOCK, pos))) {
			return false;
		}
		if (this.skyLight != null && !this.skyLight.test(world.getLightLevel(LightType.SKY, pos))) {
			return false;
		}
		
		return true;
	}
	
	public static LightPredicate fromJson(@Nullable JsonElement json) {
		if (json == null || json.isJsonNull()) {
            return ANY;
        }
		
        JsonObject jsonObject = JsonHelper.asObject(json, "light");
        
		NumberRange.IntRange blockLight = NumberRange.IntRange.fromJson(jsonObject.get("block"));
		NumberRange.IntRange skyLight = NumberRange.IntRange.fromJson(jsonObject.get("sky"));
        
		return new LightPredicate(blockLight, skyLight);
	}
}

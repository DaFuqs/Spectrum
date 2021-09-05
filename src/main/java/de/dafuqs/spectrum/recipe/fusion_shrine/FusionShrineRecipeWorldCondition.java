package de.dafuqs.spectrum.recipe.fusion_shrine;

import net.minecraft.server.world.ServerWorld;

public enum FusionShrineRecipeWorldCondition {
    DAY,
    NIGHT,
    SUNRISE,
    SUNSET,
    MIDNIGHT,
    FULL_MOON,
    NEW_MOON,
    CLEAR_SKY,
    RAIN,
    THUNDER;

    public boolean isMetCurrently(ServerWorld world) {
        switch (this) {
            case DAY -> {
                return world.getTime() % 24000 > 0 && world.getTime() % 24000 < 12000;
            }
            case NIGHT -> {
                return world.getTime() % 24000 > 13000 && world.getTime() % 24000 < 23000;
            }
            case SUNRISE -> {
                return world.getTime() % 24000 > 23000;
            }
            case SUNSET -> {
                return world.getTime() % 24000 > 12000 && world.getTime() % 24000 < 13000;
            }
            case MIDNIGHT -> {
                return world.getTime() % 24000 > 18000 && world.getTime() % 24000 < 19000;
            }
            case FULL_MOON -> {
                return world.getLunarTime() == 0;
            }
            case NEW_MOON -> {
                return world.getLunarTime() == 5;
            }
            case CLEAR_SKY -> {
                return !world.isRaining();
            }
            case RAIN -> {
                return world.isRaining();
            }
            case THUNDER -> {
                return world.isThundering();
            }
        }
        return false;
    }


}

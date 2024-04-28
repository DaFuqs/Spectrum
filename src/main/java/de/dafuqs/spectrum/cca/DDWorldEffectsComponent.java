package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.deeper_down.Season;
import de.dafuqs.spectrum.deeper_down.weather.WeatherState;
import de.dafuqs.spectrum.deeper_down.weather.WeatherThread;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.particle.client.FallingAshParticle;
import de.dafuqs.spectrum.registries.SpectrumBiomes;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumDimensions;
import de.dafuqs.spectrum.registries.SpectrumWeatherStates;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class DDWorldEffectsComponent implements CommonTickingComponent, AutoSyncedComponent {

    public static final ComponentKey<DDWorldEffectsComponent> DD_WORLD_EFFECTS_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("dd_world_effects"), DDWorldEffectsComponent.class);
    private final World world;
    private boolean dirty = false, initialized = false;
    private Thread weatherThread;

    //Date Data
    public static final int DAY_LENGTH = 24000;
    public static final long REAL_DAY_LENGTH = 86400 * 20;
    public static final int RESET_PERIOD = DAY_LENGTH * 72;
    public static final long SEASON_DURATION = REAL_DAY_LENGTH * 13;
    public static final long SEASON_PERIOD_INTERVAL = SEASON_DURATION / 3;
    private long seasonCycleStart = new Date().getTime();


    //Ash Effects - changes on average every half hour
    private static final long ASH_UPDATE_INTERVAL = 1600;
    private static final double BASE_ASH_VELOCITY = 0.25;
    private double targetAshVelocity = 0.215, lastAshVelocity = 0.215, ashScaleA = 20000, ashScaleB = 2200, ashScaleC = 200;
    private int ashSwitchTicks = 50, ashSpawns;
    private Direction.Axis ashAxis = Direction.Axis.X;

    /*
    Weather Data - It takes a full lunar cycle
        -(8 in-game days / 2.3 hours) -
    to fully fill the aquifers at base flow rate.
    */
    private static final long WEATHER_UPDATE_INTERVAL = 600;
    public static final float AQUIFER_CAP = 10000;
    public static final float BASE_AQUIFER_FLOW = AQUIFER_CAP / (DAY_LENGTH * 8F);
    private float aquiferFill = 0F, aquiferSaturation = 0F;
    private WeatherState weatherState = SpectrumWeatherStates.PLAIN_WEATHER;
    private long weatherTicks = 0;
    private boolean forced;

    public DDWorldEffectsComponent(World world) {
        this.world = world;
    }

    @Override
    public void serverTick() {
        if (world == null || !world.getRegistryKey().equals(SpectrumDimensions.DIMENSION_KEY)) {
            return;
        }

        var time = world.getTime();
        var seasonTime = getSeasonalTime();
        var random = world.getRandom();

        var season = getSeasonByDate(seasonTime);
        var period = getPeriodByDate(seasonTime);

        weatherTicks++;

        updateAshEffects(time, random);
        updateAquiferSaturation((float) time);
        updateAquiferFill(season, period);
        updateWeatherTransitions(season, period, time, random);

        if (dirty) {
            dirty = false;
            DD_WORLD_EFFECTS_COMPONENT.sync(world);
        }

        if (!initialized)
            initialized = true;
    }

    private void updateWeatherTransitions(Season season, Season.Period period, long time, Random random) {
        if (time % WEATHER_UPDATE_INTERVAL == 0) {
            //TODO: rewrite.
        }
    }

    private void updateAquiferSaturation(float time) {
        time = time % RESET_PERIOD;
        var period = time / SEASON_PERIOD_INTERVAL;
        var floor = (float) Math.sin(time / (SEASON_DURATION * 4)) * 0.25F + 0.75F;
        var firstOctave = (float) Math.sin(period) / 2;
        var secondOctave = (float) Math.cos(period / 2) / 4;
        var thirdOctave = (float) Math.sin(period / 4) / 8;
        var fourthOctave = (float) Math.cos(period / 6) / 16;
        var fifthOctave = (float) Math.sin(period / 8) / 32;
        var sixthOctave = (float) Math.cos(period / 10) / 64;
        var seventhOctave = (float) Math.sin(period / 12) / 128;
        var eightOctave = (float) Math.cos(period / 14) / 256;
        var flux = (float) Math.abs(Math.sin(period) * Math.cos(period / 23));
        aquiferSaturation = (floor * (firstOctave + secondOctave + thirdOctave + fourthOctave + fifthOctave + sixthOctave + seventhOctave + eightOctave) * flux) * 0.5F + 0.5F;
    }

    private void updateAquiferFill(Season season, Season.Period period) {
        var moonPhase = world.getMoonPhase();
        var timeOfDay = TimeHelper.getTimeOfDay(world);

        var moonMod = 0.75F + (1 - moonPhase / 7F);
        var surfaceRainMod = (world.isRaining() ? 1.5F : 1) * (world.isThundering() ? 2 : 1);
        float dayTimeMod;

        if (timeOfDay.isNight()) {
            dayTimeMod = 1.25F;
        }
        else if (timeOfDay == TimeHelper.TimeOfDay.NOON) {
            dayTimeMod = 0.667F;
        }
        else if (timeOfDay == TimeHelper.TimeOfDay.DAY) {
            dayTimeMod = 0.9F;
        }
        else {
            dayTimeMod = 1.05F;
        }

        var flowMod = (dayTimeMod * surfaceRainMod + moonMod + aquiferSaturation) * season.aquiferMod * period.effectMod;
        aquiferFill = MathHelper.clamp(aquiferFill + BASE_AQUIFER_FLOW * flowMod - weatherState.getThirst(), 0, AQUIFER_CAP);
    }

    private void updateAshEffects(long time, Random random) {
        if (time % ASH_UPDATE_INTERVAL == 0) {
            dirty = true;
            if (random.nextInt(8) == 0) {
                targetAshVelocity = MathHelper.clamp(targetAshVelocity + random.nextFloat() * 0.05 - 0.025, 0.025, 0.75);
                return;
            }

            if (random.nextFloat() > 0.75F) {
                ashSpawns = random.nextBetween(5, 7);
            }

            if (random.nextFloat() < 0.95F)
                return;

            ashScaleA = 500 * (random.nextDouble() + 1) * (random.nextDouble() * 20) + 1000;
            ashScaleB = 100 * (random.nextDouble() + 1) * (random.nextDouble() * 10) + 250;
            ashScaleC = 20 * (random.nextDouble() + 1) * (random.nextDouble() * 5) + 100;

            var newAxis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;

            if (newAxis == ashAxis)
                return;

            targetAshVelocity = BASE_ASH_VELOCITY * (random.nextDouble() * 0.5 + 0.5) * (random.nextBoolean() ? -1 : 1);
            ashAxis = newAxis;
        }
    }

    private static Season.Period getPeriodByDate(long seasonTime) {
        return Season.getPeriodByDate((int) ((seasonTime / SEASON_PERIOD_INTERVAL) % 3));
    }

    private static Season getSeasonByDate(long seasonTime) {
        return Season.getSeasonByDate((int) ((seasonTime / SEASON_DURATION) % 3));
    }

    public Season getCurrentSeason() {
        return getSeasonByDate(getSeasonalTime());
    }

    public Season.Period getCurrentPeriod() {
        return getPeriodByDate(getSeasonalTime());
    }

    public long getSeasonalTime() {
        var time = new Date().getTime() - seasonCycleStart;

        if (time <= 0)
            return 0;

        return time;
    }

    @Override
    public void clientTick() {
        if (world == null)
            return;

        var clientWorld = (ClientWorld) world;

        var time = world.getTime();
        var random = world.getRandom();
        var cameraEntity = MinecraftClient.getInstance().getCameraEntity();

        if (cameraEntity != null) {
            var maxAsh = ashSpawns / (SpectrumCommon.CONFIG.ReducedParticles ? 2 : 1);
            spawnHowlingSpiresAsh(cameraEntity, maxAsh, random, clientWorld);
            spawnPrecipitation(cameraEntity, random, clientWorld);
        }


        var ashVelocity = targetAshVelocity;
        if (ashSwitchTicks < 50) {
            ashVelocity = MathHelper.clampedLerp(lastAshVelocity, targetAshVelocity, ashSwitchTicks);
            ashSwitchTicks++;
        }

        if (time % ASH_UPDATE_INTERVAL == 0 || !initialized) {
            FallingAshParticle.setTargetVelocity(ashVelocity);
            FallingAshParticle.setPrimaryAxis(ashAxis);
            FallingAshParticle.setAshScaleA(ashScaleA);
            FallingAshParticle.setAshScaleB(ashScaleB);
            FallingAshParticle.setAshScaleC(ashScaleC);
        }

        if (!initialized)
            initialized = true;
    }

    private void spawnPrecipitation(Entity cameraEntity, Random random, ClientWorld clientWorld) {
        var thread = SpectrumClient.weatherThread();
        thread.offer(new WeatherThread.Offer(cameraEntity.getPos(), clientWorld, weatherState));
        var spawnWave = thread.requestPrecipitationSpawn();

        if (spawnWave.isEmpty())
            return;

        for (Vector3d spawnPos : spawnWave.get()) {
            weatherState.spawnAirParticle(clientWorld, spawnPos, random);
        }
    }

    private void spawnHowlingSpiresAsh(Entity cameraEntity, int maxAsh, Random random, ClientWorld clientWorld) {
        var camera = cameraEntity.getPos();

        for (int i = 0; i < maxAsh; i++) {
            var x = camera.getX() + random.nextInt(192) - 96;
            var y = camera.getY() + random.nextInt(64) - 32;
            var z = camera.getZ() + random.nextInt(192) - 96;
            var pos = new BlockPos((int) x, (int) y, (int) z);

            if (clientWorld.getBiome(pos).getKey().map(key -> key.equals(SpectrumBiomes.HOWLING_SPIRES)).orElse(false) && world.getBlockState(pos).isAir())
                clientWorld.addParticle(SpectrumParticleTypes.FALLING_ASH, x, y, z, 0, 0, 0);
        }

        for (int i = 0; i < maxAsh; i++) {
            var x = camera.getX() + random.nextInt(64) - 32;
            var y = camera.getY() + random.nextInt(29) - 8;
            var z = camera.getZ() + random.nextInt(64) - 32;
            var pos = new BlockPos((int) x, (int) y, (int) z);

            if (clientWorld.getBiome(pos).getKey().map(key -> key.equals(SpectrumBiomes.HOWLING_SPIRES)).orElse(false) && world.getBlockState(pos).isAir())
                clientWorld.addParticle(SpectrumParticleTypes.FALLING_ASH, x, y, z, 0, 0, 0);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        targetAshVelocity = tag.getDouble("ashVelocity");
        ashScaleA = tag.getDouble("ashScaleA");
        ashScaleB = tag.getDouble("ashScaleB");
        ashScaleC = tag.getDouble("ashScaleC");
        ashSpawns = tag.getInt("ashSpawns");
        ashAxis = Direction.Axis.fromName(tag.getString("ashAxis"));
        aquiferFill = tag.getFloat("aquiferFill");
        weatherTicks = tag.getLong("weatherTicks");
        seasonCycleStart = tag.getLong("seasonCycleStart");
        forced = tag.getBoolean("forced");
        if (tag.contains("weatherState")) {
            weatherState = SpectrumWeatherStates.fromTag(tag);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putDouble("ashVelocity", targetAshVelocity);
        tag.putDouble("ashScaleA", ashScaleA);
        tag.putDouble("ashScaleB", ashScaleB);
        tag.putDouble("ashScaleC", ashScaleC);
        tag.putInt("ashSpawns", ashSpawns);
        tag.putString("ashAxis", ashAxis.getName());
        tag.putFloat("aquiferFill", aquiferFill);
        tag.putLong("weatherTicks", weatherTicks);
        tag.putLong("seasonCycleStart", seasonCycleStart);
        tag.putBoolean("forced", forced);
        weatherState.save(tag);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        lastAshVelocity = targetAshVelocity;

        AutoSyncedComponent.super.applySyncPacket(buf);

        if (lastAshVelocity != targetAshVelocity) {
            ashSwitchTicks = 0;
        }
    }

    public boolean isForced() {
        return forced;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    public void setWeatherState(WeatherState weatherState) {
        this.weatherState = weatherState;
        weatherTicks = 0;
    }

    public void setAquiferFill(float fill) {
        aquiferFill = fill;
    }

    public boolean verifyFillRequest(float fill) {
        return !(fill < 0) && !(fill > AQUIFER_CAP);
    }

    public WeatherState getWeatherState() {
        return weatherState;
    }

    public long getWeatherTicks() {
        return weatherTicks;
    }

    public float getAquiferFill() {
        return aquiferFill;
    }

    public List<Float> getFillRateModifiers() {
        var modifiers = new ArrayList<Float>();
        var season = getCurrentSeason();
        var period = getCurrentPeriod();

        var moonPhase = world.getMoonPhase();
        var timeOfDay = TimeHelper.getTimeOfDay(world);

        var moonMod = 0.75F + (1 - moonPhase / 7F);
        var surfaceRainMod = (world.isRaining() ? 1.5F : 1) * (world.isThundering() ? 2 : 1);
        float dayTimeMod;

        if (timeOfDay.isNight()) {
            dayTimeMod = 1.25F;
        }
        else if (timeOfDay == TimeHelper.TimeOfDay.NOON) {
            dayTimeMod = 0.667F;
        }
        else if (timeOfDay == TimeHelper.TimeOfDay.DAY) {
            dayTimeMod = 0.9F;
        }
        else {
            dayTimeMod = 1.05F;
        }

        modifiers.add(dayTimeMod);
        modifiers.add(surfaceRainMod);
        modifiers.add(moonMod);
        modifiers.add(aquiferSaturation);
        modifiers.add(season.aquiferMod * period.effectMod);

        var flowMod = (dayTimeMod * surfaceRainMod + moonMod + aquiferSaturation) * season.aquiferMod * period.effectMod;

        modifiers.add(flowMod * BASE_AQUIFER_FLOW);
        return modifiers;
    }

    public static void sync(World world) {
        DD_WORLD_EFFECTS_COMPONENT.sync(world);
    }

    public static DDWorldEffectsComponent of(World world) {
        return DD_WORLD_EFFECTS_COMPONENT.get(world);
    }

    @Override
    public void tick() {
        //NOOP
    }
}

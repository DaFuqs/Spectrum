package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.deeper_down.Season;
import de.dafuqs.spectrum.deeper_down.weather.WeatherState;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.particle.client.FallingAshParticle;
import de.dafuqs.spectrum.registries.SpectrumBiomes;
import de.dafuqs.spectrum.registries.SpectrumDimensions;
import de.dafuqs.spectrum.registries.SpectrumWeatherStates;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class DDWorldEffectsComponent implements CommonTickingComponent, AutoSyncedComponent {

    public static final ComponentKey<DDWorldEffectsComponent> DD_WORLD_EFFECTS_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("dd_world_effects"), DDWorldEffectsComponent.class);
    private final World world;
    private boolean dirty = false, initialized = false;

    public static final int DAY_LENGTH = 24000;
    public static final long SEASON_DURATION = DAY_LENGTH * 60;
    public static final long SEASON_PERIOD_INTERVAL = DAY_LENGTH * 20;
    private static Season season = Season.FLOW;
    private static long seasonProgress = 0;


    //Ash Effects - changes on average every half hour
    private static final long ASH_UPDATE_INTERVAL = 1600;
    private static final double BASE_ASH_VELOCITY = 0.25;
    private static double targetAshVelocity = 0.215, lastAshVelocity = 0.215, ashScaleA = 20000, ashScaleB = 2200, ashScaleC = 200;
    private static int ashSwitchTicks = 50, ashSpawns;
    private static Direction.Axis ashAxis = Direction.Axis.X;

    /*
    Weather Data - It takes a full lunar cycle
        -(8 in-game days / 2.3 hours) -
    to fully fill the aquifers at base flow rate.
    */
    private static final long WEATHER_UPDATE_INTERVAL = 600;
    private static final float AQUIFER_CAP = 1000;
    private static final float BASE_AQUIFER_FLOW = AQUIFER_CAP / (DAY_LENGTH * 8F);
    private static float aquiferFillPercent = 0F, aquiferSaturation = 50F;
    private static WeatherState weatherState = SpectrumWeatherStates.PLAIN_WEATHER;

    public DDWorldEffectsComponent(World world) {
        this.world = world;
    }

    @Override
    public void serverTick() {
        if (!world.getRegistryKey().equals(SpectrumDimensions.DIMENSION_KEY)) {
            return;
        }

        var time = world.getTime();
        var random = world.getRandom();

        if (seasonProgress >= SEASON_DURATION) {
            seasonProgress = 0;
            season = season.getNextSeason();
        }
        else {
            seasonProgress++;
        }

        updateAshEffects(time, random);
        updateAquiferFill();

        if (time % WEATHER_UPDATE_INTERVAL == 0) {
            /**
             * TODO: Draw the rest of the fucking owl
             */
        }


        if (dirty) {
            dirty = false;
            DD_WORLD_EFFECTS_COMPONENT.sync(world);
        }

        if (!initialized)
            initialized = true;
    }

    private void updateAquiferFill() {
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

        var flowMod = (dayTimeMod * surfaceRainMod + moonMod) * season.aquiferMod * season.getPeriod(seasonProgress).effectMod;
        aquiferFillPercent = Math.min(aquiferFillPercent + BASE_AQUIFER_FLOW * flowMod, AQUIFER_CAP);
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

    @Override
    public void clientTick() {
        var clientWorld = (ClientWorld) world;

        var time = world.getTime();
        var random = world.getRandom();

        var maxAsh = ashSpawns / (SpectrumCommon.CONFIG.ReducedParticles ? 2 : 1);
        spawnHowlingSpiresAsh(maxAsh, random, clientWorld);



        if (!world.getRegistryKey().equals(SpectrumDimensions.DIMENSION_KEY)) {
            return;
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

    private void spawnHowlingSpiresAsh(int maxAsh, Random random, ClientWorld clientWorld) {
        var cameraEntity = MinecraftClient.getInstance().cameraEntity;

        if (cameraEntity == null)
            return;

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

    @Override
    public void tick() {
        //NOOP
    }
}

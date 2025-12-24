package net.zaharenko424.protogenmod.util;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;

import java.util.List;
import java.util.function.Predicate;

public class PacketUtil {

    //==================================================== Sounds ====================================================//

    public static void playSound(ServerLevel level, Predicate<ServerPlayer> send, double x, double y, double z, SoundEvent sound, SoundSource soundSource, float volume, float pitch){
        playSound(level, send, x, y, z, Holder.direct(sound), soundSource, volume, pitch);
    }

    public static void playSound(ServerLevel level, Predicate<ServerPlayer> send, Vec3 position, SoundEvent sound, SoundSource soundSource, float volume, float pitch) {
        playSound(level, send, position.x, position.y, position.z, sound, soundSource, volume, pitch);
    }

    public static void playSound(ServerLevel level, Predicate<ServerPlayer> send, Vec3 position, Holder<SoundEvent> sound, SoundSource soundSource, float volume, float pitch) {
        playSound(level, send, position.x, position.y, position.z, sound, soundSource, volume, pitch);
    }

    public static void playSound(ServerLevel level, Predicate<ServerPlayer> send, double x, double y, double z, Holder<SoundEvent> sound, SoundSource soundSource, float volume, float pitch) {
        PlayLevelSoundEvent.AtPosition event = EventHooks.onPlaySoundAtPosition(level, x, y, z, sound, soundSource, volume, pitch);
        if (event.isCanceled() || event.getSound() == null) return;

        volume = event.getNewVolume();

        long pSeed = level.getRandom().nextLong();
        broadcast(level, send.and(distance(x, y, z, sound.value().getRange(volume))), level.dimension(), new ClientboundSoundPacket(event.getSound(), event.getSource(), x, y, z, volume, pitch, pSeed));
    }

    public static void playSound(ServerLevel level, Predicate<ServerPlayer> send, Entity entity, SoundEvent sound, SoundSource soundSource, float volume, float pitch) {
        playSound(level, send, entity, Holder.direct(sound), soundSource, volume, pitch);
    }

    public static void playSound(ServerLevel level, Predicate<ServerPlayer> send, Entity entity, Holder<SoundEvent> sound, SoundSource soundSource, float volume, float pitch) {
        PlayLevelSoundEvent.AtEntity event = EventHooks.onPlaySoundAtEntity(entity, sound, soundSource, volume, pitch);
        if (event.isCanceled() || event.getSound() == null) return;

        volume = event.getNewVolume();

        long pSeed = level.getRandom().nextLong();
        broadcast(level, send.and(distance(entity.getX(), entity.getY(), entity.getZ(), sound.value().getRange(volume))), level.dimension(), new ClientboundSoundPacket(event.getSound(), event.getSource(), entity.getX(), entity.getY(), entity.getZ(), volume, pitch, pSeed));
    }

    //=================================================== Particles ==================================================//

    public static <T extends ParticleOptions> void sendParticles(ServerLevel level, Predicate<ServerPlayer> send, T type, double x, double y, double z, int particleCount, float xOffset, float yOffset, float zOffset, float speed){
        sendParticles(level, send, type, false, x, y, z, particleCount, xOffset, yOffset, zOffset, speed);
    }

    public static <T extends ParticleOptions> void sendParticles(ServerLevel level, Predicate<ServerPlayer> send, T type, boolean force, double x, double y, double z, int particleCount, float xOffset, float yOffset, float zOffset, float speed){
        broadcast(level, send, level.dimension(), new ClientboundLevelParticlesPacket(type, force, x, y, z, xOffset, yOffset, zOffset, speed, particleCount));
    }

    public static <T extends ParticleOptions> void sendCenteredParticles(ServerLevel level, Predicate<ServerPlayer> send, T type, Entity entity, int particleCount, float xOffset, float yOffset, float zOffset, float speed){
        sendCenteredParticles(level, send, type, false, entity.getX(), entity.getY(), entity.getZ(), particleCount, xOffset, yOffset, zOffset, speed);
    }

    public static <T extends ParticleOptions> void sendCenteredParticles(ServerLevel level, Predicate<ServerPlayer> send, T type, double x, double y, double z, int particleCount, float xOffset, float yOffset, float zOffset, float speed){
        sendCenteredParticles(level, send, type, false, x, y, z, particleCount, xOffset, yOffset, zOffset, speed);
    }

    public static <T extends ParticleOptions> void sendCenteredParticles(ServerLevel level, Predicate<ServerPlayer> send, T type, boolean force, double x, double y, double z, int particleCount, float xOffset, float yOffset, float zOffset, float speed){
        broadcast(level, send, level.dimension(), new ClientboundLevelParticlesPacket(type, force, x - xOffset / 2, y - yOffset / 2, z - zOffset / 2, xOffset, yOffset, zOffset, speed, particleCount));
    }

    public static <T extends ParticleOptions> void sendParticleWithMotion(ServerLevel level, Predicate<ServerPlayer> send, T type, Entity entity, float xOffset, float yOffset, float zOffset, float speed){
        sendParticleWithMotion(level, send, type, false, entity.getX(), entity.getY(), entity.getZ(), xOffset, yOffset, zOffset, speed);
    }

    public static <T extends ParticleOptions> void sendParticleWithMotion(ServerLevel level, Predicate<ServerPlayer> send, T type, double x, double y, double z, float xOffset, float yOffset, float zOffset, float speed){
        sendParticleWithMotion(level, send, type, false, x, y, z, xOffset, yOffset, zOffset, speed);
    }

    public static <T extends ParticleOptions> void sendParticleWithMotion(ServerLevel level, Predicate<ServerPlayer> send, T type, boolean force, double x, double y, double z, float xOffset, float yOffset, float zOffset, float speed){
        broadcast(level, send, level.dimension(), new ClientboundLevelParticlesPacket(type, force, x, y, z, xOffset, yOffset, zOffset, speed, 0));
    }

    //================================================== Predicates ==================================================//

    public static Predicate<ServerPlayer> defParticleDistance(Entity entity, boolean longDist) {
        return defParticleDistance(entity.getX(), entity.getY(), entity.getZ(), longDist);
    }

    public static Predicate<ServerPlayer> defParticleDistance(double x, double y, double z, boolean longDist) {
        return longDist ? distanceSqr(x, y, z, 512) : distanceSqr(x, y, z, 32);
    }

    public static Predicate<ServerPlayer> distance(Entity entity, double distance) {
        return distanceSqr(entity.getX(), entity.getY(), entity.getZ(), distance * distance);
    }

    public static Predicate<ServerPlayer> distance(double x, double y, double z, double distance) {
        return distanceSqr(x, y, z, distance * distance);
    }

    public static Predicate<ServerPlayer> distanceSqr(Entity entity, double distanceSqr) {
        return distanceSqr(entity.getX(), entity.getY(), entity.getZ(), distanceSqr);
    }

    public static Predicate<ServerPlayer> distanceSqr(double x, double y, double z, double distanceSqr) {
        return player -> {
            double dX = x - player.getX();
            double dY = y - player.getY();
            double dZ = z - player.getZ();
            return dX * dX + dY * dY + dZ * dZ < distanceSqr;
        };
    }

    //================================================================================================================//

    public static void broadcast(ServerLevel level, Predicate<ServerPlayer> send, ResourceKey<Level> pDimension, Packet<?> packet) {
        List<ServerPlayer> players = level.getServer().getPlayerList().getPlayers();

        for (ServerPlayer serverplayer : players) {
            if (serverplayer.level().dimension() != pDimension) continue;

            if (send.test(serverplayer)) serverplayer.connection.send(packet);
        }
    }
}

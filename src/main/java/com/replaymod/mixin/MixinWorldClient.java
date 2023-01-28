package com.replaymod.mixin;

import com.replaymod.recording.handler.RecordingEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;


@Mixin(ClientLevel.class)
public abstract class MixinWorldClient extends Level implements RecordingEventHandler.RecordingEventSender {
    @Shadow
    private Minecraft minecraft;

    protected MixinWorldClient(WritableLevelData pLevelData, ResourceKey<Level> pDimension, Holder<DimensionType> pDimensionTypeRegistration, Supplier<ProfilerFiller> pProfiler, boolean pIsClientSide, boolean pIsDebug, long pBiomeZoomSeed) {
        super(pLevelData, pDimension, pDimensionTypeRegistration, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed);
    }

    private RecordingEventHandler replayModRecording_getRecordingEventHandler() {
        return ((RecordingEventHandler.RecordingEventSender) this.minecraft.levelRenderer).getRecordingEventHandler();
    }

    // Sounds that are emitted by thePlayer no longer take the long way over the server
    // but are instead played directly by the client. The server only sends these sounds to
    // other clients so we have to record them manually.
    // E.g. Block place sounds
    //playSound(Lnet/minecraft/entity/player/Player;DDDLnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundSource;FF)V
    @Inject(method = "playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
            at = @At("HEAD"))
    public void replayModRecording_recordClientSound(Player player, double x, double y, double z, SoundEvent sound, SoundSource category,
                                                     float volume, float pitch, CallbackInfo ci) {
        if (player == this.minecraft.player) {
            RecordingEventHandler handler = replayModRecording_getRecordingEventHandler();
            if (handler != null) {
                handler.onClientSound(sound, category, x, y, z, volume, pitch);
            }
        }
    }

    // Same goes for level events (also called effects). E.g. door open, block break, etc.
    @Inject(method = "levelEvent", at = @At("HEAD"))
    private void playLevelEvent(Player player, int type, BlockPos pos, int data, CallbackInfo ci) {
        if (player == this.minecraft.player) {
            // We caused this event, the server won't send it to us
            RecordingEventHandler handler = replayModRecording_getRecordingEventHandler();
            if (handler != null) {
                handler.onClientEffect(type, pos, data);
            }
        }
    }
}

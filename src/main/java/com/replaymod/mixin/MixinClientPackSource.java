package com.replaymod.mixin;

import com.replaymod.gui.utils.Consumer;
import com.replaymod.recording.packet.ResourcePackRecorder;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.server.packs.repository.PackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(ClientPackSource.class)
public abstract class MixinClientPackSource implements ResourcePackRecorder.IClientPackSource {
    private Consumer<File> requestCallback;

    @Override
    public void setRequestCallback(Consumer<File> callback) {
        requestCallback = callback;
    }

    @Inject(method = "setServerPack", at = @At("HEAD"))
    private void recordDownloadedPack(
            File file,
            PackSource arg,
            CallbackInfoReturnable ci
    ) {
        if (requestCallback != null) {
            requestCallback.consume(file);
            requestCallback = null;
        }
    }
}

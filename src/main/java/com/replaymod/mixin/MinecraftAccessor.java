package com.replaymod.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.CrashReport;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;


@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    @Accessor
    Timer getTimer();

    @Accessor
    void setTimer(Timer value);

    @Accessor("pendingReload")
    CompletableFuture<Void> getResourceReloadFuture();

    @Accessor("pendingReload")
    void setResourceReloadFuture(CompletableFuture<Void> value);

    @Accessor("progressTasks")
    Queue<Runnable> getRenderTaskQueue();

    @Accessor("delayedCrash")
    Supplier<CrashReport> getCrashReporter();

}

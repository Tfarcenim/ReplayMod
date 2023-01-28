package com.replaymod.mixin;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(value = NetworkRegistry.class,remap = false)
public interface NetworkRegistryAccessor {
    @Invoker("gatherLoginPayloads")
    static List<NetworkRegistry.LoginPayload> invokeGatherLoginPayloads(NetworkDirection direction, boolean isLocal) {
        throw new AssertionError();
    }
}

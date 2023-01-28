package com.replaymod.mixin;

import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientboundAddPlayerPacket.class)
public interface SPacketSpawnPlayerAccessor {
}

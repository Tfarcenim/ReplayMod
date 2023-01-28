package com.replaymod.mixin;

import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientboundAddMobPacket.class)
public interface SPacketSpawnMobAccessor {
}

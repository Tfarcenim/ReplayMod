package com.replaymod.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagManager;
import net.minecraft.tags.TagNetworkSerialization;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener {
    @Shadow
    @Final
    private Connection connection;

    @Final
    @Shadow
    private Minecraft minecraft;

    //@Shadow
    //private TagManager networkTagManager;

    @Shadow
    protected abstract <T> void updateTagsForRegistry(ResourceKey<? extends Registry<? extends T>> p_205561_, TagNetworkSerialization.NetworkPayload p_205562_);

    @Inject(method = "handleUpdateTags", at = @At(value = "HEAD"), cancellable = true)
    public void replayMod_ignoreHandshakeConnectionClose(ClientboundUpdateTagsPacket packetIn, CallbackInfo ci) {
        System.out.println("Injected ClientPacketListener.handleTags");
        /*// PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.client);
        TagCollectionSupplier itagcollectionsupplier = packetIn.getTags();
        // boolean vanillaConnection = net.minecraftforge.network.NetworkHooks.isVanillaConnection(netManager);
        boolean vanillaConnection = false;
        net.minecraftforge.common.ForgeTagHandler.resetCachedTagCollections(true, vanillaConnection);
        itagcollectionsupplier = TagCollectionSupplier.reinjectOptionalTags(itagcollectionsupplier);
        this.networkTagManager = itagcollectionsupplier;
        if (!this.netManager.isMemoryConnection()) {
            itagcollectionsupplier.updateTags();
        }

        this.client.getSearchTree(SearchTreeManager.TAGS).recalculate();*/

        //PacketUtils.ensureRunningOnSameThread(packetIn, (ClientGamePacketListener) (Object)this, minecraft);
        packetIn.getTags().forEach(this::updateTagsForRegistry);
        if (!this.connection.isMemoryConnection()) {
            Blocks.rebuildCache();
        }

        this.minecraft.getSearchTree(SearchRegistry.CREATIVE_TAGS).refresh();
        ci.cancel();
    }
}

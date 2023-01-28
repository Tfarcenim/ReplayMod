package com.replaymod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.replaymod.render.blend.BlendState;
import com.replaymod.render.blend.exporters.EntityExporter;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class Mixin_RenderManager {

    @Inject(
            method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    public void preRender(Entity entity, double x, double y, double z, float yaw, float renderPartialTicks,
                          PoseStack matrixStack,
                          MultiBufferSource vertexConsumerProvider,
                          int int_1,
                          CallbackInfo ci) {
        BlendState blendState = BlendState.getState();
        if (blendState != null) {
            blendState.get(EntityExporter.class).preRender(entity, x, y, z, yaw, renderPartialTicks);
        }
    }

    @Inject(
            method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
                    shift = At.Shift.AFTER))
    public void postRender(Entity entity, double x, double y, double z, float yaw, float renderPartialTicks,
                           PoseStack matrixStack,
                           MultiBufferSource vertexConsumerProvider,
                           int int_1,
                           CallbackInfo ci) {
        BlendState blendState = BlendState.getState();
        if (blendState != null) {
            blendState.get(EntityExporter.class).postRender(entity, x, y, z, yaw, renderPartialTicks);
        }
    }
}

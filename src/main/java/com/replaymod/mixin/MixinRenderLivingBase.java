package com.replaymod.mixin;

import com.replaymod.render.blend.BlendState;
import com.replaymod.render.blend.exporters.EntityExporter;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinRenderLivingBase {
    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
            shift = At.Shift.AFTER
    ))
    private void recordModelMatrix(CallbackInfo ci) {
        BlendState blendState = BlendState.getState();
        if (blendState != null) {
            blendState.get(EntityExporter.class).postEntityLivingSetup();
        }
    }
}

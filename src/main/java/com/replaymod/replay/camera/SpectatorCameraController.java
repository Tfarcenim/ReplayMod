package com.replaymod.replay.camera;

import com.replaymod.mixin.EntityPlayerAccessor;
import com.replaymod.replay.ReplayModReplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Arrays;

import static com.replaymod.core.versions.MCVer.getMinecraft;

public class SpectatorCameraController implements CameraController {
    private final CameraEntity camera;

    public SpectatorCameraController(CameraEntity camera) {
        this.camera = camera;
    }

    @Override
    public void update(float partialTicksPassed) {
        Minecraft mc = getMinecraft();
        if (mc.options.keyShift.isDown()) {
            ReplayModReplay.instance.getReplayHandler().spectateCamera();
        }

        // Soak up all remaining key presses
        for (KeyMapping binding : Arrays.asList(mc.options.keyAttack, mc.options.keyUse,
                mc.options.keyJump, mc.options.keyShift, mc.options.keyUp,
                mc.options.keyDown, mc.options.keyLeft, mc.options.keyRight)) {
            //noinspection StatementWithEmptyBody
            while (binding.isDown()) {
                ;
            }
        }

        // Prevent mouse movement
        // No longer needed

        // Always make sure the camera is in the exact same spot as the spectated entity
        // This is necessary as some rendering code for the hand doesn't respect the view entity
        // and always uses mc.thePlayer
        Entity view = mc.getCameraEntity();
        if (view != null && view != camera) {
            camera.setCameraPosRot(mc.getCameraEntity());
            // If it's a player, also 'steal' its inventory so the rendering code knows what item to render
            if (view instanceof Player) {
                Player viewPlayer = (Player) view;
                camera.setItemSlot(EquipmentSlot.HEAD, viewPlayer.getItemBySlot(EquipmentSlot.HEAD));
                camera.setItemSlot(EquipmentSlot.MAINHAND, viewPlayer.getItemBySlot(EquipmentSlot.MAINHAND));
                camera.setItemSlot(EquipmentSlot.OFFHAND, viewPlayer.getItemBySlot(EquipmentSlot.OFFHAND));
                EntityPlayerAccessor cameraA = (EntityPlayerAccessor) camera;
                EntityPlayerAccessor viewPlayerA = (EntityPlayerAccessor) viewPlayer;
                cameraA.setItemStackMainHand(viewPlayerA.getItemStackMainHand());
                camera.swingingArm = viewPlayer.swingingArm;
                cameraA.setActiveItemStackUseCount(viewPlayerA.getActiveItemStackUseCount());
            }
        }
    }

    @Override
    public void increaseSpeed() {

    }

    @Override
    public void decreaseSpeed() {

    }
}

package com.replaymod.core.versions;

import com.replaymod.core.MinecraftMethodAccessor;
import com.replaymod.mixin.GuiScreenAccessor;
import com.replaymod.mixin.ParticleAccessor;
import com.replaymod.replaystudio.protocol.PacketTypeRegistry;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.packet.State;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.particle.Particle;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.TranslatableComponent;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Abstraction over things that have changed between different MC versions.
 */
public class MCVer {
    public static int getProtocolVersion() {
        return SharedConstants.getProtocolVersion();
    }

    public static PacketTypeRegistry getPacketTypeRegistry(boolean loginPhase) {
        return PacketTypeRegistry.get(
                ProtocolVersion.getProtocol(getProtocolVersion()),
                loginPhase ? State.LOGIN : State.PLAY
        );
    }

    public static void resizeWindow(Minecraft mc, int width, int height) {
        RenderTarget fb = mc.getMainRenderTarget();
        if (fb.width != width || fb.height != height) {
            fb.resize(width, height, false);
        }
        //noinspection ConstantConditions
        var target = mc.getMainRenderTarget();
        target.width = width;
        target.height = height;
        mc.gameRenderer.resize(width, height);
    }


    public static CompletableFuture<?>
    setServerResourcePack(File file) {
        return getMinecraft().getClientPackSource().setServerPack(
                file
                , PackSource.SERVER
        );
    }

    public static <T> void addCallback(
            CompletableFuture<T> future,
            Consumer<T> success,
            Consumer<Throwable> failure
    ) {
        future.thenAccept(success).exceptionally(throwable -> {
            failure.accept(throwable);
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    public static List<VertexFormatElement> getElements(VertexFormat vertexFormat) {
        return vertexFormat.getElements();
    }


    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public static void addButton(
            Screen screen,
            Button button
    ) {
        GuiScreenAccessor acc = (GuiScreenAccessor) screen;
        acc.getButtons().add(button);
        acc.getChildren().add(button);
    }

    public static Optional<AbstractWidget> findButton(List<AbstractWidget> buttonList, @SuppressWarnings("unused") String text, @SuppressWarnings("unused") int id) {
        final TranslatableComponent message = new TranslatableComponent(text);
        for (AbstractWidget b : buttonList) {
            if (message.equals(b.getMessage())) {
                return Optional.of(b);
            }
            // Fuzzy match (copy does not include children)
            if (b.getMessage() != null && b.getMessage().plainCopy().equals(message)) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    public static void handleKeybinds() {
        ((MinecraftMethodAccessor) getMinecraft()).replayModProcessKeyBinds();
    }


    public static long milliTime() {
        return Util.getMillis();
    }

    // TODO: this can be inlined once https://github.com/SpongePowered/Mixin/issues/305 is fixed
    public static Vec3 getPosition(Particle particle, float partialTicks) {
        ParticleAccessor acc = (ParticleAccessor) particle;
        double x = acc.getPrevPosX() + (acc.getPosX() - acc.getPrevPosX()) * partialTicks;
        double y = acc.getPrevPosY() + (acc.getPosY() - acc.getPrevPosY()) * partialTicks;
        double z = acc.getPrevPosZ() + (acc.getPosZ() - acc.getPrevPosZ()) * partialTicks;
        return new Vec3(x, y, z);
    }

    public static void openFile(File file) {
        Util.getPlatform().openFile(file);
    }

    public static void openURL(URI url) {
        Util.getPlatform().openUri(url);
    }


    private static Boolean hasOptifine;

    public static boolean hasOptifine() {
        if (hasOptifine == null) {
            try {
                Class.forName("Config");
                hasOptifine = true;
            } catch (ClassNotFoundException e) {
                hasOptifine = false;
            }
        }
        return hasOptifine;
    }


    public static abstract class Keyboard {
        public static final int KEY_LCONTROL = GLFW.GLFW_KEY_LEFT_CONTROL;
        public static final int KEY_LSHIFT = GLFW.GLFW_KEY_LEFT_SHIFT;
        public static final int KEY_ESCAPE = GLFW.GLFW_KEY_ESCAPE;
        public static final int KEY_HOME = GLFW.GLFW_KEY_HOME;
        public static final int KEY_END = GLFW.GLFW_KEY_END;
        public static final int KEY_UP = GLFW.GLFW_KEY_UP;
        public static final int KEY_DOWN = GLFW.GLFW_KEY_DOWN;
        public static final int KEY_LEFT = GLFW.GLFW_KEY_LEFT;
        public static final int KEY_RIGHT = GLFW.GLFW_KEY_RIGHT;
        public static final int KEY_BACK = GLFW.GLFW_KEY_BACKSPACE;
        public static final int KEY_DELETE = GLFW.GLFW_KEY_DELETE;
        public static final int KEY_RETURN = GLFW.GLFW_KEY_ENTER;
        public static final int KEY_TAB = GLFW.GLFW_KEY_TAB;
        public static final int KEY_F1 = GLFW.GLFW_KEY_F1;
        public static final int KEY_A = GLFW.GLFW_KEY_A;
        public static final int KEY_B = GLFW.GLFW_KEY_B;
        public static final int KEY_C = GLFW.GLFW_KEY_C;
        public static final int KEY_D = GLFW.GLFW_KEY_D;
        public static final int KEY_E = GLFW.GLFW_KEY_E;
        public static final int KEY_F = GLFW.GLFW_KEY_F;
        public static final int KEY_G = GLFW.GLFW_KEY_G;
        public static final int KEY_H = GLFW.GLFW_KEY_H;
        public static final int KEY_I = GLFW.GLFW_KEY_I;
        public static final int KEY_J = GLFW.GLFW_KEY_J;
        public static final int KEY_K = GLFW.GLFW_KEY_K;
        public static final int KEY_L = GLFW.GLFW_KEY_L;
        public static final int KEY_M = GLFW.GLFW_KEY_M;
        public static final int KEY_N = GLFW.GLFW_KEY_N;
        public static final int KEY_O = GLFW.GLFW_KEY_O;
        public static final int KEY_P = GLFW.GLFW_KEY_P;
        public static final int KEY_Q = GLFW.GLFW_KEY_Q;
        public static final int KEY_R = GLFW.GLFW_KEY_R;
        public static final int KEY_S = GLFW.GLFW_KEY_S;
        public static final int KEY_T = GLFW.GLFW_KEY_T;
        public static final int KEY_U = GLFW.GLFW_KEY_U;
        public static final int KEY_V = GLFW.GLFW_KEY_V;
        public static final int KEY_W = GLFW.GLFW_KEY_W;
        public static final int KEY_X = GLFW.GLFW_KEY_X;
        public static final int KEY_Y = GLFW.GLFW_KEY_Y;
        public static final int KEY_Z = GLFW.GLFW_KEY_Z;

        public static boolean hasControlDown() {
            return Screen.hasControlDown();
        }

        public static boolean isDown(int keyCode) {
            return InputConstants.isKeyDown(getMinecraft().getWindow().getWindow(), keyCode);
        }

    }
}

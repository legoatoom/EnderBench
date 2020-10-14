package com.github.legoatoom.enderbench.screen.ingame;

import com.github.legoatoom.enderbench.EnderBench;
import com.github.legoatoom.enderbench.ModConfigs;
import com.github.legoatoom.enderbench.client.network.PacketIDs;
import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@SuppressWarnings("ConstantConditions")
@Environment(EnvType.CLIENT)
public class EnderBenchScreen extends HandledScreen<ScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(EnderBench.MODID, "textures/gui/container/ender_bench.png");

    public EnderBenchScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices,mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        // Centering of the title.
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    public void onClose() {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        ClientSidePacketRegistry.INSTANCE.sendToServer(PacketIDs.CLOSE_BENCH_PACKET_ID, passedData);
        super.onClose();
    }
}

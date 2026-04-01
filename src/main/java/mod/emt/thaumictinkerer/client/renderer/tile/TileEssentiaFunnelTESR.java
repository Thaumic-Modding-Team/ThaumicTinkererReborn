package mod.emt.thaumictinkerer.client.renderer.tile;

import mod.emt.thaumictinkerer.tile.TileEssentiaFunnel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.client.lib.RenderCubes;

import java.awt.*;

public class TileEssentiaFunnelTESR extends TileEntitySpecialRenderer<TileEssentiaFunnel> {
    private static final ResourceLocation TEX_BRINE = new ResourceLocation("thaumcraft", "textures/models/jarbrine.png");

    @Override
    public void render(@NotNull TileEssentiaFunnel tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(tile, x, y, z, partialTicks, destroyStage, alpha);
        if(!tile.getJarStack().isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            this.renderJar(tile);
            GlStateManager.popMatrix();
        }
    }

    private void renderJar(TileEssentiaFunnel tile) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0.01, 0.5);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        Aspect aspect = tile.getEssentiaType(EnumFacing.UP);
        int aspectAmount = tile.getEssentiaAmount(EnumFacing.UP);
        if (aspectAmount > 0) {
            this.renderFluid(aspect, aspectAmount);
        }
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void renderFluid(Aspect aspect, int aspectAmount) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.disableLighting();

        RenderCubes renderBlocks = new RenderCubes();

        float level = (float) Math.min(1.0, (float) aspectAmount / 250.0 * 0.625);
        Tessellator tessellator = Tessellator.getInstance();
        renderBlocks.setRenderBounds(0.25, 0.25, 0.25, 0.75, 0.25 + level, 0.75);
        tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

        Color color = Color.BLACK;
        if (aspect != null) {
            color = new Color(aspect.getColor());
        }

        TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("thaumcraft:blocks/animatedglow");
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        renderBlocks.renderFaceYNeg(BlocksTC.jarNormal, -0.5F, 0.0F, -0.5F, icon, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 200);
        renderBlocks.renderFaceYPos(BlocksTC.jarNormal, -0.5F, 0.0F, -0.5F, icon, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 200);
        renderBlocks.renderFaceZNeg(BlocksTC.jarNormal, -0.5F, 0.0F, -0.5F, icon, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 200);
        renderBlocks.renderFaceZPos(BlocksTC.jarNormal, -0.5F, 0.0F, -0.5F, icon, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 200);
        renderBlocks.renderFaceXNeg(BlocksTC.jarNormal, -0.5F, 0.0F, -0.5F, icon, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 200);
        renderBlocks.renderFaceXPos(BlocksTC.jarNormal, -0.5F, 0.0F, -0.5F, icon, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 200);
        tessellator.draw();

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}

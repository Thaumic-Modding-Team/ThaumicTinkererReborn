package mod.emt.thaumictinkerer.client.renderer.tile;

import mod.emt.thaumictinkerer.tile.TileMobAttractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class TileMobAttractorTESR extends TileEntitySpecialRenderer<TileMobAttractor> {
    @Override
    public void render(TileMobAttractor tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(tile.discoCowDuration > 0 && tile.discoCow != null) {
            this.renderDiscoCow(tile, tile.discoCow, partialTicks);
        }
    }

    public void renderDiscoCow(TileMobAttractor tile, EntityCow cow, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();

        // Sparkle Effects
        double x = cow.lastTickPosX + (cow.posX - cow.lastTickPosX) * partialTicks;
        double y = cow.lastTickPosY + (cow.posY - cow.lastTickPosY) * partialTicks;
        double z = cow.lastTickPosZ + (cow.posZ - cow.lastTickPosZ) * partialTicks;

        x -= mc.getRenderManager().viewerPosX;
        y += cow.height * 0.5D + 0.35D;
        y -= mc.getRenderManager().viewerPosY;
        z -= mc.getRenderManager().viewerPosZ;

        Random random = new Random(187L);
        int q = mc.gameSettings.fancyGraphics ? 20 : 10;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();
        float ticks = TileMobAttractor.DISCO_COW_DURATION - tile.discoCowDuration + partialTicks;
        float f1 = ticks / 500.0F;
        float f3 = 0.0F;

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.disableAlpha();
        GlStateManager.enableCull();
        GlStateManager.depthMask(false);

        float rainbowSpeed = ticks * 0.025F;

        for (int i = 0; i < q; ++i) {
            GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(random.nextFloat() * 360.0F + f1 * 360.0F, 0.0F, 0.0F, 1.0F);

            float hue = (rainbowSpeed + (float) i / q) % 1.0F;
            int rgb = MathHelper.hsvToRGB(hue, 1.0F, 1.0F);
            float red = ((rgb >> 16) & 255) / 255.0F;
            float green = ((rgb >> 8) & 255) / 255.0F;
            float blue = (rgb & 255) / 255.0F;

            wr.begin(6, DefaultVertexFormats.POSITION_COLOR);

            float fa = random.nextFloat() * 20.0F + 5.0F + f3 * 10.0F;
            float f4 = random.nextFloat() * 2.0F + 1.0F + f3 * 2.0F;
            float fade = Math.min(ticks, 10.0F) / 10.0F;
            float beamScale = 0.25F;

            fa *= fade * beamScale;
            f4 *= fade * beamScale;

            wr.pos(0.0, 0.0, 0.0).color(1.0F, 1.0F, 1.0F, 1.0F - f3).endVertex();
            wr.pos(-0.866 * f4, fa, -0.5F * f4).color(red, green, blue, 0.0F).endVertex();
            wr.pos(0.866 * f4, fa, -0.5F * f4).color(red, green, blue, 0.0F).endVertex();
            wr.pos(0.0, fa, f4).color(red, green, blue, 0.0F).endVertex();
            wr.pos(-0.866 * f4, fa, -0.5F * f4).color(red, green, blue, 0.0F).endVertex();
            tessellator.draw();
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.popMatrix();
    }
}

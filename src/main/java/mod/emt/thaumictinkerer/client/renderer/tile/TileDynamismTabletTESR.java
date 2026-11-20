package mod.emt.thaumictinkerer.client.renderer.tile;

import mod.emt.thaumictinkerer.tile.TileDynamismTablet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class TileDynamismTabletTESR extends TileEntitySpecialRenderer<TileDynamismTablet> {
    @Override
    public void render(@NotNull TileDynamismTablet tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        this.renderItem(tile);
        GlStateManager.popMatrix();
    }

    private void renderItem(TileDynamismTablet tile) {
        ItemStack stack = tile.getTabletStack();
        if(!stack.isEmpty()) {
            RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
            EnumFacing front = tile.getTabletFront();
            float rotation = (front.getHorizontalAngle() + (front.getAxis() == EnumFacing.Axis.X ? 90 : -90)) % 360.0f;
            float activation = ((float) tile.getActivationCounter() / (float) tile.getMaxActivationCounter()) * 90.f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5, 0.5, 0.5);
            GlStateManager.rotate(activation, front.getZOffset(), 0, -front.getXOffset());

            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.translate(0, 0.25, 0);
            GlStateManager.scale(0.9, 0.9, 0.9);
            GlStateManager.rotate(rotation, 0, 1.0f, 0);
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
    }
}

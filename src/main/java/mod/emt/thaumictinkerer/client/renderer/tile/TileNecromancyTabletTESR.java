package mod.emt.thaumictinkerer.client.renderer.tile;

import mod.emt.thaumictinkerer.tile.TileNecromancyTablet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class TileNecromancyTabletTESR extends TileEntitySpecialRenderer<TileNecromancyTablet> {
    @Override
    public void render(@NotNull TileNecromancyTablet tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(tile, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        this.renderItem(tile, partialTicks);
        GlStateManager.popMatrix();
    }

    private void renderItem(TileNecromancyTablet tile, float partialTicks) {
        ItemStack centerItem = tile.getCenterItem();
        if(!centerItem.isEmpty() && Minecraft.getMinecraft().getRenderViewEntity() != null) {
            RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
            float ticks = ((float)Minecraft.getMinecraft().getRenderViewEntity().ticksExisted + partialTicks) % 360.0f;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.translate(0.5, 0.5, 0.5);
            GlStateManager.rotate(ticks, 0, 1.0f, 0);
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem(centerItem, ItemCameraTransforms.TransformType.GROUND);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}

package mod.emt.thaumictinkerer.client.renderer.tile;

import mod.emt.thaumictinkerer.tile.TileAdvancedRecharger;
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
public class TileAdvancedRechargerTESR extends TileEntitySpecialRenderer<TileAdvancedRecharger> {
    @Override
    public void render(@NotNull TileAdvancedRecharger tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(tile, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        this.renderItem(tile);
        GlStateManager.popMatrix();
    }

    private void renderItem(TileAdvancedRecharger tile) {
        ItemStack toRecharge = tile.getRechargingStack();
        if(!toRecharge.isEmpty()) {
            RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
            float rotation = (float)(720.0 * (double)(System.currentTimeMillis() & 16383L) / 16383.0);

            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.translate(0.5, 1.0, 0.5);
            GlStateManager.rotate(rotation, 0, 1.0f, 0);
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem(toRecharge, ItemCameraTransforms.TransformType.GROUND);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}

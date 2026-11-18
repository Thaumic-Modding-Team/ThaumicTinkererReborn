package mod.emt.thaumictinkerer.client.renderer.tile;

import mod.emt.thaumictinkerer.tile.TileDynamismTablet;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
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

        }
    }
}

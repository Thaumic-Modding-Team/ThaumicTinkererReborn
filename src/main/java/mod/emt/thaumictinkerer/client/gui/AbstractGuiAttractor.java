package mod.emt.thaumictinkerer.client.gui;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.inventory.containers.AbstractContainerAttractor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class AbstractGuiAttractor<T extends AbstractContainerAttractor<?>> extends GuiContainer {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ThaumicTinkerer.MOD_ID, "textures/gui/gui_attractor.png");

    public AbstractGuiAttractor(T container) {
        super(container);
    }

    @SuppressWarnings("unchecked")
    public T getContainer() {
        return (T) inventorySlots;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        ITextComponent tileName = this.getContainer().getAttractor().getDisplayName();
        if(tileName != null) {
            String name = tileName.getUnformattedText();
            this.fontRenderer.drawString(name, 8, 6, 0x404040);
        }
        this.fontRenderer.drawString(this.getContainer().getPlayer().inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 92, 0x404040);
    }
}

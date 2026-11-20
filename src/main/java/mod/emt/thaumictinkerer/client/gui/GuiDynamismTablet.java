package mod.emt.thaumictinkerer.client.gui;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.client.elements.GuiButtonClickDirection;
import mod.emt.thaumictinkerer.client.elements.GuiButtonToggleable;
import mod.emt.thaumictinkerer.inventory.containers.ContainerDyamismTablet;
import mod.emt.thaumictinkerer.tile.TileDynamismTablet;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiDynamismTablet extends GuiContainer {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ThaumicTinkerer.MOD_ID, "textures/gui/gui_dynamism_tablet.png");

    public GuiDynamismTablet(ContainerDyamismTablet container) {
        super(container);
    }

    public ContainerDyamismTablet getContainer() {
        return (ContainerDyamismTablet) inventorySlots;
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
    public void initGui() {
        super.initGui();
        TileDynamismTablet tablet = this.getContainer().getTablet();
        this.addButton(new GuiButtonToggleable(TEXTURE, ContainerDyamismTablet.BUTTON_LEFT_CLICK, this.getGuiLeft() + 84, this.getGuiTop() + 16, 176, 0, 11, 11, tablet.getClickMode() == TileDynamismTablet.ClickMode.LEFT_CLICK));
        this.addButton(new GuiButtonToggleable(TEXTURE, ContainerDyamismTablet.BUTTON_RIGHT_CLICK, this.getGuiLeft() + 84, this.getGuiTop() + 30, 176, 0, 11, 11, tablet.getClickMode() == TileDynamismTablet.ClickMode.RIGHT_CLICK));
        this.addButton(new GuiButtonToggleable(TEXTURE, ContainerDyamismTablet.BUTTON_SNEAKING, this.getGuiLeft() + 84, this.getGuiTop() + 44, 176, 0, 11, 11, tablet.isSneaking()));
        this.addButton(new GuiButtonClickDirection(TEXTURE, ContainerDyamismTablet.BUTTON_CLICK_DIRECTION, this.getGuiLeft() + 91, this.getGuiTop() + 58, 176, 22,70, 16, tablet.getClickDirection()));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        ITextComponent tileName = this.getContainer().getTablet().getDisplayName();
        if(tileName != null) {
            String name = tileName.getUnformattedText();
            this.fontRenderer.drawString(name, 8, 6, 0x404040);
        }
        this.fontRenderer.drawString(this.getContainer().getPlayer().inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 92, 0x404040);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:dynamism_tablet.left_click"), 98, 18, 0x0e0e0e);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:dynamism_tablet.right_click"), 98, 32, 0x0e0e0e);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:dynamism_tablet.sneaking"), 98, 46, 0x0e0e0e);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(!button.enabled || !button.visible)
            return;

        GuiButtonToggleable toggle1 = null;
        GuiButtonToggleable toggle2 = null;

        if (button.id == ContainerDyamismTablet.BUTTON_LEFT_CLICK) {
            toggle1 = (GuiButtonToggleable) button;
            toggle2 = (GuiButtonToggleable) this.buttonList.get(ContainerDyamismTablet.BUTTON_RIGHT_CLICK);
        } else if (button.id == ContainerDyamismTablet.BUTTON_RIGHT_CLICK) {
            toggle1 = (GuiButtonToggleable) button;
            toggle2 = (GuiButtonToggleable) this.buttonList.get(ContainerDyamismTablet.BUTTON_LEFT_CLICK);
        } else if(button.id == ContainerDyamismTablet.BUTTON_SNEAKING) {
            toggle1 = (GuiButtonToggleable) button;
            toggle1.setToggled(!toggle1.isToggled());
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, button.id);
        } else if(button.id == ContainerDyamismTablet.BUTTON_CLICK_DIRECTION && button instanceof GuiButtonClickDirection) {
            ((GuiButtonClickDirection) button).nextDirection();
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, button.id);
        }

        if (toggle1 != null && toggle2 != null && !toggle1.isToggled()) {
            toggle1.setToggled(!toggle1.isToggled());
            toggle2.setToggled(!toggle2.isToggled());
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, button.id);
        }
    }
}

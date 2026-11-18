package mod.emt.thaumictinkerer.client.gui;

import mod.emt.thaumictinkerer.client.elements.GuiButtonToggleable;
import mod.emt.thaumictinkerer.inventory.containers.ContainerMobAttractor;
import mod.emt.thaumictinkerer.tile.TileMobAttractor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiMobAttractor extends AbstractGuiAttractor<ContainerMobAttractor> {
    public GuiMobAttractor(ContainerMobAttractor container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();
        TileMobAttractor attractor = this.getContainer().getAttractor();
        this.addButton(new GuiButtonToggleable(TEXTURE, ContainerMobAttractor.BUTTON_WHITELIST, this.getGuiLeft() + 84, this.getGuiTop() + 24, 176, 0, 11, 11, attractor.isWhitelist()));
        this.addButton(new GuiButtonToggleable(TEXTURE, ContainerMobAttractor.BUTTON_IGNORE_CHILD, this.getGuiLeft() + 84, this.getGuiTop() + 38, 176, 0, 11, 11, attractor.ignoreChild()));
        this.addButton(new GuiButtonToggleable(TEXTURE, ContainerMobAttractor.BUTTON_IGNORE_ADULT, this.getGuiLeft() + 84, this.getGuiTop() + 52, 176, 0, 11, 11, attractor.ignoreAdults()));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:attractor.whitelist"), 98, 26, 0x0e0e0e);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:attractor.ignore_child"), 98, 40, 0x0e0e0e);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:attractor.ignore_adult"), 98, 54, 0x0e0e0e);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(!button.enabled || !button.visible)
            return;

        if (button.id == ContainerMobAttractor.BUTTON_WHITELIST || button.id == ContainerMobAttractor.BUTTON_IGNORE_CHILD || button.id == ContainerMobAttractor.BUTTON_IGNORE_ADULT) {
            if(button instanceof GuiButtonToggleable) {
                ((GuiButtonToggleable) button).setToggled(!((GuiButtonToggleable) button).isToggled());
                this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, button.id);
            }
        }
    }
}

package mod.emt.thaumictinkerer.client.gui;

import mod.emt.thaumictinkerer.client.elements.GuiButtonAttractor;
import mod.emt.thaumictinkerer.inventory.containers.ContainerItemAttractor;
import mod.emt.thaumictinkerer.tile.TileItemAttractor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiItemAttractor extends AbstractGuiAttractor<ContainerItemAttractor> {
    public GuiItemAttractor(ContainerItemAttractor container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();
        TileItemAttractor attractor = this.getContainer().getAttractor();
        this.addButton(new GuiButtonAttractor(ContainerItemAttractor.BUTTON_WHITELIST, this.getGuiLeft() + 84, this.getGuiTop() + 18, attractor.isWhitelist()));
        this.addButton(new GuiButtonAttractor(ContainerItemAttractor.BUTTON_MATCH_META, this.getGuiLeft() + 84, this.getGuiTop() + 32, attractor.matchMeta()));
        this.addButton(new GuiButtonAttractor(ContainerItemAttractor.BUTTON_MATCH_NBT, this.getGuiLeft() + 84, this.getGuiTop() + 46, attractor.matchNBT()));
        this.addButton(new GuiButtonAttractor(ContainerItemAttractor.BUTTON_MATCH_ORE, this.getGuiLeft() + 84, this.getGuiTop() + 60, attractor.matchOreDict()));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:attractor.whitelist"), 98, 20, 0x0e0e0e);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:attractor.match_meta"), 98, 34, 0x0e0e0e);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:attractor.match_nbt"), 98, 48, 0x0e0e0e);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:attractor.match_ore"), 98, 62, 0x0e0e0e);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(!button.enabled || !button.visible)
            return;

        if(button.id == ContainerItemAttractor.BUTTON_WHITELIST || button.id == ContainerItemAttractor.BUTTON_MATCH_META || button.id == ContainerItemAttractor.BUTTON_MATCH_NBT || button.id == ContainerItemAttractor.BUTTON_MATCH_ORE) {
            if (button instanceof GuiButtonAttractor) {
                ((GuiButtonAttractor) button).setToggled(!((GuiButtonAttractor) button).isToggled());
                this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, button.id);
            }
        }
    }
}

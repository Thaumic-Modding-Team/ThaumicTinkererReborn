package mod.emt.thaumictinkerer.api.item;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import net.minecraft.item.Item;

import java.util.Objects;

/**
 * A base class for adding items. Automatically handles item registration.
 */
public abstract class AbstractItemAddition extends Item implements IItemAddition {
    public AbstractItemAddition(String unlocName) {
        this.setRegistryName(ThaumicTinkerer.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
    }
}

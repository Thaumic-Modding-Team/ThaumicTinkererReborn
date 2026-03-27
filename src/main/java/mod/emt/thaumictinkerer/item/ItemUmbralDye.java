package mod.emt.thaumictinkerer.item;

import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import net.minecraftforge.oredict.OreDictionary;

public class ItemUmbralDye extends AbstractItemAddition {
    public ItemUmbralDye() {
        super("umbral_dye");
    }

    @Override
    public void registerOreDicts() {
        OreDictionary.registerOre("dyeBlack", this);
    }
}

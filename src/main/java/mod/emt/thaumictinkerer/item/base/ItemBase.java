package mod.emt.thaumictinkerer.item.base;

import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import net.minecraftforge.oredict.OreDictionary;

public class ItemBase extends AbstractItemAddition  {
    String oreDict;

    public ItemBase(String unlocName, String oreDict) {
        super(unlocName);
        this.oreDict = oreDict;
    }

    @Override
    public void registerOreDicts() {
        if (oreDict != null) {
            OreDictionary.registerOre(oreDict, this);
        }
    }
}

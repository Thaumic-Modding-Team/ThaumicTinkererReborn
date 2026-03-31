package mod.emt.thaumictinkerer.compat.groovyscript;

import com.cleanroommc.groovyscript.documentation.linkgenerator.BasicLinkGenerator;
import mod.emt.thaumictinkerer.ThaumicTinkerer;

public class GSLinkGenerator extends BasicLinkGenerator {
    @Override
    public String id() {
        return ThaumicTinkerer.MOD_ID;
    }

    @Override
    protected String domain() {
        return "https://github.com/Elite-Modding-Team/ThaumicTinkererReborn/";
    }

    @Override
    protected String version() {
        return ThaumicTinkerer.MOD_VERSION;
    }
}

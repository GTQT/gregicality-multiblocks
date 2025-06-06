package gregicality.multiblocks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.jetbrains.annotations.NotNull;

import gregtech.GTInternalTags;

import gregicality.multiblocks.api.utils.GCYMLog;
import gregicality.multiblocks.common.CommonProxy;
import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregicality.multiblocks.common.metatileentities.GCYMMetaTileEntities;

@Mod(modid = GregicalityMultiblocks.MODID,
     name = GregicalityMultiblocks.NAME,
     version = GregicalityMultiblocks.VERSION,
     dependencies = GTInternalTags.DEP_VERSION_STRING)
public class GregicalityMultiblocks {

    public static final String MODID = "gcym";
    public static final String NAME = "Gregicality Multiblocks";
    public static final String VERSION = "1.8.0";

    @SidedProxy(modId = MODID,
                clientSide = "gregicality.multiblocks.common.ClientProxy",
                serverSide = "gregicality.multiblocks.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void onPreInit(@NotNull FMLPreInitializationEvent event) {
        GCYMLog.init(event.getModLog());

        GCYMMetaBlocks.init();
        GCYMMetaTileEntities.init();

        proxy.preLoad();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }
}

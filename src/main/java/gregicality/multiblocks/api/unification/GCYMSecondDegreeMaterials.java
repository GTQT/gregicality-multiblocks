package gregicality.multiblocks.api.unification;

import static gregicality.multiblocks.api.unification.GCYMMaterials.*;
import static gregicality.multiblocks.api.utils.GCYMUtil.gcymId;
import static gregtech.api.GTValues.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.material.properties.BlastProperty;
import gregtech.api.unification.material.properties.MaterialToolProperty;

public final class GCYMSecondDegreeMaterials {

    private GCYMSecondDegreeMaterials() {}

    static int startID=20;

    public static int getStartID() {
        return startID++;
    }

    public static void init() {
        HSLASteel = new Material.Builder(getStartID(), gcymId("hsla_steel"))
                .ingot().fluid()
                .color(0x808080).iconSet(MaterialIconSet.METALLIC)
                .flags(EXT_METAL,GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(Invar, 2, Vanadium, 1, Titanium, 1, Molybdenum, 1)
                .toolStats(MaterialToolProperty.Builder.of(60F, 6.0F, 2048, 4)
                        .enchantability(14).build())
                .rotorStats(6.0f, 5.0f, 2400)
                .fluidPipeProperties(1711, 280, true, true, false, false)
                .blast(b -> b.temp(1711, BlastProperty.GasTier.LOW).blastStats(VA[HV], 1000))
                .build();

        TitaniumTungstenCarbide = new Material.Builder(getStartID(), gcymId("titanium_tungsten_carbide"))
                .ingot().fluid()
                .color(0x800D0D).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(TungstenCarbide, 1, TitaniumCarbide, 2)
                .toolStats(MaterialToolProperty.Builder.of(80F, 8.0F, 2048, 4)
                        .enchantability(14).build())
                .rotorStats(8.0f, 5.0f, 3200)
                .fluidPipeProperties(3800, 400, true, true, false, false)
                .blast(b -> b.temp(3800, BlastProperty.GasTier.HIGH).blastStats(VA[EV], 1000))
                .build();

        IncoloyMA956 = new Material.Builder(getStartID(), gcymId("incoloy_ma_956"))
                .ingot().fluid()
                .color(0x37BF7E).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(VanadiumSteel, 4, Manganese, 2, Aluminium, 5, Yttrium, 2)
                .toolStats(MaterialToolProperty.Builder.of(80F, 6.0F, 2048, 4)
                        .enchantability(14).build())
                .rotorStats(6.0f, 5.0f, 2400)
                .fluidPipeProperties(3625, 400, true, true, false, false)
                .blast(b -> b.temp(3625, BlastProperty.GasTier.MID).blastStats(VA[EV], 800))
                .build();
    }
}

package gregicality.multiblocks.api.unification;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.material.properties.BlastProperty;
import gregtech.api.unification.material.properties.MaterialToolProperty;

import static gregicality.multiblocks.api.unification.GCYMMaterials.*;
import static gregicality.multiblocks.api.utils.GCYMUtil.gcymId;
import static gregtech.api.GTValues.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;

public final class GCYMFirstDegreeMaterials {

    static int startID = 3000;

    private GCYMFirstDegreeMaterials() {
    }

    public static int getStartID() {
        return startID++;
    }

    public static void init() {
        Stellite100 = new Material.Builder(getStartID(), gcymId("stellite_100"))
                .ingot().fluid()
                .color(0xDEDEFF).iconSet(MaterialIconSet.BRIGHT)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(Iron, 4, Chrome, 3, Tungsten, 2, Molybdenum, 1)
                .toolStats(MaterialToolProperty.Builder.of(10.0F, 7.0F, 2048, 4)
                        .enchantability(14).build())
                .rotorStats(10.0f, 4.0f, 2560)
                .fluidPipeProperties(3790, 250, true, true, false, false)
                .blast(b -> b.temp(3790, BlastProperty.GasTier.HIGH).blastStats(VA[EV], 1000))
                .build();

        WatertightSteel = new Material.Builder(getStartID(), gcymId("watertight_steel"))
                .ingot().fluid()
                .color(0x355D6A).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(Iron, 7, Aluminium, 4, Nickel, 2, Chrome, 1, Sulfur, 1)
                .blast(b -> b.temp(3850, BlastProperty.GasTier.MID).blastStats(VA[EV], 800))
                .build();

        MaragingSteel300 = new Material.Builder(getStartID(), gcymId("maraging_steel_300"))
                .ingot().fluid()
                .color(0x637087).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(Iron, 16, Titanium, 1, Aluminium, 1, Nickel, 4, Cobalt, 2)
                .toolStats(MaterialToolProperty.Builder.of(10.0F, 8.0F, 2048, 5)
                        .enchantability(14).build())
                .rotorStats(9.0f, 4.0f, 2800)
                .fluidPipeProperties(4000, 400, true, true, false, false)
                .blast(b -> b.temp(4000, BlastProperty.GasTier.HIGH).blastStats(VA[EV], 1000))
                .build();

        HastelloyC276 = new Material.Builder(getStartID(), gcymId("hastelloy_c_276"))
                .ingot().fluid()
                .color(0xCF3939).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(Nickel, 12, Molybdenum, 8, Chrome, 7, Tungsten, 1, Cobalt, 1, Copper, 1)
                .toolStats(MaterialToolProperty.Builder.of(12.0F, 8.0F, 2048, 5)
                        .enchantability(14).build())
                .rotorStats(12.0f, 4.0f, 2800)
                .fluidPipeProperties(4625, 500, true, true, false, false)
                .blast(b -> b.temp(4625, BlastProperty.GasTier.MID))
                .build();

        HastelloyX = new Material.Builder(getStartID(), gcymId("hastelloy_x"))
                .ingot().fluid()
                .color(0x6BA3E3).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(Nickel, 8, Iron, 3, Tungsten, 4, Molybdenum, 2, Chrome, 1, Niobium, 1)
                .toolStats(MaterialToolProperty.Builder.of(12.0F, 8.0F, 2048, 6)
                        .enchantability(14).build())
                .rotorStats(12.0f, 6.0f, 3200)
                .fluidPipeProperties(4200, 500, true, true, false, false)
                .blast(b -> b.temp(4200, BlastProperty.GasTier.HIGH).blastStats(VA[EV], 900))
                .build();

        Trinaquadalloy = new Material.Builder(getStartID(), gcymId("trinaquadalloy"))
                .ingot().fluid()
                .color(0x281832).iconSet(MaterialIconSet.BRIGHT)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(Trinium, 6, Naquadah, 2, Carbon, 1)
                .toolStats(MaterialToolProperty.Builder.of(12.0F, 10.0F, 3200, 6)
                        .enchantability(14).build())
                .rotorStats(14.0f, 8.0f, 3200)
                .fluidPipeProperties(8747, 800, true, true, false, false)
                .cableProperties(V[ZPM], 8, 1)
                .blast(b -> b.temp(8747, BlastProperty.GasTier.HIGHER).blastStats(VA[ZPM], 1200))
                .build();

        Zeron100 = new Material.Builder(getStartID(), gcymId("zeron_100"))
                .ingot().fluid()
                .color(0x325A8C).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(Iron, 10, Nickel, 2, Tungsten, 2, Niobium, 1, Cobalt, 1)
                .toolStats(MaterialToolProperty.Builder.of(10.0F, 6.0F, 2048, 5)
                        .enchantability(14).build())
                .rotorStats(10.0f, 6.0f, 2800)
                .fluidPipeProperties(3693, 420, true, true, false, false)
                .blast(b -> b.temp(3693, BlastProperty.GasTier.MID).blastStats(VA[EV], 1000))
                .build();

        TitaniumCarbide = new Material.Builder(getStartID(), gcymId("titanium_carbide"))
                .ingot().fluid()
                .color(0xB20B3A).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(Titanium, 1, Carbon, 1)
                .toolStats(MaterialToolProperty.Builder.of(10.0F, 6.0F, 2048, 5)
                        .enchantability(14).build())
                .rotorStats(10.0f, 6.0f, 2800)
                .fluidPipeProperties(3430, 380, true, true, false, false)
                .blast(b -> b.temp(3430, BlastProperty.GasTier.MID).blastStats(VA[EV], 1000))
                .build();

        TantalumCarbide = new Material.Builder(getStartID(), gcymId("tantalum_carbide"))
                .ingot().fluid()
                .color(0x56566A).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(Tantalum, 1, Carbon, 1)
                .toolStats(MaterialToolProperty.Builder.of(8.0F, 6.0F, 2048, 5)
                        .enchantability(14).build())
                .rotorStats(8.0f, 5.0f, 2800)
                .fluidPipeProperties(4120, 380, true, true, false, false)
                .blast(b -> b.temp(4120, BlastProperty.GasTier.MID).blastStats(VA[EV], 1200))
                .build();

        MolybdenumDisilicide = new Material.Builder(getStartID(), gcymId("molybdenum_disilicide"))
                .ingot().fluid()
                .color(0x6A5BA3).iconSet(MaterialIconSet.METALLIC)
                .flags(EXT_METAL, GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_DENSE, GENERATE_FINE_WIRE, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_ROUND, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROTOR)
                .components(Molybdenum, 1, Silicon, 2)
                .toolStats(MaterialToolProperty.Builder.of(60F, 6.0F, 2048, 4)
                        .enchantability(14).build())
                .rotorStats(6.0f, 5.0f, 2400)
                .fluidPipeProperties(2300, 280, true, true, false, false)
                .blast(b -> b.temp(2300, BlastProperty.GasTier.MID).blastStats(VA[EV], 800))
                .build();
    }
}

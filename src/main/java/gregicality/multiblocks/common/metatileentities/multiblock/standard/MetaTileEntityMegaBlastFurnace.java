package gregicality.multiblocks.common.metatileentities.multiblock.standard;

import gregicality.multiblocks.api.capability.impl.GCYMHeatCoilRecipeLogic;
import gregicality.multiblocks.api.metatileentity.GCYMAdvanceRecipeMapMultiblockController;
import gregicality.multiblocks.api.metatileentity.GCYMMultiblockAbility;
import gregicality.multiblocks.api.render.GCYMTextures;
import gregicality.multiblocks.common.GCYMConfigHolder;
import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregicality.multiblocks.common.block.blocks.BlockLargeMultiblockCasing;
import gregicality.multiblocks.common.block.blocks.BlockUniqueCasing;
import gregtech.api.GTValues;
import gregtech.api.block.IHeatingCoilBlockStats;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.IHeatingCoil;
import gregtech.api.capability.IMufflerHatch;
import gregtech.api.capability.impl.EnergyContainerList;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockDisplayText;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.properties.impl.TemperatureProperty;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TextComponentUtil;
import gregtech.api.util.TextFormattingUtil;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.blocks.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static gregtech.api.recipes.logic.OverclockingLogic.heatingCoilOC;

public class MetaTileEntityMegaBlastFurnace extends GCYMAdvanceRecipeMapMultiblockController implements IHeatingCoil {

    private int blastFurnaceTemperature;

    public MetaTileEntityMegaBlastFurnace(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.BLAST_RECIPES);
        recipeMapWorkable = new ArrayList<>();
        this.recipeMapWorkable.add(new GCYMHeatCoilRecipeLogic(this));

    }

    private static IBlockState getCasingState() {
        return GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING.getState(BlockLargeMultiblockCasing.CasingType.HIGH_TEMPERATURE_CASING);
    }

    private static IBlockState getCasingState2() {
        return MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.TUNGSTENSTEEL_ROBUST);
    }

    private static IBlockState getFireboxState() {
        return MetaBlocks.BOILER_FIREBOX_CASING.getState(BlockFireboxCasing.FireboxCasingType.TUNGSTENSTEEL_FIREBOX);
    }

    private static IBlockState getIntakeState() {
        return MetaBlocks.MULTIBLOCK_CASING
                .getState(BlockMultiblockCasing.MultiblockCasingType.EXTREME_ENGINE_INTAKE_CASING);
    }

    private static IBlockState getPipeState() {
        return MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE);
    }

    private static IBlockState getVentState() {
        return GCYMMetaBlocks.UNIQUE_CASING.getState(BlockUniqueCasing.UniqueCasingType.HEAT_VENT);
    }

    @Override
    public void refreshThread(int thread) {
        if (!this.checkWorkingEnable()) {
            this.recipeMapWorkable = new ArrayList<>();

            for (int i = 0; i < thread; ++i) {
                this.recipeMapWorkable.add(new GCYMHeatCoilRecipeLogic(this));
            }
        }
    }

    @Override
    protected void initializeAbilities() {
        this.inputInventory = new ItemHandlerList(this.getAbilities(MultiblockAbility.IMPORT_ITEMS));
        this.inputFluidInventory = new FluidTankList(this.allowSameFluidFillForOutputs(), this.getAbilities(MultiblockAbility.IMPORT_FLUIDS));
        this.outputInventory = new ItemHandlerList(this.getAbilities(MultiblockAbility.EXPORT_ITEMS));
        this.outputFluidInventory = new FluidTankList(this.allowSameFluidFillForOutputs(), this.getAbilities(MultiblockAbility.EXPORT_FLUIDS));
        List<IEnergyContainer> energyContainer = new ArrayList<>(this.getAbilities(MultiblockAbility.INPUT_ENERGY));
        energyContainer.addAll(this.getAbilities(MultiblockAbility.INPUT_LASER));
        this.energyContainer = new EnergyContainerList(energyContainer);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity metaTileEntityHolder) {
        return new MetaTileEntityMegaBlastFurnace(this.metaTileEntityId);
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        MultiblockDisplayText.builder(textList, isStructureFormed())
                .setWorkingStatus(checkWorkingEnable(), checkActive())
                .addEnergyUsageLine(getEnergyContainer())
                .addEnergyTierLine(GTUtility.getTierByVoltage(recipeMapWorkable.get(0).getMaxVoltage()))
                .addCustom(tl -> {
                    // Tiered Hatch Line
                    if (isStructureFormed()) {
                        List<ITieredMetaTileEntity> list = getAbilities(GCYMMultiblockAbility.TIERED_HATCH);
                        if (GCYMConfigHolder.globalMultiblocks.enableTieredCasings && !list.isEmpty()) {
                            long maxVoltage = Math.min(GTValues.V[list.get(0).getTier()],
                                    Math.max(energyContainer.getInputVoltage(), energyContainer.getOutputVoltage()));
                            String voltageName = GTValues.VNF[list.get(0).getTier()];
                            tl.add(new TextComponentTranslation("gcym.multiblock.tiered_hatch.tooltip", maxVoltage,
                                    voltageName));
                        }
                    }
                })
                .addCustom(tl -> {
                    // Coil heat capacity line
                    if (isStructureFormed()) {
                        ITextComponent heatString = TextComponentUtil.stringWithColor(
                                TextFormatting.RED,
                                TextFormattingUtil.formatNumbers(blastFurnaceTemperature) + "K");

                        tl.add(TextComponentUtil.translationWithColor(
                                TextFormatting.GRAY,
                                "gregtech.multiblock.blast_furnace.max_temperature",
                                heatString));
                    }
                })
                .addWorkingStatusLine();
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        Object type = context.get("CoilType");
        if (type instanceof IHeatingCoilBlockStats stats) {
            this.blastFurnaceTemperature = stats.getCoilTemperature();
        } else {
            this.blastFurnaceTemperature = BlockWireCoil.CoilType.CUPRONICKEL.getCoilTemperature();
        }

        this.blastFurnaceTemperature += 100 *
                Math.max(0, GTUtility.getTierByVoltage(getEnergyContainer().getInputVoltage()) - GTValues.MV);

        this.thread = this.getAbilities(MultiblockAbility.THREAD_HATCH).isEmpty() ? 1 : this.getAbilities(MultiblockAbility.THREAD_HATCH).get(0).getCurrentThread();
        this.recipeMapWorkable = new ArrayList<>();

        for (int i = 0; i < this.thread; ++i) {
            this.recipeMapWorkable.add(new GCYMHeatCoilRecipeLogic(this));
        }
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.blastFurnaceTemperature = 0;
    }

    @Override
    public boolean checkRecipe(@NotNull Recipe recipe, boolean consumeIfSuccess) {
        return this.blastFurnaceTemperature >= recipe.getProperty(TemperatureProperty.getInstance(), 0);
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        TraceabilityPredicate casing = states(getCasingState()).setMinGlobalLimited(360);
        return FactoryBlockPattern.start()
                .aisle("##XXXXXXXXX##", "##XXXXXXXXX##", "#############", "#############", "#############",
                        "#############", "#############", "#############", "#############", "#############",
                        "#############", "#############", "#############", "#############", "#############",
                        "#############", "#############")
                .aisle("#XXXXXXXXXXX#", "#XXXXXXXXXXX#", "###F#####F###", "###F#####F###", "###FFFFFFF###",
                        "#############", "#############", "#############", "#############", "#############",
                        "####FFFFF####", "#############", "#############", "#############", "#############",
                        "#############", "#############")
                .aisle("XXXXXXXXXXXXX", "XXXXVVVVVXXXX", "##F#######F##", "##F#######F##", "##FFFHHHFFF##",
                        "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##",
                        "##FFFHHHFFF##", "#############", "#############", "#############", "#############",
                        "#############", "###TTTTTTT###")
                .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "#F####P####F#", "#F####P####F#", "#FFHHHPHHHFF#",
                        "######P######", "######P######", "######P######", "######P######", "######P######",
                        "##FHHHPHHHF##", "######P######", "######P######", "######P######", "######P######",
                        "######P######", "##TTTTPTTTT##")
                .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BBPBB####", "####TITIT####", "#FFHHHHHHHFF#",
                        "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####", "####BITIB####",
                        "#FFHHHHHHHFF#", "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####",
                        "####BITIB####", "##TTTTPTTTT##")
                .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BAAAB####", "####IAAAI####", "#FHHHAAAHHHF#",
                        "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####", "####IAAAI####",
                        "#FHHHAAAHHHF#", "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####",
                        "####IAAAI####", "##TTTTPTTTT##")
                .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "###PPAAAPP###", "###PTAAATP###", "#FHPHAAAHPHF#",
                        "###PTAAATP###", "###PCAAACP###", "###PCAAACP###", "###PCAAACP###", "###PTAAATP###",
                        "#FHPHAAAHPHF#", "###PTAAATP###", "###PCAAACP###", "###PCAAACP###", "###PCAAACP###",
                        "###PTAAATP###", "##TPPPMPPPT##")
                .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BAAAB####", "####IAAAI####", "#FHHHAAAHHHF#",
                        "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####", "####IAAAI####",
                        "#FHHHAAAHHHF#", "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####",
                        "####IAAAI####", "##TTTTPTTTT##")
                .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BBPBB####", "####TITIT####", "#FFHHHHHHHFF#",
                        "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####", "####BITIB####",
                        "#FFHHHHHHHFF#", "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####",
                        "####BITIB####", "##TTTTPTTTT##")
                .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "#F####P####F#", "#F####P####F#", "#FFHHHPHHHFF#",
                        "######P######", "######P######", "######P######", "######P######", "######P######",
                        "##FHHHPHHHF##", "######P######", "######P######", "######P######", "######P######",
                        "######P######", "##TTTTPTTTT##")
                .aisle("XXXXXXXXXXXXX", "XXXXVVVVVXXXX", "##F#######F##", "##F#######F##", "##FFFHHHFFF##",
                        "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##",
                        "##FFFHHHFFF##", "#############", "#############", "#############", "#############",
                        "#############", "###TTTTTTT###")
                .aisle("#XXXXXXXXXXX#", "#XXXXXXXXXXX#", "###F#####F###", "###F#####F###", "###FFFFFFF###",
                        "#############", "#############", "#############", "#############", "#############",
                        "####FFFFF####", "#############", "#############", "#############", "#############",
                        "#############", "#############")
                .aisle("##XXXXXXXXX##", "##XXXXSXXXX##", "#############", "#############", "#############",
                        "#############", "#############", "#############", "#############", "#############",
                        "#############", "#############", "#############", "#############", "#############",
                        "#############", "#############")
                .where('S', selfPredicate())
                .where('X', casing
                        .or(autoAbilities(false, true, true, true, true, true, false))
                        .or(abilities(MultiblockAbility.INPUT_ENERGY)
                                .setMaxGlobalLimited(8))
                        .or(abilities(MultiblockAbility.INPUT_LASER)
                                .setMaxGlobalLimited(1)))
                .where('F', frames(Materials.NaquadahAlloy))
                .where('H', casing)
                .where('P', states(getPipeState()))
                .where('B', states(getFireboxState()))
                .where('I', states(getIntakeState()))
                .where('T', states(getCasingState2()))
                .where('V', states(getVentState()))
                .where('M', abilities(MultiblockAbility.MUFFLER_HATCH))
                .where('C', heatingCoils())
                .where('A', air())
                .where('#', any())
                .build();
    }

    @Override
    public void addInformation(ItemStack stack, @javax.annotation.Nullable World player, @Nonnull List<String> tooltip, boolean advanced) {
        tooltip.add(TooltipHelper.RAINBOW_SLOW + I18n.format("谁家超级高炉", new Object[0]));
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gregtech.machine.electric_blast_furnace.tooltip.1"));
        tooltip.add(I18n.format("gregtech.machine.electric_blast_furnace.tooltip.2"));
        tooltip.add(I18n.format("gregtech.machine.electric_blast_furnace.tooltip.3"));
        tooltip.add(I18n.format("gregtech.machine.laser_hatch.tooltip"));
    }
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return iMultiblockPart instanceof IMufflerHatch ? Textures.ROBUST_TUNGSTENSTEEL_CASING :
                GCYMTextures.BLAST_CASING;
    }

    @Override
    protected @NotNull OrientedOverlayRenderer getFrontOverlay() {
        return GCYMTextures.MEGA_BLAST_FURNACE_OVERLAY;
    }

    @Override
    public boolean hasMufflerMechanics() {
        return true;
    }

    @Override
    public boolean canBeDistinct() {
        return true;
    }

    @Override
    public int getCurrentTemperature() {
        return this.blastFurnaceTemperature;
    }
}

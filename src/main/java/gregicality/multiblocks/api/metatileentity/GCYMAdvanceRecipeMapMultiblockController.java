package gregicality.multiblocks.api.metatileentity;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.capability.IThreadHatch;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.multiblock.AdvanceMultiMapMultiblockController;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.pattern.PatternMatchContext;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.GTValues;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiMapMultiblockController;
import gregtech.api.metatileentity.multiblock.MultiblockDisplayText;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.GTUtility;

import gregicality.multiblocks.api.capability.IParallelMultiblock;
import gregicality.multiblocks.api.capability.impl.GCYMMultiblockRecipeLogic;
import gregicality.multiblocks.common.GCYMConfigHolder;

public abstract class GCYMAdvanceRecipeMapMultiblockController extends AdvanceMultiMapMultiblockController
        implements IParallelMultiblock {

    public GCYMAdvanceRecipeMapMultiblockController(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap) {
        this(metaTileEntityId, new RecipeMap<?>[] { recipeMap });
    }

    public GCYMAdvanceRecipeMapMultiblockController(ResourceLocation metaTileEntityId, RecipeMap<?>[] recipeMaps) {
        super(metaTileEntityId, recipeMaps);
        recipeMapWorkable = new ArrayList<>();
        this.recipeMapWorkable.add(new GCYMMultiblockRecipeLogic(this));
    }
    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.initializeAbilities();
        this.thread = this.getAbilities(MultiblockAbility.THREAD_HATCH).isEmpty() ? 1 : ((IThreadHatch)this.getAbilities(MultiblockAbility.THREAD_HATCH).get(0)).getCurrentThread();
        this.recipeMapWorkable = new ArrayList<>();

        for(int i = 0; i < this.thread; ++i) {
            this.recipeMapWorkable.add(new GCYMMultiblockRecipeLogic(this));
        }
    }
    @Override
    public void refreshThread(int thread) {
        if (!this.checkWorkingEnable()) {
            this.recipeMapWorkable = new ArrayList<>();

            for(int i = 0; i < thread; ++i) {
                this.recipeMapWorkable.add(new GCYMMultiblockRecipeLogic(this));
            }
        }
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        if (isParallel()) {
            tooltip.add(I18n.format("gcym.tooltip.parallel_enabled"));
            tooltip.add(I18n.format("tile.gcym.tooltip.1"));
            tooltip.add(I18n.format("tile.gcym.tooltip.2"));
            tooltip.add(I18n.format("tile.gcym.tooltip.3"));
            tooltip.add(I18n.format("tile.gcym.tooltip.4"));
            tooltip.add(I18n.format("tile.gcym.tooltip.5"));
            tooltip.add(I18n.format("tile.gcym.tooltip.6"));
        }
        if (GCYMConfigHolder.globalMultiblocks.enableTieredCasings && isTiered())
            tooltip.add(I18n.format("gcym.tooltip.tiered_hatch_enabled"));
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
                .addWorkingStatusLine();
    }

    @Override
    public boolean isParallel() {
        return true;
    }

    @Override
    public int getMaxParallel() {
        return this.getAbilities(GCYMMultiblockAbility.PARALLEL_HATCH).isEmpty() ? 1 :
                this.getAbilities(GCYMMultiblockAbility.PARALLEL_HATCH).get(0).getCurrentParallel();
    }

    public boolean isTiered() {
        return GCYMConfigHolder.globalMultiblocks.enableTieredCasings;
    }

    @Override
    public TraceabilityPredicate autoAbilities(boolean checkEnergyIn, boolean checkMaintenance, boolean checkItemIn,
                                               boolean checkItemOut, boolean checkFluidIn, boolean checkFluidOut,
                                               boolean checkMuffler) {
        TraceabilityPredicate predicate = super.autoAbilities(checkEnergyIn, checkMaintenance, checkItemIn,
                checkItemOut, checkFluidIn, checkFluidOut, checkMuffler);
        if (isParallel())
            predicate = predicate
                    .or(abilities(GCYMMultiblockAbility.PARALLEL_HATCH).setMaxGlobalLimited(1).setPreviewCount(1));
        return predicate;
    }

    public static @NotNull TraceabilityPredicate tieredCasing() {
        return new TraceabilityPredicate(abilities(GCYMMultiblockAbility.TIERED_HATCH)
                .setMinGlobalLimited(GCYMConfigHolder.globalMultiblocks.enableTieredCasings ? 1 : 0)
                .setMaxGlobalLimited(1));
    }
}

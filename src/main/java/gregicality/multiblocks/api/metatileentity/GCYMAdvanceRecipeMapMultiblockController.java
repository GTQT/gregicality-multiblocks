package gregicality.multiblocks.api.metatileentity;

import gregicality.multiblocks.api.capability.IParallelMultiblock;
import gregicality.multiblocks.api.capability.impl.GCYMMultiblockRecipeLogic;
import gregicality.multiblocks.api.tooltips.GGCYMMMultiblockInformation;
import gregicality.multiblocks.api.tooltips.ThreadMultiblockInformation;
import gregicality.multiblocks.api.tooltips.TiredMultiblockInformation;
import gregicality.multiblocks.common.GCYMConfigHolder;
import gregtech.api.metatileentity.multiblock.AdvanceMultiMapMultiblockController;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.tooltips.TooltipBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class GCYMAdvanceRecipeMapMultiblockController extends AdvanceMultiMapMultiblockController
        implements IParallelMultiblock {

    public GCYMAdvanceRecipeMapMultiblockController(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap) {
        this(metaTileEntityId, new RecipeMap<?>[]{recipeMap});
    }

    public GCYMAdvanceRecipeMapMultiblockController(ResourceLocation metaTileEntityId, RecipeMap<?>[] recipeMaps) {
        super(metaTileEntityId, recipeMaps);
        recipeMapWorkable = new ArrayList<>();
        this.recipeMapWorkable.add(new GCYMMultiblockRecipeLogic(this));
    }

    public static @NotNull TraceabilityPredicate tieredCasing() {
        return new TraceabilityPredicate(abilities(GCYMMultiblockAbility.TIERED_HATCH)
                .setMinGlobalLimited(GCYMConfigHolder.globalMultiblocks.enableTieredCasings ? 1 : 0)
                .setMaxGlobalLimited(1));
    }

    public void refreshThread(int currentThread) {
        if (currentThread == 0) return;
        if (!isActive()) {
            recipeMapWorkable = new ArrayList<>();
            for (int i = 0; i < currentThread; i++) {
                recipeMapWorkable.add(new GCYMMultiblockRecipeLogic(this));
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        TooltipBuilder.create()
                .addIf(isParallel(), new GGCYMMMultiblockInformation())
                .addIf(isParallel(), new ThreadMultiblockInformation())
                .addIf(GCYMConfigHolder.globalMultiblocks.enableTieredCasings && isTiered(), new TiredMultiblockInformation())
                .build(this, tooltip);

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
}

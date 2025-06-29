package gregicality.multiblocks.api.capability.impl;

import gregicality.multiblocks.api.capability.IParallelMultiblock;
import gregicality.multiblocks.api.metatileentity.GCYMMultiblockAbility;
import gregicality.multiblocks.api.metatileentity.GCYMRecipeMapMultiblockController;
import gregicality.multiblocks.common.GCYMConfigHolder;
import gregtech.api.GTValues;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GCYMMultiblockRecipeLogic extends MultiblockRecipeLogic {

    public GCYMMultiblockRecipeLogic(RecipeMapMultiblockController tileEntity) {
        super(tileEntity);
    }

    public GCYMMultiblockRecipeLogic(RecipeMapMultiblockController tileEntity, boolean hasPerfectOC) {
        super(tileEntity, hasPerfectOC);
    }

    @Override
    public int getParallelLimit() {
        if (metaTileEntity instanceof IParallelMultiblock && ((IParallelMultiblock) metaTileEntity).isParallel()) {
            return ((IParallelMultiblock) metaTileEntity).getMaxParallel();
        }
        return 1;
    }

    @Override
    public void setMaxProgress(int maxProgress) {
        if (metaTileEntity instanceof IParallelMultiblock parallelMultiblock) {
            int maxParallel = parallelMultiblock.getMaxParallel();
            if (maxParallel <= 16) {
                this.maxProgressTime = maxProgress / maxParallel;
            } else if (maxParallel <= 64) {
                this.maxProgressTime = (int) (maxProgress * 4.0 / maxParallel);
            } else if (maxParallel <= 256) {
                this.maxProgressTime = (int) (maxProgress * 64.0 / maxParallel);
            } else if (maxParallel <= 1024) {
                this.maxProgressTime = (int) (maxProgress * 512.0 / maxParallel);
            } else {
                this.maxProgressTime = maxProgress;
            }
        } else {
            this.maxProgressTime = maxProgress;
        }
    }


    @Override
    public @NotNull RecipeMapMultiblockController getMetaTileEntity() {
        return (RecipeMapMultiblockController) super.getMetaTileEntity();
    }

    @Override
    public long getMaxVoltage() {
        if (!GCYMConfigHolder.globalMultiblocks.enableTieredCasings)
            return super.getMaxVoltage();

        if (getMetaTileEntity() instanceof GCYMRecipeMapMultiblockController controller && !controller.isTiered())
            return super.getMaxVoltage();

        List<ITieredMetaTileEntity> list = getMetaTileEntity().getAbilities(GCYMMultiblockAbility.TIERED_HATCH);

        if (list.isEmpty())
            return super.getMaxVoltage();

        return Math.min(GTValues.V[list.get(0).getTier()], super.getMaxVoltage());
    }

    @Override
    public void updateRecipeProgress() {
        if (this.canRecipeProgress) {
            if (this.progressTime < this.maxProgressTime && this.drawEnergy(this.recipeEUt, true)) {
                this.drawEnergy(this.recipeEUt, false);
                ++this.progressTime;
                if (this.hasNotEnoughEnergy && this.getEnergyInputPerSecond() > 19L * this.recipeEUt) {
                    this.hasNotEnoughEnergy = false;
                }
            } else if (this.checkOutputSpaceItems(previousRecipe, this.getOutputInventory()) && this.checkOutputSpaceFluids(previousRecipe, this.getOutputTank())) {
                this.completeRecipe();
            }
        } else if (this.recipeEUt > 0L) {
            this.hasNotEnoughEnergy = true;
            this.decreaseProgress();
        }
    }
}

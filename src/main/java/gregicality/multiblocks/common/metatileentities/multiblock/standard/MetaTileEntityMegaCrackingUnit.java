package gregicality.multiblocks.common.metatileentities.multiblock.standard;

import com.cleanroommc.modularui.api.drawable.IKey;
import gregicality.multiblocks.api.capability.impl.GCYMMultiblockRecipeLogic;
import gregicality.multiblocks.api.metatileentity.GCYMRecipeMapMultiblockController;
import gregicality.multiblocks.api.render.GCYMTextures;
import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregicality.multiblocks.common.block.blocks.BlockLargeMultiblockCasing;
import gregtech.api.block.IHeatingCoilBlockStats;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.ui.MultiblockUIBuilder;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.logic.OCResult;
import gregtech.api.recipes.properties.RecipePropertyStorage;
import gregtech.api.util.GTUtility;
import gregtech.api.util.KeyUtil;
import gregtech.api.util.tooltips.InformationHandler;
import gregtech.api.util.tooltips.TooltipBuilder;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//这是与GCYM的转底炉 巨冰箱同一系列的设备
//此系列设备不给多线程
public class MetaTileEntityMegaCrackingUnit extends GCYMRecipeMapMultiblockController {
    private int coilTier;

    public MetaTileEntityMegaCrackingUnit(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, new RecipeMap[]{
                RecipeMaps.CRACKING_RECIPES
        });
        this.recipeMapWorkable = new CrackingUnitWorkableHandler(this);
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("CCCCCCCCCCCCC", " C         C ", " C         C ", " C         C ", " C         C ", " C         C ", " C         C ")
                .aisle("CCCCCCCCCCCCC", "CCGGGGGGGGGCC", "CCGGGGGGGGGCC", "CCGGGGGGGGGCC", "CCGGGGGGGGGCC", "CCGGGGGGGGGCC", "CCGGGGGGGGGCC")
                .aisle("CCCCCCCCCCCCC", " GALALALALAG ", " GALALALALAG ", " GALALALALAG ", " GALALALALAG ", " GALALALALAG ", " CGGGGGGGGGC ")
                .aisle("CCCCCCCCCCCCC", " GALALALALAG ", " EAAAAAAAAAD ", " EALALALALAD ", " EAAAAAAAAAD ", " GALALALALAG ", " CGGGEEEGGGC ")
                .aisle("CCCCCCCCCCCCC", " GALALALALAG ", " EALALALALAD ", " EALALALALAD ", " EALALALALAD ", " GALALALALAG ", " CGGGEEEGGGC ")
                .aisle("CCCCCCCCCCCCC", " GALALALALAG ", " EAAAAAAAAAD ", " EALALALALAD ", " EAAAAAAAAAD ", " GALALALALAG ", " CGGGEEEGGGC ")
                .aisle("CCCCCCCCCCCCC", " GALALALALAG ", " GALALALALAG ", " GALALALALAG ", " GALALALALAG ", " GALALALALAG ", " CGGGGGGGGGC ")
                .aisle("CCCCCCCCCCCCC", "CCGGGGGGGGGCC", "CCGGGGGGGGGCC", "CCGGGGGGGGGCC", "CCGGGGGGGGGCC", "CCGGGGGGGGGCC", "CCGGGGGGGGGCC")
                .aisle("CCCCCCSCCCCCC", " C         C ", " C         C ", " C         C ", " C         C ", " C         C ", " C         C ")
                .where('S', this.selfPredicate())
                .where('C', states(getCasingState())
                        .or(abilities(MultiblockAbility.INPUT_ENERGY).setMaxGlobalLimited(3))
                        .or(abilities(MultiblockAbility.INPUT_LASER).setMaxGlobalLimited(1))
                        .or(abilities(MultiblockAbility.MAINTENANCE_HATCH).setExactLimit(1))
                )
                .where('G', states(getGlassState()))
                .where('L', heatingCoils())
                .where('D', states(getCasingState())
                        .or(autoAbilities(false, true, false, true, false, true, false))
                )
                .where('E', states(getCasingState())
                        .or(autoAbilities(false, true, true, false, true, false, false))
                )
                .where(' ', any())
                .where('A', air())
                .build();
    }

    protected IBlockState getCasingState() {
        return GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING.getState(BlockLargeMultiblockCasing.CasingType.WATERTIGHT_CASING);
    }

    protected IBlockState getGlassState() {
        return MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.FUSION_GLASS);
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GCYMTextures.WATERTIGHT_CASING;
    }

    @Override
    protected void configureDisplayText(MultiblockUIBuilder builder) {
        builder.setWorkingStatus(recipeMapWorkable.isWorkingEnabled(), recipeMapWorkable.isActive())
                .addEnergyUsageLine(getEnergyContainer())
                .addEnergyTierLine(GTUtility.getTierByVoltage(recipeMapWorkable.getMaxVoltage()))
                .addCustom((textList, syncer) -> {
                    if (!isStructureFormed()) return;

                    // Coil energy discount line
                    IKey energyDiscount = KeyUtil.number(TextFormatting.AQUA,
                            syncer.syncLong(100 - 10L * getCoilTier()), "%");

                    IKey base = KeyUtil.lang(TextFormatting.GRAY,
                            "gregtech.multiblock.cracking_unit.energy",
                            energyDiscount);

                    IKey hover = KeyUtil.lang(TextFormatting.GRAY,
                            "gregtech.multiblock.cracking_unit.energy_hover");

                    textList.add(KeyUtil.setHover(base, hover));
                })
                .addParallelsLine(recipeMapWorkable.getParallelLimit())
                .addWorkingStatusLine()
                .addProgressLine(recipeMapWorkable.getProgress(), recipeMapWorkable.getMaxProgress())
                .addRecipeOutputLine(recipeMapWorkable);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        InformationHandler.topTooltips("最强裂化机", tooltip);
        super.addInformation(stack, player, tooltip, advanced);
        TooltipBuilder.create().addCoilLogic().build(this, tooltip);
        tooltip.add(I18n.format("gregtech.machine.cracker.tooltip.1"));
        TooltipBuilder.create().addLaser().build(this, tooltip);
    }

    @Override
    public boolean canBeDistinct() {
        return true;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity metaTileEntityHolder) {
        return new MetaTileEntityMegaCrackingUnit(this.metaTileEntityId);
    }

    @Override
    protected @NotNull OrientedOverlayRenderer getFrontOverlay() {
        return GCYMTextures.MEGA_CHEMICAL_REACTOR;
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        Object type = context.get("CoilType");
        if (type instanceof IHeatingCoilBlockStats) {
            this.coilTier = ((IHeatingCoilBlockStats) type).getTier();
        } else {
            this.coilTier = 0;
        }
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.coilTier = -1;
    }

    protected int getCoilTier() {
        return this.coilTier;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    private class CrackingUnitWorkableHandler extends GCYMMultiblockRecipeLogic {

        public CrackingUnitWorkableHandler(GCYMRecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        @Override
        protected void modifyOverclockPost(@NotNull OCResult ocResult, @NotNull RecipePropertyStorage storage) {
            super.modifyOverclockPost(ocResult, storage);

            int coilTier = ((MetaTileEntityMegaCrackingUnit) metaTileEntity).getCoilTier();
            if (coilTier <= 0)
                return;

            // each coil above cupronickel (coilTier = 0) uses 10% less energy
            ocResult.setEut(Math.max(1, (long) (ocResult.eut() * (1.0 - coilTier * 0.1))));
        }
    }
}

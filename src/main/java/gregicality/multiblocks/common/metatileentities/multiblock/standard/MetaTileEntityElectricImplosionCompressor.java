package gregicality.multiblocks.common.metatileentities.multiblock.standard;

import gregicality.multiblocks.api.metatileentity.GCYMAdvanceRecipeMapMultiblockController;
import gregicality.multiblocks.api.render.GCYMTextures;
import gregicality.multiblocks.common.block.GCYMMetaBlocks;
import gregicality.multiblocks.common.block.blocks.BlockLargeMultiblockCasing;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MetaTileEntityElectricImplosionCompressor extends GCYMAdvanceRecipeMapMultiblockController {

    public MetaTileEntityElectricImplosionCompressor(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.ELECTRIC_IMPLOSION_RECIPES);
    }

    private static IBlockState getCasingState() {
        return MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.TUNGSTENSTEEL_ROBUST);
    }

    private static IBlockState getStructureState() {
        return GCYMMetaBlocks.LARGE_MULTIBLOCK_CASING.getState(BlockLargeMultiblockCasing.CasingType.ATOMIC_CASING);
    }

    private static IBlockState getPipeState() {
        return MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.POLYTETRAFLUOROETHYLENE_PIPE);
    }

    private static IBlockState geGlassState() {
        return MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.TEMPERED_GLASS);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity metaTileEntityHolder) {
        return new MetaTileEntityElectricImplosionCompressor(this.metaTileEntityId);
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("               ", "F F         F F", "F F         F F", "F F         F F", "F F         F F", "F F         F F", "               ")
                .aisle("F F         F F", "F F   FFF   F F", "FBF  FFAFF  FBF", "FBF  AAAAA  FBF", "FBF  FFAFF  FBF", "F F   FFF   F F", "F F         F F")
                .aisle("F F         F F", "FBF  FFCFF  FBF", "F FFFEEEEEFFF F", "F FAA     AAF F", "F FFFEEEEEFFF F", "FBF  FFCFF  FBF", "F F         F F")
                .aisle("F F         F F", "FBFFFFCCCFFFFBF", "F FBBBEDEBBBF F", "F F         F F", "F FBBBEDEBBBF F", "FBFFFFCCCFFFFBF", "F F         F F")
                .aisle("F F         F F", "FBF  FFCFF  FBF", "F FFFEEEEEFFF F", "F FAA     AAF F", "F FFFEEEEEFFF F", "FBF  FFCFF  FBF", "F F         F F")
                .aisle("F F         F F", "F F   F~F   F F", "FBF  FFAFF  FBF", "FBF  AAAAA  FBF", "FBF  FFAFF  FBF", "F F   FFF   F F", "F F         F F")
                .aisle("               ", "F F         F F", "F F         F F", "F F         F F", "F F         F F", "F F         F F", "               ")
                .where('~', selfPredicate())
                .where('A', states(geGlassState()))
                .where('B', states(getCasingState()))
                .where('C', states(getPipeState()))
                .where('D', frames(Materials.Naquadah))
                .where('E', air())
                .where('F', states(getStructureState()).setMinGlobalLimited(230)
                        .or(autoAbilities())
                )
                .where(' ', any())
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GCYMTextures.ATOMIC_CASING;
    }

    @Override
    protected @NotNull OrientedOverlayRenderer getFrontOverlay() {
        return GCYMTextures.ELECTRIC_IMPLOSION_OVERLAY;
    }

    @Override
    public boolean hasMufflerMechanics() {
        return false;
    }
}

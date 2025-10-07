package gregicality.multiblocks.common.metatileentities.multiblock.standard;

import gregicality.multiblocks.api.capability.impl.GCYMMultiblockRecipeLogic;
import gregicality.multiblocks.api.metatileentity.GCYMRecipeMapMultiblockController;
import gregicality.multiblocks.api.render.GCYMTextures;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.blocks.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//这是与GCYM的转底炉 巨冰箱同一系列的设备
//此系列设备不给多线程
public class MetaTileEntityMegaChemicalReactor extends GCYMRecipeMapMultiblockController {

    public MetaTileEntityMegaChemicalReactor(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, new RecipeMap[]{RecipeMaps.CHEMICAL_RECIPES,RecipeMaps.POLYMERIZATION_RECIPES, RecipeMaps.LARGE_CHEMICAL_RECIPES});
        this.recipeMapWorkable = new GCYMMultiblockRecipeLogic(this, true);
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXXXX", "XEEEX", "XEEEX", "XEEEX", "XXXXX")
                .aisle("HPXPH", "#GGG#", "#GFG#", "#GGG#", "HPXPH")
                .aisle("HPXPH", "#GGG#", "#GFG#", "#GGG#", "HPXPH")
                .aisle("HPXPH", "#GGG#", "#GFG#", "#GGG#", "HPXPH")
                .aisle("HPXPH", "#GGG#", "#GFG#", "#GGG#", "HPXPH")
                .aisle("HPXPH", "#GGG#", "#GFG#", "#GGG#", "HPXPH")
                .aisle("HPXPH", "#GGG#", "#GFG#", "#GGG#", "HPXPH")
                .aisle("HPXPH", "#GGG#", "#GFG#", "#GGG#", "HPXPH")
                .aisle("XXXXX", "XGGGX", "XGSGX", "XGGGX", "XXXXX")
                .where('S', selfPredicate())
                .where('X', states(getCasingState()))
                .where('P', states(getPipeCasingState()))
                .where('#', air())
                .where('G', states(getGlassState()))
                .where('F', states(getCoilState()))
                .where('E', states(getCasingState())
                        .or(autoAbilities())
                        .or(abilities(MultiblockAbility.INPUT_LASER)
                                .setMaxGlobalLimited(1)))
                .where('H', states(getCasingState()))
                .build();
    }

    protected IBlockState getCasingState() {
        return MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.PTFE_INERT_CASING);
    }

    protected IBlockState getPipeCasingState() {
        return MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.POLYTETRAFLUOROETHYLENE_PIPE);
    }

    protected IBlockState getGlassState() {
        return MetaBlocks.TRANSPARENT_CASING.getState(BlockGlassCasing.CasingType.FUSION_GLASS);
    }

    protected IBlockState getCoilState() {
        return MetaBlocks.FUSION_CASING.getState(BlockFusionCasing.CasingType.FUSION_COIL);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.INERT_PTFE_CASING;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(TooltipHelper.RAINBOW_SLOW + I18n.format("gregtech.machine.perfect_oc"));
        tooltip.add(I18n.format("gregtech.machine.laser_hatch.tooltip"));
    }

    @Override
    public boolean canBeDistinct() {
        return true;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity metaTileEntityHolder) {
        return new MetaTileEntityMegaChemicalReactor(this.metaTileEntityId);
    }

    @Override
    protected @NotNull OrientedOverlayRenderer getFrontOverlay() {
        return GCYMTextures.MEGA_CHEMICAL_REACTOR;
    }
}

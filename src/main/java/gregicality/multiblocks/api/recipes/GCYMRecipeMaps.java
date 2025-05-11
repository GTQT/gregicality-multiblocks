package gregicality.multiblocks.api.recipes;

import gregtech.api.gui.GuiTextures;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMapBuilder;
import gregtech.api.recipes.builders.BlastRecipeBuilder;
import gregtech.api.recipes.builders.ImplosionRecipeBuilder;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.init.SoundEvents;

import static gregtech.api.GTValues.LV;
import static gregtech.api.GTValues.VA;

public final class GCYMRecipeMaps {

    public static final RecipeMap<BlastRecipeBuilder> ALLOY_BLAST_RECIPES = new RecipeMapBuilder<>(
            "alloy_blast_smelter", new BlastRecipeBuilder())
            .itemInputs(9)
            .itemOutputs(0)
            .fluidInputs(3)
            .fluidOutputs(1)
            .itemSlotOverlay(GuiTextures.FURNACE_OVERLAY_1,false)
            .itemSlotOverlay(GuiTextures.FURNACE_OVERLAY_1,true)
            .fluidSlotOverlay(GuiTextures.FURNACE_OVERLAY_2,false)
            .sound(GTSoundEvents.FURNACE)
            .build();
    //电力
    public static final RecipeMap<SimpleRecipeBuilder> ELECTRIC_IMPLOSION_RECIPES = new RecipeMapBuilder<>(
            "electric_implosion_recipes", new SimpleRecipeBuilder().duration(20).EUt(VA[LV]))
            .itemInputs(2)
            .itemOutputs(2)
            .itemSlotOverlay(GuiTextures.IMPLOSION_OVERLAY_1, false, true)
            .itemSlotOverlay(GuiTextures.IMPLOSION_OVERLAY_2, false, false)
            .itemSlotOverlay(GuiTextures.DUST_OVERLAY, true, true)
            .progressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE)
            .sound(SoundEvents.ENTITY_GENERIC_EXPLODE)
            .build();

    private GCYMRecipeMaps() {}
}

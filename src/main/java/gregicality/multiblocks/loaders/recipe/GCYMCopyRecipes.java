package gregicality.multiblocks.loaders.recipe;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.chance.output.ChancedOutputList;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static gregicality.multiblocks.api.recipes.GCYMRecipeMaps.ELECTRIC_IMPLOSION_RECIPES;

public class GCYMCopyRecipes {
    public static void init() {
        List<GTRecipeInput> LastItemInputs=null;

        Collection<Recipe> implosionRecipesRecipeList = RecipeMaps.IMPLOSION_RECIPES.getRecipeList();
        for (Recipe recipe : implosionRecipesRecipeList) {
            List<GTRecipeInput> itemInputs = new ArrayList<>(recipe.getInputs());
            List<ItemStack> itemOutputs = recipe.getOutputs();
            long EUt = recipe.getEUt();
            int baseDuration = recipe.getDuration();

            if (!itemInputs.isEmpty()) {
                itemInputs.remove(itemInputs.size() - 1);

                if(itemInputs==LastItemInputs)continue;
                LastItemInputs=itemInputs;
            }

            RecipeBuilder<?> builder;

            builder = ELECTRIC_IMPLOSION_RECIPES.recipeBuilder();
            builder.inputIngredients(itemInputs)
                    .outputs(itemOutputs);

            ChancedOutputList<ItemStack, ChancedItemOutput> chancedOutputs = recipe.getChancedOutputs();
            chancedOutputs.getChancedEntries().forEach(chancedItemOutput -> {
                if (chancedItemOutput.getChance() > 0) {
                    builder.chancedOutput(chancedItemOutput.getIngredient(), chancedItemOutput.getChance(), chancedItemOutput.getChanceBoost());
                }
            });

            builder.duration(baseDuration)
                    .EUt(EUt)
                    .buildAndRegister();
        }
    }
}

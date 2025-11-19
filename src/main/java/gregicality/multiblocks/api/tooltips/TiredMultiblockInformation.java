package gregicality.multiblocks.api.tooltips;

import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.util.tooltips.AbstractTooltipComponent;
import gregtech.client.utils.TooltipHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class TiredMultiblockInformation extends AbstractTooltipComponent {

    @Override
    public void addInformation(MultiblockControllerBase metaTileEntity, List<String> tooltip) {
        tooltip.add(TextFormatting.GREEN + I18n.format("gcym.tooltip.tiered_avaliable"));
        tooltip.add(TextFormatting.GRAY + I18n.format("gcym.tooltip.tiered_hatch_enabled"));
    }
}

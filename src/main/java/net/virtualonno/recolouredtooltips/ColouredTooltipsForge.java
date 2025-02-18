package net.virtualonno.recolouredtooltips;

import net.virtualonno.recolouredtooltips.config.ConfigSchema;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;

@Mod(Constants.MOD_ID)
public class ColouredTooltipsForge {

    private ConfigSchema config;

    public ColouredTooltipsForge(IEventBus modEventBus) { 
        if (FMLEnvironment.dist == Dist.CLIENT) {

            modEventBus.addListener(this::onLoadComplete);
    

            NeoForge.EVENT_BUS.addListener(this::onTooltipColor);
        }
    }
    
    private void onLoadComplete(FMLLoadCompleteEvent event) {
        this.config = ConfigSchema.load(FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + ".json").toFile());
    }

    private void onTooltipColor(RenderTooltipEvent.Color event) {
        if (this.config != null) {
            final ItemStack stack = event.getItemStack();
            ConfigSchema.ColorOptions displayColor = this.config.defaultColors;

            if (stack != null && !stack.isEmpty()) {
                for (ConfigSchema.IngredientColorOptions override : this.config.overrides) {
                    if (override.target.test(stack)) {
                        displayColor = override.color;
                        break;
                    }
                }
            }

            if (displayColor != null) {
                event.setBorderStart(displayColor.borderStart.getDecimal());
                event.setBorderEnd(displayColor.borderEnd.getDecimal());
                event.setBackground(displayColor.background.getDecimal());
            }
        }
    }
}
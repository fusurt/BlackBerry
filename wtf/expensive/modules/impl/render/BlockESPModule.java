package wtf.expensive.modules.impl.render;

import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.render.RenderUtility;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleAnnotation(name = "Block ESP", type = Type.RENDER)
public class BlockESPModule extends Module {

    private final MultiBoxSetting blocks = new MultiBoxSetting("Blocks",
            new String[]{"Chest", "Ender Chest", "Spawner", "Shulker", "Bed"});
    private final EventListener<EventDraw> onDraw = event -> {
        int colorOffset = 1;
        if (event.type == EventDraw.RenderType.RENDER) {
            colorOffset++;
            Color color = new Color(ColorUtility.getColorStyle(colorOffset));
            for (TileEntity entity : mc.world.loadedTileEntityList) {
                BlockPos pos = entity.getPos();
                if (entity instanceof TileEntityChest && blocks.get(0)) {
                    RenderUtility.drawBlockBox(pos, color.getRed(), color.getGreen(),
                            color.getBlue(), color.getAlpha());
                } else if (entity instanceof TileEntityEnderChest && blocks.get(1)) {
                    RenderUtility.drawBlockBox(pos, color.getRed(),
                            color.getGreen(), color.getBlue(),
                            color.getAlpha());
                } else if (entity instanceof TileEntityBed && blocks.get(4)) {
                    RenderUtility.drawBlockBox(pos, color.getRed(), color.getGreen(),
                            color.getBlue(), color.getAlpha());
                } else if (entity instanceof TileEntityShulkerBox && blocks.get(3)) {
                    RenderUtility.drawBlockBox(pos, color.getRed(), color.getGreen(),
                            color.getBlue(), color.getAlpha());
                } else if (entity instanceof TileEntityMobSpawner && blocks.get(2)) {
                    RenderUtility.drawBlockBox(pos, color.getRed(), color.getGreen(),
                            color.getBlue(), color.getAlpha());
                }

            }
        }
    };

}
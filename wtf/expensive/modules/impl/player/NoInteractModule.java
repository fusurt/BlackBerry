package wtf.expensive.modules.impl.player;

import net.minecraft.block.Block;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;

import java.util.ArrayList;
import java.util.List;

@ModuleAnnotation(name = "NoInteract", type = Type.PLAYER)
public class NoInteractModule extends Module {


    public static BooleanSetting allBlocks = new BooleanSetting("All Blocks", false);

    public static MultiBoxSetting ignoreInteract = new MultiBoxSetting("Object",
            new String[]{"Armor Stand", "Chest", "Door", "Button", "Hopper", "Dispenser", "Note Blocks",
                    "Crafting Table", "Trap Door", "Furnace", "Gate", "Anvil", "Lever", "Mine Cart"}, () -> !allBlocks.get());

    public static List<Block> getBlocks() {
        ArrayList<Block> blocks = new ArrayList<Block>();
        if (ignoreInteract.get(2)) {
            blocks.add(Block.getBlockById(64));
            blocks.add(Block.getBlockById(71));
            blocks.add(Block.getBlockById(193));
            blocks.add(Block.getBlockById(194));
            blocks.add(Block.getBlockById(195));
            blocks.add(Block.getBlockById(196));
            blocks.add(Block.getBlockById(197));
        }
        if (ignoreInteract.get(4)) {
            blocks.add(Block.getBlockById(154));
        }
        if (ignoreInteract.get(3)) {
            blocks.add(Block.getBlockById(77));
            blocks.add(Block.getBlockById(143));
        }
        if (ignoreInteract.get(6)) {
            blocks.add(Block.getBlockById(84));
            blocks.add(Block.getBlockById(25));
        }
        if (ignoreInteract.get(8)) {
            blocks.add(Block.getBlockById(96));
            blocks.add(Block.getBlockById(167));
        }
        if (ignoreInteract.get(9)) {
            blocks.add(Block.getBlockById(61));
            blocks.add(Block.getBlockById(62));
        }
        if (ignoreInteract.get(1)) {
            blocks.add(Block.getBlockById(130));
            blocks.add(Block.getBlockById(146));
            blocks.add(Block.getBlockById(54));
        }
        if (ignoreInteract.get(7)) {
            blocks.add(Block.getBlockById(58));
        }
        if (ignoreInteract.get(10)) {
            blocks.add(Block.getBlockById(107));
            blocks.add(Block.getBlockById(183));
            blocks.add(Block.getBlockById(184));
            blocks.add(Block.getBlockById(185));
            blocks.add(Block.getBlockById(186));
            blocks.add(Block.getBlockById(187));
        }
        if (ignoreInteract.get(11)) {
            blocks.add(Block.getBlockById(145));
        }
        if (ignoreInteract.get(5)) {
            blocks.add(Block.getBlockById(23));
        }
        if (ignoreInteract.get(12)) {
            blocks.add(Block.getBlockById(69));
        }

        if (ignoreInteract.get(13)) {
            blocks.add(Block.getBlockById(328));
            blocks.add(Block.getBlockById(342));
            blocks.add(Block.getBlockById(343));
            blocks.add(Block.getBlockById(407));
            blocks.add(Block.getBlockById(408));
            blocks.add(Block.getBlockById(422));
        }

        return blocks;
    }
}

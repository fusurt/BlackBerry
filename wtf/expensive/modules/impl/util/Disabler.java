package wtf.expensive.modules.impl.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.utility.util.ChatUtility;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleAnnotation(name = "Disabler", desc = "Disables all modules", type = Type.UTIL)
public class Disabler extends Module {
    static boolean hacked, noted;
    static Vec3d lastVecNote = new Vec3d(0, 0, 0);

    public static boolean statusDisabler() {
        boolean isRW = !mc.isSingleplayer() && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null && mc.getCurrentServerData().serverIP.equalsIgnoreCase("mc.reallyworld.ru");
        return isRW && hacked || isInHub() || !isRW || getGrief() == -1;
    }

    private final BlockPos waterNeared() {
        final float r = 6.5f;
        final float min = 5.25f;
        final float max = 6.25f;
        for (float x = -r; x < r; x++) {
            for (float y = -r; y < r; y++) {
                for (float z = -r; z < r; z++) {
                    final BlockPos pos = new BlockPos((int) mc.player.posX + x + .5f, (int) mc.player.posY + y, (int) mc.player.posZ + z + .5f);
                    if (pos != null && mc.world.getBlockState(pos).getBlock() == Blocks.WATER && (mc.world.getBlockState(pos.up()).getBlock() == Blocks.WATER || mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR) &&
                            (mc.world.getBlockState(pos.up().up()).getBlock() == Blocks.WATER || mc.world.getBlockState(pos.up().up()).getBlock() == Blocks.AIR) && mc.player.getDistanceToVec3d(new Vec3d(mc.player.posX + x + .5f, mc.player.posY + y, mc.player.posZ + z + .5f)) > min && mc.player.getDistanceToVec3d(new Vec3d(mc.player.posX + x + .5f, mc.player.posY + y, mc.player.posZ + z + .5f)) < max)
                        if (rayTrace(mc.player, pos.getX(), pos.getY(), pos.getZ()))
                            return pos;
                }
            }
        }
        return null;
    }

    private final void note() {
        float x = waterNeared().getX() + .5f;
        float y = waterNeared().getY() + .2f;
        float z = waterNeared().getZ() + .5f;
        lastVecNote = new Vec3d(x, y, z);
        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));

        ChatUtility.addChatMessage("Попытка отключить античит...");
        noted = true;
    }

    public final static boolean isInHub() {
        int points = 0;
        final List<Block> blocksList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 3; ++i)
            blocksList.add(i == 0 ? Blocks.CLAY : i == 1 ? Blocks.PACKED_ICE : Blocks.WOOL);
        for (int i = 0; i < 12; ++i) {
            final IBlockState state = mc.world.getBlockState(new BlockPos(70, 108, 252 + i));
            for (final Block block : blocksList) if (state.getBlock() == block) points++;
        }
        blocksList.clear();
        return points == 9;
    }

    public static double getDistanceAtVec3dToVec3d(Vec3d first, Vec3d second) {
        double xDiff = first.x - second.x;
        double yDiff = first.y - second.y;
        double zDiff = first.z - second.z;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }

    static int getGrief() {
        try {
            Scoreboard scoreboard = mc.world.getScoreboard();
            ScoreObjective scoreobjective = null;
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(mc.player.getName());

            if (scoreplayerteam != null) {
                int i1 = scoreplayerteam.getChatFormat().getColorIndex();
                if (i1 >= 0)
                    scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
            ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
            if (scoreobjective1 != null) {
                Scoreboard scoreboard2 = scoreobjective1.getScoreboard();
                Collection<Score> collection = scoreboard.getSortedScores(scoreobjective1);
                int j = 0;

                for (Score score1 : collection) {
                    ++j;
                    ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
                    String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
                    if (s1.contains("GRIEF-")) {
                        String griefs = ("" + Integer.parseInt(s1.substring(s1.indexOf("-")))).replace("-", "");
                        int grief = Integer.parseInt(griefs);
                        return s1.contains("MEGA") ? -2 : grief;
                    }
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return -1;
    }

    public boolean rayTrace(Entity me, double x, double y, double z) {
        return mc.world.rayTraceBlocks(new Vec3d(me.posX, me.posY, me.posZ), new Vec3d(x, y, z), false, true, false) == null || mc.world.rayTraceBlocks(new Vec3d(me.posX, me.posY + 1, me.posZ), new Vec3d(x, y + 1, z), false, true, false) == null;
    }

    public static Vec3d getEntityVecPosition(Entity entityIn) {
        return new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ);
    }

    public final EventListener<EventPacket> onPacketReceiveEvent = event -> {
        if (event.getPacketType() == EventPacket.PacketType.RECEIVE) {
            if (event.getPacket() instanceof SPacketPlayerPosLook && getGrief() != -1) {
                SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
                Vec3d packetVecFlag = new Vec3d(packet.x, packet.y, packet.z);
                Vec3d badVec = lastVecNote;
                if (getDistanceAtVec3dToVec3d(packetVecFlag, badVec) < .2 && getDistanceAtVec3dToVec3d(packetVecFlag, getEntityVecPosition(mc.player)) > 3) {
                    noted = false;
                    mc.player.ticksExisted = 0;
                    hacked = false;
                    ChatUtility.addChatMessage("Выключить античит не удалось");
                } else if (noted && !hacked) {
                    hacked = true;
                    ChatUtility.addChatMessage("Успешно.");
                }
            }
        }
    };
    public final EventListener<EventUpdate> onUpdate = event -> {
            if (mc.player.ticksExisted > 30 && !noted && !hacked && !isInHub() && waterNeared() != null) note();
            if (isInHub()) {
                noted = false;
                hacked = false;
            } else if (!noted && mc.player.ticksExisted == 11 && waterNeared() == null && !isInHub() && getGrief() != -1)
               ChatUtility.addChatMessage("Пожалуйста, подойдите к воде");
    };
}

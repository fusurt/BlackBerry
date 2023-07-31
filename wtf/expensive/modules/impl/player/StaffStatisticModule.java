package wtf.expensive.modules.impl.player;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.GameType;
import wtf.expensive.Expensive;
import wtf.expensive.command.impl.CommandStaff;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.util.OptimizationModule;
import wtf.expensive.utility.color.ColorUtility;
import wtf.expensive.utility.drag.Dragging;
import wtf.expensive.utility.font.Fonts;
import wtf.expensive.utility.math.SmartScissor;
import wtf.expensive.utility.render.GlowUtility;
import wtf.expensive.utility.render.RenderUtility;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ModuleAnnotation(name = "StaffStatistic", type = Type.PLAYER)
public class StaffStatisticModule extends Module {

    public final Dragging staffListDrag = Expensive.getInstance().createDrag(this, "staffListDrag", 5, 60);


    private static final Pattern validUserPattern = Pattern.compile("^\\w{3,16}$");
    List<String> players = new ArrayList<>();
    List<String> notSpec = new ArrayList<>();
    private final EventListener<EventUpdate> onUpdate = e -> {
        if (mc.player.ticksExisted % 10 == 0) {
            players = getVanish();
            notSpec = getOnlinePlayerD();
            players.sort(String::compareTo);
            notSpec.sort(String::compareTo);
        }
    };
    private final EventListener<EventDraw> onDraw = e -> {
        if (e.type == EventDraw.RenderType.DISPLAY) {
            if (players.isEmpty() && notSpec.isEmpty()) return;
            Expensive.instance.getScaleMath().pushScale();
            List<String> all = new ArrayList<>();
            all.addAll(players);
            all.addAll(notSpec);


            float width = all.stream().max(Comparator.comparingDouble(Fonts.verdana14::getStringWidth)).map(Fonts.verdana14::getStringWidth).get().floatValue() + 50;


            float height2 = (notSpec.size() + players.size()) * 14 + 10;

            float x = staffListDrag.getX();
            float y = staffListDrag.getY();

            staffListDrag.setWidth(width);
            staffListDrag.setHeight(height2);
            SmartScissor.push();
            OptimizationModule optimizationModule = (OptimizationModule) Expensive.getInstance().getModuleManager().get(OptimizationModule.class);
            if ((optimizationModule != null && optimizationModule.state && optimizationModule.shaders.get()))
            //глоу стафф стата
                GlowUtility.drawGlowHorizontal(x, y, width, height2, 2, ColorUtility.getColor(255, 255, 255, 255), ColorUtility.getColor(255, 255, 255, 255));
            GlowUtility.drawGlowHorizontal(x, y, width, height2, 15, ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(280)), 255).getRGB(), ColorUtility.applyOpacity(new Color(Expensive.getInstance().getModuleManager().arraylist.getColor(1)), 255).getRGB());
            Fonts.vag14.drawCenteredString("Staff Statistic", x + width / 2, y + 5, ColorUtility.rgba(255, 255, 255, 255));
            if (height2 > 10) {
                int staffY = 11;
                for (String player : all) {
                    Fonts.verdana14.drawStringWithShadow(player.split(":")[0], x + 3, y + 4 + staffY, -1);
                    Fonts.verdana14.drawStringWithShadow(player.split(":")[1].equalsIgnoreCase("vanish") ? ChatFormatting.RED + "VANISH" : player.split(":")[1].equalsIgnoreCase("gm3") ? ChatFormatting.RED + "VANISH " + ChatFormatting.YELLOW + "(GM 3)" : ChatFormatting.GREEN + "ACTIVE", x + width - Fonts.verdana14.getStringWidth(player.split(":")[1].equalsIgnoreCase("vanish") ? ChatFormatting.RED + "VANISH" : player.split(":")[1].equalsIgnoreCase("gm3") ? ChatFormatting.RED + "VANISH " + ChatFormatting.YELLOW + "(GM 3)" : ChatFormatting.GREEN + "ACTIVE") - 2, y + staffY + 4, ColorUtility.rgba(255, 255, 255, 255));
                    staffY += 13;
                }
            }
            SmartScissor.unset();
            SmartScissor.pop();
            Expensive.instance.getScaleMath().popScale();
        }
    };
    public static List<String> getOnlinePlayer() {
        return mc.player.connection.getPlayerInfoMap().stream()
                .map(NetworkPlayerInfo::getGameProfile)
                .map(GameProfile::getName)
                .filter(profileName -> validUserPattern.matcher(profileName).matches())
                .collect(Collectors.toList());
    }

    // StaffCommand equals
    public static List<String> getOnlinePlayerD() {
        List<String> S = new ArrayList<>();
        for (NetworkPlayerInfo player : mc.player.connection.getPlayerInfoMap()) {
            if (mc.isSingleplayer() || player.getPlayerTeam() == null) break;
            String prefix = player.getPlayerTeam().getColorPrefix();

            if (check(ChatFormatting.stripFormatting(prefix).toLowerCase())
                    || CommandStaff.staffNames.toString().toLowerCase().contains(player.getGameProfile().getName().toLowerCase())
                    || player.getGameProfile().getName().toLowerCase().contains("1danil_mansoru1") || player.getPlayerTeam().getColorPrefix().contains("YT")) {
                String name = Arrays.asList(player.getPlayerTeam().getMembershipCollection().stream().toArray()).toString().replace("[", "").replace("]", "");

                if (player.getGameType() == GameType.SPECTATOR) {
                    S.add(player.getPlayerTeam().getColorPrefix() + name + ":gm3");
                    continue;
                }
                S.add(player.getPlayerTeam().getColorPrefix() + name + ":active");
            }
        }
        return S;
    }

    public List<String> getVanish() {
        List<String> list = new ArrayList<>();
        for (ScorePlayerTeam s : mc.world.getScoreboard().getTeams()) {
            if (s.getColorPrefix().length() == 0 || mc.isSingleplayer()) continue;
            String name = Arrays.asList(s.getMembershipCollection().stream().toArray()).toString().replace("[", "").replace("]", "");

            if (getOnlinePlayer().contains(name) || name.isEmpty())
                continue;
            if (CommandStaff.staffNames.toString().toLowerCase().contains(name.toLowerCase())
                    && check(s.getColorPrefix().toLowerCase())
                    || check(s.getColorPrefix().toLowerCase())
                    || name.toLowerCase().contains("1danil_mansoru1") || s.getColorPrefix().contains("YT"))
                list.add(s.getColorPrefix() + name + ":vanish");
        }
        return list;
    }

    public static boolean check(String name) {
        return name.contains("helper") || name.contains("moder") || name.contains("admin") || name.contains("owner") || name.contains("curator") || name.contains("������") || name.contains("�����") || name.contains("�����") || name.contains("�������");
    }

}

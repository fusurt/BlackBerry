package wtf.expensive.command.impl;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;

@CommandAnnotation(name = "rct", description = "Перезаходит на гриф")

public class CommandRCT extends Command {
    @Override
    public void error() {
        sendMessage("WTF");
    }

    @Override
    public void run(String[] args) {
        if (args.length == 1) {
            int server = -1;
            for (Score team : mc.world.getScoreboard().getScores()) {
                ScorePlayerTeam scoreplayerteam = mc.world.getScoreboard().getPlayersTeam(team.getPlayerName());
                String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam, team.getPlayerName());
                if (s1.contains("GRIEF-")) {
                    String board = s1.split("GRIEF-")[1];
                    server = Integer.parseInt(board);
                    break;
                }
            }

            if (server == -1) {
                sendMessage("Не удалось получить номер сервера");
                return;
            }


            int finalServer = server;
            new Thread(() -> {
                mc.player.sendChatMessage("/hub");
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mc.player.sendChatMessage("/grief-" + finalServer);
            }).start();
        } else {
            error();
        }
    }
}

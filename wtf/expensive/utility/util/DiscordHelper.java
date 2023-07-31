package wtf.expensive.utility.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import wtf.expensive.Expensive;
import wtf.expensive.utility.Utility;

public class DiscordHelper implements Utility {
    private static final String discordID = "1071483095796175009";
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);
        DiscordHelper.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        DiscordHelper.discordRichPresence.largeImageKey = "f52befvkxoy_1_";
        DiscordHelper.discordRichPresence.largeImageText = "vk.com/expensiveclient";
        new Thread(() -> {
            while (true) {
                try {
                    DiscordHelper.discordRichPresence.details = "User: " + Expensive.profile.getName() + " | " + "UID: " + Expensive.profile.getUID();
                    DiscordHelper.discordRichPresence.state = "Build: " + Expensive.getInstance().VERSION + " (Beta)";
                    discordRPC.Discord_UpdatePresence(discordRichPresence);
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
    }
}

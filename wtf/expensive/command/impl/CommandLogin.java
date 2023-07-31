package wtf.expensive.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.Session;
import org.apache.commons.lang3.RandomStringUtils;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandAnnotation;

import java.util.UUID;

@CommandAnnotation(name = "l", description = "Позволяет сменить ник прямо во время игры")
public class CommandLogin extends Command {

    @Override
    public void run(String[] args) {
        super.run(args);
        String username;
        if (args[1].equalsIgnoreCase("rand")) {
            username = "EXPRel" + RandomStringUtils.randomAlphabetic(5);
        } else if (args.length == 2) {
            username = args[1];
        } else {
            error();
            return;
        }
        String uuid = UUID.randomUUID().toString();
        mc.session = new Session(username, uuid, "", "mojang");
        sendMessage(ChatFormatting.GREEN + "Вы успешно вошли как " + ChatFormatting.WHITE + mc.session.getUsername());
    }

    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");

        sendMessage(ChatFormatting.WHITE + ".l " + ChatFormatting.GRAY + "<"
                + "name" + ChatFormatting.GRAY + ">");

        sendMessage(ChatFormatting.WHITE + ".l rand" + ChatFormatting.GRAY + " (Генерирует случайное имя)");
    }
}

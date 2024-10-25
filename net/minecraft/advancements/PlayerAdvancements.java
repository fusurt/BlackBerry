package net.minecraft.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerAdvancements
{
    private static final Logger field_192753_a = LogManager.getLogger();
    private static final Gson field_192754_b = (new GsonBuilder()).registerTypeAdapter(AdvancementProgress.class, new AdvancementProgress.Serializer()).registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).setPrettyPrinting().create();
    private static final TypeToken<Map<ResourceLocation, AdvancementProgress>> field_192755_c = new TypeToken<Map<ResourceLocation, AdvancementProgress>>()
    {
    };
    private final MinecraftServer field_192756_d;
    private final File field_192757_e;
//    private final Map<Advancement, AdvancementProgress> field_192758_f = Maps.<Advancement, AdvancementProgress>newLinkedHashMap();
//    private final Set<Advancement> field_192759_g = Sets.<Advancement>newLinkedHashSet();
//    private final Set<Advancement> field_192760_h = Sets.<Advancement>newLinkedHashSet();
//    private final Set<Advancement> field_192761_i = Sets.<Advancement>newLinkedHashSet();
    private EntityPlayerMP field_192762_j;
    @Nullable
    private Advancement field_194221_k;
    private boolean field_192763_k = true;

    public PlayerAdvancements(MinecraftServer p_i47422_1_, File p_i47422_2_, EntityPlayerMP p_i47422_3_)
    {
        this.field_192756_d = p_i47422_1_;
        this.field_192757_e = p_i47422_2_;
        this.field_192762_j = p_i47422_3_;
        this.func_192740_f();
    }

    public void func_192739_a(EntityPlayerMP p_192739_1_)
    {
        this.field_192762_j = p_192739_1_;
    }

    public void func_192745_a()
    {
//        for (ICriterionTrigger<?> icriteriontrigger : CriteriaTriggers.func_192120_a())
//        {
//            icriteriontrigger.func_192167_a(this);
//        }
    }

    public void func_193766_b()
    {
//        this.func_192745_a();
//        this.field_192763_k = true;
//        this.field_194221_k = null;
//        this.func_192740_f();
    }

    private void func_192751_c()
    {
        for (Advancement advancement : this.field_192756_d.func_191949_aK().func_192780_b())
        {
            this.func_193764_b(advancement);
        }
    }

    private void func_192752_d()
    {

    }

    private void func_192748_e()
    {
        for (Advancement advancement : this.field_192756_d.func_191949_aK().func_192780_b())
        {
            if (advancement.func_192073_f().isEmpty())
            {
                this.func_192750_a(advancement, "");
                advancement.func_192072_d().func_192113_a(this.field_192762_j);
            }
        }
    }

    private void func_192740_f()
    {
//        if (this.field_192757_e.isFile())
//        {
//            try
//            {
//                String s = Files.toString(this.field_192757_e, StandardCharsets.UTF_8);
//                Map<ResourceLocation, AdvancementProgress> map = (Map)JsonUtils.func_193840_a(field_192754_b, s, field_192755_c.getType());
//
//                if (map == null)
//                {
//                    throw new JsonParseException("Found null for advancements");
//                }
//
//                Stream<Entry<ResourceLocation, AdvancementProgress>> stream = map.entrySet().stream().sorted(Comparator.comparing(Entry::getValue));
//
//                for (Entry<ResourceLocation, AdvancementProgress> entry : stream.collect(Collectors.toList()))
//                {
//                    Advancement advancement = this.field_192756_d.func_191949_aK().func_192778_a(entry.getKey());
//
//                    if (advancement == null)
//                    {
//                        field_192753_a.warn("Ignored advancement '" + entry.getKey() + "' in progress file " + this.field_192757_e + " - it doesn't exist anymore?");
//                    }
//                    else
//                    {
//                        this.func_192743_a(advancement, entry.getValue());
//                    }
//                }
//            }
//            catch (JsonParseException jsonparseexception)
//            {
//                field_192753_a.error("Couldn't parse player advancements in " + this.field_192757_e, (Throwable)jsonparseexception);
//            }
//            catch (IOException ioexception)
//            {
//                field_192753_a.error("Couldn't access player advancements in " + this.field_192757_e, (Throwable)ioexception);
//            }
//        }
//
//        this.func_192748_e();
//        this.func_192752_d();
//        this.func_192751_c();
    }

    public void func_192749_b()
    {

    }

    public boolean func_192750_a(Advancement p_192750_1_, String p_192750_2_)
    {


        return false;
    }

    public boolean func_192744_b(Advancement p_192744_1_, String p_192744_2_)
    {
        return false;
    }

    private void func_193764_b(Advancement p_193764_1_)
    {
//        AdvancementProgress advancementprogress = this.func_192747_a(p_193764_1_);
//
//        if (!advancementprogress.func_192105_a())
//        {
//            for (Entry<String, Criterion> entry : p_193764_1_.func_192073_f().entrySet())
//            {
//                CriterionProgress criterionprogress = advancementprogress.func_192106_c(entry.getKey());
//
//                if (criterionprogress != null && !criterionprogress.func_192151_a())
//                {
//                    ICriterionInstance icriterioninstance = ((Criterion)entry.getValue()).func_192143_a();
//
//                    if (icriterioninstance != null)
//                    {
//                        ICriterionTrigger<ICriterionInstance> icriteriontrigger = CriteriaTriggers.<ICriterionInstance>func_192119_a(icriterioninstance.func_192244_a());
//
//                        if (icriteriontrigger != null)
//                        {
//                            icriteriontrigger.func_192165_a(this, new ICriterionTrigger.Listener(icriterioninstance, p_193764_1_, entry.getKey()));
//                        }
//                    }
//                }
//            }
//        }
    }

    private void func_193765_c(Advancement p_193765_1_)
    {
//        AdvancementProgress advancementprogress = this.func_192747_a(p_193765_1_);
//
//        for (Entry<String, Criterion> entry : p_193765_1_.func_192073_f().entrySet())
//        {
//            CriterionProgress criterionprogress = advancementprogress.func_192106_c(entry.getKey());
//
//            if (criterionprogress != null && (criterionprogress.func_192151_a() || advancementprogress.func_192105_a()))
//            {
//                ICriterionInstance icriterioninstance = ((Criterion)entry.getValue()).func_192143_a();
//
//                if (icriterioninstance != null)
//                {
//                    ICriterionTrigger<ICriterionInstance> icriteriontrigger = CriteriaTriggers.<ICriterionInstance>func_192119_a(icriterioninstance.func_192244_a());
//
//                    if (icriteriontrigger != null)
//                    {
//                        icriteriontrigger.func_192164_b(this, new ICriterionTrigger.Listener(icriterioninstance, p_193765_1_, entry.getKey()));
//                    }
//                }
//            }
//        }
    }

    public void func_192741_b(EntityPlayerMP p_192741_1_)
    {

    }

    public void func_194220_a(@Nullable Advancement p_194220_1_)
    {
//        Advancement advancement = this.field_194221_k;
//
//        if (p_194220_1_ != null && p_194220_1_.func_192070_b() == null && p_194220_1_.func_192068_c() != null)
//        {
//            this.field_194221_k = p_194220_1_;
//        }
//        else
//        {
//            this.field_194221_k = null;
//        }
//
//        if (advancement != this.field_194221_k)
//        {
//            this.field_192762_j.connection.sendPacket(new SPacketSelectAdvancementsTab(this.field_194221_k == null ? null : this.field_194221_k.func_192067_g()));
//        }
    }

    public AdvancementProgress func_192747_a(Advancement p_192747_1_)
    {

        return null;
    }

    private void func_192743_a(Advancement p_192743_1_, AdvancementProgress p_192743_2_)
    {

    }

    private void func_192742_b(Advancement p_192742_1_)
    {

    }

    private boolean func_192738_c(Advancement p_192738_1_)
    {
        for (int i = 0; p_192738_1_ != null && i <= 2; ++i)
        {
            if (i == 0 && this.func_192746_d(p_192738_1_))
            {
                return true;
            }

            if (p_192738_1_.func_192068_c() == null)
            {
                return false;
            }

            AdvancementProgress advancementprogress = this.func_192747_a(p_192738_1_);

            if (advancementprogress.func_192105_a())
            {
                return true;
            }

            if (p_192738_1_.func_192068_c().func_193224_j())
            {
                return false;
            }

            p_192738_1_ = p_192738_1_.func_192070_b();
        }

        return false;
    }

    private boolean func_192746_d(Advancement p_192746_1_)
    {
//        AdvancementProgress advancementprogress = this.func_192747_a(p_192746_1_);
//
//        if (advancementprogress.func_192105_a())
//        {
//            return true;
//        }
//        else
//        {
//            for (Advancement advancement : p_192746_1_.func_192069_e())
//            {
//                if (this.func_192746_d(advancement))
//                {
//                    return true;
//                }
//            }
//
//            return false;
//        }
        return false;
    }
}

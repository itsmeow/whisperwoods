package dev.itsmeow.whisperwoods.util;

import java.util.HashSet;
import java.util.Set;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = WhisperwoodsMod.MODID, value = Dist.CLIENT)
public class WWClientTaskQueue {

    private static final Set<MutablePair<Integer, Runnable>> TASK_QUEUE = new HashSet<MutablePair<Integer, Runnable>>();
    private static final Set<MutablePair<Integer, Runnable>> REMOVE_NEXT = new HashSet<MutablePair<Integer, Runnable>>();

    public static void schedule(int ticks, Runnable run) {
        TASK_QUEUE.add(new MutablePair<>(ticks, run));
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent event) {
        TASK_QUEUE.forEach(p -> {
            int ticksLeft = p.getLeft();
            if(ticksLeft <= 0) {
                REMOVE_NEXT.add(p);
                p.getRight().run();
            }
            p.setLeft(ticksLeft - 1);
        });
        TASK_QUEUE.removeIf(p -> REMOVE_NEXT.contains(p));
        REMOVE_NEXT.clear();
    }

}

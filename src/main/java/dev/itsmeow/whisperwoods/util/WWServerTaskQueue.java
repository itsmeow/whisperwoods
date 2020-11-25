package dev.itsmeow.whisperwoods.util;

import com.google.common.collect.ImmutableSet;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = WhisperwoodsMod.MODID)
public class WWServerTaskQueue {

    private static final Set<MutablePair<Integer, Runnable>> TASK_QUEUE = new HashSet<MutablePair<Integer, Runnable>>();

    public static void schedule(int ticks, Runnable run) {
        synchronized (TASK_QUEUE) {
            TASK_QUEUE.add(new MutablePair<>(ticks, run));
        }
    }

    @SubscribeEvent
    public static void serverTick(ServerTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            synchronized (TASK_QUEUE) {
                for(MutablePair<Integer, Runnable> p : ImmutableSet.copyOf(TASK_QUEUE)) {
                    if (p.getLeft() <= 0) {
                        TASK_QUEUE.remove(p);
                        p.getRight().run();
                    } else {
                        p.setLeft(p.getLeft() - 1);
                    }
                }
            }
        }
    }

}

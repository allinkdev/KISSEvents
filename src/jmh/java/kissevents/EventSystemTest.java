package kissevents;

import com.github.allinkdev.kissevents.EventSystem;
import com.github.allinkdev.kissevents.Listener;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class EventSystemTest implements Listener<EventSystemTest.BlaBlaEvent> {
    @Benchmark
    public void benchmarkSynchronisedEventPost() {
        EventSystem.SYNCHRONISED.registerListener(this);
        EventSystem.SYNCHRONISED.post(new BlaBlaEvent());
    }

    @Benchmark
    public void benchmarkUnsynchronisedEventPost() {
        EventSystem.UNSYNCHRONISED.registerListener(this);
        EventSystem.UNSYNCHRONISED.post(new BlaBlaEvent());
    }

    @Override
    public Class<? extends BlaBlaEvent> getType() {
        return BlaBlaEvent.class;
    }

    @Override
    public void accept(final BlaBlaEvent blaBlaEvent) {

    }

    public static final class BlaBlaEvent {

    }
}

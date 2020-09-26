package front.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

public class DebugListener implements javax.faces.event.PhaseListener {

    private Logger log = LogManager.getRootLogger();

    @Override
    public void afterPhase(PhaseEvent phaseEvent) {

        if (log.isInfoEnabled()) {
            log.info("AFTER PHASE: {}", phaseEvent.getPhaseId());
        }
    }

    @Override
    public void beforePhase(PhaseEvent phaseEvent) {
        if (log.isInfoEnabled()) {
            log.info("BEFORE PHASE: {}", phaseEvent.getPhaseId());
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}

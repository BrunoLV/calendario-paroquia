package br.com.nsfatima.calendario.integration.metrics;

import br.com.nsfatima.calendario.infrastructure.observability.WeeklyMetricsSnapshotJob;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeeklyMetricsSnapshotIntegrationTest {

    @Test
    void shouldProduceWeeklySnapshot() {
        WeeklyMetricsSnapshotJob job = new WeeklyMetricsSnapshotJob();
        assertEquals("ok", job.snapshot().get("status"));
    }
}

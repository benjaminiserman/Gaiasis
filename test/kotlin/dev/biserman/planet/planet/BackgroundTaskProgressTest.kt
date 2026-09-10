package dev.biserman.planet.planet

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BackgroundTaskProgressTest {
    @Test
    fun `progress snapshots are clamped and visible across threads`() {
        val progress = BackgroundTaskProgress()

        val updater = Thread { progress.update(1.5, "Finished") }
        updater.start()
        updater.join()

        assertEquals(BackgroundTaskProgress.Snapshot(1.0, "Finished"), progress.snapshot())

        progress.update(-0.5, "Restarted")
        assertEquals(BackgroundTaskProgress.Snapshot(0.0, "Restarted"), progress.snapshot())
    }
}

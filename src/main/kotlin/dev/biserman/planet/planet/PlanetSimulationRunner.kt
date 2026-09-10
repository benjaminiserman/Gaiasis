package dev.biserman.planet.planet

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

class BackgroundTaskProgress {
    data class Snapshot(val fraction: Double, val status: String)

    private val current = AtomicReference(Snapshot(0.0, "Starting…"))

    fun update(fraction: Double, status: String) {
        current.set(Snapshot(fraction.coerceIn(0.0, 1.0), status))
    }

    fun snapshot(): Snapshot = current.get()
}

/**
 * Gives a background coroutine exclusive ownership of a planet for one
 * simulation step. The main thread must not read mutable planet state until
 * [poll] returns the completion.
 */
class PlanetSimulationRunner : AutoCloseable {
    sealed interface Completion {
        data class Success(val planet: Planet) : Completion

        data class Failure(val cause: Throwable) : Completion
    }

    private val scope = CoroutineScope(
        SupervisorJob() +
            Dispatchers.Default.limitedParallelism(1) +
            CoroutineName("planet-simulation"),
    )
    private val completions = Channel<Completion>(capacity = 1)

    fun submit(
        planet: Planet,
        simulation: (Planet) -> Unit,
    ) {
        scope.launch {
            val completion = try {
                simulation(planet)
                Completion.Success(planet)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (cause: Throwable) {
                Completion.Failure(cause)
            }
            completions.send(completion)
        }
    }

    fun poll(): Completion? = completions.tryReceive().getOrNull()

    override fun close() {
        scope.cancel()
        completions.close()
    }
}

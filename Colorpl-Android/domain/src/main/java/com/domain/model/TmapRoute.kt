data class TmapRoute(
    val totalDistance: Int,
    val totalTime: Int,
    val totalWalkDistance: Int,
    val totalWalkTime: Int,
    val transferCount: Int,
    val totalFare: Int,
    val currency: String,
    val legs: List<Leg>
) {
    data class Leg(
        val distance: Int,
        val mode: String,
        val sectionTime: Int,
        val startName: String,
        val startLat: Double,
        val startLon: Double,
        val endName: String,
        val endLat: Double,
        val endLon: Double,
        val routeColor: String?,
        val route: String?,
        val passStops: List<PassStop>,
        val steps: List<Step>
    )

    data class PassStop(
        val name: String,
        val lat: Double,
        val lon: Double
    )

    data class Step(
        val description: String,
        val distance: Int,
        val streetName: String
    )
}
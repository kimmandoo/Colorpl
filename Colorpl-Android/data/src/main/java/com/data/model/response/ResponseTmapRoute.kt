package com.data.model.response


data class ResponseTmapRoute(
    val metaData: MetaData?
) {
    data class MetaData(
        val plan: Plan?,
        val requestParameters: RequestParameters
    ) {
        data class Plan(
            val itineraries: List<Itinerary>
        ) {
            data class Itinerary(
                val fare: Fare,
                val legs: List<Leg>,
                val pathType: Int,
                val totalDistance: Int,
                val totalTime: Int,
                val totalWalkDistance: Int,
                val totalWalkTime: Int,
                val transferCount: Int
            ) {
                data class Fare(
                    val regular: Regular
                ) {
                    data class Regular(
                        val currency: Currency,
                        val totalFare: Int
                    ) {
                        data class Currency(
                            val currency: String,
                            val currencyCode: String,
                            val symbol: String
                        )
                    }
                }

                data class Leg(
                    val distance: Int,
                    val end: End,
                    val lane: List<Lane>? = null,
                    val mode: String,
                    val passShape: PassShape? = null,
                    val passStopList: PassStopList? = null,
                    val route: String? = null,
                    val routeColor: String? = null,
                    val routeId: String? = null,
                    val sectionTime: Int,
                    val service: Int? = null,
                    val start: Start,
                    val steps: List<Step>? = null,
                    val type: Int? = null,
                ) {
                    data class End(
                        val lat: Double,
                        val lon: Double,
                        val name: String
                    )

                    data class Lane(
                        val route: String,
                        val routeColor: String,
                        val routeId: String,
                        val service: Int,
                        val type: Int
                    )

                    data class PassShape(
                        val linestring: String
                    )

                    data class PassStopList(
                        val stationList: List<Station>
                    ) {
                        data class Station(
                            val index: Int,
                            val lat: String,
                            val lon: String,
                            val stationID: String,
                            val stationName: String
                        )
                    }

                    data class Start(
                        val lat: Double,
                        val lon: Double,
                        val name: String
                    )

                    data class Step(
                        val description: String,
                        val distance: Int,
                        val linestring: String,
                        val streetName: String
                    )
                }
            }
        }

        data class RequestParameters(
            val airplaneCount: Int,
            val busCount: Int,
            val endX: String,
            val endY: String,
            val expressbusCount: Int,
            val ferryCount: Int,
            val locale: String,
            val reqDttm: String,
            val startX: String,
            val startY: String,
            val subwayBusCount: Int,
            val subwayCount: Int,
            val trainCount: Int,
            val wideareaRouteCount: Int
        )
    }
}
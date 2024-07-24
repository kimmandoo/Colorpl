package com.domain.mapper

import TmapRoute
import com.data.model.response.ResponseTmapRoute


fun ResponseTmapRoute.toEntity(): TmapRoute {
    val itinerary = this.metaData.plan.itineraries.firstOrNull() ?: throw NoSuchElementException("No itinerary found")

    return TmapRoute(
        totalDistance = itinerary.totalDistance,
        totalTime = itinerary.totalTime,
        totalWalkDistance = itinerary.totalWalkDistance,
        totalWalkTime = itinerary.totalWalkTime,
        transferCount = itinerary.transferCount,
        totalFare = itinerary.fare.regular.totalFare,
        currency = itinerary.fare.regular.currency.currency,
        legs = itinerary.legs.map { leg ->
            TmapRoute.Leg(
                distance = leg.distance,
                mode = leg.mode,
                sectionTime = leg.sectionTime,
                startName = leg.start.name,
                startLat = leg.start.lat,
                startLon = leg.start.lon,
                endName = leg.end.name,
                endLat = leg.end.lat,
                endLon = leg.end.lon,
                routeColor = leg.routeColor,
                route = leg.route,
                passStops = leg.passStopList?.stationList?.map { station ->
                    TmapRoute.PassStop(
                        name = station.stationName,
                        lat = station.lat.toDouble(),
                        lon = station.lon.toDouble()
                    )
                } ?: emptyList(),
                steps = leg.steps?.map { step ->
                    TmapRoute.Step(
                        description = step.description,
                        distance = step.distance,
                        streetName = step.streetName
                    )
                } ?: emptyList()
            )
        }
    )
}
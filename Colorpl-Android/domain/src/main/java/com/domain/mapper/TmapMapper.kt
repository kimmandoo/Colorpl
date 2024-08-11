package com.domain.mapper

import com.data.model.response.ResponseTmapRoute
import com.domain.model.Leg
import com.domain.model.Route
import com.domain.model.Step


fun ResponseTmapRoute.toEntity(): Route? {
    val itinerary = this.metaData?.plan?.itineraries?.firstOrNull()
    return itinerary?.let {
        Route(
        totalDistance = it.totalDistance,
        totalTime = itinerary.totalTime,
        totalWalkDistance = itinerary.totalWalkDistance,
        totalWalkTime = itinerary.totalWalkTime,
        transferCount = itinerary.transferCount,
        totalFare = itinerary.fare.regular.totalFare,
        currency = itinerary.fare.regular.currency.currency,
        legs = itinerary.legs.map { leg ->
            Leg(
                distance = leg.distance,
                mode = leg.mode,
                sectionTime = leg.sectionTime,
                startName = leg.start.name,
                endName = leg.end.name,
                passShape = leg.passShape?.linestring,
                steps = leg.steps?.map { step ->
                    Step(
                        description = step.description,
                        distance = step.distance,
                        linestring = step.linestring,
                        streetName = step.streetName
                    )
                }
            )
        }
    )
    }
}
package br.com.marcosprado.timesbackend.dto.analytics.response;

import java.util.List;

public record SalesVolumeResponse(
        List<String> dates,
        List<SeriesData> series
) {
}

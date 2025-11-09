package br.com.marcosprado.timesbackend.dto.analytics.response;

import java.util.List;

public record SeriesData(
        String name,
        List<Long> values
) {
}

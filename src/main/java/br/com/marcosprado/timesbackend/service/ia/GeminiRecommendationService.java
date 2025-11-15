package br.com.marcosprado.timesbackend.service.ia;

import br.com.marcosprado.timesbackend.dto.iaRecommendation.request.ProductRecommendationRequest;
import br.com.marcosprado.timesbackend.dto.iaRecommendation.response.ProductRecommendationResponse;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class GeminiRecommendationService implements IARecommendation {
    private final String systemInstruction;

    public GeminiRecommendationService() {
        this.systemInstruction = this.getSystemInstruction();
    }

    private String getSystemInstruction() {
        try {
            ClassPathResource resource = new ClassPathResource("instructions.txt");
            InputStream inputStream = resource.getInputStream();

            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao carregar instruções do sistema", e);
        }
    }

    @Override
    public ProductRecommendationResponse getProductRecommendation(ProductRecommendationRequest request) {
        Client client = new Client();

        GenerateContentConfig config =
                GenerateContentConfig.builder()
                        .systemInstruction(
                                Content.fromParts(Part.fromText(systemInstruction)))
                        .build();

        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                request.toJson(),
                config
        );

        String jsonResponse = response.text();
        jsonResponse = cleanJsonResponse(jsonResponse);

        if (jsonResponse == null || jsonResponse.isBlank()) {
            return createEmptyResponse(request.customer().id());
        }

        try {
            return ProductRecommendationResponse.fromJson(jsonResponse);
        } catch (Exception e) {
            return createEmptyResponse(request.customer().id());
        }
    }

    private String cleanJsonResponse(String response) {
        if (response == null) {
            return null;
        }

        return response
                .replaceAll("```json\\s*", "")
                .replaceAll("```\\s*", "")
                .trim();
    }

    private ProductRecommendationResponse createEmptyResponse(Integer customerId) {
        return new ProductRecommendationResponse(
                customerId,
                List.of(),
                0
        );
    }
}
package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.NutritionPlanDTOs.NutritionPlanResponse;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import lombok.RequiredArgsConstructor;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class NutritionPdfService {

    private final TemplateEngine templateEngine;

    public byte[] generateNutritionPlanPdf(NutritionPlanResponse plan) {
        Context context = new Context();
        context.setVariable("plan", plan);

        String html = templateEngine.process("nutrition-plan", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate nutrition PDF", e);
        }
    }
}
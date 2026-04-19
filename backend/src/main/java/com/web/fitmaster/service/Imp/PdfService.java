package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.WorkoutPlanDTOs;
import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generateWorkoutPlanPdf(WorkoutPlanDTOs.WorkoutPlanResponse plan) {
        // 1. حضّر الـ Thymeleaf context
        Context context = new Context();
        context.setVariable("plan", plan);

        // 2. حول الـ template لـ HTML string
        String html = templateEngine.process("workout-plan", context);

        // 3. حول الـ HTML لـ PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
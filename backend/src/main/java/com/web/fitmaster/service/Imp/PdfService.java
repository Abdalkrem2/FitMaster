package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.PdfWorkoutPlanDTO.*;
import com.web.fitmaster.dto.WorkoutPlanDTOs.*;
import com.lowagie.text.DocumentException;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generateWorkoutPlanPdf(WorkoutPlanResponse plan) {
        // 1. حول الـ WorkoutPlanResponse لـ PdfPlanResponse
        PdfPlanResponse pdfPlan = toPdfPlan(plan);

        // 2. حضّر الـ Thymeleaf context
        Context context = new Context();
        context.setVariable("plan", pdfPlan);

        // 3. حول الـ template لـ HTML string
        String html = templateEngine.process("workout-plan", context);

        // 4. حول الـ HTML لـ PDF
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

    // ─── Mapping ────────────────────────────────────────────────────────────

    private PdfPlanResponse toPdfPlan(WorkoutPlanResponse plan) {
        return PdfPlanResponse.builder()
                .name(plan.getName())
                .splitType(plan.getSplitType())
                .goal(plan.getGoal())
                .level(plan.getLevel())
                .days(plan.getDays().stream()
                        .map(this::toPdfDay)
                        .toList())
                .build();
    }

    private PdfDayResponse toPdfDay(WorkoutDayResponse day) {
        return PdfDayResponse.builder()
                .dayNumber(day.getDayNumber())
                .muscleGroupLabel(day.getMuscleGroupLabel())
                .exercises(day.getExercises().stream()
                        .map(this::toPdfExercise)
                        .toList())
                .build();
    }

    private PdfExerciseResponse toPdfExercise(WorkoutExerciseResponse ex) {
        return PdfExerciseResponse.builder()
                .orderIndex(ex.getOrderIndex())
                .exerciseName(ex.getExerciseName())
                .imageBase64(toBase64Image(ex.getImageUrl()))
                .instructions(ex.getInstructions())
                .sets(ex.getSets())
                .reps(ex.getReps())
                .repsMax(ex.getRepsMax())
                .build();
    }

    // ─── Image ──────────────────────────────────────────────────────────────

    private String toBase64Image(String imageUrl) {
        if (imageUrl == null) return "";
        try {
            URL url = new URL(imageUrl);
            BufferedImage original = ImageIO.read(url);
            BufferedImage resized = Scalr.resize(original, 120);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resized, "jpg", baos);
            String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
            return "data:image/jpeg;base64," + base64;
        } catch (Exception e) {
            return "";
        }
    }
}
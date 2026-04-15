package com.web.fitmaster.model.exercise;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "media_assets")
@Getter
@Setter
@NoArgsConstructor
public class MediaAsset {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private byte[] id;

    @Column(length = 500)
    private String url;

    @Column(length = 50)
    private String type;

    @Column(name = "license_type", length = 50)
    private String licenseType;

    @Column(name = "attribution_text", length = 500)
    private String attributionText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

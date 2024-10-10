package com.businessapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrganizationNodeDTO {
    private String type;
    private OrganizationDataDTO data;  // Alt veri yapısı
    private List<OrganizationNodeDTO> children;
    private boolean expanded;

    // Getter ve Setter metodları
}
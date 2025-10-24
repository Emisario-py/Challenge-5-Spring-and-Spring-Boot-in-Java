package org.digitalnao.meli.dto.client;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateClientRequest {

    private Long id_client;

    @NotBlank
    private String clientName;

    @NotBlank
    private String address;

    @NotNull
    @Min(0)
    private Integer age;
}

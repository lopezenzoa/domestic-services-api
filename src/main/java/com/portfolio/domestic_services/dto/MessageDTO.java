package com.portfolio.domestic_services.dto;
import lombok.Data;
@Data
public class MessageDTO {
    private Long callId;
    private Long authorId;
    private String authorRole;
    private String content;
}

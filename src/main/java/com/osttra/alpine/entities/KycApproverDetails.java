package com.osttra.alpine.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KycApproverDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isApproved;
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private KycData kycData;
    private String processId;
    private LocalDateTime localDateTime;
}

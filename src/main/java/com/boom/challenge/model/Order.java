package com.boom.challenge.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String email;
    private String phone;
    private PhotoType photoType;
    private String title;
    private String logisticInfo;
    private LocalDateTime dateTime;
    private OrderState state;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photographer", referencedColumnName = "id")
    private Photographer photographer;
    private String photosUrl;
}

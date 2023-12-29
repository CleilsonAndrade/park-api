package br.com.cleilsonandrade.parkapi.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_client_has_parking")
@EntityListeners(AuditingEntityListener.class)
public class ClientParking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "number_receipt", nullable = false, unique = true, length = 15)
  private String receipt;

  @Column(name = "plate", nullable = false, length = 8)
  private String plate;

  @Column(name = "brand", nullable = false, length = 45)
  private String brand;

  @Column(name = "model", nullable = false, length = 45)
  private String model;

  @Column(name = "color", nullable = false, length = 45)
  private String color;

  @Column(name = "dateEntry", nullable = false)
  private LocalDateTime dateEntry;

  @Column(name = "dateDeparture", nullable = true)
  private LocalDateTime dateDeparture;

  @Column(name = "value", columnDefinition = "decimal(7,2)")
  private BigDecimal value;

  @Column(name = "discount", columnDefinition = "decimal(7,2)")
  private BigDecimal discount;

  @ManyToOne
  @JoinColumn(name = "id_client", nullable = false)
  private Client client;

  @ManyToOne
  @JoinColumn(name = "id_parking", nullable = false)
  private Parking parking;

  @CreatedBy
  @Column(name = "createdBy")
  private String createdBy;

  @LastModifiedBy
  @Column(name = "modifiedBy")
  private String modifiedBy;

  @CreatedDate
  @Column(name = "createdAt")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updatedAt")
  private LocalDateTime updatedAt;
}

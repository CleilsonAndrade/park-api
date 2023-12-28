package br.com.cleilsonandrade.parkapi.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "tb_parking")
@EntityListeners(AuditingEntityListener.class)
public class Parking implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false, unique = true, length = 4)
  private String code;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private StatusParking status;

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

  public enum StatusParking {
    AVAILABLE, BUSY
  }
}

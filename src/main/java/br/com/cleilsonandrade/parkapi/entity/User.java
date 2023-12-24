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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "tb_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "username", nullable = false, unique = true, length = 100)
  private String username;

  @Column(name = "password", nullable = false, length = 200)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 25)
  private Role role = Role.ROLE_CLIENT;

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

  public enum Role {
    ROLE_ADMIN,
    ROLE_CLIENT
  }
}

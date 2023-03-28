package com.example.deansoffice.entity;

import com.example.deansoffice.model.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="token")
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer id;

  @Column(name="token", unique = true)
  public String token;

  @Column(name="token_type")
  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;

  @Column(name="revoked")
  public boolean revoked;

  @Column(name="expired")
  public boolean expired;

  @ManyToOne
  @JoinColumn(name = "login_id")
  public Login login;
}
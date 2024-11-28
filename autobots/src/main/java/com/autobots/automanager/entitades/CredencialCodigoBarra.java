package com.autobots.automanager.entitades;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@JsonDeserialize(as = CredencialCodigoBarra.class)
public class CredencialCodigoBarra extends Credencial {
	@Column(nullable = false, unique = true)
	private long codigo;
}
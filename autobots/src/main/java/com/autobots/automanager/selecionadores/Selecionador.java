package com.autobots.automanager.selecionadores;

import java.util.List;

public interface Selecionador<T, ID> {
	public T selecionar(List<T> entidade, ID id);
}
package br.ufsc.bridge.res.dab.write.medicoesobservacoes.avaliacao;

import java.util.Date;

import br.ufsc.bridge.res.dab.write.base.ParentArquetypeWrapper;

public class AvaliacaoAntropometricaBuilder<PARENT extends ParentArquetypeWrapper<?>> extends ParentArquetypeWrapper<PARENT> {

	public AvaliacaoAntropometricaBuilder(PARENT parent) {
		super(parent);
	}

	@Override
	protected String openTags() {
		return "<Avaliação_antropométrica><name><value>Avaliação antropométrica</value></name>";
	}

	@Override
	protected String closeTags() {
		return "</Avaliação_antropométrica>";
	}

	public AvaliacaoAntropometricaBuilder<PARENT> pesoCorporal(Date dataEvento, String peso) {
		new PesoCorporalBuilder<>(this.parent, dataEvento, peso);
		return this;
	}

	public AvaliacaoAntropometricaBuilder<PARENT> altura(Date dataEvento, String altura) {
		new AlturaComprimentoBuilder<>(this.parent, dataEvento, altura);
		return this;
	}

}
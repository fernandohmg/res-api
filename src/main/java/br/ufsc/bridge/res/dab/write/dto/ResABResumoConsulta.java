package br.ufsc.bridge.res.dab.write.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import br.ufsc.bridge.res.dab.domain.ResABCondutaEnum;
import br.ufsc.bridge.res.dab.domain.ResABTipoAtendimentoEnum;
import br.ufsc.bridge.res.dab.domain.ResABTurnoEnum;
import br.ufsc.bridge.res.dab.write.builder.ResumoConsultaABBuilder;
import br.ufsc.bridge.res.dab.write.builder.alergia.AlergiaReacoesAdversasBuilder;
import br.ufsc.bridge.res.dab.write.builder.alergia.RiscoReacaoAdversaBuilder;
import br.ufsc.bridge.res.dab.write.builder.caracterizacaoconsulta.CaracterizacaoConsultaABBuilder;
import br.ufsc.bridge.res.dab.write.builder.desfecho.DadosDesfechoBuilder;
import br.ufsc.bridge.res.dab.write.builder.listamedicamentos.ListaMedicamentosBuilder;
import br.ufsc.bridge.res.dab.write.builder.problema.ProblemaDiagnosticoAvaliadoBuilder;
import br.ufsc.bridge.res.dab.write.builder.procedimentospequenascirurgias.ProcedimentosPequenasCirurgiasBuilder;

@Getter
@Setter
public class ResABResumoConsulta {

	private Date dataAtendimento;

	private ResABTipoAtendimentoEnum tipoAtendimento;
	private String cnes;
	private String ine;
	private Date dataAdmissao;
	private ResABTurnoEnum turno;
	private List<ResABIdentificacaoProfissional> profissionais;

	private String peso;
	private String altura;

	private Date dum;
	private String idadeGestacional;
	private String gestasPrevias;
	private String partos;

	private List<ResABProblemaDiagnostico> problemaDiagnostico = new ArrayList<>();

	private List<ResABAlergiaReacoes> alergias = new ArrayList<>();

	private List<ResABProcedimento> procedimentos = new ArrayList<>();

	private List<ResABCondutaEnum> condutas = new ArrayList<>();

	private List<ResABMedicamento> medicamentos = new ArrayList<>();

	public String getXml() {
		ResumoConsultaABBuilder abBuilder = new ResumoConsultaABBuilder().data(this.dataAtendimento);

		//@formatter:off
		CaracterizacaoConsultaABBuilder<ResumoConsultaABBuilder> caracterizacaoConsulta = abBuilder.caracterizacaoConsulta();
		caracterizacaoConsulta
			.tipoAtendimento(this.tipoAtendimento)
			.cnes(this.cnes);

		for (ResABIdentificacaoProfissional profissional : this.profissionais) {
			caracterizacaoConsulta.identificacaoProfissional()
				.cns(profissional.getCns())
				.cbo(profissional.getCbo())
				.responsavel(profissional.isResponsavel());
		}

		caracterizacaoConsulta
			.ine(this.ine)
			.dataHoraAdmissao(this.dataAdmissao)
			.turnoAtendimento(this.turno)
		.close()
		.medicoesObservacoes()
			.avaliacaoAntropometrica()
				.pesoCorporal(this.dataAdmissao, this.peso)
				.altura(this.dataAdmissao, this.altura)
			.close()
			.gestante()
				.cicloMenstrual(this.dataAdmissao, this.dum)
				.gestacao(this.dataAdmissao, this.idadeGestacional)
				.sumarioObstetrico(this.gestasPrevias, this.partos);

		ProblemaDiagnosticoAvaliadoBuilder<ResumoConsultaABBuilder> diagnosticoAvaliadoBuilder = abBuilder.problemaDiagnostico();
		for (ResABProblemaDiagnostico diagnostico : this.problemaDiagnostico) {
			diagnosticoAvaliadoBuilder.problema()
				.descricao(diagnostico.getDescricao())
				.tipo(diagnostico.getTipo())
				.codigo(diagnostico.getCodigo());
		}

		AlergiaReacoesAdversasBuilder<ResumoConsultaABBuilder> alergiasBuilder = abBuilder.alergiaReacaoAdversa();
		for (ResABAlergiaReacoes alergia : this.alergias) {
			RiscoReacaoAdversaBuilder<AlergiaReacoesAdversasBuilder<ResumoConsultaABBuilder>> alergiaBuilder = alergiasBuilder.alergia();
			alergiaBuilder
				.agente(alergia.getAgente())
				.categoria(alergia.getCategoria())
				.gravidade(alergia.getGravidade());

			for (ResABEventoReacao evento : alergia.getEventoReacao()) {
				alergiaBuilder.eventoReacao()
					.dataInstalacao(evento.getDataInstalacao())
					.evolucaoAlergia(evento.getEvolucaoAlergia())
					.manifestacao(evento.getManifestacao());
			}
		}

		ProcedimentosPequenasCirurgiasBuilder<ResumoConsultaABBuilder> procedimentosBuilder = abBuilder.procedimentosPequenasCirurgias();
		for (ResABProcedimento procedimento : this.procedimentos) {
			procedimentosBuilder.procedimento()
				.nome(procedimento.getNome())
				.data(this.dataAdmissao)
				.codigo(procedimento.getCodigo())
				.resultadoObservacoes(procedimento.getResultadoObservacoes());
		}

		ListaMedicamentosBuilder<ResumoConsultaABBuilder> medicamentosBuilder = abBuilder.listaMedicamentos();
		for (ResABMedicamento medicamento : this.medicamentos) {
			medicamentosBuilder.itemMedicacao()
			.medicamento(medicamento.getNomeMedicamento(), medicamento.getCodigoMedicamentoCatmat())
				.formaFarmaceutica(medicamento.getDescricaoFormaFarmaceutica(), medicamento.getCodigoFormaFarmaceutica())
				.viaAdministracao(medicamento.getDescricaoViaAdministracao(), medicamento.getCodigoViaAdministracao())
				.dose(medicamento.getDescricaoDose())
				.doseEstruturada(medicamento.getCodigoDoseEstruturada());
		}

		DadosDesfechoBuilder<ResumoConsultaABBuilder> desfechoBuilder = abBuilder.dadosDesfecho();
		for (ResABCondutaEnum conduta : this.condutas) {
			desfechoBuilder.conduta(conduta);
		}

		return "";
	}
}
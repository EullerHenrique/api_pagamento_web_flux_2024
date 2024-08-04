package com.api.pagamento.service.dto.transacao;

import com.api.pagamento.domain.converter.transacao.TransacaoConverter;
import com.api.pagamento.domain.dto.response.transacao.TransacaoResponseDto;
import com.api.pagamento.domain.dto.request.transacao.TransacaoRequestDto;
import com.api.pagamento.domain.model.transacao.Transacao;
import com.api.pagamento.service.model.transacao.TransacaoModelService;
import com.api.pagamento.service.util.transacao.TransacaoUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoDtoService {

    private final TransacaoModelService transacaoModelService;
    private final TransacaoConverter transacaoConverter;
    private final TransacaoUtilService transacaoUtilService;

    /**
     * Realiza um pagamento
     *
     */
    public TransacaoResponseDto buscarTransacao(Long id) {
        Transacao transacao = transacaoModelService.buscarTransacao(id);
        return transacaoConverter.modelToResponse(transacao);
    }

    /**
     * Realiza um pagamento
     *
     */
    public List<TransacaoResponseDto> listarTransacoes() {
        List<Transacao> transacoes  = transacaoModelService.listarTranscacoes();
        return transacaoConverter.modelsToResponses(transacoes);
    }

    /**
     * Realiza um pagamento
     *
     */
    public TransacaoResponseDto pagar(TransacaoRequestDto request) {
        transacaoUtilService.validarTipoPagamentoAoPagar(request);

        TransacaoResponseDto transacaoDTO = transacaoConverter.requestToResponse(request);

        transacaoDTO.getDescricao().setNsu(transacaoUtilService.obterNsu());
        transacaoDTO.getDescricao().setCodigoAutorizacao(transacaoUtilService.obterCodigoAutorizacao());
        transacaoDTO.getDescricao().setStatus(transacaoUtilService.obterStatusAoPagar());

        Transacao transacaoNaoSalva = transacaoConverter.responseToModel(transacaoDTO);
        Long id = transacaoModelService.salvarTransacao(transacaoNaoSalva);
        transacaoDTO.setId(id.toString());

        return transacaoDTO;
    }

    /**
     * Realiza um pagamento
     *
     */
    public TransacaoResponseDto estornar(Long id){
        Transacao transacao = transacaoModelService.buscarTransacao(id);
        transacaoUtilService.validarStatusTransacaoAoEstornar(transacao);

        transacao.getDescricao().setStatus(transacaoUtilService.obterStatusAoEstornar());
        transacaoModelService.salvarTransacao(transacao);

        return transacaoConverter.modelToResponse(transacao);
    }

}

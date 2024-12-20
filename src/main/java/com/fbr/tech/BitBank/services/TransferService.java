package com.fbr.tech.BitBank.services;

import com.fbr.tech.BitBank.controllers.dto.TransferOperationDto;
import com.fbr.tech.BitBank.entities.Transfer;
import com.fbr.tech.BitBank.entities.Wallet;
import com.fbr.tech.BitBank.exception.SameWalletTransferException;
import com.fbr.tech.BitBank.exception.WalletBalanceException;
import com.fbr.tech.BitBank.exception.WalletNotFoundException;
import com.fbr.tech.BitBank.repositories.TransferRepository;
import com.fbr.tech.BitBank.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class TransferService {

    private final WalletRepository walletRepository;
    private final TransferRepository transferRepository;


    /*
    DESCRIÇÃO

    ** Este método realiza a transferência de valores entre duas carteiras
    ** ~Regras que são validadas:
        - Valida se a carteira de destino e origem são iguais (se for, a operação é negada)
        - Valida se as carteiras existem através do ID
        - Valida se a carteira de origem tem saldo suficiente para realizar a
        transferência através do metodo validateBalanceSender

        Se todos os dados estiverem validados, a operação salva a transferência no banco de dados
        e atualiza o saldo das carteiras.
     */

    @Transactional
    public void transferOperation(UUID walletSenderId,
                                  TransferOperationDto dto,
                                  Object attribute) {
        // valida se a carteira de origiem e destino não são iguais
        validateWallets(walletSenderId, dto.receiver());

        // valida se as carteiras existem
        var sender = getWalletOrElseThrow(walletSenderId);
        var receiver = getWalletOrElseThrow(dto.receiver());

        // valida se a carteira de origem tem saldo disponível
        validateBalanceSender(dto.transferValue(), sender.getBalance());

        // salva a transferência no banco de dados transfer_tb
        persistTransfer(dto.transferValue(), attribute, sender, receiver);

        // realiza a atualização das carteiras e salva na wallet_tb
        persistUpdateBalances(dto, sender, receiver);
    }


    private void validateWallets(UUID walletSenderId, UUID receiver) {

        if (walletSenderId.equals(receiver)) {
            throw new SameWalletTransferException("Os IDs do 'sender' e 'receiver' não podem ser iguais");
        }
    }

    @Description("Salva a transferencia no banco de dados")
    private void persistTransfer(BigDecimal transferValue, Object attribute, Wallet sender, Wallet receiver) {
        var transfer = new Transfer();

        transfer.setTransferValue(transferValue);
        transfer.setWalletSender(sender);
        transfer.setWalletReceiver(receiver);
        transfer.setIpAddress(attribute.toString());
        transferRepository.save(transfer);
    }

    @Description("Recebe os valores e atualiza nos bancos de dados os saldos do sender e receiver")
    private void persistUpdateBalances(TransferOperationDto dto, Wallet sender, Wallet receiver) {
        sender.setBalance(sender.getBalance().subtract(dto.transferValue()));
        receiver.setBalance(receiver.getBalance().add(dto.transferValue()));
        walletRepository.saveAll(List.of(sender, receiver));
    }

    @Description("Busca a carteira e se não achar joga uma exception")
    private Wallet getWalletOrElseThrow(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Não existe carteira com o ID informado. " + walletId));
    }


    @Description("Valida se o 'sender' tem saldo suficiente para a transferencia")
    private void validateBalanceSender(BigDecimal valueToTransfer, BigDecimal senderBalance) {

        boolean isBalanceSenderSufficientForTransfer = valueToTransfer
                .compareTo(senderBalance) < 0;

        if (!isBalanceSenderSufficientForTransfer) {
            throw new WalletBalanceException("O 'sender' não tem saldo suficiente para a transferência.");
        }
    }
}

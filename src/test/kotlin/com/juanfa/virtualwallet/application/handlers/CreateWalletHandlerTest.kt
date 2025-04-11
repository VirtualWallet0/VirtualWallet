package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.CreateWalletCommand
import com.juanfa.virtualwallet.infrastructure.db.WalletRepositoryJPA
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNotNull

@SpringBootTest
class CreateWalletHandlerTest {

    @Autowired
    lateinit var handler: CreateWalletHandler
    @Autowired
    lateinit var walletRepositoryJPA: WalletRepositoryJPA

    @Test
    fun testTheWalletShouldBeCreatedAndSave() {
        val command = CreateWalletCommand(
            id = UUID.randomUUID(),
            name = "Wallet432",
            initialAmount = 4355654,
            type = "PersoNal",
            owner = UUID.randomUUID(),
            create = LocalDateTime.now()
        )
        handler.handle(command)

        val wallet = walletRepositoryJPA.findById(command.id).orElse(null)

        assertNotNull(wallet)
        assertEquals(command.name, wallet.name)
        assertEquals(command.initialAmount, wallet.amount)
    }
}
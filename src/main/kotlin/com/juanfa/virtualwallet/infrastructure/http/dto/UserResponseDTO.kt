import java.time.LocalDateTime
import java.util.*

data class UserResponseDTO(
    val id: UUID,
    val name: String,
    val created: LocalDateTime,
    val update: LocalDateTime
)
package top.phj233.backend_sb3.model

import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
interface Base {
    val createdTime: LocalDateTime

    val modifiedTime: LocalDateTime
}

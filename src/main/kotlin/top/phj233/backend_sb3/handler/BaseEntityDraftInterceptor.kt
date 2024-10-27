package top.phj233.backend_sb3.handler

import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.DraftInterceptor
import org.springframework.stereotype.Component
import top.phj233.backend_sb3.model.Base
import top.phj233.backend_sb3.model.BaseDraft
import java.time.LocalDateTime

/**
 * 基础实体拦截器
 */
@Component
class BaseEntityDraftInterceptor : DraftInterceptor<Base, BaseDraft> {
    override fun beforeSave(draft: BaseDraft, original: Base?) {
        if (!isLoaded(draft, Base::modifiedTime)) {
            draft.modifiedTime = LocalDateTime.now()
        }
        if (original === null) {
            if (!isLoaded(draft, Base::createdTime)) {
                draft.createdTime = LocalDateTime.now()
            }
        }
    }
}

package com.yuralil.domain.enums;

/**
 * Ролі користувачів у системі Exhibitly.
 * <ul>
 *     <li>{@code ADMIN} — адміністратор, має повний доступ (керування, звіти тощо)</li>
 *     <li>{@code VISITOR} — зареєстрований користувач з базовими правами</li>
 *     <li>{@code GUEST} — неавторизований гість з доступом лише до перегляду</li>
 * </ul>
 */
public enum Role {
    ADMIN,
    VISITOR,
    GUEST
}

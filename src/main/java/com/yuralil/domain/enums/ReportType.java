package com.yuralil.domain.enums;

/**
 * Перелічення типів звітів, які підтримує система.
 * <ul>
 *     <li>{@code EXHIBIT_DESCRIPTION} — звіт з описами експонатів у форматі DOCX</li>
 *     <li>{@code CATEGORY_STATISTICS} — звіт зі статистикою по категоріях у форматі Excel</li>
 * </ul>
 */
public enum ReportType {
    EXHIBIT_DESCRIPTION,
    CATEGORY_STATISTICS
}

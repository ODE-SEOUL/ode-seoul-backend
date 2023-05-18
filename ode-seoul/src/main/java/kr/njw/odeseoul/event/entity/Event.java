package kr.njw.odeseoul.event.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String uuid;

    @Column(length = 45, nullable = false)
    private String codename;

    @Column(length = 45, nullable = false)
    private String guname;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String place;

    @Column(length = 500, nullable = false)
    private String useTarget;

    @Column(length = 500, nullable = false)
    private String useFee;

    @Column(length = 500, nullable = false)
    private String orgLink;

    @Column(length = 500, nullable = false)
    private String mainImage;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    public enum EventCategory {
        SHOW,
        EXHIBITION,
        FESTIVAL,
        EDUEXP,
        ETC
    }

    public static List<String> getCodenamesOfCategory(EventCategory category) {
        return switch (category) {
            case SHOW -> List.of("콘서트", "클래식", "뮤지컬/오페라", "연극", "무용", "국악", "독주/독창회");
            case EXHIBITION -> List.of("전시/미술");
            case FESTIVAL -> List.of("축제-기타", "축제-문화/예술", "축제-자연/경관", "축제-전통/역사", "축제-시민화합");
            case EDUEXP -> List.of("교육/체험");
            case ETC -> List.of("기타", "영화");
        };
    }

    public EventCategory getCategory() {
        return switch (this.codename) {
            case "콘서트", "클래식", "뮤지컬/오페라", "연극", "무용", "국악", "독주/독창회" -> EventCategory.SHOW;
            case "전시/미술" -> EventCategory.EXHIBITION;
            case "축제-기타", "축제-문화/예술", "축제-자연/경관", "축제-전통/역사", "축제-시민화합" -> EventCategory.FESTIVAL;
            case "교육/체험" -> EventCategory.EDUEXP;
            default -> EventCategory.ETC;
        };
    }
}

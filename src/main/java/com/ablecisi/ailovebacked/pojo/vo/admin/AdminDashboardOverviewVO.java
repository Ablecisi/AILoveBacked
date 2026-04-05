package com.ablecisi.ailovebacked.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardOverviewVO {

    /**
     * 各业务表规模（key 与前端约定一致）
     */
    private Map<String, Long> totals;

    /**
     * 近 N 日每日 DAU（含当日，无数据为 0）
     */
    private List<DauDayPointVO> dauSeries;

    /**
     * 当日 DAU（Asia/Shanghai）
     */
    private long dauToday;

    /**
     * 用户画像 interests 共现关系（ECharts graph）
     */
    private InterestGraphVO interestGraph;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DauDayPointVO {
        private String date;
        private long count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterestGraphVO {
        private List<InterestNodeVO> nodes;
        private List<InterestLinkVO> links;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterestNodeVO {
        private String id;
        private String name;
        private int value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterestLinkVO {
        private String source;
        private String target;
        private int value;
    }
}

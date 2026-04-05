package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.mapper.*;
import com.ablecisi.ailovebacked.pojo.dto.AiCharacterQueryDTO;
import com.ablecisi.ailovebacked.pojo.entity.UserProfile;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminDashboardOverviewVO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminDashboardOverviewVO.DauDayPointVO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminDashboardOverviewVO.InterestGraphVO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminDashboardOverviewVO.InterestLinkVO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminDashboardOverviewVO.InterestNodeVO;
import com.ablecisi.ailovebacked.pojo.vo.admin.DauCountRow;
import com.ablecisi.ailovebacked.service.AdminDashboardService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private static final ZoneId CN = ZoneId.of("Asia/Shanghai");
    private static final int DAU_DAYS = 14;
    private static final int GRAPH_MAX_NODES = 40;
    private static final int GRAPH_MAX_LINKS = 100;
    private static final DateTimeFormatter DAY = DateTimeFormatter.ISO_LOCAL_DATE;

    private final UserMapper userMapper;
    private final AiCharacterMapper aiCharacterMapper;
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;
    private final PostMapper postMapper;
    private final AppTypeMapper appTypeMapper;
    private final PromptTemplateMapper promptTemplateMapper;
    private final UserProfileMapper userProfileMapper;
    private final EmotionLogMapper emotionLogMapper;
    private final FollowRelationMapper followRelationMapper;
    private final ArticleLikeAdminMapper articleLikeAdminMapper;
    private final ArticleCollectAdminMapper articleCollectAdminMapper;
    private final PostLikeAdminMapper postLikeAdminMapper;
    private final CommentLikeAdminMapper commentLikeAdminMapper;
    private final UserActiveDayMapper userActiveDayMapper;
    private final ObjectMapper objectMapper;

    public AdminDashboardServiceImpl(
            UserMapper userMapper,
            AiCharacterMapper aiCharacterMapper,
            ArticleMapper articleMapper,
            CommentMapper commentMapper,
            ConversationMapper conversationMapper,
            MessageMapper messageMapper,
            PostMapper postMapper,
            AppTypeMapper appTypeMapper,
            PromptTemplateMapper promptTemplateMapper,
            UserProfileMapper userProfileMapper,
            EmotionLogMapper emotionLogMapper,
            FollowRelationMapper followRelationMapper,
            ArticleLikeAdminMapper articleLikeAdminMapper,
            ArticleCollectAdminMapper articleCollectAdminMapper,
            PostLikeAdminMapper postLikeAdminMapper,
            CommentLikeAdminMapper commentLikeAdminMapper,
            UserActiveDayMapper userActiveDayMapper,
            ObjectMapper objectMapper) {
        this.userMapper = userMapper;
        this.aiCharacterMapper = aiCharacterMapper;
        this.articleMapper = articleMapper;
        this.commentMapper = commentMapper;
        this.conversationMapper = conversationMapper;
        this.messageMapper = messageMapper;
        this.postMapper = postMapper;
        this.appTypeMapper = appTypeMapper;
        this.promptTemplateMapper = promptTemplateMapper;
        this.userProfileMapper = userProfileMapper;
        this.emotionLogMapper = emotionLogMapper;
        this.followRelationMapper = followRelationMapper;
        this.articleLikeAdminMapper = articleLikeAdminMapper;
        this.articleCollectAdminMapper = articleCollectAdminMapper;
        this.postLikeAdminMapper = postLikeAdminMapper;
        this.commentLikeAdminMapper = commentLikeAdminMapper;
        this.userActiveDayMapper = userActiveDayMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public AdminDashboardOverviewVO overview() {
        Map<String, Long> totals = buildTotals();
        LocalDate today = LocalDate.now(CN);
        LocalDate start = today.minusDays(DAU_DAYS - 1);
        List<DauDayPointVO> dauSeries = buildDauSeries(start, today);
        long dauToday = userActiveDayMapper.countUsersOnDate(today);
        InterestGraphVO graph = buildInterestGraph();
        return AdminDashboardOverviewVO.builder()
                .totals(totals)
                .dauSeries(dauSeries)
                .dauToday(dauToday)
                .interestGraph(graph)
                .build();
    }

    private Map<String, Long> buildTotals() {
        AiCharacterQueryDTO q = new AiCharacterQueryDTO();
        Map<String, Long> m = new LinkedHashMap<>();
        m.put("users", userMapper.countPageForAdmin(""));
        m.put("characters", aiCharacterMapper.pageCount(q));
        m.put("articles", articleMapper.countPageForAdmin(""));
        m.put("comments", commentMapper.countPageForAdmin("", null, null));
        m.put("conversations", conversationMapper.countPageForAdmin("", null));
        m.put("messages", messageMapper.countPageForAdmin("", null));
        m.put("posts", postMapper.countAll());
        m.put("types", appTypeMapper.countAll());
        m.put("prompts", promptTemplateMapper.countAll());
        m.put("userProfiles", userProfileMapper.countAll());
        m.put("emotionLogs", emotionLogMapper.countAll());
        m.put("followRelations", followRelationMapper.countAll());
        m.put("articleLikes", articleLikeAdminMapper.countAll());
        m.put("articleCollects", articleCollectAdminMapper.countAll());
        m.put("postLikes", postLikeAdminMapper.countAll());
        m.put("commentLikes", commentLikeAdminMapper.countAll());
        return m;
    }

    private List<DauDayPointVO> buildDauSeries(LocalDate start, LocalDate end) {
        List<DauCountRow> rows = userActiveDayMapper.countByActiveDateRange(start, end);
        Map<LocalDate, Long> map = new HashMap<>();
        if (rows != null) {
            for (DauCountRow r : rows) {
                if (r.getActiveDate() != null) {
                    map.put(r.getActiveDate(), r.getCnt());
                }
            }
        }
        List<DauDayPointVO> out = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            long c = map.getOrDefault(d, 0L);
            out.add(new DauDayPointVO(d.format(DAY), c));
        }
        return out;
    }

    private InterestGraphVO buildInterestGraph() {
        List<UserProfile> profiles = userProfileMapper.selectAllWithInterestsNotBlank();
        if (profiles == null || profiles.isEmpty()) {
            return new InterestGraphVO(List.of(), List.of());
        }
        Map<String, Integer> tagFreq = new HashMap<>();
        Map<String, Integer> linkFreq = new HashMap<>();
        for (UserProfile p : profiles) {
            List<String> tags = parseInterests(p.getInterests());
            if (tags.isEmpty()) {
                continue;
            }
            LinkedHashSet<String> uniq = new LinkedHashSet<>(tags);
            List<String> list = new ArrayList<>(uniq);
            for (String t : list) {
                tagFreq.merge(t, 1, Integer::sum);
            }
            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    String a = list.get(i);
                    String b = list.get(j);
                    if (a.compareTo(b) > 0) {
                        String tmp = a;
                        a = b;
                        b = tmp;
                    }
                    String key = a + "\n" + b;
                    linkFreq.merge(key, 1, Integer::sum);
                }
            }
        }
        List<String> topTags = tagFreq.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(GRAPH_MAX_NODES)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        Set<String> topSet = new HashSet<>(topTags);
        List<InterestNodeVO> nodes = topTags.stream()
                .map(id -> new InterestNodeVO(id, id, tagFreq.get(id)))
                .collect(Collectors.toList());
        List<InterestLinkVO> links = linkFreq.entrySet().stream()
                .filter(e -> e.getKey().contains("\n"))
                .map(e -> {
                    String[] parts = e.getKey().split("\n", 2);
                    return new Object[]{parts[0], parts[1], e.getValue()};
                })
                .filter(arr -> topSet.contains(arr[0]) && topSet.contains(arr[1]))
                .sorted((a, b) -> Integer.compare((Integer) b[2], (Integer) a[2]))
                .limit(GRAPH_MAX_LINKS)
                .map(arr -> new InterestLinkVO((String) arr[0], (String) arr[1], (Integer) arr[2]))
                .collect(Collectors.toList());
        return new InterestGraphVO(nodes, links);
    }

    private List<String> parseInterests(String raw) {
        if (raw == null) {
            return List.of();
        }
        String t = raw.trim();
        if (t.isEmpty() || "null".equalsIgnoreCase(t)) {
            return List.of();
        }
        try {
            if (t.startsWith("[")) {
                JsonNode n = objectMapper.readTree(t);
                if (n != null && n.isArray()) {
                    List<String> out = new ArrayList<>();
                    for (JsonNode x : n) {
                        if (x.isTextual()) {
                            out.add(normTag(x.asText()));
                        } else if (!x.isNull()) {
                            out.add(normTag(x.toString()));
                        }
                    }
                    return out.stream().filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
                }
            }
        } catch (Exception ignored) {
            // fall through
        }
        return Arrays.stream(t.split("[,，;；|]"))
                .map(this::normTag)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    private String normTag(String s) {
        if (s == null) {
            return "";
        }
        String x = s.trim().replace("\"", "").replace("[", "").replace("]", "");
        if (x.length() > 32) {
            x = x.substring(0, 32);
        }
        return x;
    }
}

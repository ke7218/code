package self.cases.teams.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentimentAnalyzer {
    private static final Logger Log = LoggerFactory.getLogger(SentimentAnalyzer.class);

    // 积极词汇库
    private static final Set<String> POSITIVE_WORDS = new HashSet<>(Arrays.asList(
        // 传统形容词
        "好", "棒", "赞", "优秀", "精彩", "完美", "喜欢", "开心", "快乐", "满意",
        "感谢", "期待", "支持", "热爱", "有趣", "欢乐", "温暖", "贴心", "给力",
        "厉害", "成功", "进步", "收获", "学到", "优质", "出色", "卓越", "杰出",
        "出众", "了不起", "突出", "优异", "动听", "动人", "精良", "优美", "漂亮","还不错",
        
        // 网络用语
        "666", "牛", "牛逼", "nb", "NB", "yyds", "YYDS", "太强了", "爆表",
        "神仙", "大神", "大佬", "稳", "吊打", "秒杀", "上头", "真香", "爱了",
        "太棒了", "太赞了", "太好了", "很棒", "很赞", "很好", "超棒", "超赞",
        "超好", "绝了", "绝绝子", "太强了", "强无敌", "无敌", "完美", "完美的",
        
        // 英文表达
        "good", "great", "nice", "perfect", "excellent", "wonderful", "amazing",
        "awesome", "fantastic", "brilliant", "outstanding", "superb", "cool",
        "lovely", "beautiful", "perfect", "best", "super",
        
        // 表情文字
        "❤", "♥", "👍", "💯", "🎉", "✨", "🌟", "⭐", "🔥",
        
        // 其他积极表达
        "推荐", "值得", "难忘", "震撼", "感动", "激动", "热血", "振奋", "鼓舞",
        "启发", "受益", "长见识", "涨知识", "增长见识", "学习到", "获益良多",
        "受益匪浅", "很有意义", "非常有意义", "特别有意义", "很有价值", "非常有价值",
        "特别有价值", "很有收获", "收获满满", "干货满满", "满满收获"
    ));

    // 消极词汇库
    private static final Set<String> NEGATIVE_WORDS = new HashSet<>(Arrays.asList(
        // 传统消极词
        "差", "烂", "糟", "失望", "不好", "无聊", "枯燥", "难受", "讨厌", "浪费",
        "可惜", "遗憾", "不满", "不行", "不够", "问题", "困难", "麻烦", "不合理",
        "不适", "不当", "不足", "不佳", "不满意", "糟糕", "垃圾", "废物", "无用",
        "乏味", "无趣", "单调", "乱", "差劲", "恶心", "令人不快", "令人不满",
        
        // 网络用语
        "翻车", "崩溃", "劝退", "白瞎", "白费", "emm", "呵呵", "啧啧", "呵", 
        "害", "累了", "烦了", "吐了", "扯", "扯淡", "离谱", "太离谱", "无语",
        "真无语", "服了", "醉了", "吐槽", "太水", "水货", "划水", "摆烂",
        
        // 英文表达
        "bad", "poor", "terrible", "awful", "horrible", "disappointing",
        "disappointed", "waste", "boring", "useless", "meaningless",
        
        // 表情文字
        "👎", "💔", "😒", "😑", "😤", "🙄",
        
        // 其他消极表达
        "后悔", "不值", "不值得", "浪费时间", "浪费精力", "虚度", "虚耗", "敷衍",
        "马虎", "草率", "潦草", "不走心", "不认真", "不负责", "不专业", "不到位",
        "没意思", "没意义", "没价值", "没收获", "没学到", "没用", "没帮助",
        "太差", "很差", "非常差", "太烂", "很烂", "非常烂", "太糟", "很糟",
        "非常糟", "太失望", "很失望", "非常失望"
    ));

    public static String analyzeSentiment(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "NEUTRAL";
        }

        Log.info("Analyzing sentiment for text: {}", text);
        
        int positiveCount = 0;
        int negativeCount = 0;

        // 统计积极词汇出现次数
        for (String word : POSITIVE_WORDS) {
            if (text.contains(word)) {
                positiveCount++;
                Log.debug("Found positive word: {}", word);
            }
        }

        // 统计消极词汇出现次数
        for (String word : NEGATIVE_WORDS) {
            if (text.contains(word)) {
                negativeCount++;
                Log.debug("Found negative word: {}", word);
            }
        }

        // 根据积极词和消极词的数量判断情感倾向
        String result;
        if (positiveCount > negativeCount) {
            result = "POSITIVE";
        } else if (negativeCount > positiveCount) {
            result = "NEGATIVE";
        } else {
            result = "NEUTRAL";
        }
        
        Log.info("Sentiment analysis result: {} (positive: {}, negative: {})", 
                 result, positiveCount, negativeCount);
        return result;
    }
} 
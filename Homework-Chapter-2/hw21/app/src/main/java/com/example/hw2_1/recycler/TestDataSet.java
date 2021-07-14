package com.example.hw2_1.recycler;

import java.util.ArrayList;
import java.util.List;

public class TestDataSet {

    public static List<TestData> getData() {
        List<TestData> result=  new ArrayList();
        result.add(new TestData("教育部辟谣取消教室寒暑假", "490.3w"));
        result.add(new TestData("多地降雨量破历史极值", "488.1w"));
        result.add(new TestData("31省份6月CPI出炉", "478.9w"));
        result.add(new TestData("摄影师拍到中国空间站凌日瞬间", "457.2w"));
        result.add(new TestData("赵立坚回击美方涉疆谎言", "447.0w"));
        result.add(new TestData("社保养老金等将迎来六大变化", "416.9w"));
        result.add(new TestData("中国青少年近20%超重肥胖", "390.3w"));
        result.add(new TestData("雨后长城", "383.5w"));
        result.add(new TestData("英国政府确认将如期实施解封", "375.6w"));
        result.add(new TestData("鲸类频繁搁浅", "362.0w"));
        result.add(new TestData("南非骚乱升级", "354.8w"));
        return result;
    }

}

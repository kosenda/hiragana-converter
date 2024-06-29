package ksnd.hiraganaconverter.core.model.mock

import ksnd.hiraganaconverter.core.model.ConvertHistoryData

class MockConvertHistoryData {
    val data: List<ConvertHistoryData> = listOf(
        ConvertHistoryData(id = 1, before = "漢字", after = "かんじ", time = "2021/01/01 00:00:00"),
        ConvertHistoryData(id = 2, before = "漢字", after = "カンジ", time = "2021/01/01 00:00:00"),
        ConvertHistoryData(id = 3, before = "あいうえお", after = "アイウエオ", time = "2021/01/01 00:00:00"),
    )
}

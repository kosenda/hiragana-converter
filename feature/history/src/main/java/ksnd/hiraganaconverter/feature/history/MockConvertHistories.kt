package ksnd.hiraganaconverter.feature.history

import ksnd.hiraganaconverter.core.model.ConvertHistoryData

// TODO MockDataは:featureモジュールではなく別モジュールの方がよさそうだから移動する（:core:mockとか作ろうと思う）
class MockConvertHistories {
    val data: List<ConvertHistoryData> = listOf(
        ConvertHistoryData(id = 1, before = "漢字", after = "かんじ", time = "2021/01/01 00:00:00"),
        ConvertHistoryData(id = 2, before = "漢字", after = "カンジ", time = "2021/01/01 00:00:00"),
        ConvertHistoryData(id = 3, before = "あいうえお", after = "アイウエオ", time = "2021/01/01 00:00:00"),
    )
}

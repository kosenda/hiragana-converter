[![CircleCI](https://dl.circleci.com/status-badge/img/gh/kosenda/hiragana-converter/tree/develop.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/kosenda/hiragana-converter/tree/develop)　[![Renovate](https://img.shields.io/badge/renovate-enabled-brightgreen.svg?style=flat)](https://renovatebot.com)　

# Hiragana Converter （ひらがな変換）

<img src="https://github.com/kosenda/hiragana-converter/assets/60963155/206e8a60-988b-4815-a0a7-4a5b459b10b4" width="200" alt="app icon">

## Summary
This is an Android app that uses gooラボ's API ([Japanese Hiragana Conversion API](https://labs.goo.ne.jp/api/en/hiragana-translation/)) to convert Japanese strings, including kanji and alphabets, into hiragana/katakana.

<a href='https://play.google.com/store/apps/details?id=ksnd.hiraganaconverter&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width="300" alt='Get it on Google Play' src='https://play.google.com/intl/ja/badges/static/images/badges/en_badge_web_generic.png'/></a>

## Conversion example
|-|text|
|:--|:--|
|Before|漢字とWORDを含む日本語文字列|
|After（Hiragana）|かんじと わーどを ふくむ にほんごもじれつ|
|After（Katakana）|カンジト ワードヲ フクム ニホンゴモジレツ|

## How to perform API communication within the app

1. Get your API key below.  
https://labs.goo.ne.jp/apiusage/  

2. Add the following to the bottom of `local.properties`  
`apiKey=<API key obtained in 1.>`  （Example： apiKey=011222333333abcd）

> [!NOTE]
> Mock flavor is provided, so if you want to simulate API communication using Mock, you can test it by setting a meaningless value (such as "AAA") in apiKey.

## Reference sites
https://github.com/kosenda/hiragana-converter/blob/develop/REFERENCE.md

## Typical libraries used  
|Name|Brief description|
|:--|:--|
|AboutLibraries|OSS Licenses|
|Analytics|Firebase analytics|
|App Update|In App Update|
|App Review|In App Review|
|Coil|Image loading library|
|Crashlytics|Firebase crashlytics|
|Danger|Automatic review|
|Hilt|Dependency Injection|
|Interceptor|Create error response|
|Jacoco|Code coverage library|
|Jetpack Compose|Toolkit for building native UIs in Android|
|Kotlinx serialization|Serializer / Deserializer|
|Ktlint|Formatter|
|Material3|Design|
|Mockk|Unit test mock|
|Preferences DataStore|Permanent data|
|Renovate|Automated project dependency updates|
|Retrofit2|Library for API communications|
|Robolectric|Unit Test Framework|
|Roborazzi|Make JVM Android Integration Test Visible|
|Room|Database|
|Secrets gradle plugin|Reading API keys from `local.properties`|
|Timber|Log output library|
|Truth|Assertions used in testing|
|Turbine|testing library for kotlinx.coroutines Flow|

## Animation ( Lottie )
- https://lottiefiles.com/animations/loading-Y3Pt36IpkJ
- https://lottiefiles.com/animations/downloading-or-importing-icon-animation-D3lBDrmw1L

## Image
|Conversion screen(Light)|Conversion screen(Dark)|
|:-:|:-:|
|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/5ac9e2e0-461f-4401-bd13-934459f7941a">|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/f82f2f13-f117-48fd-83dc-eee19aa1318c">|

|Setting screen(Light)|Setting screen(Dark)|
|:-:|:-:|
|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/e46a5c3d-a84d-493c-88c1-21a26dfc8d2d">|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/c467a50e-f4b9-4311-bc6a-de196587de0f">|

|History screen(Light)|History screen(Dark)|
|:-:|:-:|
|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/c61537ae-3733-4392-9fa8-240f6b4a659e">|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/dea7436c-3617-433c-b5fb-4310e660c69b">|

|Info screen(Light)|Info screen(Dark)|
|:-:|:-:|
|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/2abd8765-8db6-4df3-b558-637b90f99ed1">|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/34326658-e6e8-479c-9e8f-584cce36cbc2">|

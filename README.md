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
|OSS licenses plugin|OSS Licenses|
|Preferences DataStore|Permanent data|
|Renovate|Automated project dependency updates|
|Retrofit2|Library for API communications|
|Robolectric|Unit Test Framework|
|Roborazzi|Make JVM Android Integration Test Visible|
|Room|Database|
|Secrets gradle plugin|Reading API keys from `local.properties`|
|Showkase|auto-generates a browser for Jetpack Compose UI|
|Timber|Log output library|
|Truth|Assertions used in testing|
|Turbine|testing library for kotlinx.coroutines Flow|

## Animation ( Lottie )
- https://lottiefiles.com/animations/loading-Y3Pt36IpkJ
- https://lottiefiles.com/animations/downloading-or-importing-icon-animation-D3lBDrmw1L

## Image
|Conversion screen(Light)|Conversion screen(Dark)|
|:-:|:-:|
|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/af920334-fd73-40b4-803c-525d0cc64f21">|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/9a67591f-cd1e-4184-9d79-79a3f69f69af">|

|Setting screen(Light)|Setting screen(Dark)|
|:-:|:-:|
|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/2ad5a49f-eaed-425d-8b66-83ec84223582">|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/d8d47e0e-06ea-4131-be83-cb11c2e5dcc3">|

|History screen(Light)|History screen(Dark)|
|:-:|:-:|
|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/24908a61-184b-4768-adfa-c3361d365549">|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/a7a3ef8f-cbdd-4b27-b17e-c8f7f2a3af79">|

|Info screen(Light)|Info screen(Dark)|
|:-:|:-:|
|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/708f10fb-d93c-4d28-91d1-7b5a25c543ec">|<img width="280" alt="convert_screen" src="https://github.com/kosenda/hiragana-converter/assets/60963155/718a2722-3ab9-48f7-95be-65efe6a9112c">|

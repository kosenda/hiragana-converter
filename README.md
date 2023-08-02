[![CircleCI](https://circleci.com/gh/circleci/circleci-docs.svg?style=svg)](https://https://github.com/kosenda/hiragana-converter)　[![Renovate](https://img.shields.io/badge/renovate-enabled-brightgreen.svg?style=flat)](https://renovatebot.com)　

# Hiragana Converter （Japanese: ひらがな変換）

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

## Typical libraries used  
|Name|Brief description|
|:--|:--|
|Analytics|Firebase analytics|
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
|Room|Database|
|Secrets gradle plugin|Reading API keys from `local.properties`|
|Truth|Assertions used in testing|
|Timber|Log output library|

## Image
|Conversion screen(Light)|Conversion screen(Dark)|
|:-:|:-:|
|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210051987-be80696a-8bad-4b68-b9ae-72ddf2097b1e.PNG">|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210051997-1f7f02d2-ea72-4768-a696-1db928339cb4.PNG">|

|Setting screen(Light)|Setting screen(Dark)|
|:-:|:-:|
|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052008-847e6dca-9961-4a0b-83a0-c0a48e964180.PNG">|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052014-e5bea46c-10af-4b92-b346-12e97cab3572.PNG">|

|History screen(Light)|History screen(Dark)|
|:-:|:-:|
|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052019-3b9c0f74-f874-4258-9eee-e29d2488d52b.PNG">|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052028-29f8d902-2aed-47c8-a164-f90aa2b1d9b4.PNG">|

|Info screen(Light)|Info screen(Dark)|
|:-:|:-:|
|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052030-5425ffc2-3f1f-4a72-94c9-b52b33144654.PNG">|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052039-eaf98871-e8db-439e-98e6-4c80124ada5b.PNG">|

## Animation
https://lottiefiles.com/animations/loading-lottie-animation-7xyTDwuIwc

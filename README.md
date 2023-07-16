# Hiragana Converter （Japanese app name: ひらがな変換）

## Summary
This is an Android app that uses gooラボ's API ([Japanese Hiragana Conversion API](https://labs.goo.ne.jp/api/en/hiragana-translation/)) to convert Japanese strings, including kanji and alphabets, into hiragana/katakana.

<a href='https://play.google.com/store/apps/details?id=ksnd.hiraganaconverter&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width="300" alt='Get it on Google Play' src='https://play.google.com/intl/ja/badges/static/images/badges/en_badge_web_generic.png'/></a>

## Conversion example
|-|text|
|:--|:--|
|Before|漢字とWORDを含む日本語文字列|
|After（Hiragana）|かんじと わーどを ふくむ にほんごもじれつ|
|After（Katakana）|カンジト ワードヲ フクム ニホンゴモジレツ|

## About fonts

#### [Corporate Logo (Round) ver3](https://logotype.jp/font-corpmaru.html)
- Free "Corporate Logo (Round) ver3" font DL recommended for cute occasions. 
- かわいい場面におすすめの「コーポレート・ロゴ（ラウンド）ver3」フォント無料DL 

#### [Corporate Yawa Akira ver3](https://logotype.jp/corp-yawamin.html)
- Free download of the rounded "Corporate Yawa Akira ver3" font
- 丸く加工した「コーポレートやわ明ver3」フォントの無料ダウンロード  
  
#### [Nostal Dot (M+)](https://logotype.jp/nosutaru-dot.html)
- 「Nostal Dot (M+)」 font DL  
- 「ノスタルドット（M+）」フォントDL  
  
#### [BIZ UDGothic](https://fonts.google.com/specimen/BIZ+UDGothic)
- a universal design typeface designed to be easy to read and ideal for education and business documentation
  
## How to perform API communication within the app

1. Get your API key below.  
https://labs.goo.ne.jp/apiusage/  

2. Add the following to the bottom of `local.properties`  
`apiKey=<API key obtained in 1.>`  （Example： apiKey=011222333333abcd）  

## Typical libraries used  
|Name|Brief description|
|:--|:--|
|Jetpack Compose|Toolkit for building native UIs in Android|
|Material3|Design|
|Retrofit2|Library for API communications|
|kotlinx serialization|Serializer / Deserializer|
|Interceptor|Create error response|
|Preferences DataStore|Permanent data|
|Room|Database|
|Robolectric|Unit Test Framework|
|Truth|Assertions used in testing|
|Hilt|Dependency Injection|
|Coil|Image loading library|
|Timber|Log output library|
___
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

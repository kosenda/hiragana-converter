# Google Play でリリースしたアプリのコピー版です
## アプリ名「ひらがな変換」（ Hiragana Converter ）
Google Play  
https://play.google.com/store/apps/details?id=ksnd.hiraganaconverter
___
## アプリの説明
漢字やアルファベットを含む日本語文字列をひらがな／カタカナに変換します。
### 変換例
|変換前 / 変換後|文章|
|:--|:--|
|変換前|漢字とWORDを含む日本語文字列|
|変換後（ひらがな）|かんじと わーどを ふくむ にほんごもじれつ|
|変換後（カタカナ）|カンジト ワードヲ フクム ニホンゴモジレツ|
___
## 使用しているAPIについて
gooラボ様の「ひらがな化API」になります。  
https://labs.goo.ne.jp/api/jp/hiragana-translation/
___
## 使用しているフォントについて

- かわいい場面におすすめの「コーポレート・ロゴ（ラウンド）ver3」フォント無料DL  
https://logotype.jp/font-corpmaru.html  

- 丸く加工した「コーポレートやわ明ver3」フォントの無料ダウンロード  
https://logotype.jp/corp-yawamin.html  

- 「ノスタルドット（M+）」フォントDL  
https://logotype.jp/nosutaru-dot.html  

- BIZ UDGothic  
https://fonts.google.com/specimen/BIZ+UDGothic  
___
## アプリ内でAPI通信を行う方法

1. 以下でAPIキーを取得してください。  
https://labs.goo.ne.jp/apiusage/  

2. local.properties の一番下に以下を追加してください。  
apiKey=<1.で取得したAPIキー>  
（例： apiKey=011222333333abcd）
___
## 使用している代表的なライブラリ  
|名前|簡単な説明|
|:--|:--|
|Jetpack Compose|UIアプリ開発ツールキット|
|Material3|デザイン|
|Retrofit2|API通信|
|kotlinx serialization|シリアライザー|
|Interceptor|エラーレスポンス作成
|Preferences DataStore|永続化データ|
|Room|データベース|
|Robolectric|単体テストフレームワーク|
|Truth|アサーション|
|Hilt|依存性注入|
|Coil|画像読み込み|
|Timber|ログ出力|
___
## イメージ
|変換画面(ライト)|変換画面(ダーク)|
|:-:|:-:|
|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210051987-be80696a-8bad-4b68-b9ae-72ddf2097b1e.PNG">|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210051997-1f7f02d2-ea72-4768-a696-1db928339cb4.PNG">|

|設定画面(ライト)|設定画面(ダーク)|
|:-:|:-:|
|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052008-847e6dca-9961-4a0b-83a0-c0a48e964180.PNG">|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052014-e5bea46c-10af-4b92-b346-12e97cab3572.PNG">|

|履歴画面(ライト)|履歴画面(ダーク)|
|:-:|:-:|
|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052019-3b9c0f74-f874-4258-9eee-e29d2488d52b.PNG">|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052028-29f8d902-2aed-47c8-a164-f90aa2b1d9b4.PNG">|

|インフォ画面(ライト)|インフォ画面(ダーク)|
|:-:|:-:|
|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052030-5425ffc2-3f1f-4a72-94c9-b52b33144654.PNG">|<img width="320" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/210052039-eaf98871-e8db-439e-98e6-4c80124ada5b.PNG">|

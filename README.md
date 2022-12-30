# Google Play でリリースしたアプリのコピー版です
## アプリ名「ひらがな変換」（ Hiragana Converter ）
Google Play  
https://play.google.com/store/apps/details?id=ksnd.hiraganaconverter

## アプリの説明
漢字やアルファベットを含む日本語文字列をひらがな／カタカナに変換します。
#### 変換例  
　＜変換前＞  
　　漢字とWORDを含む日本語文字列  
　＜ひらがな＞  
　　かんじと わーどを ふくむ にほんごもじれつ  
　＜カタカナ＞  
　　カンジト ワードヲ フクム ニホンゴモジレツ  

## 使用しているAPIについて
gooラボ様の「ひらがな化API」になります。  
https://labs.goo.ne.jp/api/jp/hiragana-translation/

## 使用しているフォントについて

- かわいい場面におすすめの「コーポレート・ロゴ（ラウンド）ver3」フォント無料DL  
https://logotype.jp/font-corpmaru.html  

- 丸く加工した「コーポレートやわ明ver3」フォントの無料ダウンロード  
https://logotype.jp/corp-yawamin.html  

- 「ノスタルドット（M+）」フォントDL  
https://logotype.jp/nosutaru-dot.html  

- BIZ UDGothic  
https://fonts.google.com/specimen/BIZ+UDGothic  

## アプリ内でAPI通信を行う方法

- 1.以下でAPIキーを取得してください。  
https://labs.goo.ne.jp/apiusage/  

- 2.local.properties の一番下に以下を追加してください。  
apiKey=<1.で取得したAPIキー>  
（例： apiKey=011222333333abcd）

## 使用している代表的なライブラリ
- Material3(デザイン)
- Retrofit2(API通信)
- kotlinx serialization(シリアライザー)
- Interceptor(エラーレスポンス作成)
- Preferences DataStore(永続化データ)
- Room(データベース)
- Robolectric(単体テストフレームワーク)
- Truth(アサーション)
- Hilt(依存性注入)
- COIL(画像読み込み)
- Timber(ログ出力)

## イメージ
|変換画面|
|:-:|
|<img width="640" alt="convert_screen" src="https://user-images.githubusercontent.com/60963155/204125570-59fb7b51-0fff-465a-ad28-4d7290aaf989.PNG">|

|設定画面|
|:-:|
|<img width="640" alt="setting_dialog" src="https://user-images.githubusercontent.com/60963155/204125574-1505e36c-de19-441b-a969-623a468ff52c.PNG">|

|履歴画面|
|:-:|
|<img width="640" alt="record_dialog" src="https://user-images.githubusercontent.com/60963155/204125577-f33773ef-3f87-4ad9-a265-3e75f8bb5a69.PNG">|

|インフォ画面|
|:-:|
|<img width="640" alt="info_dialog" src="https://user-images.githubusercontent.com/60963155/204125579-daf59ad4-3019-4e23-9214-aa8e972d936e.PNG">|


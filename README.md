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

## イメージ

![スライド1](https://user-images.githubusercontent.com/60963155/204125570-59fb7b51-0fff-465a-ad28-4d7290aaf989.PNG)
![スライド2](https://user-images.githubusercontent.com/60963155/204125574-1505e36c-de19-441b-a969-623a468ff52c.PNG)
![スライド3](https://user-images.githubusercontent.com/60963155/204125577-f33773ef-3f87-4ad9-a265-3e75f8bb5a69.PNG)
![スライド4](https://user-images.githubusercontent.com/60963155/204125579-daf59ad4-3019-4e23-9214-aa8e972d936e.PNG)

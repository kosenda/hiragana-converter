# Google Play でリリースしたアプリのコピー版です
## アプリ名「ひらがな変換」（ Hiragana Converter ）
Google Play  
https://play.google.com/store/apps/details?id=ksnd.hiraganaconverter&hl=ja&gl=JP  

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

![Screenshot_1668350425](https://user-images.githubusercontent.com/60963155/201527568-37677799-e99c-4f0d-8204-f547dbbab0c4.png)
![Screenshot_1668350443](https://user-images.githubusercontent.com/60963155/201527571-1ca04aab-ecb6-443f-9f0d-2be0f08cc41d.png)
![Screenshot_1668350453](https://user-images.githubusercontent.com/60963155/201527585-228ad2ca-eb48-4b6d-bfbc-b8ae97be7dd7.png)

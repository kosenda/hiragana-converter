# Google Play でリリースしたアプリのコピー版です
## アプリ名「ひらがな変換」（ Hiragana Converter ）
[Google Play]  
https://play.google.com/store/apps/details?id=ksnd.hiraganaconverter&hl=ja&gl=JP  

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

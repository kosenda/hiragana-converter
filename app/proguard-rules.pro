# ■ OkHttp の設定 -----------------------------------------------------------------------------------

# 以下を参考
#https://square.github.io/okhttp/features/r8_proguard/

# nullability情報を埋め込むためのJSR 305アノテーションの警告を無視するための設定
-dontwarn javax.annotation.**
# リソースは相対パスでロードするため、このクラスのパッケージを保存するための設定
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz


# ■ Retrofitの設定 ----------------------------------------------------------------------------------

# 以下を参考
# https://github.com/square/retrofit/blob/master/retrofit/src/main/resources/META-INF/proguard/retrofit2.pro

# Retrofitはジェネリックパラメータにリフレクションを行う。Signatureを使うにはInnerClassesが必要。
# InnerClassesを使うにはEnclosingMethodが必要
-keepattributes Signature, InnerClasses, EnclosingMethod
# Retrofitでメソッドとパラメータのアノテーションのリフレクションを行うための設定
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
# アノテーションのデフォルト値を保持する
-keepattributes AnnotationDefault
# Serviceのメソッドのパラメータを保持するための設定
-keepclasseswithmembers interface * {
    @retrofit2.http.* <methods>;
}
# Kotlinでのみ使用できるトップレベルの関数の警告を無視するための設定
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
# 継承したServiceを保持するための設定
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>
# CallとResponseのジェネリックのシグネチャを保持する
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response


# ■ kotlinx.serializationの設定 ---------------------------------------------------------------------

# 以下を参考
# https://github.com/Kotlin/kotlinx.serialization#android

# NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializerがある場合に使用する設定
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
# アプリ固有のもの
# Change here com.yourcompany.yourpackage
-keepclassmembers @kotlinx.serialization.Serializable class ksnd.open.hiraganaconverter.model.** {
    # lookup for plugin generated serializable classes
    *** Companion;
    # lookup for serializable objects
    *** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
# lookup for plugin generated serializable classes
-if @kotlinx.serialization.Serializable class ksnd.open.hiraganaconverter.model.**
-keepclassmembers class ksnd.open.hiraganaconverter.model.<1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}
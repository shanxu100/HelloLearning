# Android Build相关


---------
# 设置SourceSet
> https://developer.android.com/studio/write/add-resources

## 设置
```java
android {
    
    // ...

    // 方式一
    sourceSets{
        // 在main里面设置共有的，在flavor中设置特有的。打包时会将两者合并
        main {
            manifest.srcFile 'AndroidManifest.xml'
            res.srcDirs = ['res']
            java.srcDirs = ['src']
            resources.srcDirs = ['src','res']
            aidl.srcDirs = ['src','res']
            renderscript.srcDirs = ['src','res']
            assets.srcDirs = ['assets']
        }
        flavor1{
            resources{
                exclude "**/drawable-xxhdpi/"
                exclude "**/drawable-xxxhdpi/**"
            }
        }
        mirror {
            assets.srcDirs = [getProjectDir().getAbsolutePath() + "/mirror/assets"]
        }
        product {
            assets.srcDirs = [getProjectDir().getAbsolutePath() + "/product/assets"]
        }
    }

    // 方式二
    sourceSets {
        flavorOne.java.srcDir 'src/common/java'
        flavorTwo.java.srcDir 'src/common/java'
        flavorThree.java.srcDir 'src/common/java'
    }

}


```
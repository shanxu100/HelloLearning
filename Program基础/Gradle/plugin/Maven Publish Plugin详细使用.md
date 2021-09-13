# Maven Publish Plugin详细使用

maven-publish是一个Gradle插件，可以将输出的artifacts发布到Maven仓库中。
[Maven Publish Plugin官方使用手册](https://docs.gradle.org/current/userguide/publishing_maven.html#publishing_maven)

使用：
1. 与sdk的flavor和buildType绑定，可以发布不同特性的sdk
2. 根据 sdkactive 参数，判断发布的sdk是release类型还是snapshot类型，是remote仓库还是local仓库
3. 需要gradle版本 ```com.android.tools.build:gradle:3.6.1``` 及以上
4. 自定义pom文件

```java

apply plugin: 'maven-publish'

/**
 * 特性：

 *
 *
 * 参考：https://docs.gradle.org/current/userguide/publishing_maven.html
 */

/**
 *
 * 在跟build.gradle 中增加如下定义
 * ext{
 *     sdk_version_code = 100004126
 *     sdk_version_name = "1.0.4.126"
 *     sdk_group_id = 'com.xxxx.xxx'
 *     sdk_artifact_name = "xxx"
 * }
 *
 **/

/**
 * 使用示例：xxxxx为模块名
 * apply from: rootProject.getRootDir().path + "/publish.gradle"
 *
 * gradlew :xxxxx:publish  -Psdkactive=local_release -Pwithflavors=root -Pwithdebug -info
 * gradlew :xxxxx:publish  -Psdkactive=local_snapshot  -info
 * gradlew :xxxxx:publish  -Psdkactive=release  -info
 * gradlew :xxxxx:publish  -Psdkactive=snapshot  -info
 *
 */

ext {

    MAVEN_LOCAL_RELEASE_URL = rootDir.absolutePath + File.separator + "repo"
    MAVEN_LOCAL_SNAPSHOT_URL = rootDir.absolutePath + File.separator + "repo"
    MAVEN_RELEASE_URL = "http://artifactory.xxxx.com/artifactory/Product-xxx-release/"
    MAVEN_SNAPSHOT_URL = "http://artifactory.xxxx.com/artifactory/Product-xxxx-snapshot/"


    /**
     * maven仓库的用户名和密码
     */
    MAVEN_USERNAME = project.hasProperty('myusername') ? myusername : ''
    MAVEN_PASSWORD = project.hasProperty('mypwd') ? mypwd : ''

    /**
     * groupId : 唯一标识（通常为模块包名，也可以任意）
     */
    DEFAULT_GROUP_ID = rootProject.ext.sdk_group_id

    /**
     * artifactId : 项目名称（通常为类库模块名称，也可以任意）
     */
    DEFAULT_ARTIFACT_ID = rootProject.ext.sdk_artifact_name

    /**
     * version : 版本号
     */
    DEFAULT_PUBLISH_VERSION = rootProject.ext.sdk_version_name
    DEFAULT_PUBLISH_VERSION_SNAPSHOT = rootProject.ext.sdk_version_name + '-SNAPSHOT'

    /**
     * 参数 sdkactive 对应的4个值，切勿更改
     *     remote_release 向maven仓发布稳定版本，
     *     remote_snapshot 向maven发布抢先版（开发中的不稳定版本）
     *     local_release 向本地发布release版本
     *     local_snapshot 向本地发布snapshot版本
     */
    SDK_ACTIVE_RELEASE = 'remote_release'
    SDK_ACTIVE_SNAPSHOT = 'remote_snapshot'
    SDK_ACTIVE_LOCAL_RELEASE = 'local_release'
    SDK_ACTIVE_LOCAL_SNAPSHOT = 'local_snapshot'

    /**
     * 参数 withflavors ：指定编译的flavor
     */
    DEFAULT_WITH_FLAVORS = 'root:test:security:unite:beta:dev'

}

ext {
    // 读取构建命令中输入的参数值
    sdk_active = project.hasProperty('sdkactive') ? project.property('sdkactive') : SDK_ACTIVE_LOCAL_SNAPSHOT
    with_debug = project.hasProperty('withdebug')
    with_flavors = project.hasProperty('withflavors') ? project.property('withflavors') : DEFAULT_WITH_FLAVORS
}

// 关闭 module 产物的发布
// https://docs.gradle.org/current/userguide/publishing_gradle_module_metadata.html
tasks.withType(GenerateModuleMetadata) {
    enabled = false
}

// 在此处设置publish相关任务
afterEvaluate {

    publishing {

        repositories {
            maven {
                // 指定仓库地址
                // change URLs to point to your repos, e.g. http://my.org/repo
                // url = 'http://www.example.com/library'
                url = getUrl(sdk_active)
                if (needAuthentication(url.toString())) {
                    credentials {
                        username cloudartifact_username
                        password cloudartifact_password
                    }
                }
            }
        }


        // 创建发布任务
        publications {


            // 方式-：普通方式
            releasePublish(MavenPublication) {
                // Applies the component for the release build variant.
                from components.release

                // You can then customize attributes of the publication as shown below.
                groupId = DEFAULT_GROUP_ID
                artifactId = DEFAULT_ARTIFACT_ID
                version = getVersionId(sdk_active)
            }


            // ================================================================

            
            // 方式二：适配sdk设置flavor的情况，如：
            // flavorDimensions "env"
            // productFlavors {
                // envRoot {
                    // 默认环境
                    // dimension "env"
                    // buildConfigField("String", "flavorcfg", "\"root\"")
                // }
                // envUnite {
                    // dimension "env"
                    // buildConfigField("String", "flavorcfg", "\"unite\"")
                // }
                // envDev {
                    // dimension "env"
                    // buildConfigField("String", "flavorcfg", "\"dev\"")
                // }
                // envTest {
                    // dimension "env"
                    // buildConfigField("String", "flavorcfg", "\"test\"")
                // }
                // envBeta {
                    // dimension "env"
                    // buildConfigField("String", "flavorcfg", "\"beta\"") 
                // }
                // envSecurity {
                    // dimension "env"
                    // buildConfigField("String", "flavorcfg", "\"security\"")
                // }
            // }

            if (containsFlavor("root")) {
                println("containsFlavor root")
                envRootRelease(MavenPublication) {
                    // Applies the component for the release build variant.
                    from components.envRootRelease

                    // You can then customize attributes of the publication as shown below.
                    groupId = DEFAULT_GROUP_ID
                    artifactId = DEFAULT_ARTIFACT_ID
                    version = getVersionId(sdk_active)
                    createPom(pom)
                }
            }

            // 略....

            if (!with_debug) {
                println("does not have Property: with debug")
            }

            if (with_debug && containsFlavor("root")) {
                envRootDebug(MavenPublication) {
                    // Applies the component for the debug build variant.
                    from components.envRootDebug

                    groupId = DEFAULT_GROUP_ID
                    artifactId = DEFAULT_ARTIFACT_ID + '-debug'
                    version = getVersionId(sdk_active)
                    createPom(pom)
                }
            }

            // 略....

        }
    }

}


private boolean containsFlavor(String flavor) {
    return with_flavors.toLowerCase(Locale.ROOT).contains(flavor.toLowerCase(Locale.ROOT))
}

private String getVersionId(String sdkActiveStr) {
    return isSnapshotRepo(sdkActiveStr) ? DEFAULT_PUBLISH_VERSION_SNAPSHOT : DEFAULT_PUBLISH_VERSION
}

/**
 * 获取 url
 * @param sdkActiveStr sdk_active参数
 * @return url
 */
private String getUrl(String sdkActiveStr) {

    String url = '';

    if (isSnapshotRepo(sdkActiveStr)) {

        if (isLocalRepo(sdkActiveStr)) {
            url = MAVEN_LOCAL_SNAPSHOT_URL
        } else {
            url = MAVEN_SNAPSHOT_URL
        }
    } else {

        if (isLocalRepo(sdkActiveStr)) {
            url = MAVEN_LOCAL_RELEASE_URL
        } else {
            url = MAVEN_RELEASE_URL
        }
    }

    if (url == null || url.equals('')) {
        url = MAVEN_LOCAL_SNAPSHOT_URL
    }
    println 'get publish url = ' + url
    return url;
}

/**
 * 是否是 local 的repo
 * @param sdkActiveStr sdkActiveStr
 * @return true：使用local仓库    false：使用remote仓库
 */
private boolean isLocalRepo(String sdkActiveStr) {
    if (sdkActiveStr == null || sdkActiveStr.equals('')) {
        return true
    }
    return sdkActiveStr.contains("local");
}

/**
 * 是否是 snapshot 的repo
 * @param sdkActiveStr
 * @return true：使用snapshot仓库    false：使用remote仓库
 */
private boolean isSnapshotRepo(String sdkActiveStr) {
    if (sdkActiveStr == null || sdkActiveStr.equals('')) {
        return true
    }
    return sdkActiveStr.contains("snapshot");
}


/**
 * 判断访问maven仓库的时候，是否需要用户名和密码
 * @param url maven仓库地址
 * @return true：需要用户名面
 */
private boolean needAuthentication(String url) {
    if (url == null || url.equals("")) {
        return false
    }
    if (url.toLowerCase(Locale.ROOT).startsWith("http")) {
        return true
    }
    return false;
}

//pom文件依赖节点
private void createPom(pom) {

//    方式一：自定义
//    https://taccisum.github.io/learn_gradle_5.html
    pom.withXml {
        asNode().dependencies.'*'.findAll() {
            it.scope.text() == 'runtime'
        }.each() {
            it.scope*.value = 'compile'
        }
    }

//    方式二：自定义
//    pom.withXml {
//
//        // 删除旧的 dependencies 节点
//        Node rootNode = asNode();
//        rootNode.remove(rootNode.dependencies)
//
//        // 添加新的 dependencies 节点
//        def dependenciesNode = asNode().appendNode('dependencies')
//        configurations.implementation.allDependencies.each {
//            if (it.name != "unspecified") {
//                //依赖maven上sdk
//                if (it.group != "unspecified" && it.version != "unspecified") {
//                    def dependencyNode = dependenciesNode.appendNode('dependency')
//                    dependencyNode.appendNode('groupId', it.group)
//                    dependencyNode.appendNode('artifactId', it.name)
//                    dependencyNode.appendNode('version', it.version)
//                } else {//依赖project场景，使用自身的版本号以及groupId
//                    def dependencyNode = dependenciesNode.appendNode('dependency')
//                    dependencyNode.appendNode('groupId', groupId)
//                    dependencyNode.appendNode('artifactId', rootProject.ext.projectnames.get(it.name))
//                    dependencyNode.appendNode('version', version)
//                }
//            }
//        }
//    }


}

```



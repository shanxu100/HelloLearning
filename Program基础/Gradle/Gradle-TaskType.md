## Gradle TaskType


### Copy
> https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Copy.html
```java
task chVer(type: Copy) { // 指定Type为Copy任务
    from "src/main/manifest/AndroidManifestCopy.xml"  // 复制src/main/manifest/目录下的AndroidManifest.xml
    into 'src/main'  // 复制到指定目标目录
    rename { String fileName -> //在复制时重命名文件
        fileName = "AndroidManifest.xml" // 重命名
    }

    from new File(project.getBuildDir(), 'outputs/apk/debug/framework-manager-debug.apk').absolutePath
    into new File(project.getRootProject().getProjectDir(), 'demo-hmscoreapp/src/main/assets')

    from new File(buildDir, "armeabi-v7a")
    into "src/main/temp-v7a"
}
```

### Exec
> https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Exec.html
```java
task myPreTask(type: Exec){
    def cmdPath = rootProject.path + File.separator +"mycmd.bat"
    def shellPath = rootProject.path + File.separator + "test.sh";
    if (System.getProperty('os.name').toLowerCase(Locale.ROOT).contains('windows')) {
        println "windows"
        commandLine 'cmd', '/c', cmdPath
    } else {
        println "linux"
        commandLine 'sh', '-c', shellPath
    }
}
```

### Delete
> https://s0docs0gradle0org.icopy.site/current/dsl/org.gradle.api.tasks.Delete.html

### Zip —— 压缩成一个zip包
> https://s0docs0gradle0org.icopy.site/current/dsl/org.gradle.api.tasks.bundling.Zip.html
```java
task zip(type: Zip) {
    from 'src/dist'
    into('libs') 
}
```

### Jar —— 打成jar包
```java
  task customJar(type: Jar) {
  	manifest {
  		attributes firstKey: 'firstValue', secondKey: 'secondValue'
  	}
  	archiveName = 'hello.jar'
  	destinationDir = file("${buildDir}/jars")
  	from sourceSets.main.classes
    include('com/example/mylib/xxx/xxx/xxx/xxx.class')
  }
```

### Download —— 访问http接口和下载
```java
// 访问http接口
URLConnection connection = new URL(MAVEN_URL).openConnection()
def respText = connection.content.text;
println "versionInfo = " + respText

String[] splits = respText.split("href=\"")
def downloadUrl = MAVEN_URL + splits[splits.length - 1].split("\">")[0]
println "downloadUrl = " + downloadUrl

// 下载文件
def out = new BufferedOutputStream(new FileOutputStream(soFiles))
out << new URL(downloadUrl).openStream()
out.close()
```








